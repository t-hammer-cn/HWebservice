package com.utaoo.client.test;


import com.alibaba.fastjson.JSONObject;
import com.utaoo.client.utils.HttpClientUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestVisaRequest {
    public static void main(String[] args) throws Exception {
        String dept = "";
        String code = "";
        String actionName = "";
        String baseUrl = "";
//        String constructStr = String.format("Body:%sResponse:{return}", actionName);
        String constructStr = "";
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("DeptID", code);
        params.put("DocNum", "");
        String cspName = "e2acsp11_s";
        String libFile = "C:\\WINDOWS\\system32\\e2acsp11_s.dll";
        HttpClientUtil.init(cspName, libFile);
        JSONObject jsonObject = HttpClientUtil.request(baseUrl, params, actionName, constructStr);
        System.out.println("res:" + jsonObject.toJSONString());
    }
}
