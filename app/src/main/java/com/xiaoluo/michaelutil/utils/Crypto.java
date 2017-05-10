package com.xiaoluo.michaelutil.utils;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
    static boolean isDebug = false;
    private static final String TAG = Crypto.class.getSimpleName();

    public static final String PKCS12_DERIVATION_ALGORITHM = "PBEWITHSHA256AND256BITAES-CBC-BC";
    public static final String PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    private static String DELIMITER = "]";

    private static int KEY_LENGTH = 256;
    // minimum values recommended by PKCS#5, increase as necessary
    private static int ITERATION_COUNT = 1000;
    private static final int PKCS5_SALT_LENGTH = 8;

    private static SecureRandom random = new SecureRandom();

    private Crypto() {
    }

    public static void listAlgorithms(String algFilter) {
        Provider[] providers = Security.getProviders();
        for (Provider p : providers) {
            String providerStr = String.format("%s/%s/%f\n", p.getName(),
                    p.getInfo(), p.getVersion());
            if (isDebug)
                ILog.d(TAG, providerStr);
            Set<Service> services = p.getServices();
            List<String> algs = new ArrayList<String>();
            for (Service s : services) {
                boolean match = true;
                if (algFilter != null) {
                    match = s.getAlgorithm().toLowerCase()
                            .contains(algFilter.toLowerCase());
                }

                if (match) {
                    String algStr = String.format("\t%s/%s/%s", s.getType(),
                            s.getAlgorithm(), s.getClassName());
                    algs.add(algStr);
                }
            }

            Collections.sort(algs);
            for (String alg : algs) {
                if (isDebug) ILog.d(TAG, "\t" + alg);
            }
            if (isDebug) ILog.d(TAG, "");
        }
    }

    // Illustration code only: don't use in production!
    public static SecretKey deriveKeyPad(String password) {
        try {
            long start = System.currentTimeMillis();
            byte[] keyBytes = new byte[KEY_LENGTH / 8];
            // explicitly fill with zeros
            Arrays.fill(keyBytes, (byte) 0x0);

            // if password is shorter then key length, it will be zero-padded
            // to key length
            byte[] passwordBytes = password.getBytes("UTF-8");
            int length = passwordBytes.length < keyBytes.length ? passwordBytes.length
                    : keyBytes.length;
            System.arraycopy(passwordBytes, 0, keyBytes, 0, length);

            SecretKey result = new SecretKeySpec(keyBytes, "AES");
            long elapsed = System.currentTimeMillis() - start;
            if (isDebug) ILog.d(TAG, String.format("Padding key derivation took %d [ms].",
                    elapsed));

            return result;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // Illustration code only: don't use in production!
    public static SecretKey deriveKeySha1prng(String password) {
        try {
            long start = System.currentTimeMillis();
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] seed = password.getBytes("UTF-8");
            sr.setSeed(seed);
            kgen.init(KEY_LENGTH, sr);

            SecretKey result = kgen.generateKey();
            long elapsed = System.currentTimeMillis() - start;
            if (isDebug)
                ILog.d(TAG, String.format("SHA1PRNG key derivation took %d [ms].",
                        elapsed));

            return result;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static SecretKey deriveKeyPkcs12(byte[] salt, String password) {
        try {
            long start = System.currentTimeMillis();
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                    ITERATION_COUNT, KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance(PKCS12_DERIVATION_ALGORITHM);
            SecretKey result = keyFactory.generateSecret(keySpec);
            long elapsed = System.currentTimeMillis() - start;
            if (isDebug)
                ILog.d(TAG, String.format("PKCS#12 key derivation took %d [ms].",
                        elapsed));

            return result;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static SecretKey deriveKeyPbkdf2(byte[] salt, String password) {
        try {
            long start = System.currentTimeMillis();
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt,
                    ITERATION_COUNT, KEY_LENGTH);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance(PBKDF2_DERIVATION_ALGORITHM);
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            if (isDebug) ILog.d(TAG, "key bytes: " + toHex(keyBytes));

            SecretKey result = new SecretKeySpec(keyBytes, "AES");
            long elapsed = System.currentTimeMillis() - start;
            if (isDebug) {
                ILog.d(TAG, String.format("PBKDF2 key derivation took %d [ms].",
                        elapsed));
            }
            return result;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateIv(int length) {
        byte[] b = new byte[length];
        random.nextBytes(b);

        return b;
    }

    public static byte[] generateSalt() {
        byte[] b = new byte[PKCS5_SALT_LENGTH];
        random.nextBytes(b);
        return b;
    }

    public static String encryptPkcs12(String plaintext, SecretKey key,
                                       byte[] salt) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            PBEParameterSpec pbeSpec = new PBEParameterSpec(salt,
                    ITERATION_COUNT);
            cipher.init(Cipher.ENCRYPT_MODE, key, pbeSpec);
            if (isDebug) ILog.d(TAG, "Cipher IV: " + toHex(cipher.getIV()));
            byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));

            return String.format("%s%s%s", toBase64(salt), DELIMITER,
                    toBase64(cipherText));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String plaintext, SecretKey key, byte[] salt) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);

            byte[] iv = generateIv(cipher.getBlockSize());
            if (isDebug)
                ILog.d(TAG, "IV: " + toHex(iv));
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
            if (isDebug)
                ILog.d(TAG, "Cipher IV: "
                        + (cipher.getIV() == null ? null : toHex(cipher.getIV())));
            byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));

            if (salt != null) {
                return String.format("%s%s%s%s%s", toBase64(salt), DELIMITER,
                        toBase64(iv), DELIMITER, toBase64(cipherText));
            }

            return String.format("%s%s%s", toBase64(iv), DELIMITER,
                    toBase64(cipherText));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] bytes) {
        StringBuffer buff = new StringBuffer();
        for (byte b : bytes) {
            buff.append(String.format("%02X", b));
        }

        return buff.toString();
    }

    public static String toBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public static byte[] fromBase64(String base64) {
        return Base64.decode(base64, Base64.NO_WRAP);
    }

    public static String decryptPkcs12(byte[] cipherBytes, SecretKey key,
                                       byte[] salt) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            PBEParameterSpec pbeSpec = new PBEParameterSpec(salt,
                    ITERATION_COUNT);
            cipher.init(Cipher.DECRYPT_MODE, key, pbeSpec);
            if (isDebug) ILog.d(TAG, "Cipher IV: " + toHex(cipher.getIV()));
            byte[] plainBytes = cipher.doFinal(cipherBytes);
            String plainrStr = new String(plainBytes, "UTF-8");

            return plainrStr;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(byte[] cipherBytes, SecretKey key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            if (isDebug) ILog.d(TAG, "Cipher IV: " + toHex(cipher.getIV()));
            byte[] plaintext = cipher.doFinal(cipherBytes);
            String plainrStr = new String(plaintext, "UTF-8");

            return plainrStr;
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptPkcs12(String ciphertext, String password) {
        String[] fields = ciphertext.split(DELIMITER);
        if (fields.length != 2) {
            throw new IllegalArgumentException("Invalid encypted text format");
        }

        byte[] salt = fromBase64(fields[0]);
        byte[] cipherBytes = fromBase64(fields[1]);
        SecretKey key = deriveKeyPkcs12(salt, password);

        return decryptPkcs12(cipherBytes, key, salt);
    }

    public static String decryptPbkdf2(String ciphertext, String password) {
        String[] fields = ciphertext.split(DELIMITER);
        if (fields.length != 3) {
            throw new IllegalArgumentException("Invalid encypted text format");
        }

        byte[] salt = fromBase64(fields[0]);
        byte[] iv = fromBase64(fields[1]);
        byte[] cipherBytes = fromBase64(fields[2]);
        SecretKey key = deriveKeyPbkdf2(salt, password);

        return decrypt(cipherBytes, key, iv);
    }

    public static String decryptNoSalt(String ciphertext, SecretKey key) {
        String[] fields = ciphertext.split(DELIMITER);
        if (fields.length != 2) {
            throw new IllegalArgumentException("Invalid encypted text format");
        }
        byte[] iv = fromBase64(fields[0]);
        byte[] cipherBytes = fromBase64(fields[1]);

        return decrypt(cipherBytes, key, iv);
    }

    public static class CryptoHelper {
        abstract class CryptoTask extends AsyncTask<Void, Void, String> {

            Exception error;

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    return doCrypto();
                } catch (Exception e) {
                    error = e;
                    if (isDebug)
                        ILog.e(TAG, "Error: " + e.getMessage());

                    return null;
                }
            }

            protected abstract String doCrypto();

            @Override
            protected void onPostExecute(String result) {


                if (error != null) {


                    return;
                }


                updateUi(result);
            }

            protected abstract void updateUi(String result);
        }

        abstract class Encryptor {
            SecretKey key;

            abstract SecretKey deriveKey(String passpword, byte[] salt);

            abstract String encrypt(String plaintext, String password);

            abstract String decrypt(String ciphertext, String password);

            String getRawKey() {
                if (key == null) {
                    return null;
                }

                return Crypto.toHex(key.getEncoded());
            }
        }

        private final Encryptor PADDING_ENCRYPTOR = new Encryptor() {

            @Override
            public SecretKey deriveKey(String password, byte[] salt) {
                return Crypto.deriveKeyPad(password);
            }

            @Override
            public String encrypt(String plaintext, String password) {
                key = deriveKey(password, null);
                if (isDebug) ILog.d(TAG, "Generated key: " + getRawKey());
                return Crypto.encrypt(plaintext, key, null);
            }

            @Override
            public String decrypt(String ciphertext, String password) {
                SecretKey key = deriveKey(password, null);

                return Crypto.decryptNoSalt(ciphertext, key);
            }
        };

        private final Encryptor SHA1PRNG_ENCRYPTOR = new Encryptor() {
            @Override
            public SecretKey deriveKey(String password, byte[] salt) {
                return Crypto.deriveKeySha1prng(password);
            }

            @Override
            public String encrypt(String plaintext, String password) {
                key = deriveKey(password, null);
                if (isDebug) {
                    ILog.d(TAG, "Generated key: " + getRawKey());
                }
                return Crypto.encrypt(plaintext, key, null);
            }

            @Override
            public String decrypt(String ciphertext, String password) {
                SecretKey key = deriveKey(password, null);

                return Crypto.decryptNoSalt(ciphertext, key);
            }
        };

        private final Encryptor PKCS12_ENCRYPTOR = new Encryptor() {

            @Override
            public SecretKey deriveKey(String password, byte[] salt) {
                return Crypto.deriveKeyPkcs12(salt, password);
            }

            @Override
            public String encrypt(String plaintext, String password) {
                byte[] salt = Crypto.generateSalt();
                key = deriveKey(password, salt);
                if (isDebug) ILog.d(TAG, "Generated key: " + getRawKey());

                return Crypto.encryptPkcs12(plaintext, key, salt);
            }

            @Override
            public String decrypt(String ciphertext, String password) {
                return Crypto.decryptPkcs12(ciphertext, password);
            }
        };

        private final Encryptor PBKDF2_ENCRYPTOR = new Encryptor() {

            @Override
            public SecretKey deriveKey(String password, byte[] salt) {
                return Crypto.deriveKeyPbkdf2(salt, password);
            }

            @Override
            public String encrypt(String plaintext, String password) {
                byte[] salt = Crypto.generateSalt();
                key = deriveKey(password, salt);
                if (isDebug) ILog.d(TAG, "Generated key: " + getRawKey());

                return Crypto.encrypt(plaintext, key, salt);
            }

            @Override
            public String decrypt(String ciphertext, String password) {
                return Crypto.decryptPbkdf2(ciphertext, password);
            }
        };


        /**
         * 加密
         *
         * @param plaintext 需要加密的内容
         * @param password  加密密码
         * @return
         */
        public String encrypt(String plaintext, String password) {
            if (TextUtils.isEmpty(plaintext)) {
                return plaintext;
            }
            String ciphertext = encryptor.encrypt(plaintext, password);
            return ciphertext;
        }

        /**
         * 解密
         *
         * @param ciphertext 待解密内容
         * @param password   解密密钥
         * @return
         */
        public String decrypt(String ciphertext, String password) {
            if (TextUtils.isEmpty(ciphertext)) {
                return ciphertext;
            }
            String plaintext = encryptor.decrypt(ciphertext, password);
            return plaintext;
        }

        private Encryptor encryptor;

        public CryptoHelper() {
            encryptor = PADDING_ENCRYPTOR;
        }

        String plaintext = "PADDING_ENCRYPTOR";
        String password = "password";
        String _ciphertext = "";
//        public void main(){
//            encryptor = PADDING_ENCRYPTOR;
//
//            String ciphertext = encryptor.encrypt(plaintext, password);
//            _ciphertext=ciphertext;
//            String plaintext =  encryptor.decrypt(_ciphertext, password);
//        }
    }
}