package com.utaoo.client.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class UKeyStore {
    private static UKeyStore UKeyStore;
    private String cspName = "WDPKCS";
    private String libFile = "C:/Windows/system32/WatchDataV5/Watchdata CSP v5.2/WDPKCS.dll";
    private String pinPassword;
    private Provider provider;
    private Long registeTime = System.currentTimeMillis() + (8 * 60 * 1000);

    public static UKeyStore getInstance() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        UKeyStore result = null;
        if (UKeyStore.UKeyStore != null && (System.currentTimeMillis() <= UKeyStore.UKeyStore.registeTime)) {
            result = UKeyStore.UKeyStore;
        } else {
            result = new UKeyStore();
        }
        return result;
    }

    public static UKeyStore getInstance(String cspName, String libFile, String pin) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        UKeyStore result = null;
        if (UKeyStore.UKeyStore != null && (System.currentTimeMillis() <= UKeyStore.UKeyStore.registeTime)) {
            result = UKeyStore.UKeyStore;
        } else {
            result = new UKeyStore(cspName, libFile, pin);
        }
        return result;
    }


    public static UKeyStore getInstance(String cspName, String libFile) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        if (UKeyStore.UKeyStore != null) {
            return UKeyStore.UKeyStore;
        } else {
            return new UKeyStore(cspName, libFile);
        }
    }


    public Provider getProvider() {
        return provider;
    }

    public void changePin(String pin) {
        this.pinPassword = pin;
    }

    private void preInif(String cspName, String libFile) {
        this.cspName = cspName;
        this.libFile = libFile;
        UKeyStore.UKeyStore = this;
    }

    private UKeyStore() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        this.preInif(cspName, libFile);
        this.initProvider();
    }

    private UKeyStore(String cspName, String libFile, String pin) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        this.preInif(cspName, libFile);
        this.pinPassword = pin;
        initProvider();
    }

    private UKeyStore(String cspName, String libFile) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        this.preInif(cspName, libFile);
        this.initProvider();
    }

    private void initProvider() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        //程序并为将私钥提取出来，只是调用了私钥的接口
        String pkcs11config = "name=" + cspName + "\n" + "library=" + libFile + "\n";
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
    public KeyStore getKeyStore(String pinPassword) throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        if (provider == null) {
            throw new RuntimeException("还未初始化Ukey！");
        }
        KeyStore ks;
        ks = KeyStore.getInstance("PKCS11", provider);
        char[] pin = null;
        if (StringUtils.isNotBlank(pinPassword)) {
            pin = pinPassword.toCharArray();
        }
        ks.load(null, pin);
        return ks;
    }

    public KeyStore getKeyStore() throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        return getKeyStore(pinPassword);
    }

}
