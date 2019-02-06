package ru.nikita.abeserver.services.impl;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.nikita.abeserver.domain.FTP;
import ru.nikita.abeserver.domain.File;
import ru.nikita.abeserver.domain.dto.AdminFTPDTO;
import ru.nikita.abeserver.repositories.FTPRepository;
import ru.nikita.abeserver.repositories.FileRepository;
import ru.nikita.abeserver.services.CryptoService;
import ru.nikita.abeserver.services.FileService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Qualifier("FTPFS")
@Service
public class FileServiceFTPFSImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileService.class);
    private FileRepository fileRepository;
    private CryptoService cryptoService;
    private FTPRepository ftpRepository;

    private String PATH = "/home/nikita/test/upload";

    public FileServiceFTPFSImpl(FileRepository fileRepository, CryptoService cryptoService, FTPRepository ftpRepository) {
        this.fileRepository = fileRepository;
        this.cryptoService = cryptoService;
        this.ftpRepository = ftpRepository;
    }

    @Override
    public File addFile(InputStream inputStream, String atrs, String name, Long ftpId){
        try {
            atrs += " AND expTime>" + (new Date().getTime()/1000);
            Optional<FTP> ftp = ftpRepository.findById(ftpId);
            if(!ftp.isPresent()) return null;
            File file = new File();
            byte[] targetArray = new byte[inputStream.available()];
            inputStream.read(targetArray);
            CryptoServiceImpl.EncodedDao encodedDao = cryptoService.encodeAES(targetArray);
            String aesKeyEncoded = cryptoService.abeEncoding(atrs, encodedDao.getBase64Key());
            if(aesKeyEncoded == null) throw new RuntimeException("ABE crypto exception");
            file.setAESKeyBase64(aesKeyEncoded);
//            file.setAESKeyBase64("aeskey");
            file.setName(name);
            file.setAttributes(atrs);
            String uuid;
            do{
                uuid = UUID.randomUUID().toString();
            }while (fileRepository.findFirstByGuidEquals(uuid).isPresent());
            file.setGuid(uuid);
            Files.write(Paths.get(PATH + "/" + file.getGuid()), encodedDao.getEncoded());
            if(uploadFTPFile(file.getGuid(), Files.newInputStream(Paths.get(PATH + "/" + file.getGuid())), ftp.get())){
                Files.delete(Paths.get(PATH + "/" + file.getGuid()));
                file.setFtp(ftp.get());
                file.setCreate(new Date());
                logger.info("Создан файл: " + file.getName() + " на сервере: " + file.getFtp().getUrl() + ":" + file.getFtp().getUrl());
                return fileRepository.save(file);
            }else{
                //@TODO добавить обработку ошибки
            }
//            Path link = Files.write(Paths.get(PATH + "/" + file.getGuid()), targetArray);

        } catch (NoSuchFileException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public File getFileByGuid(String guid){
        Optional<File> file = fileRepository.findFirstByGuidEquals(guid);
        if(file.isPresent()){
            logger.info("Запрос на файл. Файл найден: " + guid);
            return file.get();
        }else {
            logger.info("Запрос на файл. Файл не найден: " + guid);
            return null;
        }
    }

    @Override
    public List<File> getFiles(){
        List<File> list = new LinkedList<>();
        fileRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public void deleteFile(String guid){
        logger.info("Удаление файла: " + guid);
        Optional<File> file = fileRepository.findFirstByGuidEquals(guid);
        if(file.isPresent()) {
            fileRepository.delete(file.get());
            deleteFTPFile(file.get());
        }
    }

    private boolean uploadFTPFile(String filename, InputStream inputStream, FTP ftp){
        FTPClient client = new FTPClient();
        boolean stored = false;
        try {
            client.connect(ftp.getUrl(), ftp.getPort());
            client.login(ftp.getAdminLogin(), ftp.getAdminPass());
            stored = client.storeFile("/" + filename, inputStream);
            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stored;
    }

    private boolean deleteFTPFile(File file){
        FTP ftp = file.getFtp();
        FTPClient client = new FTPClient();
        boolean removed = false;
        try {
            client.connect(ftp.getUrl(), ftp.getPort());
            client.login(ftp.getAdminLogin(), ftp.getAdminPass());
            removed = client.deleteFile("/" + file.getGuid());
            client.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return removed;
    }

    @Override
    public List<FTP> getFTP(){
        List<FTP> list = new LinkedList<>();
        ftpRepository.getAllByDeletedIsFalse().forEach(list::add);
        return list;
    }

    @Override
    public FTP createFTP(AdminFTPDTO ftpdto){
        FTP ftp = new FTP();
        ftp.setAdminLogin(ftpdto.getAdminLogin());
        ftp.setAdminPass(ftpdto.getAdminPass());
        ftp.setUserLogin(ftpdto.getUserLogin());
        ftp.setUserPass(ftpdto.getUserPass());
        ftp.setPort(ftpdto.getPort());
        ftp.setUrl(ftpdto.getUrl());
        ftp.setDeleted(false);
        return ftpRepository.save(ftp);
    }

    @Override
    public FTP updateFTP(AdminFTPDTO ftpdto){
        Optional<FTP> byId = ftpRepository.findById(ftpdto.getId());
        if(!byId.isPresent()){
            return null;
        }
        FTP ftp = byId.get();
        ftp.setAdminLogin(ftpdto.getAdminLogin());
        ftp.setAdminPass(ftpdto.getAdminPass());
        ftp.setUserLogin(ftpdto.getUserLogin());
        ftp.setUserPass(ftpdto.getUserPass());
        ftp.setPort(ftpdto.getPort());
        ftp.setUrl(ftpdto.getUrl());
        return ftpRepository.save(ftp);
    }

    @Override
    public boolean removeFTP(AdminFTPDTO ftpdto){
        Optional<FTP> byId = ftpRepository.findById(ftpdto.getId());
        if(!byId.isPresent()) {
            return false;
        }
        FTP ftp = byId.get();
        ftp.setDeleted(true);
        FTP save = ftpRepository.save(ftp);
        return save != null;
    }


}
