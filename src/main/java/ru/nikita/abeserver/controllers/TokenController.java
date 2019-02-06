package ru.nikita.abeserver.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.services.TokenService;
import ru.nikita.abeserver.services.UserService;

import javax.servlet.http.HttpServletResponse;

@RestController
public class TokenController {
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);
    private TokenService tokenService;
    private UserService userService;

    private User getUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByLogin(userDetails.getUsername());
    }

    public TokenController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }


    @RequestMapping(value = "/user/token", method = RequestMethod.POST)
    public String generetaKey(@RequestParam(value = "token_guid") String guid) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByLogin(userDetails.getUsername());
        String str = tokenService.getKeyByToken(guid, user.getId());
        if(str == null){
            return "error";
        }else{
            logger.info("Пользователь: " + getUser().getLogin() + " запросил ключ по токену: " + guid);
            return str;
        }
    }

//    @RequestMapping(value = "/admin/token/add", method = RequestMethod.GET)
//    public String addTokenGet(Model model){
//
//    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleException3(NumberFormatException ex, HttpServletResponse response) {

    }


}
