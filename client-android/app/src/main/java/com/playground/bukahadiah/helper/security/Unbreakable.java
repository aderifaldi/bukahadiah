package com.playground.bukahadiah.helper.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by aderifaldi on 14/12/2016.
 */

public class Unbreakable {
    private static byte[] w = { 0x24, 0x68, 0x78, 0x71, 0x49, 0x73, 0x41,
            0x24, 0x28, 0x78, 0x41, 0x49, 0x63, 0x41,0x73, 0x9};
    public static String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(w, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            final String encryptedString = Base64.encodeBase64String(cipher
                    .doFinal(strToEncrypt.getBytes()));
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String decrypt(String strToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            final SecretKeySpec secretKey = new SecretKeySpec(w, "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            final String decryptedString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
            return decryptedString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getEncryptedString(String str) {
        final String strToEncrypt = str;
        final String encryptedStr = Unbreakable.encrypt(strToEncrypt);
        return encryptedStr;
    }
    public static String getDecriyptedString(String str) {
        final String strToDecrypt = str;
        final String decryptedStr = Unbreakable.decrypt(strToDecrypt);
        return decryptedStr;
    }
}
