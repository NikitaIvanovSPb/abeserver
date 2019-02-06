package ru.nikita.abeserver.services.impl;

import org.springframework.stereotype.Service;
import ru.nikita.abeserver.domain.ABEKey;
import ru.nikita.abeserver.repositories.ABEKeyRepository;
import ru.nikita.abeserver.services.CryptoService;
import ru.nikita.openabeapi.OpenABEApi;
import ru.nikita.openabeapi.Schema;
import ru.nikita.openabeapi.dao.MasterKeysObj;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

@Service
public class CryptoServiceImpl implements CryptoService {

    private OpenABEApi openABEApi;
    private String abeSchemaName;
    private Schema abeSchema;
    private ABEKeyRepository abeKeyRepository;
    private static final String ABE_WORKING_NAME = "working";

    public CryptoServiceImpl(OpenABEApi openABEApi, ABEKeyRepository abeKeyRepository) {
        this.abeKeyRepository = abeKeyRepository;
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
            Properties prop = new Properties();
            prop.load(input);
            abeSchemaName = prop.getProperty("openabe.schema");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.openABEApi = openABEApi;
        abeSchema = Schema.valueOf(abeSchemaName);
    }


    private MasterKeysObj generateAndSetABEMasterKey(){
        return openABEApi.generateMasterKeys(abeSchema, ABE_WORKING_NAME);
    }

    @Override
    public String abeEncoding(String atr, String textForEncoding){
        return openABEApi.encoding(abeSchema, atr, ABE_WORKING_NAME, textForEncoding);
    }

    private boolean setABEMasterKey(MasterKeysObj masterKeysObj){
        return openABEApi.setMasterKeys(masterKeysObj, abeSchema, ABE_WORKING_NAME);
    }

    @Override
    public String generateUserKey(String atrs){
        return openABEApi.generateUserKey(abeSchema, atrs, ABE_WORKING_NAME);
    }

    private String removeABEMasterKey(){
        return openABEApi.remuveMasterKey(abeSchema, ABE_WORKING_NAME);
    }

    private byte[] generateKeyAES(){
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(null, UUID.randomUUID().toString().getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
            return secret.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public EncodedDao encodeAES(byte[] toEncode){
        try {
            byte [] key = generateKeyAES();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
            AlgorithmParameters params = cipher.getParameters();
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] encoded = cipher.doFinal(toEncode);
            return new EncodedDao(getBase64StringFromIvAndKey(iv, key), encoded);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decodeAES(byte[] toDecode, String base64Key){
        try {
            byte[] iv = new byte[16];
            byte[] key = new byte[32];
            if(getIvAndKeyFromBase64String(iv, key, base64Key)) {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
                return cipher.doFinal(toDecode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean getIvAndKeyFromBase64String(byte[] iv, byte[] key, String base64){
        try {
            byte[] data = Base64.getDecoder().decode(base64);
            for (int i = 0; i < 16; i++) {
                iv[i] = data[i];
            }
            for (int i = 0; i < 32; i++) {
                key[i] = data[i + 16];
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private String getBase64StringFromIvAndKey(byte[] iv, byte[] key){
        try {
            byte[] data = new byte[48];
            for (int i = 0; i < 16; i++) {
                 data[i] = iv[i];
            }
            for (int i = 0; i < 32; i++) {
                data[i + 16] = key[i];
            }
            return Base64.getEncoder().encodeToString(data);
        }catch (Exception e){
            return null;
        }
    }

    public class EncodedDao{
        private String base64Key;
        private byte[] encoded;

        public EncodedDao(String base64Key, byte[] encoded) {
            this.base64Key = base64Key;
            this.encoded = encoded;
        }

        public String getBase64Key() {
            return base64Key;
        }

        public byte[] getEncoded() {
            return encoded;
        }
    }

    @Override
    public Schema getAbeSchema() {
        return abeSchema;
    }

    @Override
    public List<ABEKey> getMasterKeys(){
        List<ABEKey> linkedList = new LinkedList<>();
        abeKeyRepository.findAll().forEach(linkedList::add);
        return linkedList;
    }

    @Override
    public ABEKey getActiveMasterKey(){
        Optional<ABEKey> firstByActive = abeKeyRepository.findFirstByActive(true);
        return firstByActive.orElse(null);
    }

    @Override
    public ABEKey createMasterKey(String name){
        ABEKey masterKey = new ABEKey();
        masterKey.setName(name);
        String id = UUID.randomUUID().toString();
        MasterKeysObj masterKeysObj = openABEApi.generateMasterKeys(abeSchema, id);
        if(!openABEApi.remuveMasterKey(abeSchema, id).equals("remove")) throw new RuntimeException("error");
        masterKey.setPublicKey(masterKeysObj.getPublicKey());
        masterKey.setSecretKey(masterKeysObj.getSecretKey());
        masterKey.setCreate(new Date());
        masterKey.setActive(false);
        return abeKeyRepository.save(masterKey);
    }

    @Override
    public boolean unsetActiveMasterKey(){
        Optional<ABEKey> firstByActive = abeKeyRepository.findFirstByActive(true);
        if(firstByActive.isPresent()){
            ABEKey abeKey = firstByActive.get();
            abeKey.setActive(false);
            ABEKey save = abeKeyRepository.save(abeKey);
            openABEApi.remuveMasterKey(abeSchema, ABE_WORKING_NAME);
            return save != null;
        }
        return true;

    }

    @Override
    public boolean setActiveMasterKey(Long id){
        Optional<ABEKey> masterKey = abeKeyRepository.findById(id);
        if(!masterKey.isPresent()) throw new RuntimeException("key does not exist");
        if(getActiveMasterKey() != null) unsetActiveMasterKey();
        ABEKey abeKey = masterKey.get();
        abeKey.setActive(true);
        MasterKeysObj masterKeysObj = new MasterKeysObj();
        masterKeysObj.setPublicKey(abeKey.getPublicKey());
        masterKeysObj.setSecretKey(abeKey.getSecretKey());
        if(openABEApi.setMasterKeys(masterKeysObj, abeSchema, ABE_WORKING_NAME)) {
            return abeKeyRepository.save(abeKey) != null;
        }
        return false;
    }

    @Override
    public boolean removeMasterKey(Long id){
        Optional<ABEKey> masterKey = abeKeyRepository.findById(id);
        if (!masterKey.isPresent()) throw new RuntimeException("Key does not exist");
        ABEKey abeKey = masterKey.get();
        if(abeKey.getActive()){
            unsetActiveMasterKey();
        }
        abeKeyRepository.deleteById(id);
        return true;
    }
}
