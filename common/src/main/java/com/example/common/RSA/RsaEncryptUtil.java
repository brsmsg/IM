package com.example.common.RSA;

import android.os.Build;

import androidx.annotation.RequiresApi;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import android.util.Base64;

/**
 * @author brsmsg
 * @time 2020/4/8
 */
public class RsaEncryptUtil {
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 明文加密最大长度（字节）
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * 解密密文最大长度（字节）
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 存放公钥和私钥
     */
    private static HashMap<Integer, String> keyMap = new HashMap<>();

    /**
     * 生成公钥和私钥，生成的密钥放在keyMap中
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        String privateKey = Base64.encodeToString(rsaPrivateKey.getEncoded(), Base64.DEFAULT);
        String publicKey = Base64.encodeToString(rsaPublicKey.getEncoded(), Base64.DEFAULT);
        keyMap.put(0, publicKey);
        keyMap.put(1, privateKey);
    }

    /**
     * 加密
     * @param content 加密信息
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String publicKey) throws Exception {
        byte[] decodedKey = Base64.decode(publicKey, Base64.DEFAULT);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(new X509EncodedKeySpec(decodedKey));
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
        //分段加密
        byte[] data = content.getBytes("UTF-8");
        byte[] ret = process(cipher, MAX_ENCRYPT_BLOCK, data);
        return Base64.encodeToString(ret,Base64.DEFAULT);
    }

    /**
     * 解密
     * @param content 解密信息
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String content, String privateKey) throws Exception{
        byte[] data = Base64.decode(content, Base64.DEFAULT);
        byte[] decodedKey = Base64.decode(privateKey, Base64.DEFAULT);
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
        byte[] ret = process(cipher, MAX_DECRYPT_BLOCK, data);
        return new String(ret);
    }

    /**
     * 获得公钥
     * @return
     */
    public static String getPublicKey() {
        if (keyMap.size() == 2) {
            return keyMap.get(0);
        }
        return null;
    }

    /**
     * 获得私钥
     * @return
     */
    public static String getPrivateKey() {
        if (keyMap.size() == 2) {
            return keyMap.get(1);
        }
        return null;
    }

    private static byte[] process(Cipher cipher, int size, byte[] data) throws Exception{
        int length = data.length;
        int offset = 0;
        byte[] cache;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while (length - offset > 0) {
            if (length - offset > size) {
                cache = cipher.doFinal(data, offset, size);
            } else {
                cache = cipher.doFinal(data, offset, length - offset);
            }
            output.write(cache, 0, cache.length);
            offset += size;
        }
        byte[] ret = output.toByteArray();
        return ret;
    }
}
