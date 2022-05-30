package com.utaoo.client.utils;


import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyStore;


@SuppressWarnings("all")
public class TrustSSLSocketFactory extends SSLSocketFactory {

    private SSLContext sslcontext = null;

    public static SSLContext createEasySSLContext() throws IOException {
        try {
            SSLContext sslcontext = null;
            sslcontext = SSLContext.getInstance("TLS");
            /** 5.ssl注册密匙库 */
            KeyManager[] keyManagers = null;
            TrustManager[] trustManagers = null;

            /** 3.获取ukey证书 */
            KeyStore uks = getKeyStoreByUKey();
            if (uks != null) {
                KeyManagerFactory factory = KeyManagerFactory.getInstance("SunX509");
                factory.init(UKeyStore.getInstance().getKeyStore(), null);
                keyManagers = factory.getKeyManagers();
            }
            /** //4.加载自己的可信任证书库（外交部不需要）
             KeyStore trustStore = getKeyStore();if (trustStore != null) {
             TrustManagerFactory factory = TrustManagerFactory.getInstance("X509");
             factory.init(trustStore);
             trustManagers = factory.getTrustManagers();
             }*/
            sslcontext.init(keyManagers, new TrustManager[]{new TrustAnyTrustManager()}, null);
            return sslcontext;
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    private SSLContext getSSLContext() throws IOException {
        if (this.sslcontext == null) {
            this.sslcontext = createEasySSLContext();
        }
        return this.sslcontext;
    }

    public Socket createSocket() throws IOException {
        return getSSLContext().getSocketFactory().createSocket();
    }


    public boolean equals(Object obj) {
        return ((obj != null) && obj.getClass().equals(TrustSSLSocketFactory.class));
    }

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
    public static KeyStore getKeyStore() throws Exception {
        //这个方法并不需要！
        // 获得密匙库
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(new File("E:/test.keystore"));
        // 密匙库的密码
        trustStore.load(instream, "你的私库密码".toCharArray());
        return trustStore;
    }

    public static KeyStore getKeyStoreByUKey() throws Exception {
        return UKeyStore.getInstance().getKeyStore();
    }

    /**
     * 获取key
     *
     * @return
     * @throws Exception
     */
    @Deprecated
    public static KeyStore getKeyStoreByKey() throws Exception {
        KeyStore ks = KeyStore.getInstance("Windows-MY");
        ks.load(null);
        return ks;
    }

}