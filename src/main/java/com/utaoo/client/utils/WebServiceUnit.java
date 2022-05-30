package com.utaoo.client.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.util.List;
import java.util.Map;


public class WebServiceUnit {
    public static String formatRequestParams(String methodName, Map<String, Object> params, String url) throws Exception {
        StringBuffer soapRequestParams = new StringBuffer();
//        soapRequestParams.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        soapRequestParams.append("<soapevn:Envelope xmlns:soapevn=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:eop=\"" + url + "\">");
        soapRequestParams.append("<soapevn:Header/>");
        soapRequestParams.append("<soapevn:Body>");
        soapRequestParams.append(String.format("<%s>", methodName));
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            soapRequestParams.append("<" + stringObjectEntry.getKey() + ">");
            soapRequestParams.append(stringObjectEntry.getValue());
            soapRequestParams.append("</" + stringObjectEntry.getKey() + ">");
        }
        soapRequestParams.append(String.format("<%s>", methodName));
        soapRequestParams.append("</soapevn:Body>");
        soapRequestParams.append("</soapevn:Envelope>");
        return soapRequestParams.toString();
    }

    private static String formatXML(String xmlStr) throws DocumentException {
        // 创建一个XML解析器对象
        SAXReader reader = new SAXReader();
        StringReader stringReader = new StringReader(xmlStr);
        // 读取XML文档，返回Document对象
        Document document = reader.read(stringReader);
        // 获取根元素节点
        Element root = document.getRootElement();
        StringBuilder sb = new StringBuilder();
        return recursion(root, sb).toString();
    }

    private static StringBuilder recursion(Element ele, StringBuilder sb) {
        sb.append("<");
        // 解析元素节点
        sb.append(ele.getName());

        // 解析属性节点
        List<Attribute> attributes = ele.attributes();
        for (Attribute attribute : attributes) {
            sb.append(" ");
            sb.append(attribute.getName());
            sb.append("=");
            sb.append(attribute.getValue());
        }

        sb.append(">");

        // 解析文本节点
        sb.append(ele.getText());

        // 递归解析元素节点
        List<Element> elements = ele.elements();
        for (Element element : elements) {
            recursion(element, sb);
        }
        sb.append("<" + ele.getName() + "/>\n");
        return sb;
    }

    public static JSONObject extractRealRes(String xmlStr, String action) throws DocumentException {
        JSONObject jsonObject = xml2Json(xmlStr);
        return extractRealRes(jsonObject, action);
    }

    public static JSONObject extractRealRes(JSONObject jsonObject, String action) throws DocumentException {
        String returnStr = jsonObject.getJSONObject("Body").getJSONObject(action + "Response").getString("return");
        return xml2Json(returnStr);
    }

    private static JSONObject xml2Json(String xmlStr) throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlStr);
        JSONObject json = new JSONObject();
        dom4j2Json(doc.getRootElement(), json);
        return json;
    }


    private static void dom4j2Json(Element element, JSONObject json) {
        List<Element> chdEl = element.elements();
        for (Element e : chdEl) {
            if (!e.elements().isEmpty()) {
                JSONObject chdjson = new JSONObject();
                dom4j2Json(e, chdjson);
                Object o = json.get(e.getName());
                if (o != null) {
                    JSONArray jsona = null;
                    if (o instanceof JSONObject) {
                        JSONObject jsono = (JSONObject) o;
                        json.remove(e.getName());
                        jsona = new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if (o instanceof JSONArray) {
                        jsona = (JSONArray) o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                } else {
                    if (!chdjson.isEmpty()) {
                        json.put(e.getName(), chdjson);
                    }
                }
            } else {
                if (!e.getText().isEmpty()) {
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }
}

