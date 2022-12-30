package com.util.Encryption;

import com.model.User;
import com.model.jwt.RootUser;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public interface Encrypt {
    public <T> T getSessionParameter(String token, String key);

    public HashMap<String, Object> decryptJWT(String encryptedJWT);

    public String encryptJWT(RootUser user);

    public String encryptSHA256(String msg);

    public String bytesToHex(byte[] bytes);

    public String encryptAES(String text, boolean isSlashIncludedString);

    public String decryptAES(String cipherText);
}
