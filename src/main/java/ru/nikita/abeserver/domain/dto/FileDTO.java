package ru.nikita.abeserver.domain.dto;

import lombok.Data;
import ru.nikita.abeserver.domain.File;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class FileDTO {
    private String guid;
    private String name;
    private String attributes;
    private UserFTPDTO ftp;
    private Long create;
    private String aesKeyBase64;

    public static FileDTO convert(File file){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setGuid(file.getGuid());
        fileDTO.setAttributes(file.getAttributes());
        fileDTO.setCreate(file.getCreate().getTime());
        fileDTO.setName(file.getName());
        fileDTO.setFtp(UserFTPDTO.convert(file.getFtp()));
        fileDTO.setAesKeyBase64(file.getAESKeyBase64());
        return fileDTO;
    }

    public static List<FileDTO> convertList(List<File> list){
        return list.stream()
                .map(FileDTO::convert)
                .collect(Collectors.toList());
    }

    public static FileDTOList getDTOList(List<File> list){
        return new FileDTOList(convertList(list));
    }
}
