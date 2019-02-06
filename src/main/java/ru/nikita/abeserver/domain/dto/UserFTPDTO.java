package ru.nikita.abeserver.domain.dto;

import lombok.Data;
import ru.nikita.abeserver.domain.FTP;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserFTPDTO {
    Long id;
    String url;
    Integer port;
    String login;
    String pass;

    public static UserFTPDTO convert(FTP ftp){
        UserFTPDTO ftpdto = new UserFTPDTO();
        ftpdto.setId(ftp.getId());
        ftpdto.setUrl(ftp.getUrl());
        ftpdto.setPort(ftp.getPort());
        ftpdto.setLogin(ftp.getUserLogin());
        ftpdto.setPass(ftp.getUserPass());
        return ftpdto;
    }

    public static List<UserFTPDTO> convertList(List<FTP> list){
        return list.stream()
                .map(UserFTPDTO::convert)
                .collect(Collectors.toList());
    }

}
