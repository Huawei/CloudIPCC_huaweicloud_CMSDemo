

package com.huawei.testdemo.request;

import com.huawei.testdemo.util.LogUtils;
import com.huawei.testdemo.util.StringUtils;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Request {
    private static final Logger LOG = LoggerFactory.getLogger(Request.class);
    private static final int MAXCONNECTION = 2000;
    private static final int MAXPERROUTE = 2000;
    private static final int REQUESTTIMEOUT = 2000;
    private static final int CONNECTIMEOUT = 2000;
    private static final int SOCKETIMEOUT = 11000;
    private static PoolingHttpClientConnectionManager connManager = null;
    private static CloseableHttpClient client = null;

    public Request() {
    }

    public static void init() {
        try {
            SSLContext sslContext = (new SSLContextBuilder()).loadTrustMaterial((KeyStore)null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf).build();
            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            connManager.setMaxTotal(MAXCONNECTION);
            connManager.setDefaultMaxPerRoute(MAXPERROUTE);
        } catch (RuntimeException var3) {
            throw var3;
        } catch (Exception var4) {
            LOG.error("init connection pool failed \r\n {}: ", LogUtils.encodeForLog(var4.getMessage()));
            return;
        }

        client = getConnection();
    }

    private static CloseableHttpClient getConnection() {
        RequestConfig restConfig = RequestConfig.custom().setConnectionRequestTimeout(REQUESTTIMEOUT).setConnectTimeout(CONNECTIMEOUT).setSocketTimeout(SOCKETIMEOUT).build();
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {
                    return false;
                } else if (exception instanceof NoHttpResponseException) {
                    return true;
                } else if (exception instanceof InterruptedIOException) {
                    return false;
                } else if (exception instanceof SSLHandshakeException) {
                    return false;
                } else if (exception instanceof UnknownHostException) {
                    return false;
                } else if (exception instanceof ConnectTimeoutException) {
                    return false;
                } else if (exception instanceof SSLException) {
                    return false;
                } else {
                    HttpClientContext clientContext = HttpClientContext.adapt(context);
                    HttpRequest request = clientContext.getRequest();
                    return !(request instanceof HttpEntityEnclosingRequest);
                }
            }
        };
        CloseableHttpClient httpClient = HttpClients.custom().disableCookieManagement().setConnectionManager(connManager).setDefaultRequestConfig(restConfig).setRetryHandler(retryHandler).build();
        return httpClient;
    }

    public static Map<String, Object> logPost(String url, Map<String, Object> entityParams) {
        Map<String, Object> result = null;
        HttpPost post = null;
        CloseableHttpResponse response = null;

        try {
            url = Normalizer.normalize(url, Form.NFKC);
            post = new HttpPost(url);
            if (entityParams != null) {
                String jsonString = beanToJson(entityParams);
                HttpEntity entity = new StringEntity(jsonString);
                post.setEntity(entity);
            }

            post.setHeader("Content-Type", "application/json;charset=UTF-8");
            response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String entityContent = EntityUtils.toString(entity, "UTF-8");
                    result = StringUtils.jsonToMap(entityContent);
                    saveCookie(result, response);
                } else {
                    result = StringUtils.jsonToMap("{\"message\":\"entity is null\", \"retcode\":\"ENTITYBLANK\"}");
                }

                try {
                    EntityUtils.consume(entity);
                } catch (IOException var20) {
                    LOG.error("release entity failed \r\n {}", LogUtils.encodeForLog(var20.getMessage()));
                }
            }
        } catch (UnsupportedEncodingException var21) {
            result = returnConnectError(var21);
        } catch (ClientProtocolException var22) {
            result = returnConnectError(var22);
        } catch (IOException var23) {
            result = returnConnectError(var23);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException var19) {
                    LOG.error("release response failed \r\n {}", LogUtils.encodeForLog(var19.getMessage()));
                }
            }

        }

        return result;
    }

    public static Map<String, Object> sendPost(String url, String token, String jsonString, String cookieString) {
        Map<String, Object> result = null;
        HttpPost post = null;
        CloseableHttpResponse response = null;

        try {
            url = Normalizer.normalize(url, Form.NFKC);
            post = new HttpPost(url);
            if (jsonString != null) {
                HttpEntity entity = new StringEntity(jsonString);
                post.setEntity(entity);
            }

            post.setHeader("Content-Type", "application/json;charset=UTF-8");
            post.addHeader("cmsgateway-token", token);
            post.setHeader("Cookie", cookieString);
            response = client.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String entityContent = EntityUtils.toString(entity, "UTF-8");
                    result = StringUtils.jsonToMap(entityContent);
                } else {
                    result = StringUtils.jsonToMap("{\"message\":\"entity is null\", \"retcode\":\"ENTITYBLANK\"}");
                }

                try {
                    EntityUtils.consume(entity);
                } catch (IOException var21) {
                    LOG.error("release entity failed \r\n {}", LogUtils.encodeForLog(var21.getMessage()));
                }
            }
        } catch (UnsupportedEncodingException var22) {
            result = returnConnectError(var22);
        } catch (ClientProtocolException var23) {
            result = returnConnectError(var23);
        } catch (IOException var24) {
            result = returnConnectError(var24);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException var20) {
                    LOG.error("release response failed \r\n {}", LogUtils.encodeForLog(var20.getMessage()));
                }
            }

        }

        return result;
    }

    public static Map<String, Object> sendGet(String url, String token, String cookieString) {
        Map<String, Object> result = null;
        HttpGet get = null;
        CloseableHttpResponse response = null;

        try {
            url = Normalizer.normalize(url, Form.NFKC);
            get = new HttpGet(url);
            get.setHeader("Content-Type", "application/json;charset=UTF-8");
            get.addHeader("cmsgateway-token", token);
            get.setHeader("Cookie", cookieString);
            response = client.execute(get);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String entityContent = EntityUtils.toString(entity, "UTF-8");
                    result = StringUtils.jsonToMap(entityContent);
                } else {
                    result = StringUtils.jsonToMap("{\"message\":\"entity is null\", \"retcode\":\"ENTITYBLANK\"}");
                }

                try {
                    EntityUtils.consume(entity);
                } catch (IOException var18) {
                    LOG.error("release entity failed \r\n {}", LogUtils.encodeForLog(var18.getMessage()));
                }
            }
        } catch (SocketException | NoHttpResponseException | SSLHandshakeException var19) {
            result = StringUtils.jsonToMap("{\"message\":\"server refuse\", \"retcode\":\"HANDSHAKEERROR\"}");
            LOG.error("request to server failed: \r\n {}", LogUtils.encodeForLog(var19.getMessage()));
        } catch (Exception var20) {
            result = returnConnectError(var20);
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                    response.close();
                } catch (IOException var17) {
                    LOG.error("release response failed \r\n {}", LogUtils.encodeForLog(var17.getMessage()));
                }
            }

        }

        return result;
    }

    public static String beanToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter writer = new StringWriter();
        JsonGenerator gen = null;
        String json = null;

        try {
            gen = (new JsonFactory()).createJsonGenerator(writer);
            mapper.writeValue(gen, object);
            json = writer.toString();
            writer.close();
        } catch (IOException var18) {
            ;
        } finally {
            if (gen != null) {
                try {
                    gen.close();
                } catch (IOException var17) {
                    LOG.error("beanToJson failed \r\n {}", LogUtils.encodeForLog(var17.getMessage()));
                }
            }

            try {
                writer.close();
            } catch (IOException var16) {
                LOG.error("beanToJson failed \r\n {}", LogUtils.encodeForLog(var16.getMessage()));
            }

        }

        return json;
    }

    private static void saveCookie(Map<String, Object> result, CloseableHttpResponse response) {
        if (result != null && result.get("retcode") != null && "0".equalsIgnoreCase(result.get("retcode").toString())) {
            String json = beanToJson(result.get("result"));
            HashMap<String, Object> map = StringUtils.jsonToMap(json);
            if (map != null && map.get("token") != null) {
                String cookie = getCookie(response);
                result.put("cookieString", cookie);
            }
        }

    }

    private static HashMap<String, Object> returnConnectError(Exception e) {
        LOG.error("request to server failed: \r\n {}", LogUtils.encodeForLog(e.getMessage()));
        String returnString = "{\"message\":\"request to server failed\", \"retcode\":\"NETWORKERROR\"}";
        return StringUtils.jsonToMap(returnString);
    }

    private static String getCookie(CloseableHttpResponse response) {
        Header[] allHeaders = response.getAllHeaders();

        if (allHeaders == null || allHeaders.length == 0)
        {
            return "";
        }
        StringBuffer list = new StringBuffer();
        String setCookie;
        for (Header header : allHeaders)
        {
            if (!header.getName().equals("Set-Cookie"))
            {
                continue;
            }

            setCookie = header.getValue();
            if (setCookie != null)
            {
                list.append(setCookie).append(";");
            }
        }
        return list.toString();
    }


}
