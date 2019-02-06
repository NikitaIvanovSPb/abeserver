package ru.nikita.abeserver.services;

import ru.nikita.abeserver.domain.FTP;
import ru.nikita.abeserver.domain.File;
import ru.nikita.abeserver.domain.dto.AdminFTPDTO;

import java.io.InputStream;
import java.util.List;

public interface FileService {


    File addFile(InputStream inputStream, String atrs, String name, Long ftpId);

    File getFileByGuid(String guid);

    List<File> getFiles();

    void deleteFile(String guid);

    List<FTP> getFTP();

    FTP createFTP(AdminFTPDTO ftpdto);

    FTP updateFTP(AdminFTPDTO ftpdto);

    boolean removeFTP(AdminFTPDTO ftpdto);
}
