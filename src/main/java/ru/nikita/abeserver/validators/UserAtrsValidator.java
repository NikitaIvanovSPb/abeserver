package ru.nikita.abeserver.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.nikita.abeserver.services.CryptoService;


@Component
public class UserAtrsValidator implements Validator {

    private CryptoService cryptoService;

    public UserAtrsValidator(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return String.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

    }
}
