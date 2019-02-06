package ru.nikita.abeserver.controllers;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.nikita.abeserver.domain.File;
import ru.nikita.abeserver.domain.User;
import ru.nikita.abeserver.services.FileService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

@Controller
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    String PATH = "./upload";
    private FileService fileService;
    private UserDetails getUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails;
    }
    public FileController(@Qualifier("FTPFS") FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "/admin/upload", method = RequestMethod.POST)
    public void uploadFile(@RequestParam("name") String name, @RequestParam("attributes") String attributes, @RequestParam("ftpId") String ftpId, HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if(!ServletFileUpload.isMultipartContent(request)){
            throw new ServletException("Content type is not multipart/form-data");
        }
        try {
            File upload = fileService.addFile(request.getPart("upload").getInputStream(), attributes, name, Long.parseLong(ftpId));
            if(upload == null) response.setStatus(500);
            else {
                logger.info("Администратор: " + getUser().getUsername() + " загрузил файл: " + upload.getGuid());
            }
        } catch (Exception e) {
            response.setStatus(500);
        }
    }
}
