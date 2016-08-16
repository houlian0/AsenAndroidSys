package com.asen.android.lib.base.core.network.wsdl;

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
 * ͨ��Ksoap����WSDL�Ĺ�����
 *
 * @author Asen
 * @version v1.0
 * @date 2016-01-27 17:20
 */
public class KsoapConnUtil {

    /**
     * ���ӳ�ʱ��ʱ��
     */
    public static int CONN_TIMEOUT = 10 * 1000;

    /**
     * ͨ��webservice���ӻ�ȡ�������������
     *
     * @param serviceUrl ���ӵ�ַ
     * @param namespace  ���õ�webservice����ռ�
     * @param modelName  ������
     * @param key        ��������
     * @param value      ֵ������
     * @return �����ַ������
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static String getWsdlConnResult(String serviceUrl, String namespace, String modelName, String key, String value) throws IOException, XmlPullParserException {
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        return getWsdlConnResult(serviceUrl, namespace, modelName, params);
    }

    /**
     * ͨ��webservice���ӻ�ȡ���
     *
     * @param serviceUrl ���ӵ�ַ
     * @param modelName  ������
     * @param params     ��������
     * @return �����ַ������
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static String getWsdlConnResult(String serviceUrl, String namespace, String modelName, Map<String, String> params) throws IOException, XmlPullParserException {
        HttpTransportSE httpSE = new HttpTransportSE(serviceUrl);
        httpSE.debug = true;
        // ����soapObject���󲢴��������ռ�ͷ�����
        SoapObject soapObject = new SoapObject(namespace, modelName);
        // ��Ӳ���
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> e : entries) {
                soapObject.addProperty(e.getKey(), e.getValue());
            }
        }

        // ����SoapSerializationEnvelope���󲢴���SOAPЭ��İ汾��
        SoapSerializationEnvelope soapserial = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapserial.bodyOut = soapObject;
        // ������.NET�ṩ��Web service���������õļ�����
        soapserial.dotNet = true;

        // ����HttpTransportSE�����call���������� webserice
        httpSE.call(namespace + modelName, soapserial);
        if (soapserial.getResponse() != null) {
            // ��ȡ��������Ӧ���ص�SOAP��Ϣ
            SoapObject result = (SoapObject) soapserial.bodyIn;
            Object property = result.getProperty(0);
            return property.toString();
        } else {
            throw new RuntimeException("Response is Null!!!");
        }
    }

    /**
     * ͨ��webservice���ӻ�ȡ�ļ����ļ���Base64��ʽ��Ϊ�������
     *
     * @param serviceUrl ���ӵ�ַ
     * @param modelName  ������
     * @param key        ��������
     * @param value      ֵ������
     * @param folder     �����ļ����ļ���
     * @return �������سɹ����ļ�File
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static File getWsdlConnFile(String serviceUrl, String namespace, String modelName, String key, String value, File folder) throws IOException, ParserConfigurationException, SAXException {
        Map<String, String> params = new HashMap<>();
        params.put(key, value);
        return getWsdlConnFile(serviceUrl, namespace, modelName, params, folder);
    }

    /**
     * ͨ��webservice���ӻ�ȡ�ļ����ļ���Base64��ʽ��Ϊ�������
     *
     * @param serviceUrl ���ӵ�ַ
     * @param modelName  ������
     * @param params     ����
     * @param folder     �����ļ����ļ���
     * @return �������سɹ����ļ�File
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

        // ����soapObject���󲢴��������ռ�ͷ�����
        SoapObject soapObject = new SoapObject(namespace, modelName);
        // ��Ӳ���
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> e : entries) {
                soapObject.addProperty(e.getKey(), e.getValue());
            }
        }
        // ����SoapSerializationEnvelope���󲢴���SOAPЭ��İ汾��
        SoapSerializationEnvelope soapserial = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapserial.bodyOut = soapObject;
        // ������.NET�ṩ��Web service���������õļ�����
        soapserial.dotNet = true;

        HttpTransportSE httpSE = new HttpTransportSE(serviceUrl); // ��������ȡbyte[]
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
                File file = new File(folder, AppUtil.getUUid() + ".tmp~"); // �ļ���������
                FileUtil.createFile(file); // �������ļ�

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
