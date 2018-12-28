package com.huawei.testdemo.util;

public class LogUtils {
    public LogUtils() {
    }

    public static String encodeForLog(Object obj) {
        if (obj == null) {
            return "null";
        } else {
            String msg = obj.toString();
            int length = msg.length();
            StringBuilder sb = new StringBuilder(length);

            for(int i = 0; i < length; ++i) {
                char ch = msg.charAt(i);
                if (ch == '\r' || ch == '\n') {
                    ch = '_';
                }

                sb.append(ch);
            }

            return sb.toString();
        }
    }
}
