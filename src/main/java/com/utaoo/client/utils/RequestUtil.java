package com.utaoo.client.utils;


import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class RequestUtil {
    public static JSONObject request(String baseUrl, Map<String, Object> params, String actionName) throws Exception {
        String soapStr = WebServiceUnit.formatRequestParams(actionName, params, baseUrl);
        HttpClientUtil httpClientUtil = HttpClientUtil.getInstance(baseUrl);
        String resStr = httpClientUtil.doPostSoap(soapStr, actionName);
        JSONObject jsonObject = WebServiceUnit.extractRealRes(resStr, actionName);
        return jsonObject;
    }
}
