package com.util.Encryption;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.model.jwt.RootUser;
import com.util.Constant;
import com.util.TimeFormatter;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;

@Service
public class EncryptionService implements Encrypt {
    private final static String SECRET_KEY = "secret";
    private static final String ALG = "AES/CBC/PKCS5Padding";
    private static final String KEY = "0123456789012345";
    private final String IV = KEY.substring(0, 16); // 16byte

    @Override
    public <T> T getSessionParameter(String token, String key) {
        if (token != null) {
            HashMap<String, Object> hashMap = decryptJWT(token);
            return (T) hashMap.get(key);
        } else {
            return null;
        }
    }

    @Override
    public HashMap<String, Object> decryptJWT(String encryptedJWT) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .build(); //Reusable verifier instance
        DecodedJWT jwt = verifier.verify(encryptedJWT);
        HashMap<String, Object> hashMap = new HashMap<>();

        String email = jwt.getClaim(JWTEnum.EMAIL.name()).asString();
        hashMap.put(JWTEnum.EMAIL.name(), email);
        String signature = jwt.getClaim(JWTEnum.SIGNATURE.name()).asString();
        hashMap.put(JWTEnum.SIGNATURE.name(), signature);
        String grant = jwt.getClaim(JWTEnum.GRANT.name()).asString();
        hashMap.put(JWTEnum.GRANT.name(), grant);
        String version = jwt.getClaim(JWTEnum.VERSION.name()).asString();
        hashMap.put(JWTEnum.VERSION.name(), version);

        if (jwt.getClaim(JWTEnum.TOKEN.name()) != null) {
            hashMap.put(JWTEnum.TOKEN.name(), jwt.getClaim(JWTEnum.TOKEN.name()).asString());
        }
        if (jwt.getClaim(JWTEnum.NO.name()) != null) {
            hashMap.put(JWTEnum.NO.name(), jwt.getClaim(JWTEnum.NO.name()).asInt());
        }

        if (jwt.getClaim(JWTEnum.ID.name()) != null) {
            hashMap.put(JWTEnum.ID.name(), jwt.getClaim(JWTEnum.ID.name()).asString());
        }
        return hashMap;
    }

    @Override
    public String encryptJWT(RootUser user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withExpiresAt(TimeFormatter.LongTimeStamp(1))
                    .withClaim(JWTEnum.VERSION.name(), Constant.VERSION)
                    .withClaim(JWTEnum.GRANT.name(), user.getGrant().name())
                    .withClaim(JWTEnum.TOKEN.name(), user.getAccess_token())
                    .withClaim(JWTEnum.EMAIL.name(), user.getEmail())
                    .withClaim(JWTEnum.ID.name(), user.getId())
                    .withClaim(JWTEnum.NO.name(), user.getNo())
                    .withClaim(JWTEnum.SIGNATURE.name(), encryptSHA256(SECRET_KEY))
                    .withIssuer("auth0")
                    .sign(algorithm);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String encryptSHA256(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());
        return bytesToHex(md.digest());
    }

    @Override
    public String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    /**
     * AES256 Encrypt Algorithm
     */


    @Override
    public String encryptAES(String text, boolean isSlashIncludedString) throws Exception {
        Cipher cipher = Cipher.getInstance(ALG);
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        if (isSlashIncludedString) {
            return Base64.getEncoder().encodeToString(encrypted).replaceAll("/", "_");
        } else {
            return Base64.getEncoder().encodeToString(encrypted);
        }
    }

    @Override
    public String decryptAES(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(ALG);
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public String decryptAESWithSlash(String cipherText) throws Exception {
        cipherText = cipherText.replaceAll("_", "/");
        Cipher cipher = Cipher.getInstance(ALG);
        SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(IV.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
