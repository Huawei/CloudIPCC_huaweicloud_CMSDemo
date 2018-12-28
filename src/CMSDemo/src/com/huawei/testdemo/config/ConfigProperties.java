

package com.huawei.testdemo.config;

import com.huawei.testdemo.util.LogUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigProperties {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigProperties.class);
    private static Map<String, Properties> propsMap = new ConcurrentHashMap();

    private ConfigProperties() {
    }

    public static boolean loadConfig() {
        Field[] configListFields = ConfigList.class.getFields();
        String filePath = getConfigFilePath();

        try {
            Field[] var5 = configListFields;
            int var4 = configListFields.length;

            for(int var3 = 0; var3 < var4; ++var3) {
                Field field = var5[var3];
                String fileName = String.valueOf(field.get((Object)null));
                File fileConf = new File(filePath + fileName);
                if (fileConf.exists()) {
                    readFileProperties(fileConf);
                } else {
                    LOG.info("File:" + LogUtils.encodeForLog(fileConf.getAbsoluteFile()) + " not exist");
                    propsMap.put(fileName, new Properties());
                }
            }

            return true;
        } catch (IllegalAccessException | IllegalArgumentException var8) {
            LOG.info("Load properties from config file has exception");
            return false;
        }
    }

    private static String getConfigFilePath() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            return "";
        } else {
            URL url = contextClassLoader.getResource("");
            if (url == null) {
                return "";
            } else {
                String filePath = url.getPath().substring(0, url.getPath().lastIndexOf("/classes"));

                try {
                    filePath = URLDecoder.decode(filePath, "UTF-8") + "/config/";
                } catch (UnsupportedEncodingException var4) {
                    filePath = "";
                }

                return filePath;
            }
        }
    }

    private static void readFileProperties(File file) {
        BufferedInputStream inputFile = null;

        try {
            inputFile = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
            Properties props = new Properties();
            props.load(inputFile);
            propsMap.put(file.getName(), props);
        } catch (IOException var6) {
            LOG.error("Load config file failed.");
        } finally {
            closeFile(inputFile);
        }

    }

    public static String getKey(String configFile, String key) {
        if (propsMap.containsKey(configFile)) {
            String result = ((Properties)propsMap.get(configFile)).getProperty(key);
            if (result != null) {
                return result.trim();
            }
        }

        return "";
    }

    public static void setKey(String configFile, String key, String value) {
        if (propsMap.containsKey(configFile)) {
            if (value != null) {
                ((Properties)propsMap.get(configFile)).setProperty(key, value);
            } else {
                propsMap.remove(key);
            }
        }

    }

    public static Properties getProperties(String configFile) {
        return propsMap.containsKey(configFile) ? (Properties)propsMap.get(configFile) : null;
    }

    public static void setProperties(String configFile, Properties prop) {
        propsMap.put(configFile, prop);
    }

    private static void closeFile(InputStream inputFile) {
        try {
            if (inputFile != null) {
                inputFile.close();
            }
        } catch (IOException var2) {
            LOG.error("Close file failed.");
        }

    }

    public static void reLoadConfig(String configFile) {
        String filePath = getConfigFilePath() + configFile;
        File file = new File(filePath);
        if (!file.exists()) {
            LOG.info("File " + LogUtils.encodeForLog(file.getAbsoluteFile()) + "is not exist");
            propsMap.put(configFile, new Properties());
        } else {
            BufferedInputStream inputFile = null;

            try {
                inputFile = new BufferedInputStream(new FileInputStream(file.getAbsolutePath()));
                Properties props = new Properties();
                props.load(inputFile);
                propsMap.put(configFile, props);
            } catch (IOException var8) {
                LOG.error("Load config file failed.");
            } finally {
                closeFile(inputFile);
            }

        }
    }
}
