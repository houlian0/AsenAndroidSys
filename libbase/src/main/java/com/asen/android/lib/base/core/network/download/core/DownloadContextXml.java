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
 * Simple to Introduction
 * 下载文件上下文 管理类
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownloadContextXml {

    private File parentFile;

    private File contextFile;

    private Document contextDocument;

    public DownloadContextXml(File folder) {
        parentFile = folder;
        contextFile = new File(parentFile, DownloadFileService.DOWNLOAD_CONTEXT_FILENAME);
    }

    /**
     * 读取XML
     *
     * @param file 文件
     * @return Document对象
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private Document readXml(File file) throws ParserConfigurationException, IOException {
        if (!file.exists()) {
            String tmpName = "." + file.getName();
            file = new File(file.getParent(), tmpName);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 获得一个DocumentBuilderFactory
        DocumentBuilder builder = factory.newDocumentBuilder(); // 获得一个DocumentBuilder
        Document document = null;
        if (file.exists()) {
            try {
                document = builder.parse(file);
            } catch (SAXException e) {
                FileUtil.delete(file); // xml文件有误，删除重新生成
            }
        }
        if (document == null) {
            FileUtil.createFile(file); // 创建文件
            document = builder.newDocument();

        }
        return document;
    }

    /**
     * 保存XML
     *
     * @param file     文件
     * @param document Document对象
     * @throws TransformerException
     */
    private void saveXml(File file, Document document) throws TransformerException {
        String tmpName = "." + file.getName();
        File tmpFile = new File(file.getParent(), tmpName);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();//获得一个TransformerFactory对象
        Transformer transformer = transformerFactory.newTransformer(); //获得一个Transformer对象
        Source xmlSource = new DOMSource(document);   //把document对象用一个DOMSource对象包装起来
        Result outputTarget = new StreamResult(tmpFile);   //建立一个存储目标对象
        transformer.setOutputProperty("encoding", "UTF-8");//设定文档编码，属性也可以使用OutputKeys的静态常量属性设定
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");//输出方式，可以是xml、html和text
        transformer.setOutputProperty(OutputKeys.CDATA_SECTION_ELEMENTS, "yes");
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 是否缩进
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); // 缩进处理多少个字节
        transformer.transform(xmlSource, outputTarget);  //生成相应的xml文件

        FileUtil.deleteFile(file);
        FileUtil.rename(tmpFile, file);
    }

    private void initContextDocument() throws IOException, SAXException, ParserConfigurationException {
        if (contextDocument == null)
            contextDocument = readXml(contextFile);
    }

    /**
     * 获取下载的上下文，并移除
     *
     * @param url 下载的url地址
     * @return 下载当前url的信息
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public SaveContext getDownloadContext(String url) throws ParserConfigurationException, IOException, SAXException {
        initContextDocument();
        NodeList nodeList = contextDocument.getElementsByTagName("file");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element item = (Element) nodeList.item(i);
            String urlItem = item.getElementsByTagName("url").item(0).getTextContent();
            if (urlItem.equals(url)) { // 存在下载地址
                long time = DateUtil.getDateByFormat(item.getElementsByTagName("time").item(0).getTextContent(), DateUtil.dateFormatYMDHMS).getTime();
                String featid = item.getElementsByTagName("featid").item(0).getTextContent();
                long fileSize = ConvertUtil.stringToLong(item.getElementsByTagName("fileSize").item(0).getTextContent());
                long currentLength = ConvertUtil.stringToLong(item.getElementsByTagName("currentLength").item(0).getTextContent());
//                long downloadLength = ConvertUtil.stringToLong(item.getElementsByTagName("downloadLength").item(0).getTextContent());
                boolean isOriginal = ConvertUtil.stringToBoolean(item.getElementsByTagName("isOriginal").item(0).getTextContent(), false);
                int threadNumber = ConvertUtil.stringToInt(item.getElementsByTagName("threadNumber").item(0).getTextContent(), 3);
                return new SaveContext(url, featid, time, fileSize, currentLength, isOriginal, threadNumber);
            }
        }
        return null;
    }

    /**
     * 增加下载的Context，并保存
     *
     * @param context 下载的上下文
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    public void setDownloadContext(SaveContext context) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        initContextDocument();

        NodeList nodeList = contextDocument.getElementsByTagName("files");
        Element element;
        if (nodeList.getLength() == 0) {
            element = contextDocument.createElement("files");
            contextDocument.appendChild(element);
            element.appendChild(createContextElement(context)); // 首次肯定是创建新的
        } else {
            element = (Element) nodeList.item(0);
            Element child = getChildElementByUrl(context.getUrl());
            if (child == null) {
                element.appendChild(createContextElement(context)); // 首次肯定是创建新的
            } else {
                updateContextElement(child, context);
            }
        }

        saveXml(contextFile, contextDocument);
    }

    private Element getChildElementByUrl(String url) { // 根据下载地址找出对应的节点
        NodeList nodeList = contextDocument.getElementsByTagName("file");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element item = (Element) nodeList.item(i);
            String urlItem = item.getElementsByTagName("url").item(0).getTextContent();
            if (urlItem.equals(url)) { // 存在下载地址
                return item;
            }
        }
        return null;
    }

    /**
     * 根据文件下载地址 移除下载文件上下文
     *
     * @param url 下载地址
     */
    public void removeContextElement(String url) throws TransformerException {
        Element child = getChildElementByUrl(url);
        if (child != null) {
            contextDocument.getElementsByTagName("files").item(0).removeChild(child);
            saveXml(contextFile, contextDocument);
        }
    }

    private void updateContextElement(Element element, SaveContext context) {
//        element.setAttribute("url", context.getUrl());
        element.getElementsByTagName("url").item(0).setTextContent(context.getUrl());
        element.getElementsByTagName("time").item(0).setTextContent(DateUtil.getStringByFormat(new Date(context.getTime()), DateUtil.dateFormatYMDHMS));
        element.getElementsByTagName("featid").item(0).setTextContent(context.getFeatid());
        element.getElementsByTagName("fileSize").item(0).setTextContent(context.getFileSize() + "");
        element.getElementsByTagName("currentLength").item(0).setTextContent(context.getCurrentLength() + "");
//        element.getElementsByTagName("downloadLength").item(0).setTextContent(context.getDownloadLength() + "");
        element.getElementsByTagName("isOriginal").item(0).setTextContent(context.isOriginal() + "");
        element.getElementsByTagName("threadNumber").item(0).setTextContent(context.getThreadNumber() + "");
    }

    private Element createContextElement(SaveContext context) { // 创建新的Element
        Element child = contextDocument.createElement("file");
//        child.setAttribute("url", context.getUrl());
//        child.setIdAttribute("url", true);

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

//        Element downloadLength = contextDocument.createElement("downloadLength");
//        downloadLength.setTextContent(context.getDownloadLength() + "");
//        child.appendChild(downloadLength);

        Element isOriginal = contextDocument.createElement("isOriginal");
        isOriginal.setTextContent(context.isOriginal() + "");
        child.appendChild(isOriginal);

        Element threadNumber = contextDocument.createElement("threadNumber");
        threadNumber.setTextContent(context.getThreadNumber() + "");
        child.appendChild(threadNumber);

        return child;
    }

    /**
     * 根据featid删除配置信息
     *
     * @param featid 下载编号
     */
    public void deleteDownConfig(String featid) {
        File file = new File(parentFile, featid + DownloadFileService.DOWNLOAD_BREAKPOINT_SUFFIX);
        FileUtil.deleteFile(file);
    }

    /**
     * 保存与更新断点信息
     *
     * @param featid   断点文件编号
     * @param itemList 断点信息集合
     */
    public void createOrUpdateDownConfig(String featid, long downloadLength, List<DownProgressItem> itemList) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        File file = new File(parentFile, featid + DownloadFileService.DOWNLOAD_BREAKPOINT_SUFFIX);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 获得一个DocumentBuilderFactory
        DocumentBuilder builder = factory.newDocumentBuilder(); // 获得一个DocumentBuilder

        Document document;
        if (file.exists()) {
            document = builder.parse(file);
            NodeList childNodes = document.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) { // 移除所有的节点
                document.removeChild(childNodes.item(i));
            }
        } else {
            FileUtil.createFile(file); // 创建文件
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
     * 查询当前文件的断点信息
     *
     * @param featid 文件下载编号
     * @return 断点信息集合
     */
    public SaveProgress queryDownConfig(String featid) throws IOException, SAXException, ParserConfigurationException {
        List<DownProgressItem> result = new ArrayList<>();
        long downLength = 0;

        File file = new File(parentFile, featid + DownloadFileService.DOWNLOAD_BREAKPOINT_SUFFIX);
        if (file.exists()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // 获得一个DocumentBuilderFactory
            DocumentBuilder builder = factory.newDocumentBuilder(); // 获得一个DocumentBuilder
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