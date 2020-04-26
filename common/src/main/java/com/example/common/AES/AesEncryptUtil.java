package com.example.common.AES;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import android.util.Base64;

/**
 * @author huangwenyu
 * @program rsa
 * @create 2020-04-21
 */
public class AesEncryptUtil {

    public static String encrypt(String content, String password) throws Exception {
        //1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        //2.根据password规则初始化密钥生成器
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
        secureRandom.setSeed(password.getBytes("UTF-8"));
        keyGenerator.init(128, secureRandom);
        //3.产生原始对称密钥
        SecretKey originalKey = keyGenerator.generateKey();
        //4.获得原始对称密钥的字节数组
        byte[] raw = originalKey.getEncoded();
        //5.根据字节数组生成AES密钥
        SecretKey key = new SecretKeySpec(raw, "AES");
        //6.根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance("AES");
        //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
        byte[] byteEncode = content.getBytes("UTF-8");
        //9.根据密码器的初始化方式--加密：将数据加密
        byte[] byteAes = cipher.doFinal(byteEncode);
        //10.将加密后的数据转换为字符串
        String contentEncoded = Base64.encodeToString(byteAes, Base64.DEFAULT);
        //11.将字符串返回
        return contentEncoded;
    }

    public static String decrypt(String content, String password) throws Exception {
        //1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        //2.根据password规则初始化密钥生成器
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", new CryptoProvider());
        random.setSeed(password.getBytes("UTF-8"));
        keygen.init(128, random);
        //3.产生原始对称密钥
        SecretKey originalKey = keygen.generateKey();
        //4.获得原始对称密钥的字节数组
        byte[] raw = originalKey.getEncoded();
        //5.根据字节数组生成AES密钥
        SecretKey key = new SecretKeySpec(raw, "AES");
        //6.根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance("AES");
        //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.DECRYPT_MODE, key);
        //8.将加密并编码后的内容解码成字节数组
        byte[] byteContent = Base64.decode(content, Base64.DEFAULT);
        /*
         * 解密
         */
        byte[] byteDecode = cipher.doFinal(byteContent);
        String contentDecoded = new String(byteDecode, "UTF-8");
        return contentDecoded;
    }
}
