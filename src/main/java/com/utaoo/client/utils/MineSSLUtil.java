package com.utaoo.client.utils;

import org.apache.http.client.config.RequestConfig;

import java.io.File;

public class MineSSLUtil {

    public static boolean checkJar() {
        String path = MineSSLUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.indexOf("file:") != -1 && path.indexOf("jar") != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 超时时间配置
     *
     * @return
     */
    public static RequestConfig getConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(6000).setConnectionRequestTimeout(3000)
                .setSocketTimeout(6000).build();
    }


    public static String getPath() {
        String path = MineSSLUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);
        path = path.replace("target/classes/", "");
        if (!path.endsWith("/") && !path.endsWith("\\")) {
            path = path + File.separator;
        }
        return path;
    }
}
