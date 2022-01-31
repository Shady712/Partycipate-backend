package com.sasd.eventor.services.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


@Service
@AllArgsConstructor
public class SaltService {
    private static final String SECRET = "OdinDlyaLudeiDrugoiDlyaChudovish";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static final int HMAC_LENGTH = 128;

    public boolean checkPassword(String password, User user) {
        return user.getPasswordHash().equals(createHash(password, extractSalt(user)));
    }

    public String createHash(String password) {
        SecureRandom random = new SecureRandom();
        byte[] s = new byte[16];
        random.nextBytes(s);
        String salt = bytesToHex(s);
        return createHash(password, salt);
    }

    private String createHash(String password, String salt) {
        String hmacSHA512Algorithm = "HmacSHA512";
        try {
            return hmac(hmacSHA512Algorithm, password + salt) + salt;
        } catch (Throwable exception) {
            throw new EventorException("Hash creation failed due to '" + exception + "'");
        }
    }

    private String hmac(String algorithm, String data)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        return bytesToHex(mac.doFinal(data.getBytes()));
    }

    private String extractSalt(User user) {
        return user.getPasswordHash().substring(HMAC_LENGTH);
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}