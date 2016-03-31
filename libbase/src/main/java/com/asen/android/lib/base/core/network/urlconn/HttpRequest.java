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
 * 需要自行控制参数特殊字符问题
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
class HttpRequest {

    private static final String TAG = HttpRequest.class.getName();

    private int TIMEOUT = 10 * 1000;

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2的形式。
     * @return URL 所代表远程资源的响应结果
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
//            urlNameString = urlNameString.replaceAll(" ", "%20"); // 转义空格

            if (!TextUtils.isEmpty(param)) url += "?" + param;

            LogUtil.d(TAG, url);

            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestMethod("GET");
            // 建立实际的连接
            conn.connect();
            // // 获取所有响应头字段
            // Map<String, List<String>> map = conn.getHeaderFields();
            // // 遍历所有的响应头字段
            // for (String key : map.keySet()) {
            // System.out.println(key + "--->" + map.get(key));
            // }
            // 得到响应码
            int res = conn.getResponseCode();
            if (res == 200) {
                // 定义BufferedReader输入流来读取URL的响应
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
        // 使用finally块来关闭输入流
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
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    String sendPost(String url, String param) throws HttpResponseException {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(),"utf-8")); // 乱码情况解决
            if (param != null) {
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数
                out.print(param);
                // flush输出流的缓冲
                out.flush();
            }
            // 得到响应码
            int res = conn.getResponseCode();
            if (res == 200) {
                // 定义BufferedReader输入流来读取URL的响应
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
        // 使用finally块来关闭输出流、输入流
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
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url    访问的服务器URL
     * @param params 普通参数
     * @param files  文件参数
     * @return
     * @throws IOException
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
            conn.setConnectTimeout(TIMEOUT);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }

            out = new DataOutputStream(conn.getOutputStream());
            out.write(sb.toString().getBytes());
            // 发送文件数据
            if (files != null) {
                for (Map.Entry<String, File> file : files.entrySet()) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    // name是post中传参的键 filename是文件的名称
                    sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\"" + file.getValue().getName() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                    sb1.append(LINEND);
                    out.write(sb1.toString().getBytes());

                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    is.close();
                    out.write(LINEND.getBytes());
                }

                // 请求结束标志
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                out.write(end_data);
                out.flush();
            }

            // 得到响应码
            int res = conn.getResponseCode();
            if (res == 200) {
                // 定义BufferedReader输入流来读取URL的响应
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
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url    访问的服务器URL
     * @param infoList 普通参数
     * @param fileList  文件参数
     * @return
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
            conn.setConnectTimeout(TIMEOUT);
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            // 首先组拼文本类型的参数
            StringBuilder sb = new StringBuilder();
            for (HttpTextEntity info : infoList) {
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\"" + info.getName() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(info.getValue());
                sb.append(LINEND);
            }

            out = new DataOutputStream(conn.getOutputStream());
            out.write(sb.toString().getBytes());
            // 发送文件数据
            if (fileList != null) {
                for (HttpFileEntity file : fileList) {
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append(PREFIX);
                    sb1.append(BOUNDARY);
                    sb1.append(LINEND);
                    // name是post中传参的键 filename是文件的名称
                    sb1.append("Content-Disposition: form-data; name=\"" + file.getName() + "\"; filename=\"" + file.getValue().getName() + "\"" + LINEND);
                    sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                    sb1.append(LINEND);
                    out.write(sb1.toString().getBytes());

                    InputStream is = new FileInputStream(file.getValue());
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    is.close();
                    out.write(LINEND.getBytes());
                }

                // 请求结束标志
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                out.write(end_data);
                out.flush();
            }

            // 得到响应码
            int res = conn.getResponseCode();
            if (res == 200) {
                // 定义BufferedReader输入流来读取URL的响应
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
     * 通过GET请求获得byte[]
     *
     * @param url
     * @return
     */
    byte[] sendGet2ByteArray(String url) {
        byte[] result = null;
        InputStream is = null;
        ByteArrayOutputStream os = null;
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestMethod("GET");
            // 建立实际的连接
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
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
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

    // /**
    // * 通过GET请求获得String
    // *
    // * @param url
    // * @return
    // */
    // static String sendGet2String(String url) {
    // StringBuffer result = new StringBuffer("");
    // BufferedReader in = null;
    // try {
    // URL realUrl = new URL(url);
    // // 打开和URL之间的连接
    // HttpURLConnection conn = (HttpURLConnection) realUrl
    // .openConnection();
    // // 设置通用的请求属性
    // conn.setRequestProperty("accept", "*/*");
    // conn.setRequestProperty("connection", "Keep-Alive");
    // conn.setRequestProperty("user-agent",
    // "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    // conn.setConnectTimeout(TIMEOUT);
    // conn.setRequestMethod("GET");
    // // 建立实际的连接
    // conn.connect();
    // // 定义 BufferedReader输入流来读取URL的响应
    // in = new BufferedReader(
    // new InputStreamReader(conn.getInputStream()));
    // String line;
    // while ((line = in.readLine()) != null) {
    // result.append(line);
    // }
    // } catch (Exception e) {
    // System.out.println("发送GET请求出现异常！" + e);
    // e.printStackTrace();
    // }
    // // 使用finally块来关闭输入流
    // finally {
    // try {
    // if (in != null) {
    // in.close();
    // }
    // } catch (Exception e2) {
    // e2.printStackTrace();
    // }
    // }
    //
    // return result.toString();
    // }
}
