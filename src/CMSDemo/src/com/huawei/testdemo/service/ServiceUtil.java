
package com.huawei.testdemo.service;

import com.huawei.testdemo.config.ConfigProperties;


public class ServiceUtil {
    private static final String SSLENABLE = "1";
    private static String ip;
    private static String port;
    private static String prefix = "";

    public ServiceUtil() {
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void serviceInit() {
        ip = ConfigProperties.getKey("cmsconfig.properties", "CMS_IP");
        port = ConfigProperties.getKey("cmsconfig.properties", "CMS_PORT");

        if (SSLENABLE.equals(ConfigProperties.getKey("cmsconfig.properties", "SSLEable"))) {
            prefix = "https://" + ip + ":" + port + "/cmsgateway/resource/";
        } else{
            prefix = "http://" + ip + ":" + port + "/cmsgateway/resource/";
        }

    }
}
