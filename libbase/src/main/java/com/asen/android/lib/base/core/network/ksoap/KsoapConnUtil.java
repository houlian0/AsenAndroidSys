package com.asen.android.lib.base.core.network.ksoap;


import com.asen.android.lib.base.tool.util.AppUtil;
import com.asen.android.lib.base.tool.util.FileUtil;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Simple to Introduction
 * Ksoap连接工具
 *
 * @ProjectName: HelloWorld
 * @Description:
 * @Author: Asen
 * @CreateDate: 2016-01-27
 * @Time: 14:13
 * @Version: [v1.0]
 */
public class KsoapConnUtil {

//    /**
//     * 调用的webservice命令空间
//     */
//    private static final String PACE = "http://tempuri.org/";

    /**
     * 连接超时的时间
     */
    private static final int CONN_TIMEOUT = 6000;

    /**
     * 通过webservice连接获取结果
     *
     * @param serviceUrl 连接地址
     * @param namespace  调用的webservice命令空间
     * @param modelName  方法名
     * @param json       单个参数，参数名为 "json"
     * @return 返回字符串结果
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static String getWsdlConnResult(String serviceUrl, String namespace, String modelName, String json) throws IOException, XmlPullParserException {
        Map<String, String> params = new HashMap<>();
        params.put("json", json);
        return getWsdlConnResult(serviceUrl, namespace, modelName, params);
    }

    /**
     * 通过webservice连接获取结果
     *
     * @param serviceUrl 连接地址
     * @param modelName  方法名
     * @param params     参数
     * @return 返回字符串结果
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static String getWsdlConnResult(String serviceUrl, String namespace, String modelName, Map<String, String> params) throws IOException, XmlPullParserException {
        HttpTransportSE httpSE = new HttpTransportSE(serviceUrl);
        httpSE.debug = true;
        // 创建soapObject对象并传入命名空间和方法名
        SoapObject soapObject = new SoapObject(namespace, modelName);
        // 添加参数
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> e : entries) {
                soapObject.addProperty(e.getKey(), e.getValue());
            }
        }

        // 创建SoapSerializationEnvelope对象并传入SOAP协议的版本号
        SoapSerializationEnvelope soapserial = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapserial.bodyOut = soapObject;
        // 设置与.NET提供的Web service保持有良好的兼容性
        soapserial.dotNet = true;

        // 调用HttpTransportSE对象的call方法来调用 webserice
        httpSE.call(namespace + modelName, soapserial);
        if (soapserial.getResponse() != null) {
            // 获取服务器响应返回的SOAP消息
            SoapObject result = (SoapObject) soapserial.bodyIn;
            Object property = result.getProperty(0);
            return property.toString();
        } else {
            throw new RuntimeException("Response is Null!!!");
        }
    }

    /**
     * 通过webservice连接获取文件，文件以Base64形式作为结果传输
     *
     * @param serviceUrl 连接地址
     * @param modelName  方法名
     * @param json       JSON参数
     * @param folder     文件夹
     * @return 返回字符串结果
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static File getWsdlConnFile(String serviceUrl, String namespace, String modelName, String json, File folder) throws IOException, ParserConfigurationException, SAXException {
        Map<String, String> params = new HashMap<>();
        params.put("json", json);
        return getWsdlConnFile(serviceUrl, namespace, modelName, params, folder);
    }

    /**
     * 通过webservice连接获取文件，文件以Base64形式作为结果传输
     *
     * @param serviceUrl 连接地址
     * @param modelName  方法名
     * @param params     参数
     * @param folder     文件夹
     * @return 返回字符串结果
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static File getWsdlConnFile(String serviceUrl, String namespace, String modelName, Map<String, String> params, File folder) throws IOException, SAXException, ParserConfigurationException {
        URL url = new URL(serviceUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(CONN_TIMEOUT);
        conn.setRequestMethod("POST");

        // 创建soapObject对象并传入命名空间和方法名
        SoapObject soapObject = new SoapObject(namespace, modelName);
        // 添加参数
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> e : entries) {
                soapObject.addProperty(e.getKey(), e.getValue());
            }
        }
        // 创建SoapSerializationEnvelope对象并传入SOAP协议的版本号
        SoapSerializationEnvelope soapserial = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapserial.bodyOut = soapObject;
        // 设置与.NET提供的Web service保持有良好的兼容性
        soapserial.dotNet = true;

        HttpTransportSE httpSE = new HttpTransportSE(serviceUrl); // 借用来获取byte[]
        byte[] bytes = httpSE.createRequestData(soapserial, "UTF-8");
//        System.out.println(new String(bytes, "UTF-8"));

        conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
        conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        conn.setRequestProperty("SOAPAction", namespace + modelName);
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Connection", "close");
        OutputStream outputStream = new BufferedOutputStream(conn.getOutputStream());
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();

        int code = conn.getResponseCode();
        if (code == 200) {
            InputStream is = null;
            OutputStream os = null;
            try {
                File file = new File(folder, AppUtil.getUUid() + ".tmp~"); // 文件下载名称
                FileUtil.createFile(file); // 创建新文件

                is = new BufferedInputStream(conn.getInputStream());
                os = new FileOutputStream(file, true);

                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                parser.parse(is, new KsoapFileHandler(os));
                os.flush();

                return file;
            } catch (IOException | ParserConfigurationException | SAXException e) {
                throw e;
            } finally {
                try {
                    if (is != null) is.close();
                    if (os != null) os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new RuntimeException("Network conn code is " + code);
        }


    }

}
