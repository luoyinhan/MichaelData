package com.xiaoluo.michaelutil.utils;


import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 加密算法计算
 * Created by Michael on 2016/1/16.
 */
public class AES {
    Crypto.CryptoHelper cryptoHelper = new Crypto.CryptoHelper();

    /**
     * 加密
     *
     * @param plaintext 需要加密的内容
     * @return
     */
    public String encrypt(String plaintext, String password) {

        return cryptoHelper.encrypt(plaintext, password);
    }

    /**
     * 解密
     *
     * @param ciphertext 待解密内容
     * @return
     */
    public String decrypt(String ciphertext, String password) {
        String plaintext = cryptoHelper.decrypt(ciphertext, password);
        return plaintext;
    }

    /**
     * 将要传递的参数进行包装，加密
     *
     * @param parameters Map类型，里面包含了传递过来的参数
     * @return
     * @throws Exception
     */
    public String wrapperParameter(Map<String, String> parameters, String key) throws Exception {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = parameters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();

            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        String result = sb.toString();
        result = result.substring(0, result.length() - 1);
        result = encrypt(result, key);
        return result;
    }

    /**
     * 将传递过来的参数解密后提取出参数，并以Map类型封装
     *
     * @param secStr
     * @return
     * @throws Exception
     */
    public Map<String, String> dewrapperParameter(String secStr, String key) throws Exception {
        String decrypt = decrypt(secStr, key);
        Map<String, String> parameters = new HashMap<String, String>();
        if (TextUtils.isEmpty(decrypt)) {
            return parameters;
        }
        String[] values = decrypt.split("&");
        String[] temp;
        for (String str : values) {
            temp = str.split("=");
            parameters.put(temp[0], temp[1]);
        }
        return parameters;
    }

}
