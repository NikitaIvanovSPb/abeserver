package ru.nikita.abeserver.domain.dto;

import lombok.Data;
import ru.nikita.abeserver.domain.FTP;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class AdminFTPDTO {
    Long id;
    String url;
    Integer port;
    String userLogin;
    String userPass;
    String adminLogin;
    String adminPass;

    public static AdminFTPDTO convert(FTP ftp){
        AdminFTPDTO ftpdto = new AdminFTPDTO();
        ftpdto.setId(ftp.getId());
        ftpdto.setUrl(ftp.getUrl());
        ftpdto.setPort(ftp.getPort());
        ftpdto.setUserLogin(ftp.getUserLogin());
        ftpdto.setUserPass(ftp.getUserPass());
        ftpdto.setAdminLogin(ftp.getAdminLogin());
        ftpdto.setAdminPass(ftp.getAdminPass());
        return ftpdto;
    }

    public static List<AdminFTPDTO> convertList(List<FTP> list){
        return list.stream()
                .map(AdminFTPDTO::convert)
                .collect(Collectors.toList());
    }

    public static AdminFTPDTOList getDTOList(List<FTP> list){
        return new AdminFTPDTOList(convertList(list));
    }
}
