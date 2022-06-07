package com.utaoo.client.test;


import com.alibaba.fastjson.JSONObject;
import com.utaoo.client.utils.HttpClientUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestRequest {
    public static void main(String[] args) throws Exception {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("registUser", "");
        params.put("bauletternum", "");
        params.put("param", "");
        String actionName = "";
        String baseUrl = "";
        String constructStr = String.format("Body:%sResponse:{return}", actionName);
        String cspName = "WDPKCS";
        String libFile = "C:\\WINDOWS\\system32\\WatchDataV5\\Watchdata CSP v5.2\\WDPKCS.dll";
        HttpClientUtil.init(cspName, libFile);
        JSONObject jsonObject = HttpClientUtil.request(baseUrl, params, actionName, "");
        System.out.println("result:" + jsonObject.toJSONString());
        System.out.println("status:" + jsonObject.getInteger("status"));
        System.out.println("message:" + jsonObject.getString("message"));
        System.out.println("result:" + jsonObject.getJSONObject("result"));
    }
}
