package ru.nikita.abeserver.validators;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.services.UserService;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {
    private UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(@Nullable Object o, Errors errors) {
        User user = (User) o;
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]*$");

        if((user.getLogin().length() < 6 || user.getLogin().length() > 32)){
            errors.rejectValue("login", "error.login", "размер логина должен быть между 6 и 32 символами");
        }
        if(user.getLogin().length() >= 6 && user.getLogin().length() <= 32 && !pattern.matcher(user.getLogin()).matches()){
            errors.rejectValue("login","error.login", "логин может содержать только символы \"a-Z A-Z 0-9 _\"");
        }
        User existUser = userService.findByLogin(user.getLogin());
        if(existUser != null && !existUser.getId().equals(user.getId())){
            errors.rejectValue("login","error.login", "пользователь с таким логином уже зарегистрирован");
        }
        if(user.getId() == null && (user.getPassword().length() < 8 || user.getPassword().length() > 32)){
            errors.rejectValue("password", "error.password", "размер пароля должен быть между 8 и 32 символами");
        }
        if(user.getId() != null && user.getPassword().length() != 0 && (user.getPassword().length() < 8 || user.getPassword().length() > 32)){
            errors.rejectValue("password", "error.password", "размер пароля должен быть между 8 и 32 символами");
        }
        if(user.getPassword().length() >= 8 && user.getPassword().length() <= 32 && !pattern.matcher(user.getPassword()).matches()){
            errors.rejectValue("login","error.login", "пароль может содержать только символы \"a-Z A-Z 0-9 _\"");
        }
        if(user.getPassword().length() != 0 && !user.getPassword().equals(user.getConfirmPassword())){
            errors.rejectValue("confirmPassword", "error.confirmPassword", "пароли должны совпадать");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name", "имя должно быть указано");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "error.lastName", "фамилия должна быть указана");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "patronymic", "error.patronymic", "отчество должно быть указано");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "error.email", "e-mail должен быть указан");
    }

    public void trimFields(User user) {
        user.setLogin(user.getLogin().trim());
        user.setPassword(user.getPassword().trim());
        user.setConfirmPassword(user.getConfirmPassword().trim());
        user.setEmail(user.getEmail().trim());
        user.setName(user.getName().trim());
        user.setLastName(user.getLastName().trim());
        user.setPatronymic(user.getPatronymic().trim());
    }
}
