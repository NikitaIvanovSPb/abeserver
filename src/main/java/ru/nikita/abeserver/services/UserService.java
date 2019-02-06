package ru.nikita.abeserver.services;

import org.springframework.data.domain.Page;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.domain.dto.UserDTO;

import java.util.List;

public interface UserService {
    //
    User create(UserDTO userDTO);

    User create(User user) throws Exception;

    User update(UserDTO userDTO);

    User update(User user, String login);

    void delete(Long userId);

    void delete(User user);
    boolean isdbClear();
    User findByLogin(String login);

    List<User> getUsers();

    User findById(Long id);

//    Page<User> searchBySortedBy(String searchBy, String sortedBy, String search, int page, int limit);

}
