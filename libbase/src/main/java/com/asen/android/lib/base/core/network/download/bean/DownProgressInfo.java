package com.asen.android.lib.base.core.network.download.bean;

import com.asen.android.lib.base.core.network.urlconn.bean.DownFileInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * ���ع��̵���Ϣ�����ȵ���Ϣ��
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownProgressInfo {

    /**
     * ���ص�URL��ַ
     */
    private URL url;

    /**
     * �ļ���Ϣ
     */
    private DownFileInfo downFileInfo;

    /**
     * ��ʱ�ļ�
     */
    private File tmpFile;

    /**
     * ��ǰ�����߳��ܹ�������ֽڳ���
     */
    private long currentLength;

    /**
     * ��ǰ�����Ѿ����ص��ļ��ܴ�С
     */
    private long downloadLength;

    /**
     * ���صĸ����̶߳ε���Ϣ
     */
    private List<DownProgressItem> progressItems;

    /**
     * �ļ������
     */
    private RandomAccessFile fileOutputStream;

    /**
     * ��ȡ�ļ������
     *
     * @return �ļ������
     */
    public RandomAccessFile getFileOutputStream() {
        return fileOutputStream;
    }

    /**
     * ���캯��
     *
     * @param url      ���ص�URL��ַ
     * @param downFileInfo ���ص��ļ���Ϣ
     * @param tmpFile  ��ʱ�ļ�File������ʱ�������س���ʱ�ļ���������֮����������������
     * @throws IOException
     */
    public DownProgressInfo(URL url, DownFileInfo downFileInfo, File tmpFile) throws IOException {
        this.url = url;
        this.downFileInfo = downFileInfo;
        this.tmpFile = tmpFile;
        progressItems = new ArrayList<>();

        if (!tmpFile.exists()) tmpFile.createNewFile();
        fileOutputStream = new RandomAccessFile(tmpFile, "rws");
    }

    /**
     * ��ȡ���ص�URL��ַ
     *
     * @return URL��ַ
     */
    public URL getUrl() {
        return url;
    }

    /**
     * ��ȡ�ļ�����Ϣ
     *
     * @return �ļ�����Ϣ
     */
    public DownFileInfo getDownFileInfo() {
        return downFileInfo;
    }

    /**
     * ��ʱ�ļ�
     *
     * @return ��ʱ�ļ�
     */
    public File getTmpFile() {
        return tmpFile;
    }

    /**
     * ��ȡ��ǰ�����߳�ִ����ʱ���ļ��Ĵ�С
     *
     * @return �ļ��Ĵ�С
     */
    public long getCurrentLength() {
        return currentLength;
    }

    /**
     * ��ȡ��ǰ��������ɵ��ļ���С
     *
     * @return �ļ���С
     */
    public long getDownloadLength() {
        return downloadLength;
    }

    /**
     * ���õ�ǰ�����߳�ִ����ʱ���ļ��Ĵ�С
     *
     * @param currentLength �ļ��Ĵ�С
     */
    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    /**
     * ���õ�ǰ��������ɵ��ļ���С
     *
     * @param downloadLength �ļ���С
     */
    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }

    /**
     * ����ļ�������Ϣ
     *
     * @param item �ļ�������Ϣ
     */
    public void addDownProgressItem(DownProgressItem item) {
        this.progressItems.add(item);
    }

    /**
     * �жϽ�����Ϣ�б��Ƿ�Ϊ��
     *
     * @return �յĻ�������true�����򷵻�false
     */
    public boolean isProgressItemsEmpty() {
        return progressItems.isEmpty();
    }

    /**
     * ��ȡ������Ϣ�б�
     *
     * @return ������Ϣ�б�
     */
    public List<DownProgressItem> getProgressItems() {
        return progressItems;
    }

}
