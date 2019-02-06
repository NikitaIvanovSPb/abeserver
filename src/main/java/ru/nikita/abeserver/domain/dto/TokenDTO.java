package ru.nikita.abeserver.domain.dto;

import lombok.Data;
import ru.nikita.abeserver.domain.Token;
import ru.nikita.abeserver.domain.User;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TokenDTO {

    private String guid;
    private String attrbutes;
    private boolean used;
    private Long create;
    private Long generate;

    public static TokenDTO convert(Token token){
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setAttrbutes(token.getAttrbutes());
        tokenDTO.setGuid(token.getGuid());
        tokenDTO.setUsed(token.isUsed());
        tokenDTO.setCreate(token.getCreate().getTime());
        if(token.getGenerateDate() != null ) tokenDTO.setGenerate(token.getGenerateDate().getTime());
        return tokenDTO;
    }

    public static List<TokenDTO> convertList(List<Token> list){
        return list.stream()
                .map(TokenDTO::convert)
                .collect(Collectors.toList());
    }

    public static TokenDTOList getDTOList(List<Token> list){
        return new TokenDTOList(convertList(list));
    }
}
