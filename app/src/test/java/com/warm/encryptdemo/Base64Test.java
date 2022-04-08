package com.warm.encryptdemo;

import android.util.Base64;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;


/**
 * 作者：warm
 * 时间：2018-01-29 10:10
 * 描述：
 */
public class Base64Test {
    private String str="你好啊！";

    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void Base64() throws Exception{
        byte[] b = Base64.encode(str.getBytes(), Base64.DEFAULT);
        System.out.println(Arrays.toString(b));
    }
}
