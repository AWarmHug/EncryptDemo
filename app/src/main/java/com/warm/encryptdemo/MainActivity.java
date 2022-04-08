package com.warm.encryptdemo;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.warm.encryptdemo.databinding.ActivityMainBinding;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";

    private ActivityMainBinding mBinding;

    private String publicKeyStr;
    private String privateKeyStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        final String str = "你好啊";


        mBinding.tvBase64Encode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Base64: ------------------------------------------------------");

                final String str = mBinding.etContent.getText().toString();

                byte[] encodeBytes = Base64.encode(str.getBytes(), Base64.DEFAULT);
                String encode = Arrays.toString(encodeBytes);
                Log.d(TAG, "onCreate: encode:--------" + encode);

                String encodeToString = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
                Log.d(TAG, "onCreate: encodeToString:" + encodeToString);

                mBinding.tvContent.setText(encodeToString);
            }
        });
        mBinding.tvBase64Decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encodeStr = mBinding.tvContent.getText().toString();
                byte[] decodeBytes = Base64.decode(encodeStr, Base64.DEFAULT);
                Log.d(TAG, "onCreate: decodeBytes = " + Arrays.toString(decodeBytes));
                String decodeStr = new String(decodeBytes);
                Log.d(TAG, "onCreate: decodeString = " + decodeStr);

                mBinding.tvContent.setText(decodeStr);
            }
        });

        final String key = AesCbcUtil.generateKeyToString(AesCbcUtil.KEY_SIZE_256);
        Log.d(TAG, "onCreate: key=" + key);

        mBinding.tvAesEncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Aes: ------------------------------------------------------");

                final String str = mBinding.etContent.getText().toString();
                String encryptText = AesCbcUtil.encrypt(str, key);
                Log.d(TAG, "onCreate: encryptText=" + encryptText);
                mBinding.tvContent.setText(encryptText);

            }
        });

        mBinding.tvAesDocode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aesEncryptStr = mBinding.tvContent.getText().toString();

                String decryptText = AesCbcUtil.decrypt(aesEncryptStr, key);
                Log.d(TAG, "onCreate: decryptText=" + decryptText);
                mBinding.tvContent.setText(decryptText);

            }
        });


        mBinding.tvGenerateKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Rsa: ------------------------------------------------------");

                KeyPair keyPair = RsaUtil.generateKeyPair(RsaUtil.LENGTH_2048);
                PublicKey publicKey = keyPair.getPublic();
                PrivateKey privateKey = keyPair.getPrivate();
                publicKeyStr = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
                privateKeyStr = Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT);
                Log.d(TAG, "onCreate: publicKey=" + publicKeyStr);
                Log.d(TAG, "onCreate: privateKey=" + privateKeyStr);
            }
        });

        mBinding.tvRsaEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String str = mBinding.etContent.getText().toString();

                String encryptRsa = RsaUtil.encryptByPublicKey(str, RsaUtil.generatePublic(publicKeyStr));
                Log.d(TAG, "onCreate: " + encryptRsa);
                mBinding.tvContent.setText(encryptRsa);
            }
        });

        mBinding.tvRsaDecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rsaEncryptStr = mBinding.tvContent.getText().toString();

                String decryptRsa = RsaUtil.decryptToStringByPrivateKey(rsaEncryptStr, RsaUtil.generatePrivate(privateKeyStr));
                Log.d(TAG, "onCreate: " + decryptRsa);
                mBinding.tvContent.setText(decryptRsa);
            }
        });

        mBinding.tvGetSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Rsa: ------------------------------------------------------");
               String getSignature = GetSignature.getSignature(MainActivity.this);
                mBinding.tvContent.setText(getSignature);

            }
        });



        mBinding.tvTestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "TestTime: ------------------------------------------------------");

                String value = readAssetsTxt(MainActivity.this, "data.json");
                long time = System.currentTimeMillis();

                String aesEncrypt = AesCbcUtil.encrypt(value, key);
                long timeAesEncrypt = System.currentTimeMillis();
                Log.d(TAG, "AesCbcUtil---加密耗时: " + (timeAesEncrypt - time));

                String aesDecrypt = AesCbcUtil.decrypt(aesEncrypt, key);
                long timeAesDecrypt = System.currentTimeMillis();
                Log.d(TAG, "AesCbcUtil---解密耗时: " + (timeAesDecrypt - timeAesEncrypt));

                String rsaEncrypt = RsaUtil.encryptByPublicKey(value.substring(0,128), RsaUtil.generatePublic(publicKeyStr));
                long timeRsaEncrypt = System.currentTimeMillis();
                Log.d(TAG, "RsaUtil---加密耗时: " + (timeRsaEncrypt - timeAesDecrypt));

                String decryptRsa = RsaUtil.decryptToStringByPrivateKey(rsaEncrypt, RsaUtil.generatePrivate(privateKeyStr));
                long timeRsaDecrypt = System.currentTimeMillis();
                Log.d(TAG, "RsaUtil---解密耗时: " + (timeRsaDecrypt - timeRsaEncrypt));


            }
        });

        mBinding.tvMockHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MockHttpActivity.class));
            }
        });
        mBinding.tvNativeKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mBinding.tvContent.setText(GetSignature.getKey(MainActivity.this));
            }
        });


    }

    public static String readAssetsTxt(Context context, String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "读取错误，请检查文件名";
    }

}
