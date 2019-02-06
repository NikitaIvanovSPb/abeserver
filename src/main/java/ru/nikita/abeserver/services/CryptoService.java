package ru.nikita.abeserver.services;

import ru.nikita.abeserver.domain.ABEKey;
import ru.nikita.abeserver.services.impl.CryptoServiceImpl;
import ru.nikita.openabeapi.Schema;
import ru.nikita.openabeapi.dao.MasterKeysObj;

import java.util.List;

public interface CryptoService {

    String abeEncoding(String atr, String textForEncoding);


    String generateUserKey(String atrs);

    CryptoServiceImpl.EncodedDao encodeAES(byte[] toEncode);

    byte[] decodeAES(byte[] toDecode, String base64Key);

    Schema getAbeSchema();

    List<ABEKey> getMasterKeys();

    ABEKey getActiveMasterKey();

    ABEKey createMasterKey(String name);

    boolean unsetActiveMasterKey();

    boolean setActiveMasterKey(Long id);

    boolean removeMasterKey(Long id);
}
