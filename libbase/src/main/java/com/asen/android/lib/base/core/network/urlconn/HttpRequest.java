package com.asen.android.lib.base.core.network.urlconn;

import android.text.TextUtils;

import com.asen.android.lib.base.core.network.urlconn.bean.HttpFileEntity;
import com.asen.android.lib.base.core.network.urlconn.bean.HttpTextEntity;
import com.asen.android.lib.base.tool.util.LogUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * ���������࣬��Ҫ���п��Ʋ��������ַ����⣨���಻���Ÿ��ⲿʹ�ã�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
class HttpRequest {

    private static final String TAG = HttpRequest.class.getName();

    public static final int SINGLE_NODE_BYTE_SIZE = 1024 * 1024;

    /**
     * ��ָ��URL����GET����������
     *
     * @param url   ���������URL
     * @param param ����������������Ӧ���� name1=value1&name2=value2����ʽ��
     * @return URL ����Զ����Դ����Ӧ���
     * @throws HttpResponseException
     */
    String sendGet(String url, String param) throws HttpResponseException {
        String result = "";
        BufferedReader in = null;
        HttpURLConnection conn = null;
        try {
//            String urlNameString = url + "?" + param;
//            System.out.println(urlNameString);

            // urlString = urlString.replaceAll("&", "%26");
            // urlString = urlString.replaceAll("?", "%3F");
            // urlString = urlString.replaceAll("=", "%3D");
            // urlString = urlString.replaceAll("|", "%124");
            // urlString = urlString.replaceAll("#", "%23");
            // urlString = urlString.replaceAll("/", "%2F");
            // urlString = urlString.replaceAll("+", "%2B");
            // urlString = urlString.replaceAll("%", "%25");
            // urlString = urlString.replaceAll("<", "%3C");
            // urlString = urlString.replaceAll(">", "%3E");
//            urlNameString = urlNameString.replaceAll(" ", "%20"); // ת��ո�

            if (!TextUtils.isEmpty(param)) url += "?" + param;

            LogUtil.d(TAG, url);

            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            conn = (HttpURLConnection) realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setConnectTimeout(HttpUtil.CONN_TIMEOUT);
            conn.setRequestMethod("GET");
            // ����ʵ�ʵ�����
            conn.connect();
            // // ��ȡ������Ӧͷ�ֶ�
            // Map<String, List<String>> map = conn.getHeaderFields();
            // // �������е���Ӧͷ�ֶ�
            // for (String key : map.keySet()) {
            // System.out.println(key + "--->" + map.get(key));
            // }
            // �õ���Ӧ��
            int res = conn.getResponseCode();
            if (res == 200) {
                // ����BufferedReader����������ȡURL����Ӧ
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } else {
                throw new HttpResponseException("CONN ERROR: " + res);
            }
        } catch (Exception e) {
            throw new HttpResponseException(e);
        }
        // ʹ��finally�����ر�������
        finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ��ָ�� URL ����POST����������
     *
     * @param url   ��������� URL
     * @param param ����������������Ӧ���� name1=value1&name2=value2 ����ʽ��
     * @return ����Զ����Դ����Ӧ���
     * @throws HttpResponseException
     */
    String sendPost(String url, String param) throws HttpResponseException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // �򿪺�URL֮�������
            conn = (HttpURLConnection) realUrl.openConnection();
            // ����ͨ�õ���������
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setConnectTimeout(HttpUtil.CONN_TIMEOUT);
            conn.setRequestMethod("POST");
            // ����POST�������������������
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // ��ȡURLConnection�����Ӧ�������
//            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(),"utf-8")); // ����������
            if (param != null) {
                out = new PrintWriter(conn.getOutputStream());
                // �����������
                out.print(param);
                // flush������Ļ���
                out.flush();
            }
            // �õ���Ӧ��
            int res = conn.getResponseCode();
            if (res == 200) {
                // ����BufferedReader����������ȡURL����Ӧ
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } else {
                throw new HttpResponseException("CONN ERROR: " + res);
            }
        } catch (IOException e) {
            throw new HttpResponseException(e);
        }
        // ʹ��finally�����ر��������������
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * ͨ��ƴ�ӵķ�ʽ�����������ݣ�ʵ�ֲ��������Լ��ļ�����
     *
     * @param url    ���ʵķ�����URL
     * @param params ��ͨ����
     * @param files  �ļ�����
     * @return ����Զ����Դ����Ӧ���
     * @throws HttpResponseException
     */
    String sendPost(String url, Map<String, String> params, Map<String, File> files) throws HttpResponseException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        String result = "";
        HttpURLConnection conn = null;
        DataOutputStream out = null;
        BufferedReader in = null;
        try {
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(HttpUtil.CONN_TIMEOUT);
            conn.setDoInput(true);// ��������
            conn.setDoOutput(true);// �������
            conn.setUseCaches(false); // ������ʹ�û���
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            out = new DataOutputStream(conn.getOutputStream());

            // ������ƴ�ı����͵Ĳ���
            StringBuilder sb = new StringBuilder();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    sb.append("Content-Disposition: form-data; name=\"").append(entry.getKey()).append("\"").append(LINEND);
                    sb.append("Content-Type: text/plain; charset=").append(CHARSET).append(LINEND);
                    sb.append("Content-Transfer-Encoding: 8bit").append(LINEND);
                    sb.append(LINEND);
                    sb.append(entry.getValue());
                    sb.append(LINEND);
                }
                out.write(sb.toString().getBytes());
            }

            // �����ļ�����
            if (files != null) {
                for (Map.Entry<String, File> file : files.entrySet()) {
                    sb = new StringBuilder();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    // name��post�д��εļ� filename���ļ�������
                    sb.append("Content-Disposition: form-data; name=\"").append(file.getKey()).append("\"; filename=\"").append(file.getValue().getName()).append("\"").append(LINEND);
                    sb.append("Content-Type: application/octet-stream; charset=").append(CHARSET).append(LINEND);
                    sb.append(LINEND);
                    out.write(sb.toString().getBytes());

                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[SINGLE_NODE_BYTE_SIZE];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    is.close();
                    out.write(LINEND.getBytes());
                }

                // ���������־
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                out.write(end_data);
            }
            out.flush();

            // �õ���Ӧ��
            int res = conn.getResponseCode();
            if (res == 200) {
                // ����BufferedReader����������ȡURL����Ӧ
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } else {
                throw new HttpResponseException("CONN ERROR: " + res);
            }
        } catch (IOException e) {
            throw new HttpResponseException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }

    /**
     * ͨ��ƴ�ӵķ�ʽ�����������ݣ�ʵ�ֲ��������Լ��ļ�����
     *
     * @param url      ���ʵķ�����URL
     * @param infoList ��ͨ����
     * @param fileList �ļ�����
     * @return ����Զ����Դ����Ӧ���
     * @throws HttpResponseException
     */
    String sendPost(String url, List<HttpTextEntity> infoList, List<HttpFileEntity> fileList) throws HttpResponseException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        String result = "";
        HttpURLConnection conn = null;
        DataOutputStream out = null;
        BufferedReader in = null;
        try {
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(HttpUtil.CONN_TIMEOUT);
            conn.setDoInput(true);// ��������
            conn.setDoOutput(true);// �������
            conn.setUseCaches(false); // ������ʹ�û���
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            out = new DataOutputStream(conn.getOutputStream());

            // ������ƴ�ı����͵Ĳ���
            StringBuilder sb = new StringBuilder();
            if (infoList != null) {
                for (HttpTextEntity info : infoList) {
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    sb.append("Content-Disposition: form-data; name=\"").append(info.getName()).append("\"").append(LINEND);
                    sb.append("Content-Type: text/plain; charset=").append(CHARSET).append(LINEND);
                    sb.append("Content-Transfer-Encoding: 8bit").append(LINEND);
                    sb.append(LINEND);
                    sb.append(info.getValue());
                    sb.append(LINEND);
                }
                out.write(sb.toString().getBytes());
            }

            // �����ļ�����
            if (fileList != null) {
                for (HttpFileEntity file : fileList) {
                    sb = new StringBuilder();
                    sb.append(PREFIX);
                    sb.append(BOUNDARY);
                    sb.append(LINEND);
                    // name��post�д��εļ� filename���ļ�������
                    sb.append("Content-Disposition: form-data; name=\"").append(file.getName()).append("\"; filename=\"").append(file.getValue().getName()).append("\"").append(LINEND);
                    sb.append("Content-Type: application/octet-stream; charset=").append(CHARSET).append(LINEND);
                    sb.append(LINEND);
                    out.write(sb.toString().getBytes());

                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[SINGLE_NODE_BYTE_SIZE];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    is.close();
                    out.write(LINEND.getBytes());
                }

                // ���������־
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                out.write(end_data);
            }
            out.flush();

            // �õ���Ӧ��
            int res = conn.getResponseCode();
            if (res == 200) {
                // ����BufferedReader����������ȡURL����Ӧ
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } else {
                throw new HttpResponseException("CONN ERROR: " + res);
            }
        } catch (IOException e) {
            throw new HttpResponseException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }

    /**
     * ͨ�����ķ�ʽ�ϴ���Ϣ
     *
     * @param url                      ���ʵķ�����URL
     * @param httpOutputStreamListener ����Ϣ���ݵļ����ӿ�
     * @return ����Զ����Դ����Ӧ���
     * @throws HttpResponseException
     */
    String sendPostData(String url, OnHttpOutputStreamListener httpOutputStreamListener) throws HttpResponseException {
        String result = "";
        HttpURLConnection conn = null;
        DataOutputStream out = null;
        BufferedReader in = null;
        try {
            URL uri = new URL(url);
            conn = (HttpURLConnection) uri.openConnection();
            conn.setConnectTimeout(HttpUtil.CONN_TIMEOUT);
            conn.setDoInput(true);// ��������
            conn.setDoOutput(true);// �������
            conn.setUseCaches(false); // ������ʹ�û���
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            conn.setRequestProperty("SOAPAction", "");

            if (httpOutputStreamListener != null) {
                out = new DataOutputStream(conn.getOutputStream());
                httpOutputStreamListener.outputData(out);
                out.flush();
            }

            // �õ���Ӧ��
            int res = conn.getResponseCode();
            if (res == 200) {
                // ����BufferedReader����������ȡURL����Ӧ
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } else {
                throw new HttpResponseException("CONN ERROR: " + res);
            }
        } catch (IOException e) {
            throw new HttpResponseException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }

    /**
     * ͨ��GET������byte[]
     *
     * @param url get������ʵ�ַ
     * @return ����Զ����Դ����Ӧ������ֽ����飩
     */
    byte[] sendGet2ByteArray(String url) throws HttpResponseException {
        byte[] result = null;
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(HttpUtil.CONN_TIMEOUT);
            conn.setRequestMethod("GET");
            // ����ʵ�ʵ�����
            conn.connect();
            is = conn.getInputStream();
            os = new ByteArrayOutputStream();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                result = os.toByteArray();
            }
        } catch (Exception e) {
//            System.out.println("���� POST ��������쳣��" + e);
            e.printStackTrace();
            throw new HttpResponseException(e);
        }
        // ʹ��finally�����ر��������������
        finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }


}
