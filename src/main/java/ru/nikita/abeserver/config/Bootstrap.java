package ru.nikita.abeserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.nikita.abeserver.domain.Attribute;
import ru.nikita.abeserver.domain.Role;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.repositories.AttributeRepository;
import ru.nikita.abeserver.repositories.RoleRepository;
import ru.nikita.abeserver.repositories.UserRepository;
import ru.nikita.abeserver.services.UserService;

import java.util.LinkedList;
import java.util.List;


@Component
@Slf4j
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {
    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private UserService userService;
    private AttributeRepository attributeRepository;

    public Bootstrap(RoleRepository roleRepository, UserRepository userRepository, UserService userService, AttributeRepository attributeRepository) {
        this.roleRepository = roleRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.attributeRepository = attributeRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            if (userService.isdbClear()){
                addEntities();
                addAttributes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEntities() throws Exception {
        Role adminRole = new Role();
        adminRole.setName(Role.ADMIN_ROLE);
        adminRole = roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName(Role.USER_ROLE);
        userRole = roleRepository.save(userRole);

        User adminUser = new User();
        adminUser.setLogin("admin");
        adminUser.setPassword("pass");
        adminUser.setAdmin(true);
        adminUser.setName("Admin");
        adminUser.setLastName("Adminus");
        adminUser.setPatronymic("Adminius");
        adminUser.setEmail("admin@mail.com");
        adminUser.getRoles().add(adminRole);
        userService.create(adminUser);

    }

    private void addAttributes(){
        List<Attribute> attributes = new LinkedList<>();
        attributes.add(new Attribute("DN", Attribute.Type.User, "Distinguished Name", "Отличительное (уникальное) имя"));
        attributes.add(new Attribute("DC", Attribute.Type.User, "Domain Component", "Компонент(класс) доменного имени."));
        attributes.add(new Attribute("OU", Attribute.Type.User, "Organizational Unit", "Подразделение"));
        attributes.add(new Attribute("CN", Attribute.Type.User, "Common Name", "Общее имя"));
        attributes.add(new Attribute("givenName", Attribute.Type.User, "First name", "Имя"));
        attributes.add(new Attribute("name", Attribute.Type.User, "Full name", "Полное имя"));
        attributes.add(new Attribute("sn", Attribute.Type.User, "Last name", "Фамилия"));
        attributes.add(new Attribute("displayName", Attribute.Type.User, "Display Name", "Выводимое имя"));
        attributes.add(new Attribute("mail", Attribute.Type.User, "E-mail", "Электронная почта"));
        attributes.add(new Attribute("sAMAccountName", Attribute.Type.User, "User logon name(pre-Windows 2000)", "Имя входа пользователя (пред-Windows 2000)"));
        attributes.add(new Attribute("userPrincipalName", Attribute.Type.User, "User logon name", "Имя входа пользователя"));
        attributes.add(new Attribute("memberOf", Attribute.Type.User, "Member Of", "Член групп (в какую группу входит данный пользователь)"));
        attributeRepository.saveAll(attributes);
    }
}
