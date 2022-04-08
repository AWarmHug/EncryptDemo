package com.warm.encryptdemo;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

public class Http {


    public void doHttp(final Request request, final Callback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Server server = new Server();




                final Response response = server.handle(request);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        }).start();


    }

    interface Callback {
        void onResponse(Response body);
    }


}

class Request {
    String key;

    String jsonEncrypt;

    public Request(String key, String jsonEncrypt) {
        this.key = key;
        this.jsonEncrypt = jsonEncrypt;
    }
}

class Body {
    int code;
    String body;

    public Body(int code, String body) {
        this.code = code;
        this.body = body;
    }
}

class Response {
    int httpCode;
    String jsonEncrypt;

    public Response(int httpCode, String jsonEncrypt) {
        this.httpCode = httpCode;
        this.jsonEncrypt = jsonEncrypt;
    }
}


class Server {
    private String privateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDiT/jlJZQN8haUHBjV+lob0I898/dw8lIUeKe0Wquswsx3IpzEefmyo3sNXSBGmol9dbMdjxhzBXbtYIS4zG02pPRhlA0vkOSFVPFWeVl3FAsJF/Rp23v5twf4Zp6UL6BcoysqHNbKgl1QWX+nZbkapxUYWsbmKuDGCWp+LE6crwNZqxH+zVwgV0IPsD26jl6CAiLnzfY+Ie91tannvAVR1SuYglscaXh8vLfkKPYXkh+s01WfMTuiQpIi3pdHEuzSaKnup5suAx9FHoXku/5hlJDO2qLy2nNDb9nvJCgL7jnzhm2T1mjzupE3ObEcyjl1VDhbu2heqnmZRCLIfuKlAgMBAAECggEAGSBrRaMkIAmWi3/fG2Aowa596OaoZ2g6yBO+qEiU3PaNKwEIdMwHdtetzDBgaquybjUzhimiu5/fXMBQRrrzuT3GoFAxcpbcuGz+P+1Ii0Rfa/VhPqDkJTfhcP6RHBTO8i9vlcUOl/yyDZS86+y1s4mEzxJtGOr+enOwg4JkgVgQ6RXVbTVry02FzIqr5AA73Lb5cUioqr6UWxecxi1D4PFpz4StXqexS6adM3f/aXOZ5WFT1VvL3rwVobTGkfqjruldgg62ysgb+oGD5/XLzSSL7/pQfhvULWwxQadOYuWBKnl6nKp9Jr5jLmL/RkbPVvq4CTOhDwxvhH3WVlGC1QKBgQD8MNlmJYZXzJWnEJerEilRAIrne96MgMipaO6+7ajKzsiqldsQX8wEyw94mFXKfUY8t42VF1k84WWKlWKGMDFzHBbex+o6d3eSZdbIJEl7/nGdKOVS92Ave3o6IhIxOQDjrHQCK6wX8G70v0UcpglPdRNSfuqB4b6sWsJgtzgCvwKBgQDluw72+UaDNIbG0eewaQnORdNJoFSHiWwdwQS0AXNJZgZSC0eNa2NYBEnfm8OutlkjXcS96islnIV7f4+8Xd8tT7y4124y6VEn2mhdSbR2KFiRMGSLCfklfeYeuMACx2QcczEPfkSjsgc1DTGKLlJKR5QFopzGKqKaI2FfaP4HmwKBgQCuDkj9ZwjBsEHZfGoxd+dsMKOuAKAo+PmFcxbUT9Mfpnh4w9TeCEAXTs7uvLT5fFztqwHc1LCShD5A17mYQ8rdV5WxTOksUSgFsqP6npDZM1PW1FLyYCnSm7ZQSROlRWKHwI6QsPOl0hvCcACTNUVXwcYEOfRAkD25Vyqw9cOCoQKBgQCJ6UzniOgCthK+4p4TMkt3sEVSKtTz9NSZ3BjlswUxp/TdZfYTeMdPvGtYuJq1lLRYXd9RH3W9g4RCimaQb04eVoL1 +CYonBZjNWN3tVqHoFri9kk1iswt7LGr+b8TK/32clSed2/fUW2wNHZr/Ljb7LJ425EACfJJVDoNW+s8RwKBgCUq4qYduMBIk2lTFTsl0bFcyi2EKF7WrksqJf75Tt62+mbuwHrezblDOgRyuCTxsYhlWsaC2p9gm3gkuNHX98Sj9uVYs254Dyj7GVSiVcmWGU6pFt5KS3T9p0qjNbdsLQmdR2ADgT0f8w0XpaFD+8QpRBjXkNnG3rOul0OSOQPK";
    private Gson mGson = new Gson();

    public Server() {

    }

    Response handle(Request request) {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String decryptKey = RsaUtil.decryptToStringByPrivateKey(request.key, RsaUtil.generatePrivate(privateKey));
        String json = AesCbcUtil.decrypt(request.jsonEncrypt, decryptKey);

        Body requestBody = mGson.fromJson(json, Body.class);
        Body body;
        if (requestBody.code == 0) {
            body = new Body(200, "from Server::::success");
        } else {
            body = new Body(201, "from Server:::::error");
        }

        String bodyJson = mGson.toJson(body);

        String bodyJsonEncrypt = AesCbcUtil.encrypt(bodyJson, decryptKey);

        Response response = new Response(200, bodyJsonEncrypt);

        return response;

    }


}


