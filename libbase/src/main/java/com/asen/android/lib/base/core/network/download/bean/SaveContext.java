package com.asen.android.lib.base.core.network.download.bean;

/**
 * ���ص���������Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class SaveContext {

    /**
     * ���ص�ַ
     */
    private String url;

    /**
     * �ļ�Ψһֵ
     */
    private String featid;

    /**
     * �ϴ��ļ����ص�ʱ��
     */
    private long time;

    /**
     * �ϴ��ļ����صĴ�С
     */
    private long fileSize;

    /**
     * ��ǰ�����߳��ܹ�������ֽڳ���
     */
    private long currentLength;

    /**
     * �Ƿ�ԭ�ļ������
     */
    private boolean isOriginal;

    /**
     * �߳�������Ĭ��3���߳���
     */
    private int threadNumber;

    /**
     * ���캯��
     *
     * @param url           ���ص�URL��ַ
     * @param featid        Ψһ���루ÿ�����µ�ַ����ʱ������һ��Ψһ�ı���ֵ��
     * @param time          �ϴ��ļ����ص�ʱ��
     * @param fileSize      �ϴ��ļ����صĴ�С
     * @param currentLength ��ǰ�����߳��ܹ�������ֽڳ���
     * @param isOriginal    �Ƿ�ԭ�ļ������
     * @param threadNumber  �߳�������Ĭ��3���߳���
     */
    public SaveContext(String url, String featid, long time, long fileSize, long currentLength, boolean isOriginal, int threadNumber) {
        this.url = url;
        this.featid = featid;
        this.time = time;
        this.fileSize = fileSize;
        this.currentLength = currentLength;
        this.isOriginal = isOriginal;
        this.threadNumber = threadNumber;
    }

    /**
     * �Ƿ�ԭ�ļ������
     *
     * @return true����ԭ�ļ��������false����uuid�������ļ������
     */
    public boolean isOriginal() {
        return isOriginal;
    }

    /**
     * �����Ƿ���ԭ�ļ������
     *
     * @param original true����ԭ�ļ��������false����uuid�������ļ������
     */
    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    /**
     * ��ȡͬʱ���ص����ļ����߳�����
     *
     * @return ͬʱ���ص����ļ����߳�����
     */
    public int getThreadNumber() {
        return threadNumber;
    }

    /**
     * ����ͬʱ���ص����ļ����߳�����
     *
     * @param threadNumber ͬʱ���ص����ļ����߳�����
     */
    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    /**
     * ��ȡ��ǰ�����߳��ܹ�������ֽڳ���
     *
     * @return ��ǰ�����߳��ܹ�������ֽڳ���
     */
    public long getCurrentLength() {
        return currentLength;
    }

    /**
     * ���õ�ǰ�����߳��ܹ�������ֽڳ���
     *
     * @param currentLength ��ǰ�����߳��ܹ�������ֽڳ���
     */
    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    /**
     * ��ȡ���ص�ַ
     *
     * @return ���ص�ַ
     */
    public String getUrl() {
        return url;
    }

    /**
     * �������ص�ַ
     *
     * @param url ���ص�ַ
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * ��ȡ���ص�Ψһ����
     *
     * @return Ψһ����
     */
    public String getFeatid() {
        return featid;
    }

    /**
     * �������ص�Ψһ����
     *
     * @param featid Ψһ����
     */
    public void setFeatid(String featid) {
        this.featid = featid;
    }

    /**
     * ��ȡ�ϴ��ļ����ص�ʱ��
     *
     * @return �ϴ��ļ����ص�ʱ��
     */
    public long getTime() {
        return time;
    }

    /**
     * �����ϴ��ļ����ص�ʱ��
     *
     * @param time �ϴ��ļ����ص�ʱ��
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * ��ȡ�ϴ��ļ����صĴ�С
     *
     * @return �ϴ��ļ����صĴ�С
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * �����ϴ��ļ����صĴ�С
     *
     * @param fileSize �ϴ��ļ����صĴ�С
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
