package com.utaoo.client.utils;

import cn.hutool.core.codec.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

public class Base64Utils {
    public static String strToBase64(String origin) {
        if (StringUtils.isBlank(origin)) {
            return "";
        }
        return Base64.encode(origin, Charset.forName("utf-8"));
    }

    public static String base64ToStr(String base64) {
        if (StringUtils.isBlank(base64)) {
            return "";
        }
        return Base64.decodeStr(base64, Charset.forName("utf-8"));
    }

}
