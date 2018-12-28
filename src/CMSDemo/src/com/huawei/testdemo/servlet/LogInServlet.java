
package com.huawei.testdemo.servlet;

import com.huawei.testdemo.service.LogInService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogInServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(LogInServlet.class);
    private static final long serialVersionUID = 1L;

    public LogInServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        String workNo = request.getParameter("workNo");
        String password = request.getParameter("password");
        Map<String, Object> loginRes  = LogInService.logIn(workNo, password);
        if ("0".equals(loginRes.get("retcode"))) {
            Map<String, Object> result = (Map<String, Object>) loginRes.get("result");
            String token = (String) result.get("token");
            String cookieString = (String)loginRes.get("cookieString");
            HttpSession session = request.getSession();
            session.setAttribute("token", token);
            session.setAttribute("cookieString", cookieString);
            loginRes.remove("cookieString");
            String list = loginRes.toString();
            PrintWriter out = null;
            try
            {
                out = response.getWriter();
                out.print(list);
                out.flush();
            }
            catch (Exception e)
            {
                LOG.error(e.getMessage());
            }
            finally
            {
                if (null != out)
                {
                    out.close();
                }
            }

        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doPost(request, response);
    }
}
