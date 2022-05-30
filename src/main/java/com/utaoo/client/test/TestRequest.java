package com.utaoo.client.test;


import com.alibaba.fastjson.JSONObject;
import com.utaoo.client.utils.RequestUtil;

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
        JSONObject jsonObject = RequestUtil.request(baseUrl, params, actionName);
        System.out.println("status:" + jsonObject.getInteger("status"));
        System.out.println("message:" + jsonObject.getString("message"));
        System.out.println("result:" + jsonObject.getJSONObject("result"));
    }
}
