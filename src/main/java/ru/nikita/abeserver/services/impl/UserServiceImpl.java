package ru.nikita.abeserver.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nikita.abeserver.domain.Role;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.domain.dto.UserDTO;
import ru.nikita.abeserver.repositories.RoleRepository;
import ru.nikita.abeserver.repositories.UserRepository;
import ru.nikita.abeserver.services.UserService;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


//
    @Override
    public User create(UserDTO userDTO){
        Optional<User> userOptional = userRepository.findByLoginEquals(userDTO.getLogin());
        if (userOptional.isPresent()){
            throw new RuntimeException("User with this login already exist");
        }
        User user = new User();
        user.setAdmin(userDTO.getAdmin());
        if(user.getAdmin()){
            user.getRoles().add(roleRepository.findById(1L).get());
        }
        user.getRoles().add(roleRepository.findById(2L).get());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setLogin(userDTO.getLogin());
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setPatronymic(userDTO.getPatronymic());
        user.setEmail(userDTO.getEmail());
        user.setRegistrationDate(new Date());
        return userRepository.save(user);
    }

    @Override
    public User create(User user) {
        Optional<User> userOptional = userRepository.findByLoginEquals(user.getLogin());
        if (userOptional.isPresent()){
            throw new RuntimeException("User with this login already exist");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getAdmin() != null){
            user.setAdmin(user.getAdmin());
        }else{
            user.setAdmin(false);
        }
        user.getRoles().add(roleRepository.findById(2L).get());
        user.setRegistrationDate(new Date());
        return userRepository.save(user);
    }

    @Override
    public User update(UserDTO userDTO){
        User savedUser = userRepository.findById(userDTO.getId()).orElse(null);
        if(savedUser == null){
            throw new RuntimeException("User with this login does not exist");
        }
        if(userDTO.getAdmin() != null){
            if(savedUser.getAdmin() && !userDTO.getAdmin()){
                savedUser.getRoles().remove(roleRepository.findByNameEquals(Role.ADMIN_ROLE).get());
            }else if(!savedUser.getAdmin() && userDTO.getAdmin()){
                savedUser.getRoles().add(roleRepository.findByNameEquals(Role.ADMIN_ROLE).get());
            }
            savedUser.setAdmin(userDTO.getAdmin());
        }
        if(userDTO.getLogin() != null) savedUser.setLogin(userDTO.getLogin());
        if(userDTO.getPassword() != null && userDTO.getPassword().length() != 0) savedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        if(userDTO.getName() != null) savedUser.setName(userDTO.getName());
        if(userDTO.getLastName() != null) savedUser.setLastName(userDTO.getLastName());
        if(userDTO.getPatronymic() != null) savedUser.setPatronymic(userDTO.getPatronymic());
        if(userDTO.getEmail() != null) savedUser.setEmail(userDTO.getEmail());
        return userRepository.save(savedUser);
    }

    @Override
    public User update(User user, String login){
        User savedUser = userRepository.findByLoginEquals(login).orElse(null);
        if(savedUser == null){
            throw new RuntimeException("User with this login does not exist");
        }
        if(user.getAdmin() != null){
            if(savedUser.getAdmin() && !user.getAdmin()){
                savedUser.getRoles().remove(roleRepository.findByNameEquals(Role.ADMIN_ROLE).get());
            }else if(!savedUser.getAdmin() && user.getAdmin()){
                savedUser.getRoles().add(roleRepository.findByNameEquals(Role.ADMIN_ROLE).get());
            }
            savedUser.setAdmin(user.getAdmin());
        }
        if(user.getLogin() != null) savedUser.setLogin(user.getLogin());
        if(user.getPassword() != null && user.getPassword().length() != 0) savedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getName() != null) savedUser.setName(user.getName());
        if(user.getLastName() != null) savedUser.setLastName(user.getLastName());
        if(user.getPatronymic() != null) savedUser.setPatronymic(user.getPatronymic());
        if(user.getEmail() != null) savedUser.setEmail(user.getEmail());
        return userRepository.save(savedUser);
    }

    @Override
    public void delete(Long userId){
        userRepository.deleteById(userId);
    }

    @Override
    public void delete(User user){
        userRepository.deleteById(user.getId());
    }

    @Override
    public boolean isdbClear() {
        return userRepository.count() == 0;
    }

    @Override
    public User findByLogin(String login) {
        Optional<User> userOptional = userRepository.findByLoginEquals(login);
        return userOptional.orElse(null);
    }

    @Override
    public List<User> getUsers(){
        List<User> list = new LinkedList<>();
        userRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public User findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

//    @Override
//    public Page<User> searchBySortedBy(String searchBy, String sortedBy, String search, int page, int limit) {
//        PageRequest pageRequest = PageRequest.of(page, limit, Sort.by(sortedBy));
//        switch (searchBy){
//            case "login":
//                return userRepository.findAllByLoginContainingIgnoreCase(search, pageRequest);
//            case "email":
//                return userRepository.findAllByEmailContainingIgnoreCase(search, pageRequest);
//            case "learnGroup":
//                return userRepository.findAllByLearnGroupContainingIgnoreCase(search, pageRequest);
//            default:
//                return userRepository.findAllByLoginContainingIgnoreCase(search, pageRequest);
//        }
//    }


}
