package com.asen.android.lib.base.core.network.urlconn;

import com.asen.android.lib.base.core.network.urlconn.bean.HttpFileEntity;
import com.asen.android.lib.base.core.network.urlconn.bean.HttpTextEntity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * �������󹤾���
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class HttpUtil {

    /**
     * �Ƿ��ϴ���SQL���
     */
    public static boolean IS_SQL = false;

    /**
     * Http�������ӳ�ʱ��ʱ����������ֵ��
     */
    public static int CONN_TIMEOUT = 10 * 1000;

    static HttpRequest mHttpRequest;

    static {
        mHttpRequest = new HttpRequest();
    }

    /**
     * get����
     *
     * @param url url��ַ
     * @return ��õ�string���
     * @throws HttpResponseException
     */
    public static String get(String url) throws HttpResponseException {
        return get(url, null);
    }

    /**
     * get����
     *
     * @param url      url��ַ
     * @param infoList ����
     * @return ��õ�string���
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
     * ����ֽ�byte����
     *
     * @param url url��ַ
     * @return ��õ�byte���
     */
    public static byte[] getBytes(String url) throws HttpResponseException {
        return mHttpRequest.sendGet2ByteArray(url);
    }

    /**
     * post����
     *
     * @param url      url��ַ
     * @param infoList ����
     * @return ��õ�string���
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
     * post���󣨺��ļ���
     *
     * @param url      url��ַ
     * @param infoList ����
     * @param fileList �ļ�����
     * @return ��õ�string���
     * @throws HttpResponseException
     */
    public static String post(String url, List<HttpTextEntity> infoList, List<HttpFileEntity> fileList) throws HttpResponseException {
        return mHttpRequest.sendPost(url, infoList, fileList);
    }

    /**
     * post���󣨺��ļ���
     *
     * @param url    url��ַ
     * @param params ����
     * @param files  �ļ�����
     * @return ��õ�string���
     * @throws HttpResponseException
     */
    public static String post(String url, Map<String, String> params, Map<String, File> files) throws HttpResponseException {
        return mHttpRequest.sendPost(url, params, files);
    }

    /**
     * ͨ�����ķ�ʽ�ϴ���Ϣ
     *
     * @param url                      ���ʵķ�����URL
     * @param httpOutputStreamListener ����Ϣ���ݵļ����ӿ�
     * @return ����Զ����Դ����Ӧ���
     * @throws HttpResponseException
     */
    public static String postData(String url, OnHttpOutputStreamListener httpOutputStreamListener) throws HttpResponseException {
        return mHttpRequest.sendPostData(url, httpOutputStreamListener);
    }

}