package ru.nikita.abeserver.controllers;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.nikita.abeserver.domain.FTP;
import ru.nikita.abeserver.domain.File;
import ru.nikita.abeserver.domain.Token;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.domain.dto.*;
import ru.nikita.abeserver.services.*;
import ru.nikita.abeserver.validators.UserValidator;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedList;

@RestController
@Slf4j
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    private UserService userService;
    private FileService fileService;
    private UserValidator userValidator;
    private CryptoService cryptoService;
    private TokenService tokenService;
    private MailService mailService;
    private final int LIMIT = 10;


    public AdminController(UserService userService, @Qualifier("FTPFS") FileService fileService, UserValidator userValidator, CryptoService cryptoService, TokenService tokenService, MailService mailService) {
        this.userService = userService;
        this.fileService = fileService;
        this.userValidator = userValidator;
        this.cryptoService = cryptoService;
        this.tokenService = tokenService;
        this.mailService = mailService;
    }

    private User getUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByLogin(userDetails.getUsername());
    }

    //                                                       \\
    // ============= Управление пользователями ============= \\
    //                                                       \\

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public UserDTOList getUsers(){
        return UserDTO.getDTOList(userService.getUsers());
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.POST)
    public UserDTO postUsers(@RequestBody UserDTO userDTO){
        if(userDTO.getId() == -1L){
            return UserDTO.convert(userService.create(userDTO));
        }else{
            return UserDTO.convert(userService.update(userDTO));
        }
    }

    @RequestMapping(value = "/admin/users/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long id){
        userService.delete(id);
    }

    //                                                  \\
    // ============= Управление токенами =============  \\
    //                                                  \\

    @RequestMapping(value = "/admin/tokens/get", method = RequestMethod.POST)
    public TokenDTOList getTokens(HttpServletResponse response,@RequestBody TokenObj obj){
        User byId = userService.findById(Long.parseLong(obj.getUserId()));
        if(byId == null) {
            response.setStatus(500);
            return null;
        }
        return TokenDTO.getDTOList(new LinkedList<>(byId.getTokens()));
    }

    @RequestMapping(value = "/admin/tokens", method = RequestMethod.POST)
    public void createToken(HttpServletResponse response, @RequestBody TokenObj obj) {
        Token token = tokenService.createToken(obj.getAttrs(), new Date(obj.getExpTime()), Long.parseLong(obj.getUserId()));
        if (token == null) {
            response.setStatus(500);
        } else {
            logger.info("Администратор: " + getUser().getLogin() + " создал токен: " + token.getGuid() + " для пользователя: " + token.getUser().getLogin());
        }
    }

    @RequestMapping(value = "/admin/tokens", method = RequestMethod.DELETE)
    public void deleteToken(HttpServletResponse response, @RequestBody TokenObj obj){
        if(!tokenService.deleteToken(obj.getGuid())){
            response.setStatus(500);
        }else {
            logger.info("Администратор: " + getUser().getLogin() + " удалил токен: " + obj.getGuid());
        }

    }

    //                                                  \\
    // ============= Управление файлами =============   \\
    //                                                  \\

    @RequestMapping(value = "/admin/files", method = RequestMethod.GET)
    public FileDTOList getFiles(){
        return FileDTO.getDTOList(fileService.getFiles());
    }

    @RequestMapping(value = "/admin/files/{id}", method = RequestMethod.DELETE)
    public void deleteFile(@PathVariable String id){
        fileService.deleteFile(id);
        logger.info("Администратор: " + getUser().getLogin() + " удалил файл: " + id);
    }

    @RequestMapping(value = "/admin/files/send", method = RequestMethod.POST)
    public void deleteFile(HttpServletResponse response, @RequestBody SendMailObject sendMailObject){
        User userById = userService.findById(sendMailObject.getUserId());
        File fileByGuid = fileService.getFileByGuid(sendMailObject.getFileId());
        if(userById != null && fileByGuid != null){
            mailService.SendFileForUser(userById, fileByGuid);
            logger.info("Администратор: " + getUser().getLogin() + " отправил файл: " + fileByGuid.getGuid() + " пользователю: " + userById.getLogin());
        }else {
            response.setStatus(500);
        }
    }

    //                                                  \\
    // ============= Управление ключами =============   \\
    //                                                  \\

    @RequestMapping(value = "/admin/attrs", method = RequestMethod.GET)
    public AttributeDTOList getAttrs() {
        return tokenService.getUserAttrs();
    }

    @RequestMapping(value = "/admin/keys", method = RequestMethod.GET)
    public ABEKeyDTOList getKeys() {
        return ABEKeyDTO.getDTOList(cryptoService.getMasterKeys());
    }

    @RequestMapping(value = "/admin/keys", method = RequestMethod.POST)
    public ABEKeyDTO generateKey(@RequestBody StringNameDTO name){

        ABEKeyDTO convert = ABEKeyDTO.convert(cryptoService.createMasterKey(name.name));
        logger.info("Администратор: " + getUser().getLogin() + " создал ключ: " + convert.getName() + "(id: " + convert.getId() + ")");
        return convert;
    }

    @RequestMapping(value = "/admin/keys/set", method = RequestMethod.POST)
    public void setKey(HttpServletResponse response, @RequestBody LongIdDTO id){
        if (cryptoService.setActiveMasterKey(id.id)){
            response.setStatus(200);
            logger.info("Администратор: " + getUser().getLogin() + " установил актианым ключ c id: " + id.getId());
        }else{
            response.setStatus(500);
        }
    }

    @RequestMapping(value = "/admin/keys/unset", method = RequestMethod.POST)
    public void setKey(HttpServletResponse response){
        if (cryptoService.unsetActiveMasterKey()){
            response.setStatus(200);
            logger.info("Администратор: " + getUser().getLogin() + " сросил актианый ключ");
        }else{
            response.setStatus(500);
        }
    }

    @RequestMapping(value = "/admin/keys", method = RequestMethod.DELETE)
    public void removeKey(HttpServletResponse response, @RequestBody LongIdDTO id){
        if(cryptoService.removeMasterKey(id.id)){
            response.setStatus(200);
            logger.info("Администратор: " + getUser().getLogin() + " удалил ключ c id:" + id);
        }else{
            response.setStatus(500);
        }
    }

    //                                                  \\
    // ============= Управление FTP =============       \\
    //                                                  \\

    @RequestMapping(value = "/admin/ftp", method = RequestMethod.GET)
    public AdminFTPDTOList getFtp(){
        return AdminFTPDTO.getDTOList(fileService.getFTP());
    }

    @RequestMapping(value = "/admin/ftp", method = RequestMethod.POST)
    public AdminFTPDTO postFtp(HttpServletResponse response, @RequestBody AdminFTPDTO ftpdto){
        FTP ftp;
        if(ftpdto.getId() == -1){
            ftp = fileService.createFTP(ftpdto);
        }else {
            ftp = fileService.updateFTP(ftpdto);
        }
        if(ftp == null){
            response.setStatus(500);
            return null;
        }
        return AdminFTPDTO.convert(ftp);
    }

    @RequestMapping(value = "/admin/ftp", method = RequestMethod.DELETE)
    public void deleteFtp(HttpServletResponse response, @RequestBody AdminFTPDTO ftpdto){
        if(!fileService.removeFTP(ftpdto)) response.setStatus(500);
    }


    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleException3(NumberFormatException ex, HttpServletResponse response) {

    }

    @Data
    static class StringNameDTO{
        private String name;

        public StringNameDTO(){}
    }

    @Data
    static class LongIdDTO{
        private long id;

        public LongIdDTO(){}
    }

    @Data
    static class TokenObj{
        private String userId;
        private String attrs;
        private Long expTime;
        private String guid;

        public TokenObj() {
        }
    }

    @Data
    static class SendMailObject{
        private Long userId;
        private String fileId;

        public SendMailObject() {}
    }
}
