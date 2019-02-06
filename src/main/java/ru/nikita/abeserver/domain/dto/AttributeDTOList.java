package ru.nikita.abeserver.domain.dto;

import java.util.List;

public class AttributeDTOList extends ListResult<AttributeDTO> {
    public AttributeDTOList(List<AttributeDTO> results) {
        super(results);
    }
}
