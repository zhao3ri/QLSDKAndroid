package com.qinglan.sdk.android.net;

import com.qinglan.sdk.android.common.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhaoj on 2018/9/20
 *
 * @author zhaoj
 */
class HttpRequest {
    private static final String LOG_HTTP_REQUEST_INFO = "REQUEST";
    private static final String LOG_HTTP_GET_ERROR = "GetError";
    private static final String LOG_HTTP_POST_ERROR = "PostError";

    private static final String CONTENT_TYPE_URL_ENCODED = "application/x-www-form-urlencoded";
    private static final String CHARSET = "utf-8";
    private static final int TIME_OUT = 20000;

    protected synchronized static String execute(IRequestInfo request) throws Exception {
        String jsonResult = "";
        String url = request.getUrl();
        switch (request.getMethod()) {
            case HttpMethod.GET:
                if (request.getParams() == null) {
                    jsonResult = get(url);
                } else {
                    jsonResult = get(url, request.getParams());
                }
                break;
            case HttpMethod.POST:
                if (request.getParams() == null) {
                    jsonResult = post(url);
                } else {
                    jsonResult = post(url, request.getParams());
                }
                break;
        }
        return jsonResult;
    }

    private synchronized static String get(String url) throws Exception {
        return get(url, null);
    }

    /**
     * 执行http GET请求
     *
     * @param url
     * @return 返回Json格式的响应数据
     * @throws Exception
     */
    private synchronized static String get(String url, Map<String, Object> params) throws Exception {
        String jsonResult = "";
        InputStream is = null;
        StringBuffer reqUrl = new StringBuffer(url);
        try {
            if (params != null && params.size() > 0) {
                String urlEncodedForm = toUrlEncodedFormParams(params);
                reqUrl.append("?");
                reqUrl.append(urlEncodedForm);
            }
            HttpURLConnection conn = getHttpURLConnection(reqUrl.toString());
            conn.setRequestMethod("GET");
            jsonResult = getHttpConnectionResponse(conn, is);
        } catch (IOException e) {
            Log.e(LOG_HTTP_GET_ERROR, e.toString());
            throw (e);
        } finally {
            closeStream(is);
        }
        return jsonResult;
    }

    private synchronized static String post(String url) throws Exception {
        return post(url, null);
    }

    /**
     * 执行http post请求
     *
     * @param url
     * @param params 包含请求参数的Map
     * @return
     * @throws Exception
     */
    private synchronized static String post(String url, Map<String, Object> params) throws Exception {
        String jsonResult = "";
        OutputStream os = null;
        InputStream is = null;

        try {
            HttpURLConnection conn = getHttpURLConnection(url);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", CONTENT_TYPE_URL_ENCODED);
            conn.setRequestProperty("Charset", CHARSET);

            os = conn.getOutputStream();
            if (params != null && params.size() > 0) {
                String urlEncodedForm = toUrlEncodedFormParams(params);
                os.write(urlEncodedForm.getBytes());
                os.flush();
            }
            jsonResult = getHttpConnectionResponse(conn, is);
        } catch (IOException e) {
            Log.e(LOG_HTTP_POST_ERROR, e.toString());
            throw (e);
        } finally {
            closeStream(is);
            closeStream(os);
        }
        return jsonResult;
    }

    private static String getHttpConnectionResponse(HttpURLConnection conn, InputStream is) throws IOException {
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            is = conn.getInputStream();
            return read(is);
        } else {
            throw (new ConnectException("Unknown error:" + conn.getResponseCode() + "!\n" + conn.getResponseMessage()));
        }
    }

    /**
     * 将包含http post请求数据的map，解析为UrlEncoded格式的字符串
     *
     * @param params
     * @return
     */
    public static String toUrlEncodedFormParams(Map<String, Object> params) {
        StringBuffer strBuffer = new StringBuffer();
        Set<String> keySet = params.keySet();
        Iterator<String> i = keySet.iterator();
        while (i.hasNext()) {
            try {
                String key = i.next();
                Object value = params.get(key);
                strBuffer.append(key);
                strBuffer.append("=");
                strBuffer.append(value);
                if (i.hasNext()) {
                    strBuffer.append("&");
                }
            } catch (Exception e) {
                Log.d(LOG_HTTP_REQUEST_INFO, i.next() + " key is error");
                continue;
            }
        }
        Log.d("params-->" + strBuffer.toString());
        return strBuffer.toString();
    }

    /**
     * 获得HttpURLConnection连接实例
     *
     * @param strURL
     * @return 返回HttpURLConnection实例
     * @throws IOException
     */
    protected static HttpURLConnection getHttpURLConnection(String strURL)
            throws IOException {
        URL url = new URL(strURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(TIME_OUT);
        conn.setReadTimeout(TIME_OUT);
        return conn;
    }

    /**
     * 从输入流中读出文本信息
     */
    private static String read(InputStream is) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[128];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }

        String text = new String(out.toByteArray(), CHARSET);
        out.flush();
        closeStream(out);
        return text;
    }

    /**
     * 关闭IO流
     */
    public static void closeStream(Object obj) {
        if (obj != null && obj instanceof InputStream) {
            InputStream is = (InputStream) obj;
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (obj != null && obj instanceof OutputStream) {
            OutputStream os = (OutputStream) obj;
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
