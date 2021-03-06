package com.asen.android.lib.base.core.network.wsdl;

import com.asen.android.lib.base.tool.util.Base64Util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 通过 Ksoap 将大文件转成Base64的形式进行传输时，解析 大文件传输的内容 -- Sax解析
 *
 * @author Asen
 * @version v1.0
 * @date 2016-01-28 17:20
 */
public class KsoapFileHandler extends DefaultHandler {

    private OutputStream outputStream; // 文件输出流

    private boolean isStartRecord = false;

    private StringBuilder sb = null;

    public KsoapFileHandler(OutputStream os) {
        outputStream = os;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if ("ns1:data".equals(qName)) {
            isStartRecord = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if ("ns1:data".equals(qName)) {
            isStartRecord = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        if (isStartRecord) { // 开始记录
            try {
                byte[] decode = null;

                String content = new String(ch, start, length).trim(); // 当前获得的字符串
                if (content.length() == 0) return;

                if (sb == null) {
                    if (content.length() % 4 == 0) { // 是4的倍数
                        decode = Base64Util.decode(content, "UTF-8");
                    } else {
                        sb = new StringBuilder(content);
                    }
                } else {
                    sb.append(content);
                    decode = Base64Util.decode(sb.toString(), "UTF-8");
                    sb = null;
                }

                if (decode != null)
                    outputStream.write(decode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
