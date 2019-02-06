package ru.nikita.abeserver.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.nikita.abeserver.domain.File;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.services.FileService;
import ru.nikita.abeserver.services.UserService;
import ru.nikita.abeserver.validators.UserValidator;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@Slf4j
public class AuthorisationController {
    private UserValidator userValidator;
    private UserService userService;
    private FileService fileService;

    public AuthorisationController(UserValidator userValidator, UserService userService) {
        this.userValidator = userValidator;
        this.userService = userService;
    }

    private void checkAuthorize(Model model){
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("username", userDetails.getUsername());
            model.addAttribute("unauthorized", false);
            User user = userService.findByLogin(userDetails.getUsername());
            model.addAttribute("isAdmin", user.getAdmin());
        }catch (ClassCastException | NullPointerException e){
            model.addAttribute("unauthorized", true);
            model.addAttribute("username", "default");
            model.addAttribute("isAdmin", false);
        }
    }

    @RequestMapping(value = "/admin/")
    public String app(Model model){
        return "app";
    }


//    @RequestMapping(value = "/registration", method = RequestMethod.GET)
//    public String registrationGet(Model model){
//        checkAuthorize(model);
//        User user = new User();
//        model.addAttribute("user", user);
//        return "registration";
//    }
//
//    @RequestMapping(value = "/registration", method = RequestMethod.POST)
//    public String registrationPost(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) throws Exception {
//        userValidator.trimFields(user);
//        userValidator.validate(user, result);
//        log.info(user.toString());
//        if(result.hasErrors()){
//            model.addAttribute("user", user);
//            checkAuthorize(model);
//            return "registration";
//        }
//        userService.create(user);
//        return "redirect:/login?reg";
//
//    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginGet(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           @RequestParam(value = "reg", required = false) String reg,
                           Model model){
        checkAuthorize(model);
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("reg", reg != null);
        return "/login";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model){
        checkAuthorize(model);
        return "/home";
    }
}
