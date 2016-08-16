package com.asen.android.lib.base.core.network.urlconn;

import com.asen.android.lib.base.core.network.urlconn.bean.HttpFileEntity;
import com.asen.android.lib.base.core.network.urlconn.bean.HttpTextEntity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 网络请求工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class HttpUtil {

    /**
     * 是否上传的SQL语句
     */
    public static boolean IS_SQL = false;

    /**
     * Http请求连接超时的时间间隔（毫秒值）
     */
    public static int CONN_TIMEOUT = 10 * 1000;

    static HttpRequest mHttpRequest;

    static {
        mHttpRequest = new HttpRequest();
    }

    /**
     * get请求
     *
     * @param url url地址
     * @return 获得的string结果
     * @throws HttpResponseException
     */
    public static String get(String url) throws HttpResponseException {
        return get(url, null);
    }

    /**
     * get请求
     *
     * @param url      url地址
     * @param infoList 参数
     * @return 获得的string结果
     * @throws HttpResponseException
     */
    public static String get(String url, List<HttpTextEntity> infoList) throws HttpResponseException {
        if (infoList == null) return mHttpRequest.sendGet(url, null);

        StringBuffer sb = null;
        try {
            for (HttpTextEntity info : infoList) {
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append(info.getName());
                    sb.append("=");
                    sb.append(URLEncoder.encode(info.getValue(), "utf-8"));
                } else {
                    sb.append("&");
                    sb.append(info.getName());
                    sb.append("=");
                    sb.append(URLEncoder.encode(info.getValue(), "utf-8"));
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new HttpResponseException(e);
        }

        String params = sb == null ? null : sb.toString();
        if (IS_SQL) {
            assert params != null;
            params = params.replace("+", "%20");
        }

        return mHttpRequest.sendGet(url, params);
    }

    /**
     * 获得字节byte数组
     *
     * @param url url地址
     * @return 获得的byte结果
     */
    public static byte[] getBytes(String url) throws HttpResponseException {
        return mHttpRequest.sendGet2ByteArray(url);
    }

    /**
     * post请求
     *
     * @param url      url地址
     * @param infoList 参数
     * @return 获得的string结果
     * @throws HttpResponseException
     */
    public static String post(String url, List<HttpTextEntity> infoList) throws HttpResponseException {
        if (infoList == null) return mHttpRequest.sendPost(url, null);

        StringBuffer sb = null;
        try {
            for (HttpTextEntity info : infoList) {
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append(info.getName());
                    sb.append("=");
                    sb.append(URLEncoder.encode(info.getValue(), "utf-8"));
                } else {
                    sb.append("&");
                    sb.append(info.getName());
                    sb.append("=");
                    sb.append(URLEncoder.encode(info.getValue(), "utf-8"));
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new HttpResponseException(e);
        }

        String params = sb == null ? null : sb.toString();
        if (IS_SQL) {
            assert params != null;
            params = params.replace("+", "%20");
        }

        return mHttpRequest.sendPost(url, params);
    }

    /**
     * post请求（含文件）
     *
     * @param url      url地址
     * @param infoList 参数
     * @param fileList 文件参数
     * @return 获得的string结果
     * @throws HttpResponseException
     */
    public static String post(String url, List<HttpTextEntity> infoList, List<HttpFileEntity> fileList) throws HttpResponseException {
        return mHttpRequest.sendPost(url, infoList, fileList);
    }

    /**
     * post请求（含文件）
     *
     * @param url    url地址
     * @param params 参数
     * @param files  文件参数
     * @return 获得的string结果
     * @throws HttpResponseException
     */
    public static String post(String url, Map<String, String> params, Map<String, File> files) throws HttpResponseException {
        return mHttpRequest.sendPost(url, params, files);
    }

    /**
     * 通过流的方式上传信息
     *
     * @param url                      访问的服务器URL
     * @param httpOutputStreamListener 流信息传递的监听接口
     * @return 返回远程资源的响应结果
     * @throws HttpResponseException
     */
    public static String postData(String url, OnHttpOutputStreamListener httpOutputStreamListener) throws HttpResponseException {
        return mHttpRequest.sendPostData(url, httpOutputStreamListener);
    }

}