package com.huawei.testdemo.service;

import com.huawei.testdemo.request.Request;
import java.util.HashMap;
import java.util.Map;

public class LogInService {
    public LogInService() {
    }

    public static Map<String, Object> logIn(String workNo, String password) {
        String url = ServiceUtil.getPrefix() + "userauth/auth";
        Map<String, Object> loginParam = new HashMap<String, Object>();
        loginParam.put("agentId", workNo);
        loginParam.put("password", password);

        return Request.logPost(url, loginParam);
    }
}
