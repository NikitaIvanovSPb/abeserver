package ru.nikita.abeserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.services.UserService;

@SpringBootApplication
public class AbeserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbeserverApplication.class, args);
    }
}
