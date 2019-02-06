package ru.nikita.abeserver.services.impl;

import org.springframework.stereotype.Service;
import ru.nikita.abeserver.domain.Attribute;
import ru.nikita.abeserver.domain.Token;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.domain.dto.AttributeDTO;
import ru.nikita.abeserver.domain.dto.AttributeDTOList;
import ru.nikita.abeserver.repositories.AttributeRepository;
import ru.nikita.abeserver.repositories.TokenRepository;
import ru.nikita.abeserver.services.CryptoService;
import ru.nikita.abeserver.services.TokenService;
import ru.nikita.abeserver.services.UserService;

import java.util.*;

@Service
public class TokenServiceImpl implements TokenService {

    private TokenRepository tokenRepository;
    private CryptoService cryptoService;
    private UserService userService;
    private AttributeRepository attributeRepository;

    public TokenServiceImpl(TokenRepository tokenRepository, CryptoService cryptoService, UserService userService, AttributeRepository attributeRepository) {
        this.tokenRepository = tokenRepository;
        this.cryptoService = cryptoService;
        this.userService = userService;
        this.attributeRepository = attributeRepository;
    }

    @Override
    public Token createToken(String atr, Date expTime, Long userId){
        Token token = new Token();
        User user = userService.findById(userId);
        if(user == null) return null;
        String guid;
        do {
            guid = UUID.randomUUID().toString();
        }while (tokenRepository.findFirstByGuidEquals(guid).isPresent());
        atr += "|expTime=" + (expTime.getTime() / 1000);
        String key = cryptoService.generateUserKey(atr);
        if(key == null) return null;
        token.setKey(key);
        token.setGuid(guid);
        token.setAttrbutes(atr);
        token.setUsed(false);
        token.setUser(user);
        token.setCreate(new Date());
        token = tokenRepository.save(token);
        return token;
    }

    @Override
    public boolean deleteToken(String guid){
        Optional<Token> token = tokenRepository.findFirstByGuidEquals(guid);
        if(token.isPresent()){
            tokenRepository.delete(token.get());
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<Token> getTokensByUser(User user){
        List<Token> list = new LinkedList<>();
        tokenRepository.getAllByUserEqualsAndUsedIsFalse(user).forEach(list::add);
        return list;
    }

    @Override
    public String getKeyByToken(String guid, Long userId){
        try {
            Thread.sleep(1000*10);
            Optional<Token> token = tokenRepository.findFirstByGuidEquals(guid);
            if(token.isPresent()){
                User user = userService.findById(userId);
                if(!token.get().isUsed()) {
                    if(user == null || !user.getId().equals(token.get().getUser().getId())) return "error";
                        Token token1 = token.get();
                        token1.setUsed(true);
                        token1.setGenerateDate(new Date());
                        tokenRepository.save(token1);
                        return token1.getKey();
                }else return "used";
            }else {
                Thread.sleep(1000 * 20);
                return "not exist";
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "error";
    }

    @Override
    public AttributeDTOList getUserAttrs(){
        List<Attribute> attributes = new LinkedList<>();
        attributeRepository.getAllByTypeEquals(Attribute.Type.User).forEach(attributes::add);
        return AttributeDTO.getDTOList(attributes);
    }

}
