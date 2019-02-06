package ru.nikita.abeserver.domain.dto;

import java.util.List;

public class UserDTOList extends ListResult<UserDTO>{
    public UserDTOList(List<UserDTO> result) {
        super(result);
    }
}
