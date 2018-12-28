package com.huawei.testdemo.service;

import com.huawei.testdemo.request.Request;
import com.huawei.testdemo.util.StringUtils;
import java.util.Map;

public class SendService {
    public SendService() {
    }

    public static String send(String urlEnd, String token, String reqbody, String reqmethod, String cookieString) {
        String url = ServiceUtil.getPrefix() + urlEnd;
        String resp;
        Map<String, Object> result;
        if (reqmethod.equals("0")) {
            result = Request.sendGet(url, token, cookieString);
            resp = StringUtils.beanToJson(result);
        } else {
            result = Request.sendPost(url, token, reqbody, cookieString);
            resp = StringUtils.beanToJson(result);
        }

        return resp;
    }
}
