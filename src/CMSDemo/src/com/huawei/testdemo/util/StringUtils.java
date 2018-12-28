//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.huawei.testdemo.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils {
    public static final String EMPTY_STRING = "";
    public static final String CONNECT_ERROR = "{\"message\":\"request to server failed\", \"retcode\":\"NETWORKERROR\"}";
    public static final String HAND_SHAKE_ERROR = "{\"message\":\"server refuse\", \"retcode\":\"HANDSHAKEERROR\"}";
    public static final String SOCKET_TIMEOUT = "{\"message\":\"socket time out\", \"retcode\":\"SOCKETTIMEOUT\"}";
    public static final String ENTITY_BLANK = "{\"message\":\"entity is null\", \"retcode\":\"ENTITYBLANK\"}";
    public static final String SUCCESS = "0";
    public static final String NETWORKERROR = "NETWORKERROR";
    public static final String AGENTNOTLOG = "100-006";
    public static final String SOCKETTIMEOUT = "SOCKETTIMEOUT";
    public static final String HANDSHAKEERROR = "HANDSHAKEERROR";
    private static final Logger LOG = LoggerFactory.getLogger(StringUtils.class);

    public StringUtils() {
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static HashMap<String, Object> jsonToMap(String json) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            HashMap<String, Object> result = (HashMap)objectMapper.readValue(json, HashMap.class);
            return result;
        } catch (JsonParseException var4) {
            LOG.error("catch JsonParseException");
            return null;
        } catch (JsonMappingException var5) {
            LOG.error("catch JsonMappingException");
            return null;
        } catch (IOException var6) {
            LOG.error("catch IOException");
            return null;
        }
    }

    public static String beanToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        JsonGenerator gen = null;
        String json = "";

        try {
            gen = (new JsonFactory()).createJsonGenerator(writer);
            mapper.writeValue(gen, object);
            json = writer.toString();
        } catch (IOException var18) {
            LOG.error("object to json string failed");
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException var17) {
                    LOG.error("close Json generator failed");
                }
            }

            try {
                writer.close();
            } catch (IOException var16) {
                LOG.error("close StringWriter failed");
            }

        }

        return json;
    }
}
