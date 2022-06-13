package com.utaoo.client.utils;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Map;

public final class UKeyStore {
    private static UKeyStore UKEY_STORE;
    private String pinPassword = "lss123";
    private Provider provider;
    private Long registeTime = System.currentTimeMillis() + (1 * 60 * 1000);
    private KeyStore keyStore;
    private String keyId;

    public static String getKeyId() {
        return UKEY_STORE.keyId;
    }

    void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    static UKeyStore getInstance() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        return getInstance(null);
    }

    static UKeyStore getInstance(String pin) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        UKeyStore result = null;
        if (UKeyStore.UKEY_STORE != null) {
            result = UKeyStore.UKEY_STORE;
        } else {
            UKeyStore.UKEY_STORE = result = new UKeyStore(pin);
        }
        return result;
    }

    private UKeyStore(String pin) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        if (StringUtils.isNotBlank(pin)) {
            this.pinPassword = pin;
        }
        this.initProvider();
        UKeyStore.UKEY_STORE = this;
    }

    Provider getProvider() {
        return provider;
    }

    @Deprecated
    public void changePin(String pin) {
        this.pinPassword = pin;
    }

    private void initProvider() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        //程序并为将私钥提取出来，只是调用了私钥的接口
        String pkcs11config = "name=" + HttpClientUtil.getCspName() + "\n" + "library=" + HttpClientUtil.getLibFile() + "\n";
        byte[] pkcs11configbytes = pkcs11config.getBytes();
        ByteArrayInputStream configStream = new ByteArrayInputStream(pkcs11configbytes);
        Provider provider = new sun.security.pkcs11.SunPKCS11(configStream);

        this.provider = provider;
        Security.addProvider(provider);
    }

    /**
     * 获取ukey证书容器
     *
     * @param pinPassword
     * @return
     * @throws CertificateException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     */
    KeyStore getKeyStore(String pinPassword) throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        if (this.provider == null) {
            throw new RuntimeException("还未初始化Ukey！");
        }
        if (System.currentTimeMillis() >= UKeyStore.UKEY_STORE.registeTime) {
            this.initProvider();
        }
        this.keyStore = KeyStore.getInstance("PKCS11", provider);
        char[] pin = null;
        if (StringUtils.isNotBlank(pinPassword)) {
            pin = pinPassword.toCharArray();
        }
        this.keyStore.load(null, pin);
        return this.keyStore;
    }

    KeyStore getKeyStore() throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        return getKeyStore(pinPassword);
    }

}
