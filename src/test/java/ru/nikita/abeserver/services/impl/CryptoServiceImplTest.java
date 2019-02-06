package ru.nikita.abeserver.services.impl;

import org.junit.Test;
import ru.nikita.abeserver.repositories.ABEKeyRepository;
import ru.nikita.openabeapi.OpenABEApi;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.*;

public class CryptoServiceImplTest {

    @Test
    public void testCrypto() throws IOException {
        ABEKeyRepository abeKeyRepository = null;
        CryptoServiceImpl cryptoService = new CryptoServiceImpl(new OpenABEApi(), abeKeyRepository);
        String testString = "string for test";
        CryptoServiceImpl.EncodedDao encoded = cryptoService.encodeAES(testString.getBytes(UTF_8));
        byte[] decoded = cryptoService.decodeAES(encoded.getEncoded(), encoded.getBase64Key());
        assertArrayEquals(testString.getBytes(), decoded);
    }

}