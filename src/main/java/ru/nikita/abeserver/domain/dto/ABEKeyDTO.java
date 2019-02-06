package ru.nikita.abeserver.domain.dto;

import lombok.Data;
import ru.nikita.abeserver.domain.ABEKey;
import ru.nikita.abeserver.domain.File;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ABEKeyDTO {
    private Long id;
    private String name;
    private Long create;
    private boolean active;

    public static ABEKeyDTO convert(ABEKey abeKey){
        ABEKeyDTO abeKeyDTO = new ABEKeyDTO();
        abeKeyDTO.setId(abeKey.getId());
        abeKeyDTO.setName(abeKey.getName());
        abeKeyDTO.setCreate(abeKey.getCreate().getTime());
        abeKeyDTO.setActive(abeKey.getActive());
        return abeKeyDTO;
    }

    public static List<ABEKeyDTO> convertList(List<ABEKey> list){
        return list.stream()
                .map(ABEKeyDTO::convert)
                .collect(Collectors.toList());
    }

    public static ABEKeyDTOList getDTOList(List<ABEKey> list){
        return new ABEKeyDTOList(convertList(list));
    }
}
