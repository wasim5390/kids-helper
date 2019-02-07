package com.uiu.helper.KidsHelper.helpers;

/**
 * Created by Awais on 06/07/18.
 */


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Awais on 06/07/18.
 */
public class EncryptionHelper {

    private static final String encryptionKey = "TN59Wb2dGr5Q6nW0KN4ImALf364m5mxW";

    public static String encrypt(String plainText) {

        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            return new String(Base64Coder.encode(encryptedBytes));
        } catch (Exception ex) {
            return "";
        }
    }

    public static String decrypt(String encrypted) {

        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
            byte[] plainBytes = cipher.doFinal(Base64Coder.decode(encrypted));
            return new String(plainBytes);
        } catch (Exception ex) {
            return "";
        }
    }

    private static Cipher getCipher(int cipherMode) throws Exception {

        String encryptionAlgorithm = "AES";
        SecretKeySpec keySpecification = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), encryptionAlgorithm);
        Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
        cipher.init(cipherMode, keySpecification);
        return cipher;
    }
}


