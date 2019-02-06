package ru.nikita.abeserver.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import ru.nikita.openabeapi.OpenABEApi;

import java.io.IOException;

@Configuration
public class Boot {
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public OpenABEApi openABEApi(){
        try {
            return new OpenABEApi();
        } catch (IOException e) {
            System.out.println("fatal error open abe init!!!");
        }
        return null;
    }
}
