package com.sasd.eventor.services.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.auth0.jwt.algorithms.Algorithm;
import com.sasd.eventor.exception.EventorException;
import com.sasd.eventor.model.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@Service
@AllArgsConstructor
public class SaltService {
    private final int SALT_LENGTH = 16;
    private static final String SECRET = "OdinDlyaLudeiDrugoiDlyMonstrov";
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private static String hmac(String algorithm, String data)
            throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        return bytesToHex(mac.doFinal(data.getBytes()));
    }

    public String createHash(String password, String salt) {
        String hmacSHA256Algorithm = "HmacSHA512";
        try {
            return hmac(hmacSHA256Algorithm, password + salt);
        } catch (Throwable exception) {
            throw new EventorException("Hash creation failed due to '" + exception + "'");
        }
    }

    public String createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return bytesToHex(salt);
    }

    public boolean checkPassword(String password, User user) {
        return user.getPasswordHash().equals(createHash(password, user.getSalt()));
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
//
//    public String createHashs(String password) {
//        // Generate a random salt
//        SecureRandom random = new SecureRandom();
//        byte[] salt = new byte[SALT_LENGTH];
//        random.nextBytes(salt);
//
//        // Hash the password
//        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
//        int hashSize = hash.length;
//
//        // format: algorithm:iterations:hashSize:salt:hash
//        String parts = "sha1:" +
//                PBKDF2_ITERATIONS +
//                ":" + hashSize +
//                ":" +
//                toBase64(salt) +
//                ":" +
//                toBase64(hash);
//        return parts;
//    }
}