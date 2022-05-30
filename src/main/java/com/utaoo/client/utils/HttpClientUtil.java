package com.utaoo.client.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;


@SuppressWarnings("all")
public class HttpClientUtil {


    private String baseVUrl = "";
    private static HttpClientUtil soapevnUtil;
    private SSLContext sslContext;


    /**
     * @param closeableHttpClient
     * @param url
     * @param soap
     * @param SOAPAction
     * @return
     */
    public String doPostSoap(String soap, String SOAPAction) {
        return doPostSoap(baseVUrl, soap, SOAPAction);
    }

    /**
     * @param closeableHttpClient
     * @param url
     * @param soap
     * @param SOAPAction
     * @return
     */
    public String doPostSoap(String url, String soap, String SOAPAction) {
        //请求体
        String retStr = "";
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
            httpPost.setHeader("SOAPAction", SOAPAction);
            StringEntity data = new StringEntity(soap,
                    Charset.forName("UTF-8"));
            httpPost.setEntity(data);
            CloseableHttpClient client = createClient();
            CloseableHttpResponse response = client
                    .execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                retStr = EntityUtils.toString(httpEntity, "UTF-8");
            }
            // 释放资源
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retStr;
    }

    public static HttpClientUtil getInstance(String baseUrl) throws IOException {
        if (HttpClientUtil.soapevnUtil != null) {
            return HttpClientUtil.soapevnUtil;
        } else {
            return new HttpClientUtil(baseUrl);
        }
    }

    private HttpClientUtil(String baseUrl) throws IOException {
        this.baseVUrl = baseUrl;
    }


    private void initSSLContext() throws InterruptedException, IOException {
        this.setSslContext(TrustSSLSocketFactory.createEasySSLContext());
    }

    private void setSslContext(SSLContext sslContext) {
        this.sslContext = sslContext;
    }

    private SSLContext getSslContext() throws IOException, InterruptedException {
        //由csp模块控制超时时间
        initSSLContext();
        return this.sslContext;
    }

    public CloseableHttpClient createClient() throws IOException, InterruptedException {
        return HttpClientBuilder.create().setSSLContext(this.getSslContext()).build();
    }
}
