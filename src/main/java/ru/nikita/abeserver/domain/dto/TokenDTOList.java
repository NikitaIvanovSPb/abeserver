package ru.nikita.abeserver.domain.dto;

import java.util.List;

public class TokenDTOList extends ListResult<TokenDTO> {
    public TokenDTOList(List<TokenDTO> results) {
        super(results);
    }
}
