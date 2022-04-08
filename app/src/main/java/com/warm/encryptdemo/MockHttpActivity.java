package com.warm.encryptdemo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.warm.encryptdemo.databinding.ActivityMockHttpBinding;

//https://www.cnblogs.com/xixi3616/p/9932499.html
public class MockHttpActivity extends AppCompatActivity {

    private ActivityMockHttpBinding mBinding;

    private Gson mGson = new Gson();

    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4k/45SWUDfIWlBwY1fpaG9CPPfP3cPJSFHintFqrrMLMdyKcxHn5sqN7DV0gRpqJfXWzHY8YcwV27WCEuMxtNqT0YZQNL5DkhVTxVnlZdxQLCRf0adt7+bcH+GaelC+gXKMrKhzWyoJdUFl/p2W5GqcVGFrG5irgxglqfixOnK8DWasR/s1cIFdCD7A9uo5eggIi5832PiHvdbWp57wFUdUrmIJbHGl4fLy35Cj2F5IfrNNVnzE7okKSIt6XRxLs0mip7qebLgMfRR6F5Lv+YZSQztqi8tpzQ2/Z7yQoC+4584Ztk9Zo87qRNzmxHMo5dVQ4W7toXqp5mUQiyH7ipQIDAQAB";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_mock_http);

        mBinding.tvMockHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHttp();
            }
        });

    }

    private void startHttp() {


        final String randomKey = AesCbcUtil.generateKeyToString(AesCbcUtil.KEY_SIZE_256);

        String keyEncrypt = RsaUtil.encryptByPublicKey(randomKey, RsaUtil.generatePublic(publicKey));


        Body body = new Body(0, "请求数据");

        String json = mGson.toJson(body);

        final String jsonEncrypt = AesCbcUtil.encrypt(json, randomKey);

        Request request = new Request(keyEncrypt, jsonEncrypt);

        Http http = new Http();

        http.doHttp(request, new Http.Callback() {
            @Override
            public void onResponse(Response response) {

                if (response.httpCode == 200) {
                    String json = AesCbcUtil.decrypt(response.jsonEncrypt, randomKey);
                    Body body = mGson.fromJson(json, Body.class);

                    mBinding.tvMockHttp.setText(body.body);
                }
            }
        });

    }


}
