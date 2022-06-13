package com.utaoo.client.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;


@SuppressWarnings("all")
public final class HttpClientUtil {
    private String baseVUrl = "";
    private static HttpClientUtil soapevnUtil;
    private static String cspName;
    private static String libFile;
    private static String keyParam = null;

    static String getCspName() {
        return cspName;
    }

    static String getLibFile() {
        return libFile;
    }

    /**
     * @param closeableHttpClient
     * @param url
     * @param soap
     * @param SOAPAction
     * @return
     */
    private String doPostSoap(String soap, String SOAPAction) throws IOException, InterruptedException {
        return doPostSoap(baseVUrl, soap, SOAPAction);
    }

    /**
     * @param closeableHttpClient
     * @param url
     * @param soap
     * @param SOAPAction
     * @return
     */
    private String doPostSoap(String url, String soap, String SOAPAction) throws IOException, InterruptedException {
        //请求体
        String retStr = "";
        HttpPost httpPost = new HttpPost(url);
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
        response.close();
        client.close();
        return retStr;
    }

    public static HttpClientUtil getInstance(String baseUrl, String libFile, String cspName, String keyParam) throws IOException, InterruptedException {
        HttpClientUtil.libFile = libFile;
        HttpClientUtil.cspName = cspName;
        HttpClientUtil.keyParam = keyParam;
        if (HttpClientUtil.soapevnUtil != null) {
            return HttpClientUtil.soapevnUtil;
        } else {
            return new HttpClientUtil(baseUrl);
        }
    }

    private HttpClientUtil(String baseUrl) throws IOException, InterruptedException {
        this.baseVUrl = baseUrl;
        HttpClientUtil.soapevnUtil = this;
    }

    private CloseableHttpClient createClient() throws IOException, InterruptedException {
        return HttpClientBuilder.create().setSSLContext(TrustSSLSocketFactory.createEasySSLContext(keyParam)).setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
    }


    public JSONObject request(String baseUrl, Map<String, Object> params, String actionName, String resultConstruct) throws Exception {
        return request(baseUrl, baseUrl, params, actionName, resultConstruct);
    }

    public JSONObject request(String baseUrl, String nameSpace, Map<String, Object> params, String actionName, String resultConstruct) throws Exception {
        String resStr = requestXMLres(baseUrl, nameSpace, params, actionName);
        JSONObject jsonObject = WebServiceUnit.extractRealRes(resStr, resultConstruct);
        return jsonObject;
    }

    public String requestXMLres(String baseUrl, Map<String, Object> params, String actionName) throws Exception {
        return requestXMLres(baseUrl, baseUrl, params, actionName);
    }

    public String requestXMLres(String baseUrl, String nameSpace, Map<String, Object> params, String actionName) throws Exception {
        if (StringUtils.isBlank(libFile)) {
            throw new RuntimeException("还未初始化！");
        }
        String soapStr = WebServiceUnit.formatRequestParams(actionName, params, nameSpace);
        return HttpClientUtil.soapevnUtil.doPostSoap(soapStr, actionName);
    }
}
