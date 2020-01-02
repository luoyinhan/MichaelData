package com.xiaoluo.michaelutil.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import Decoder.BASE64Decoder;


/**
 * @desc:SSLSocketFactory
 * @author：Micheal
 * @date：2017/11/9
 */
public class MySSLSocketFactory {
    private static final String KEY_STORE_TYPE_BKS = "BKS";// 安卓端的证书类型 必须是bks
    private static final String KEY_STORE_PASSWORD = "123456";// 证书密码（应该是客户端证书密码）
    private static final String KEY_STORE_TRUST_PASSWORD = "123456";// 授信证书密码（应该是服务端证书密码）
    private static boolean isEncode = false;
    private static MySSLSocketFactory mySSLSocketFactory = null;

    private MySSLSocketFactory() {
    }

    public static MySSLSocketFactory getInstance() {
        if (mySSLSocketFactory == null) {
            mySSLSocketFactory = new MySSLSocketFactory();
        }
        return mySSLSocketFactory;
    }

    /**
     * 别名 任意取
     */
    private static final String ALIAS = "alias";

    /**
     * @param context
     * @param cerFilePath 公钥文件的位置
     * @param keyFilePath 私钥文件的位置
     * @return
     */
    public SSLSocketFactory getSocketFactory(Context context, String cerFilePath, String keyFilePath) {
        InputStream trust_input = null;// 服务器授信证书
        InputStream stream = null;
        try {
            //服务器的信任的CA证书，防止钓鱼
            trust_input = context.getResources().getAssets().open("tclient.bks");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
            trustStore.load(trust_input, KEY_STORE_TRUST_PASSWORD.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            try {
                if (trust_input != null) {
                    trust_input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 本地的公钥/私钥
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
            keyStore.load(null);
            stream = new FileInputStream(cerFilePath);//客户端的公钥证书
            Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(stream);
            // 设置自己的证书
            keyStore.setCertificateEntry(ALIAS, certificate);
            Certificate[] certs = new Certificate[1];
            certs[0] = certificate;
            // 加载客戶端的私钥
            PrivateKey priKey = loadPrivateKey(context, keyFilePath,"3245661165113131351516");
            keyStore.setKeyEntry(ALIAS, priKey, KEY_STORE_TRUST_PASSWORD.toCharArray(), certs);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory factory = new Tls12SocketFactory(sslContext.getSocketFactory());
            return factory;
        } catch (Exception e) {
            e.printStackTrace();
            ILog.e("getSocketFactory Exception-----" + e.toString());
            return null;
        } finally {
            try {
                if (trust_input != null) {
                    trust_input.close();
                }
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载私钥
     *
     * @param context
     * @return
     * @throws
     */
    private PrivateKey loadPrivateKey(final Context context, final String filePath,final String key) throws Exception {
        String privateKey = getKeyString(context, filePath);
        if (isEncode) {// 需要解密
            privateKey = AesUtile.decodePriKey(privateKey, key);
        }
        try {
            BASE64Decoder base64Decoder = new BASE64Decoder();
            byte[] buffer = base64Decoder.decodeBuffer(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            ILog.e("loadPrivateKey  PrivateKey:------ 私钥非法");
            throw new Exception("私钥非法");
        } catch (IOException e) {
            throw new Exception("私钥数据内容读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 提取私钥的内容
     *
     * @param context
     * @param filePath
     * @return
     */
    private static String getKeyString(Context context, String filePath) {
        String header = "-----BEGIN ENCODED RSA PRIVATE KEY-----";
        String end = "-----END RSA PRIVATE KEY-----";
        String headerencode = "-----BEGIN RSA PRIVATE KEY-----";
        StringBuffer sb = new StringBuffer();
        InputStream inputStream = null;
        BufferedReader br = null;
        String resData = null;
        try {
            inputStream = new FileInputStream(filePath);
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            if (sb.toString().contains(header)) {// 则加密处理了
                resData = sb.toString().replace(header, "");
                isEncode = true;
            } else {
                resData = sb.toString().replace(headerencode, "");
                isEncode = false;
            }
            resData = resData.replace(end, "");
            return resData;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
