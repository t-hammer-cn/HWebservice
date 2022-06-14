package com.utaoo.client.utils;


import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;


@SuppressWarnings("all")
public final class TrustSSLSocketFactory extends SSLSocketFactory {

    static SSLContext createEasySSLContext() {
        return createEasySSLContext(null, null);
    }

    static SSLContext createEasySSLContext(String keyParam, String pin) {
        Exception runEx = null;
        try {
            SSLContext sslcontext = null;
            sslcontext = SSLContext.getInstance("TLS");
            /** 5.ssl注册密匙库 */
            KeyManager[] keyManagers = null;
            TrustManager[] trustManagers = null;

            /** 3.获取ukey证书 */
            KeyStore uks = getKeyStoreByUKey(pin);
            if (uks != null) {
                KeyManagerFactory factory = KeyManagerFactory.getInstance("SunX509");
                factory.init(uks, null);
                keyManagers = factory.getKeyManagers();
            }
            /** //4.加载自己的可信任证书库（当前应用下不需要）
             KeyStore trustStore = getKeyStore();if (trustStore != null) {
             TrustManagerFactory factory = TrustManagerFactory.getInstance("X509");
             factory.init(trustStore);
             trustManagers = factory.getTrustManagers();
             }*/
            Enumeration<String> aliases = uks.aliases();
            if (StringUtils.isNotBlank(keyParam)) {
                String keyId = null;
                while (aliases.hasMoreElements()) {
                    String aliasName = aliases.nextElement();
                    Integer offset = aliasName.indexOf(keyParam);
                    if (offset != -1) {
                        keyId = aliasName.substring(0, offset);
                        break;
                    }
                }
                if (StringUtils.isBlank(keyId)) {
                    throw new RuntimeException("构建keyId失败！");
                } else {
                    UKeyStore.getInstance().setKeyId(keyId);
                }
            }
            sslcontext.init(keyManagers, new TrustManager[]{new TrustAnyTrustManager()}, null);
            return sslcontext;
        } catch (IOException e) {
            runEx = e;
            e.printStackTrace();
        } catch (KeyManagementException e) {
            runEx = e;
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            runEx = e;
            e.printStackTrace();
        } catch (CertificateException e) {
            runEx = e;
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            runEx = e;
            e.printStackTrace();
        } catch (KeyStoreException e) {
            runEx = e;
            e.printStackTrace();
        }
        UKeyStore.destory();
        if (runEx instanceof KeyStoreException) {
            throw new RuntimeException("Ukey未插入！");
        } else {
            throw new RuntimeException(runEx.getMessage());
        }
    }

    private SSLContext getSSLContext() throws IOException {
        return createEasySSLContext();
    }

    @Override
    public Socket createSocket() throws IOException {
        return getSSLContext().getSocketFactory().createSocket();
    }

    @Override
    public boolean equals(Object obj) {
        return ((obj != null) && obj.getClass().equals(TrustSSLSocketFactory.class));
    }

    @Override
    public int hashCode() {
        return TrustSSLSocketFactory.class.hashCode();
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {

        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
            throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port, localHost, localPort);
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress,
                               int localPort) throws IOException {
        return getSSLContext().getSocketFactory().createSocket(address, port, localAddress, localPort);
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose)
            throws IOException {
        return getSSLContext().getSocketFactory().createSocket(s, host, port, autoClose);
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return null;
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return null;
    }

    /**
     * 加载可信任证书库
     *
     * @throws Exception
     */
    @Deprecated
    private static KeyStore getKeyStore() throws Exception {
        //这个方法并不需要！
        // 获得密匙库
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(new File("E:/test.keystore"));
        // 密匙库的密码
        trustStore.load(instream, "你的私库密码".toCharArray());
        return trustStore;
    }


    private static KeyStore getKeyStoreByUKey(String pin) throws CertificateException, IOException, NoSuchAlgorithmException, KeyStoreException {
        return UKeyStore.getInstance(pin).getKeyStore();
    }

    /**
     * 获取key
     *
     * @return
     * @throws Exception
     */
    @Deprecated
    private static KeyStore getKeyStoreByKey() throws Exception {
        KeyStore ks = KeyStore.getInstance("Windows-MY");
        ks.load(null);
        return ks;
    }

}