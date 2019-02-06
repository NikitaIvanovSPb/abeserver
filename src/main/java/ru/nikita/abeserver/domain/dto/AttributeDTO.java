package ru.nikita.abeserver.domain.dto;

import lombok.Data;
import ru.nikita.abeserver.domain.Attribute;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class AttributeDTO {
    private String engName;
    private String rusName;
    private String valName;

    public static AttributeDTO convert(Attribute attribute){
        AttributeDTO attributeDTO = new AttributeDTO();
        attributeDTO.setEngName(attribute.getEngName());
        attributeDTO.setRusName(attribute.getRusName());
        attributeDTO.setValName(attribute.getValName());
        return attributeDTO;
    }

    public static List<AttributeDTO> convertList(List<Attribute> list){
        return list.stream()
                .map(AttributeDTO::convert)
                .collect(Collectors.toList());
    }

    public static AttributeDTOList getDTOList(List<Attribute> list){
        return new AttributeDTOList(convertList(list));
    }
}
