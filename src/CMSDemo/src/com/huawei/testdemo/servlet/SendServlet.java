
package com.huawei.testdemo.servlet;

import com.huawei.testdemo.service.SendService;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(SendServlet.class);
    private static final long serialVersionUID = 1L;

    public SendServlet() {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        HttpSession session = request.getSession();
        String urlEnd = request.getParameter("urls").replaceAll("[^a-z/]", "");
        String token = (String)session.getAttribute("token");
        String cookieString = (String) session.getAttribute("cookieString");
        String reqbody = request.getParameter("reqbody");
        String reqmethod = request.getParameter("reqmethod");
        String body  = SendService.send(urlEnd, token, reqbody, reqmethod, cookieString);
        PrintWriter out = null;
        try
        {
            out = response.getWriter();
            out.print(body);
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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doPost(request, response);
    }
}
