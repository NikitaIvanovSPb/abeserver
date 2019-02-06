package ru.nikita.abeserver.domain.dto;

import java.util.List;

public class ABEKeyDTOList extends ListResult<ABEKeyDTO> {
    public ABEKeyDTOList(List<ABEKeyDTO> results) {
        super(results);
    }
}
