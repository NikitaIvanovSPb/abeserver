package ru.nikita.abeserver.services;

import ru.nikita.abeserver.domain.Token;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.domain.dto.AttributeDTOList;

import java.util.Date;
import java.util.List;

public interface TokenService {

    Token createToken(String atr, Date expTime, Long userId);

    boolean deleteToken(String guid);

    List<Token> getTokensByUser(User user);

    String getKeyByToken(String guid, Long userId);

    AttributeDTOList getUserAttrs();
}
