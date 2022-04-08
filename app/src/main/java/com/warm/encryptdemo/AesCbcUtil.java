package com.warm.encryptdemo;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 作者：warm
 * 时间：2018-01-29 11:20
 * 描述：
 */
public class AesCbcUtil {
    public static final String ALGORITHM = "AES";

    public static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    public static final int KEY_SIZE_128 = 128;

    public static final int KEY_SIZE_192 = 192;
    public static final int KEY_SIZE_256 = 256;
    /**
     * iv偏移量应该是16位
     */
    public static final String defaultIv = "1234567891234567";


    /**
     * 随机生成一个秘钥
     */
    public static byte[] generateKey(int keySize) {

        byte[] keyBytes = new byte[0];
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(keySize);
            SecretKey secretKey = keyGenerator.generateKey();
            keyBytes = secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyBytes;
    }

    public static String generateKeyToString(int keySize) {
        return Base64.encodeToString(generateKey(keySize), Base64.DEFAULT);
    }

    public static String encrypt(String text, String key) {
        return encrypt(text, new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), ALGORITHM));
    }


    public static String encrypt(String text, SecretKeySpec secretKeySpec) {
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            byte[] iv = defaultIv.getBytes()/*generateIV(secretKeySpec.getEncoded().length)*/;
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] resultBytes = cipher.doFinal(text.getBytes());
            result = Base64.encodeToString(resultBytes, Base64.DEFAULT);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static byte[] generateIV(int length) {
        byte[] iv = new byte[length];
        Random random = new SecureRandom();
        random.nextBytes(iv);
        return iv;
    }

    public static String decrypt(String text, String key) {
        return decrypt(text, new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), ALGORITHM));
    }

    public static String decrypt(String text, SecretKeySpec secretKeySpec) {
        String result = null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            byte[] iv = defaultIv.getBytes()/*generateIV(secretKeySpec.getEncoded().length)*/;
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] resultBytes = cipher.doFinal(Base64.decode(text.getBytes(), Base64.DEFAULT));
            result = new String(resultBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return result;
    }


}
