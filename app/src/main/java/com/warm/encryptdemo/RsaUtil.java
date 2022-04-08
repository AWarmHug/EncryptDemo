package com.warm.encryptdemo;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * 作者：warm
 * 时间：2018-01-29 15:14
 * 描述：
 */
public class RsaUtil {
    //https://blog.csdn.net/binjianliu/article/details/77890800

    public static final String ALGORITHM = "RSA";
    public static final int LENGTH_1024 = 1024;
    public static final int LENGTH_2048 = 2048;
    public static final String TRANSFORMATION = "RSA/NONE/PKCS1Padding";

    public static KeyPair generateKeyPair(int length) {
        KeyPair keyPair = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM);
            generator.initialize(length);
            keyPair = generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPair;
    }

    /**
     * 根据字符串生成PublicKey;
     *
     * @param publicKey
     * @return
     */
    public static PublicKey generatePublic(String publicKey) {
        PublicKey key = null;
        byte[] keyBytes = Base64.decode(publicKey, Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        try {
            key = KeyFactory.getInstance(ALGORITHM).generatePublic(keySpec);

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return key;
    }

    public static PrivateKey generatePrivate(String privateKey) {
        PrivateKey key = null;
        byte[] keyBytes = Base64.decode(privateKey, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec=new PKCS8EncodedKeySpec(keyBytes);
        try {
            key=KeyFactory.getInstance(ALGORITHM).generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return key;
    }


    private static byte[] handleData(byte[] textBytes, Key key, int mode) {
        byte[] result = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(mode, key);
            result = cipher.doFinal(textBytes);
        } catch (NoSuchAlgorithmException | IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 公钥加密
     *
     * @param text
     * @param publicKey
     * @return
     */
    public static String encryptByPublicKey(String text, Key publicKey) {
        return encryptByPublicKey(text.getBytes(), publicKey);
    }

    /**
     * 公钥加密
     *
     * @param text
     * @param publicKey
     * @return
     */
    public static String encryptByPublicKey(byte[] text, Key publicKey) {
        byte[] resultBytes = handleData(text, publicKey, Cipher.ENCRYPT_MODE);
        return Base64.encodeToString(resultBytes, Base64.DEFAULT);
    }


    public static byte[] decryptByPrivateKey(byte[] text, PrivateKey privateKey) {
        return handleData(Base64.decode(text, Base64.DEFAULT), privateKey, Cipher.DECRYPT_MODE);
    }

    public static String decryptToStringByPrivateKey(byte[] text, PrivateKey privateKey) {
        return new String(decryptByPrivateKey(text, privateKey));
    }

    public static String decryptToStringByPrivateKey(String text, PrivateKey privateKey) {
        return new String(decryptByPrivateKey(text.getBytes(), privateKey));
    }

}
