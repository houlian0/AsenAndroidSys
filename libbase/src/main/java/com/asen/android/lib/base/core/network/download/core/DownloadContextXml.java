package com.asen.android.lib.base.core.network.download.core;


import com.asen.android.lib.base.core.network.download.bean.DownProgressItem;
import com.asen.android.lib.base.core.network.download.bean.SaveContext;
import com.asen.android.lib.base.core.network.download.bean.SaveProgress;
import com.asen.android.lib.base.tool.util.ConvertUtil;
import com.asen.android.lib.base.tool.util.DateUtil;
import com.asen.android.lib.base.tool.util.FileUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * �����ļ������� ������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
class DownloadContextXml {

    private File parentFile; // �����浽���ļ���

    private File contextFile; // ���ص�context�ļ���һ������ϵ���Ϣ���ļ���

    private Document contextDocument;

    DownloadContextXml(File folder) {
        parentFile = folder;
        contextFile = new File(parentFile, DownloadFileService.DOWNLOAD_CONTEXT_FILENAME);
    }

    /**
     * ��ȡXML
     *
     * @param file �ļ�
     * @return Document����
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private Document readXml(File file) throws ParserConfigurationException, IOException {
        if (!file.exists()) {
            String tmpName = "." + file.getName();
            file = new File(file.getParent(), tmpName);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // ���һ��DocumentBuilderFactory
        DocumentBuilder builder = factory.newDocumentBuilder(); // ���һ��DocumentBuilder
        Document document = null;
        if (file.exists()) {
            try {
                document = builder.parse(file);
            } catch (SAXException e) {
                FileUtil.delete(file); // xml�ļ�����ɾ����������
            }
        }
        if (document == null) {
            FileUtil.createFile(file); // �����ļ�
            document = builder.newDocument();

        }
        return document;
    }

    /**
     * ����XML
     *
     * @param file     �ļ�
     * @param document Document����
     * @throws TransformerException
     */
    private void saveXml(File file, Document document) throws TransformerException, IOException {
        String tmpName = "." + file.getName();
        File tmpFile = new File(file.getParent(), tmpName);
        FileUtil.createFile(tmpFile);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();//���һ��TransformerFactory����
        Transformer transformer = transformerFactory.newTransformer(); //���һ��Transformer����
        Source xmlSource = new DOMSource(document);   //��document������һ��DOMSource�����װ����
        Result outputTarget = new StreamResult(new FileOutputStream(tmpFile));   //����һ���洢Ŀ�����
        transformer.setOutputProperty("encoding", "UTF-8");//�趨�ĵ����룬����Ҳ����ʹ��OutputKeys�ľ�̬���������趨
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");//�����ʽ��������xml��html��text
        transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // �Ƿ�����
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); // ����������ٸ��ֽ�
        transformer.transform(xmlSource, outputTarget);  //������Ӧ��xml�ļ�

        FileUtil.deleteFile(file);
        FileUtil.rename(tmpFile, file);
    }

    private void initContextDocument() throws IOException, SAXException, ParserConfigurationException {
        if (contextDocument == null)
            contextDocument = readXml(contextFile);
    }

    /**
     * ��ȡ���ص������ģ����Ƴ�
     *
     * @param url ���ص�url��ַ
     * @return ���ص�ǰurl����Ϣ
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    SaveContext getDownloadContext(String url) throws ParserConfigurationException, IOException, SAXException {
        initContextDocument();
        NodeList nodeList = contextDocument.getElementsByTagName("file");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element item = (Element) nodeList.item(i);
            String urlItem = item.getElementsByTagName("url").item(0).getTextContent();
            if (urlItem.equals(url)) { // �������ص�ַ
                long time = DateUtil.getDateByFormat(item.getElementsByTagName("time").item(0).getTextContent(), DateUtil.dateFormatYMDHMS).getTime();
                String featid = item.getElementsByTagName("featid").item(0).getTextContent();
                long fileSize = ConvertUtil.stringToLong(item.getElementsByTagName("fileSize").item(0).getTextContent());
                long currentLength = ConvertUtil.stringToLong(item.getElementsByTagName("currentLength").item(0).getTextContent());
                boolean isOriginal = ConvertUtil.stringToBoolean(item.getElementsByTagName("isOriginal").item(0).getTextContent(), false);
                int threadNumber = ConvertUtil.stringToInt(item.getElementsByTagName("threadNumber").item(0).getTextContent(), 3);
                return new SaveContext(url, featid, time, fileSize, currentLength, isOriginal, threadNumber);
            }
        }
        return null;
    }

    /**
     * �������ص�Context��������
     *
     * @param context ���ص�������
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    void setDownloadContext(SaveContext context) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        initContextDocument();

        NodeList nodeList = contextDocument.getElementsByTagName("files");
        Element element;
        if (nodeList.getLength() == 0) {
            element = contextDocument.createElement("files");
            contextDocument.appendChild(element);
            element.appendChild(createContextElement(context)); // �״ο϶��Ǵ����µ�
        } else {
            element = (Element) nodeList.item(0);
            Element child = getChildElementByUrl(context.getUrl());
            if (child == null) {
                element.appendChild(createContextElement(context)); // �״ο϶��Ǵ����µ�
            } else {
                updateContextElement(child, context);
            }
        }

        saveXml(contextFile, contextDocument);
    }

    private Element getChildElementByUrl(String url) { // �������ص�ַ�ҳ���Ӧ�Ľڵ�
        NodeList nodeList = contextDocument.getElementsByTagName("file");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element item = (Element) nodeList.item(i);
            String urlItem = item.getElementsByTagName("url").item(0).getTextContent();
            if (urlItem.equals(url)) { // �������ص�ַ
                return item;
            }
        }
        return null;
    }

    /**
     * �����ļ����ص�ַ �Ƴ������ļ�������
     *
     * @param url ���ص�ַ
     */
    void removeContextElement(String url) throws TransformerException, IOException {
        Element child = getChildElementByUrl(url);
        if (child != null) {
            contextDocument.getElementsByTagName("files").item(0).removeChild(child);
            saveXml(contextFile, contextDocument);
        }
    }

    private void updateContextElement(Element element, SaveContext context) {
        element.getElementsByTagName("url").item(0).setTextContent(context.getUrl());
        element.getElementsByTagName("time").item(0).setTextContent(DateUtil.getStringByFormat(new Date(context.getTime()), DateUtil.dateFormatYMDHMS));
        element.getElementsByTagName("featid").item(0).setTextContent(context.getFeatid());
        element.getElementsByTagName("fileSize").item(0).setTextContent(context.getFileSize() + "");
        element.getElementsByTagName("currentLength").item(0).setTextContent(context.getCurrentLength() + "");
        element.getElementsByTagName("isOriginal").item(0).setTextContent(context.isOriginal() + "");
        element.getElementsByTagName("threadNumber").item(0).setTextContent(context.getThreadNumber() + "");
    }

    private Element createContextElement(SaveContext context) { // �����µ�Element
        Element child = contextDocument.createElement("file");

        Element url = contextDocument.createElement("url");
        url.setTextContent(context.getUrl());
        child.appendChild(url);

        Element time = contextDocument.createElement("time");
        time.setTextContent(DateUtil.getStringByFormat(new Date(context.getTime()), DateUtil.dateFormatYMDHMS));
        child.appendChild(time);

        Element featid = contextDocument.createElement("featid");
        featid.setTextContent(context.getFeatid());
        child.appendChild(featid);

        Element fileSize = contextDocument.createElement("fileSize");
        fileSize.setTextContent(context.getFileSize() + "");
        child.appendChild(fileSize);

        Element currentLength = contextDocument.createElement("currentLength");
        currentLength.setTextContent(context.getCurrentLength() + "");
        child.appendChild(currentLength);

        Element isOriginal = contextDocument.createElement("isOriginal");
        isOriginal.setTextContent(context.isOriginal() + "");
        child.appendChild(isOriginal);

        Element threadNumber = contextDocument.createElement("threadNumber");
        threadNumber.setTextContent(context.getThreadNumber() + "");
        child.appendChild(threadNumber);

        return child;
    }

    /**
     * ����featidɾ��������Ϣ
     *
     * @param featid ���ر��
     */
    void deleteDownConfig(String featid) {
        File file = new File(parentFile, featid + DownloadFileService.DOWNLOAD_BREAKPOINT_SUFFIX);
        FileUtil.deleteFile(file);
    }

    /**
     * ��������¶ϵ���Ϣ
     *
     * @param featid   �ϵ��ļ����
     * @param itemList �ϵ���Ϣ����
     */
    void createOrUpdateDownConfig(String featid, long downloadLength, List<DownProgressItem> itemList) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        File file = new File(parentFile, featid + DownloadFileService.DOWNLOAD_BREAKPOINT_SUFFIX);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // ���һ��DocumentBuilderFactory
        DocumentBuilder builder = factory.newDocumentBuilder(); // ���һ��DocumentBuilder

        Document document;
        if (file.exists()) {
            document = builder.parse(file);
            NodeList childNodes = document.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) { // �Ƴ����еĽڵ�
                document.removeChild(childNodes.item(i));
            }
        } else {
            FileUtil.createFile(file); // �����ļ�
            document = builder.newDocument();
        }

        Element root = document.createElement("items");
        root.setAttribute("downloadLength", downloadLength + "");

        for (DownProgressItem item : itemList) {
            Element element = document.createElement("item");

            Element startSeek = document.createElement("startSeek");
            startSeek.setTextContent(item.getStartSeek() + "");
            Element endSeek = document.createElement("endSeek");
            endSeek.setTextContent(item.getEndSeek() + "");
            Element downSize = document.createElement("downSize");
            downSize.setTextContent(item.getDownSize() + "");

            element.appendChild(startSeek);
            element.appendChild(endSeek);
            element.appendChild(downSize);

            root.appendChild(element);
        }

        document.appendChild(root);

        saveXml(file, document);
    }

    /**
     * ��ѯ��ǰ�ļ��Ķϵ���Ϣ
     *
     * @param featid �ļ����ر��
     * @return �ϵ���Ϣ����
     */
    SaveProgress queryDownConfig(String featid) throws IOException, SAXException, ParserConfigurationException {
        List<DownProgressItem> result = new ArrayList<>();
        long downLength = 0;

        File file = new File(parentFile, featid + DownloadFileService.DOWNLOAD_BREAKPOINT_SUFFIX);
        if (file.exists()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // ���һ��DocumentBuilderFactory
            DocumentBuilder builder = factory.newDocumentBuilder(); // ���һ��DocumentBuilder
            Document document = builder.parse(file);

            NodeList items = document.getElementsByTagName("items");

            if (items.getLength() > 0) {
                downLength = ConvertUtil.stringToLong(((Element) items.item(0)).getAttribute("downloadLength"));
            }

            NodeList nodeList = document.getElementsByTagName("item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element item = (Element) nodeList.item(i);
                long startSeek = ConvertUtil.stringToLong(item.getElementsByTagName("startSeek").item(0).getTextContent());
                long endSeek = ConvertUtil.stringToLong(item.getElementsByTagName("endSeek").item(0).getTextContent());
                long downSize = ConvertUtil.stringToLong(item.getElementsByTagName("downSize").item(0).getTextContent());
                result.add(new DownProgressItem(startSeek, endSeek, downSize));
            }
        }

        return new SaveProgress(downLength, result);
    }

}