package com.ecom.ecommerce.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility layer for Encryption/Decryption
 */
@Component
public class AESGCMUtil {

    private final String masterKey;

    public AESGCMUtil(@Value("${aesgcm.masterkey.base64}") String masterKey) {
        this.masterKey = masterKey;
    }

    private static final int AES_KEY_SIZE = 256; // bits
    private static final int GCM_IV_LENGTH = 12; // bytes
    private static final int GCM_TAG_LENGTH = 128; // bits

    private SecretKey getKeyFromBase64() {
        byte[] decoded = Base64.getDecoder().decode(masterKey);
        return new SecretKeySpec(decoded, 0, decoded.length, "AES");
    }

//    // Generate a random AES-256 key
//    public static String generateKey() throws Exception {
//        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
//        keyGen.init(AES_KEY_SIZE);
//        SecretKey key = keyGen.generateKey();
//        return Base64.getEncoder().encodeToString(key.getEncoded());
//    }

    /**
     * Encrypt the provided String
     * @param plainText
     * @return
     * @throws Exception
     */
    public String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        cipher.init(Cipher.ENCRYPT_MODE, getKeyFromBase64(), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Store IV + ciphertext together
        ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + cipherText.length);
        byteBuffer.put(iv);
        byteBuffer.put(cipherText);
        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    /**
     * Decrypt the provided String
     * @param encryptedBase64
     * @return
     * @throws Exception
     */
    public String decrypt(String encryptedBase64) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(encryptedBase64);
        ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

        byte[] iv = new byte[GCM_IV_LENGTH];
        byteBuffer.get(iv);

        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, getKeyFromBase64(), new GCMParameterSpec(GCM_TAG_LENGTH, iv));
        byte[] plainText = cipher.doFinal(cipherText);

        return new String(plainText, StandardCharsets.UTF_8);
    }
}

