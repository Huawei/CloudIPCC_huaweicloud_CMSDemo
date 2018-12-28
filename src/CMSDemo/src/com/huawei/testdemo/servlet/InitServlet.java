
package com.huawei.testdemo.servlet;

import com.huawei.testdemo.config.ConfigProperties;
import com.huawei.testdemo.request.Request;
import com.huawei.testdemo.service.ServiceUtil;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitServlet implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(InitServlet.class);

    public InitServlet() {
    }

    public void contextDestroyed(ServletContextEvent arg0) {
    }

    public void contextInitialized(ServletContextEvent arg0) {
        boolean loadConfig = ConfigProperties.loadConfig();
        if (!loadConfig) {
            LOG.error("load config failed");
        }

        ServiceUtil.serviceInit();
        Request.init();
        LOG.info("contextInitialized detected.");
    }
}
