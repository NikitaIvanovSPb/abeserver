package ru.nikita.abeserver.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.nikita.abeserver.domain.File;
import ru.nikita.abeserver.domain.Token;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.domain.dto.FileDTO;
import ru.nikita.abeserver.domain.dto.TokenDTO;
import ru.nikita.abeserver.services.FileService;
import ru.nikita.abeserver.services.TokenService;
import ru.nikita.abeserver.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserRestController {
    private FileService fileService;
    private TokenService tokenService;
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private User getUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByLogin(userDetails.getUsername());
    }
    public UserRestController(FileService fileService, TokenService tokenService, UserService userService) {
        this.fileService = fileService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @RequestMapping(value = "/user/tokens", method = RequestMethod.GET)
    public List<TokenDTO> getTokens(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findByLogin(userDetails.getUsername());
        List<Token> tokensByUser = tokenService.getTokensByUser(user);
        return TokenDTO.convertList(tokensByUser);
    }

    @RequestMapping(value = "/user/file", method = RequestMethod.POST)
    public FileDTO postUsers(HttpServletResponse response, @RequestParam(value = "file_guid") String guid){
        File fileByGuid = fileService.getFileByGuid(guid);
        if(fileByGuid == null){
            response.setStatus(500);
            return null;
        }else{
            logger.info("Пользователь: " + getUser().getLogin() + " запросил файл: " + guid);
            return FileDTO.convert(fileByGuid);
        }
    }
}
