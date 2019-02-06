package ru.nikita.abeserver.domain.dto;

import lombok.Data;
import ru.nikita.abeserver.domain.User;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    Long id;
    String login;
    Long registrationDate;
    String name;
    String lastName;
    String patronymic;
    String email;
    Boolean admin;
    String password;

    public static UserDTO convert(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.id = user.getId();
        userDTO.login = user.getLogin();
        userDTO.registrationDate = user.getRegistrationDate().getTime();
        userDTO.name = user.getName();
        userDTO.lastName = user.getLastName();
        userDTO.patronymic = user.getPatronymic();
        userDTO.email = user.getEmail();
        userDTO.admin = user.getAdmin();
        userDTO.password = "";
        return userDTO;
    }

    public static List<UserDTO> convertList(List<User> list){
        return list.stream()
                .map(UserDTO::convert)
                .collect(Collectors.toList());
    }

    public static UserDTOList getDTOList(List<User> list){
        return new UserDTOList(convertList(list));
    }
}
