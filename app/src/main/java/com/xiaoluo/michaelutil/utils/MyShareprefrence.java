package com.xiaoluo.michaelutil.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @Description: 数据缓存类
 * @author: Micheal
 * @date: 2015年1月4日下午4:57:48
 */
public class MyShareprefrence {
    private static SharedPreferences share;
    private static MyShareprefrence instance;
    private static Context mContext;
    public static MyShareprefrence getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    private MyShareprefrence() {
        share = mContext.getSharedPreferences("mydata", Context.MODE_PRIVATE);
    }

    //初始化
    public static void init(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new MyShareprefrence();
        }
    }



    // 存储用户信息对象
    public <T> void setSpData(T t, String name) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 将oil对象放到OutputStream中
        // 将oil对象转换成byte数组，并将其进行base64编码
        String oilBase64 = new String(Base64.encodeBase64(baos.toByteArray()));
        share.edit().putString(name, oilBase64).apply();
    }


    // 获取对象数据
    public <T> T getSpData(String name) {
        String oilBase64 = share.getString(name, "0");
        byte[] base64Bytes = Base64.decodeBase64(oilBase64.getBytes());
        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(bais);
            T oil = (T) ois.readObject();
            return oil;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void setStringData(String data, String name) {
        data = encrypt(data);
        share.edit().putString(name, data).apply();
    }

    public String getStringData(String name) {
        String result = share.getString(name, "");
        result = decrypt(result);
        return result;
    }


    public void setBooleanValue(String key, boolean value) {
        share.edit().putBoolean(key, value).apply();
    }

    public boolean getBooleanValue(String key) {
        return share.getBoolean(key, false);
    }

    private String encrypt(String value) {
        if (TextUtils.isEmpty(value))
            return "";
        return new String(Base64.encodeBase64(value.getBytes()));
    }

    private String decrypt(String value) {
        if (TextUtils.isEmpty(value))
            return "";
        return new String(Base64.decodeBase64(value.getBytes()));
    }
}
