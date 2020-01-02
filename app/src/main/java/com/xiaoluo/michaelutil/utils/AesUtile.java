package com.xiaoluo.michaelutil.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class AesUtile {
    private static final String AES = "AES";

    public static void main(String[] args) {
        // 供应商传输密钥
        String aesKey = "L4yFzF4sqc5BDWSKhvRR1A==";

        String header = "-----BEGIN ENCODED RSA PRIVATE KEY-----";

        String end = "-----END RSA PRIVATE KEY-----";

        String headerencode = "-----BEGIN RSA PRIVATE KEY-----\n";

        String endencode = "\n-----END RSA PRIVATE KEY-----";

        String encodedPriKeyContent = "ziSAXPOs2yY0suwxXu/66LfKI/ThCD0AWVZp+QO7JkqW5KBNDwxaFgrxpvBNjD" +
                "+SVp1666U3f3OFp7NLAJ8seEtrp2age4S1zlS23Y8U2q21HndrS6oV3rAHaCMrLPTfE1YuG4sSrcCGNm1FK4yU8nxZa063sd9s4Z03fKcrL9YcgoRMRWLJwwUM6TqiJEhBESh8Zz4KZfNvdI2FQlW6SveVRyZU2Hlvnri+rZ7iybA0VpcPQt7ou3yOycFmwF" +
                "/2oUvIKuahzU2yT1RPg4GX9mGC7ePtsHNEGaRgg85YdGzPMIanwVoLeVSA9sVuccqh5EQSbm8zaBHe1le3GYYQCWCA6InjZ1ZpkjqdSQjlOYCkA/9RgxqQAzRXqnAuV7J+VXhKYNjC6kbgb3Y+2MZxn1tTEuJVpqlwycvXlQK7TRXNclEyNW45k1saJ4ZvpnH" +
                "/P9CtiY0LHqf9z4YOhYv9oUEZW5GQ8TsyoNfOndlFzhBR5ZaHj4a3iBlrReC9Sru84+FqCHab883bD2hyKZ8xAB8HEzV1V4fZejSbOD8Bvzab/yDc4XpQjZwWuyCeaegx7Zu2j/nGoe+15wVq11tYeGPjtArNEYbVvOC6E5028r26vAHUfIGIu8y7HO+xJj/zlikrZdtGG6WjxIJBtGT" +
                "+aBHWO37lklgFWuXMkFJ430/FB/DY3OZxyvQpz5wJzQesRHLH+8xTfNBKqwpZt/SZVG4HZnuTXji2TgGAyKGO13veb77GDDCp1qn4flxydcv3yeZ9eS4z+jpwa6d6fGCByJZkI" +
                "/Q8sJyEx8g5JBQ92MQM1aG7ftfbGmf0CWJJopVjdymZDYrgXRvF08LE2xe7Hr2MqtjeC7GqXHLYRQDP0lcD7i2MasZvUXwaFWg0MtW9X4puY2OjTn0KVb0WjCw6pNEUJYtTcZrt9pjVSbvtnBZ7RZzzuuFKVxAvL" +
                "+RYpzeGDepQUe5fWeALu3EGO1PRluy3cfcA42EkxDoE4vez8sLivgUv2PPnbYj9twjdLuFcVmmHc1mngpjA66Fu789Xwd5bC8/G1OWa7cP2bw56S0jp8FZ71DagTHSv0iMRo1P107vPyoCNlfdO1xmNluGLgNvsC4yhjI2+/Mbrsjq21tr0AM9XYz57mjFXWRj+Oj7" +
                "/m878ztxtMKfYEPJohzIJUUMnJvFL6gQZwi2/OCSfemQMi/Z+NafbjJWc7B7oRd5gN9CbqWLmd+yEX8W8iM91m8v9JDwg1HsDYzir6S5tmFhMW2VQVqB5l0C+xe504J8/rvTzv/u645t8Y7VruPnKXoyYeGhxSIjbawo0fS0YC3FDVXiEvGDIXMMKaVuUAd00CgeIWZsktY3/YCO67iVYv8fLl7ZFJ" +
                "/p0W4CK9HmCyDFdKvVX9LjPY9gC28GzmPOg5834Dheix5g9B/2qatV4OXU0ZgZYmF7aCBCoSo9PugvoVjxgcY52vpgKts2Q9TD13vYjwIv1/aM8hPy4i/5EmP+Mx29M1ELim+mfH58YWU7FEZKRrRkXtOPHDqhqKY4U7PdUSSJAmRihncmLyF+MLJ+gBffC9kPbroUl6LNWV" +
                "/N6gKMJ2RMbbEJH3jzgILXkzQK1G2XtT6qsQrLyKTIvNtvubQV/hYTIqBMD0bUtkaQajA3yTUVLeKecVbUsoqjKNOVrKP8qOg+43CA1awrTLfC0vGAeQaehU5v0OkNAFgE/2OQcXGxuMYwxEAdNQDpHbq7T5Xf+JVY1udc3xmAM6Ykt8GE/AgdHJCYl5WESYXwcCJp2H3lffecQqK8c9Rur9" +
                "+ZlsnKjIWhy1OnwxVgw0wHrfyelVagFrYP+IjLUcVqX6HWpBp40gSIms94w37DHTcafDCSFBhk1syGdhn/bAfM/GM5k4bF/bf5JW+VE92icaGAV/ycium/sNTt5GX880f/ZhUg3SNAuW7M4wpYgiMjGhw9UyiiG5y94jY/54blA7hCZt9sBe5ZObi54Y3JTR6I0zOBcJ" +
                "/XSZMHYFSusGeHh4eZU49QgadxrE4yQXmgbTyCGEca8jrIBsYlym31eYMoA6NnUM3tJ4YSCyg9PSUWHs65+uCkqS1x/CyR7Wb1MDIZloyOv0BIpr4HG4EkEm5xCQqiB1hzkUfGhGhnxYkvPCzQRDNVlpc9DY2k7G3HEnkDOz83fh1q1bfxHYk2vaj/CeGrpGyLCzwfHwj0E8Z6adawmIqlnr7WY/AGH/g0=";

        FileReader reader = null;
        String string = "";
        try {
            reader = new FileReader(
                    new File(
                            "C:\\Users\\cqca63674\\Desktop\\11CAG2017110700121562等20个证书文件-加密\\11CAG2017110700121562.key"));
            BufferedReader bfReader = new BufferedReader(reader);
            try {
                String str = bfReader.readLine();
                while (str != null) {
                    string += str;
                    str = bfReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bfReader.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        encodedPriKeyContent = string.replace(header, "");
        encodedPriKeyContent = encodedPriKeyContent.replace(end, "");
        System.out.println("加密私钥");
        System.err.println(encodedPriKeyContent);

        // 解密私钥
        String decodePriKeyContent = decodePriKey(encodedPriKeyContent, aesKey);
        System.out.println("以下为解密后的私钥文件内容：");
        System.out.println(decodePriKeyContent);
        try {
            FileWriter writer = new FileWriter("C:\\Users\\cqca63674\\Desktop\\11CAG2017110700121562.key");
            writer.write(headerencode);
            writer.write(decodePriKeyContent);
            writer.write(endencode);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用aes密钥加密证书私钥明文内容
     *
     * @param priKeyContent 待加密的证书私钥原始内容
     * @param aesKey
     * @return
     */
    public static String encodePriKey(String priKeyContent, String aesKey) {
        byte[] aesKeyBytes = decodeBase64(aesKey);
        byte[] priKeyContentBytes = decodeBase64(priKeyContent);
        byte[] encodedPriKeyBytes = aesEncrypt(priKeyContentBytes, aesKeyBytes);
        return encodeBase64(encodedPriKeyBytes);
    }

    /**
     * 解密证书私钥
     *
     * @param encodedPriKeyContent 被加密的证书私钥密文内容
     * @param aesKey
     * @return
     */
    public static String decodePriKey(String encodedPriKeyContent, String aesKey) {
//        String data = toHexString(aesKey);
//        byte[] aesKeyBytes = decodeBase64(aesKey);
        byte[] aesKeyBytes = hexStringToBytes(aesKey);
//        ILog.d("byte[] size= " + aesKeyBytes.length + "-----aesKey--" + toHexString(aesKey));
        byte[] encodedPriKeyContentBytes = decodeBase64(encodedPriKeyContent);
        byte[] decodedPriKeyBytes = aesDecrypt(encodedPriKeyContentBytes, aesKeyBytes);
        return new String(decodedPriKeyBytes);
    }

    /**
     * 32字符串转换为16位的字节数组
     *
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if ((hexString == null) || (hexString.trim().equals(""))) {
            return null;
        }
        if (hexString.length() % 2 != 0) {
            hexString = '0' + hexString;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            bytes[i] = ((byte) ("0123456789ABCDEF".indexOf(hexChars[pos]) << 4 | "0123456789ABCDEF".indexOf(hexChars[(pos + 1)])));
        }
        return bytes;
    }

    /**
     * Base64编码.
     */
    public static String encodeBase64(byte[] input) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            return base64Encoder.encode(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Base64解码.
     */
    public static byte[] decodeBase64(String input) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return decoder.decodeBuffer(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * aes加密
     *
     * @param input
     * @param key
     * @return
     */
    public static byte[] aesEncrypt(byte[] input, byte[] key) {
        return aes(input, key, Cipher.ENCRYPT_MODE);
    }

    /**
     * 使用AES解密字符串, 返回原始字符串.
     *
     * @param input Hex编码的加密字符串
     * @param key   符合AES要求的密钥
     */
    public static byte[] aesDecrypt(byte[] input, byte[] key) {
        /**
         * @modified lanwl 调整返回值为byte[]，避免字符集问题造成解析错误
         */
        return aes(input, key, Cipher.DECRYPT_MODE);
    }

    /**
     * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
     *
     * @param input 原始字节数组
     * @param key   符合AES要求的密钥
     * @param mode  Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
     */
    private static byte[] aes(byte[] input, byte[] key, int mode) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(mode, secretKey);
            return cipher.doFinal(input);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

}
