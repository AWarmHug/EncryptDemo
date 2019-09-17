package com.warm.encryptdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.nio.charset.Charset;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG";
    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAndK6AXnmBKyNsVN0CY+5bOc9uRsPwetdiE4uCKf7l9j7BivfZVyRJQOC+SnaWX9wpp+POXOQ7oQRYXHmoUCYrVYVDFTIe8cc9CZc8HJHXRFibZTkFGD3Lfifdo8LudxPHJSqUju2z/7fNQd3Zj0IQltqi7whaX/WXIccpg2y9yMMQRUywTmrQfRzLiPdPUF+7auoMYVBLlsAsyn/AducB0PGBthYQFxbCWUElE5hUrjEkcBlyz0WPr/NbshbZul4L7bZEiQaib5sa/SjHz50EaOAUf4QmnWDDsxdMaJeDufsrbkQvyQzjrEnRBXPqOhx24GDQdt4G5u4oxU7VZRZOQIDAQAB";
    private String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCd0roBeeYErI2xU3QJj7ls5z25Gw/B612ITi4Ip/uX2PsGK99lXJElA4L5KdpZf3Cmn485c5DuhBFhceahQJitVhUMVMh7xxz0JlzwckddEWJtlOQUYPct+J92jwu53E8clKpSO7bP/t81B3dmPQhCW2qLvCFpf9ZchxymDbL3IwxBFTLBOatB9HMuI909QX7tq6gxhUEuWwCzKf8B25wHQ8YG2FhAXFsJZQSUTmFSuMSRwGXLPRY+v81uyFtm6XgvttkSJBqJvmxr9KMfPnQRo4BR/hCadYMOzF0xol4O5+ytuRC/JDOOsSdEFc+o6HHbgYNB23gbm7ijFTtVlFk5AgMBAAECggEASydVu+44QnGI57A9yW/PdLSJB+SDFGzoFUhlU2nqvNxubPnnX3U9CMsrS0tK1uOA2VeR9GG72li5jhWXJaKFEcW0MDMPZSHj/79k8nr40LC+lkAYfDvQeOuzw9lKVC883PdOQ+p7m4Ba+f2cNetKS48MpY3ouzxM77LHSausVVJwzbsgOwnTbH5722DuIY0dwlmDJOxweAKHdA7EL5nRnuRYgUDaQZUwIVPSp/gznB+w9DJP2Kc3c++lquh5RwB2f0tt3bSyryi4xF9vwTy4TIIFNQz7ngwgUKnjcKnQM4Hs6NpnJAIrxSYbVD32NNqB41DNu5SA5NxkElwAq7C9IQKBgQDv1TPT/jTpN/wnLO2pe+4eLEeY5Owv/GzK7ogTlFb8/n4Y9U9hVPHe/4/PdfnU+K4jcjR7FFYAfy7gLexXg+/FUrUggNuOFZOR6UpeKsFeTRoppiozoYlHEyCGC7jWA4vuka4vFfTkLMUPsnVFuFfwGDyBdUK5ylhD1qk/BhuohwKBgQCodkhJHvDzuvFWfUSPvW1e7B5BlIaEVDo1Ds1tLiQ1kgJRWHaxNOtsCczn+pt1adRyoSCnXBYi+oEIVc8NiaWF+nTIkgzDBX8/M6bjf2DaoVfHOW04sKwy0nDxHUW58NRo23lTupCAV/9htHaE7d43XN+stttQtr2kDuYFkOMgPwKBgHcN+iVxWqivPv/fgLcSqzjVOFGAynJjt0nhsTEvASOj3crb86TYexz89lHBOdzr+d3ksoKqrKqgeA+XHGyxNRRaI0nYTaUxtsjG60Dlron2cldcgjSRKslaiFmidgXfStaKANKk7qVaSBSrvSV4BkdMcLS/FfmOpdQ/OlLr4Ez/AoGBAJOzQSV7D19ipH5kTlyIhQAYnV56ToZvIqmqVMpJDk45ufaATXCJKXAMF6+8tdh39WCbjPmvb2kzoTYhpewEpy+UrBe+lZnXk/1h2AzWfaCynTd7ThvKKvBUOdntBFlGobVd2D5k8JTWkn6jrtZVh+OadjIEEL3vus3AkkSHB9wZAoGBAOIQaYIlZD9Vjho9BDkfiAJGwD1jJWRNsusEs5k/lj5o0NU7WaAOTMPHZTwNs8RAk9w71C74Bi70e/7a3NX40MiREBaYKje2HEpshvfwnZAaBShYEoU/3PxvfE+iF6Q3shNW1vnTd2uR+Jur/BZACndXegCJJPLxms8ob9Ildztp";

    @BindView(R.id.tv2)
    TextView tv2;
    private String str = "你好啊！";

    @BindView(R.id.tv)
    TextView tv;

    @BindView(R.id.tv3)
    TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        String strBytes = Arrays.toString(str.getBytes());
        Log.d(TAG, "onCreate: str" + strBytes);

        Log.d(TAG, "onCreate: " + Arrays.toString(Base64.decode("5L2g5aW95ZWK77yB", Base64.DEFAULT)));
        Log.d(TAG, "onCreate: " + new String(Base64.decode("5L2g5aW95ZWK77yB", Base64.DEFAULT)));


        byte[] encodeBytes = Base64.encode(str.getBytes(), Base64.DEFAULT);
        String encode = Arrays.toString(encodeBytes);
        Log.d(TAG, "onCreate: encode:--------" + encode);
        String encodeToString = Base64.encodeToString(str.getBytes(), Base64.DEFAULT);

        Log.d(TAG, "onCreate: encodeToString:" + encodeToString);

        Log.d(TAG, "onCreate: " + Arrays.toString(Base64.decode(encodeToString, Base64.DEFAULT)));

        byte[] decodeBytes = Base64.decode(encodeBytes, Base64.DEFAULT);
        Log.d(TAG, "onCreate: decodeBytes" + Arrays.toString(decodeBytes));

        Log.d(TAG, "onCreate: decodeString" + new String(decodeBytes));

        Log.d(TAG, "onCreate: " + Arrays.toString(str.getBytes(Charset.forName("US-ASCII"))));

        Log.d(TAG, "onCreate: ------------------------------------------------------");

        String key = AesCbcUtil.generateKeyToString(AesCbcUtil.KEY_SIZE_256);
        Log.d(TAG, "onCreate: key=" + key);

        String encryptText = AesCbcUtil.encrypt(str, key);
        Log.d(TAG, "onCreate: encryptText=" + encryptText);

        String decryptText = AesCbcUtil.decrypt(encryptText, key);
        Log.d(TAG, "onCreate: decryptText=" + decryptText);

        Log.d(TAG, "onCreate: ------------------------------------------------------");
//        KeyPair keyPair = RsaUtil.generateKeyPair(RsaUtil.LENGTH_2048);
//        PublicKey publicKey = keyPair.getPublic();
//        PrivateKey privateKey = keyPair.getPrivate();
//        Log.d(TAG, "onCreate: publicKey=" + new String(publicKey.getEncoded()));
//        Log.d(TAG, "onCreate: privateKey=" + new String(privateKey.getEncoded()));
        String encryptRsa = RsaUtil.encryptByPublicKey("sjsjskdkjsnkdnjsdnakndsnjds", RsaUtil.generatePublic(publicKey));
        Log.d(TAG, "onCreate: " + encryptRsa);
        String decryptRsa = RsaUtil.decryptToStringByPrivateKey(encryptRsa, RsaUtil.generatePrivate(privateKey));

        Log.d(TAG, "onCreate: " + decryptRsa);

        Log.d(TAG, "onCreate: " + GetSignature.getSignature(this));

        tv3.setText(GetSignature.getKey(this));

    }
}
