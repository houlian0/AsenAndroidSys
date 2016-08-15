package com.asen.android.lib.base.core.network.download.bean;

import java.util.List;

/**
 * �����ļ��Ľ�����Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class SaveProgress {

    /**
     * ��ǰ���صĴ�С
     */
    private long downloadLength;

    /**
     * ���������̵߳Ľ�����Ϣ����
     */
    private List<DownProgressItem> mProgressInfoList;

    /**
     * ���캯��
     *
     * @param downloadLength   ��ǰ���صĴ�С
     * @param progressInfoList ���������̵߳Ľ�����Ϣ����
     */
    public SaveProgress(long downloadLength, List<DownProgressItem> progressInfoList) {
        this.downloadLength = downloadLength;
        mProgressInfoList = progressInfoList;
    }

    /**
     * ��ȡ��ǰ���صĴ�С
     *
     * @return ��ǰ���صĴ�С
     */
    public long getDownloadLength() {
        return downloadLength;
    }

    /**
     * ���õ�ǰ���صĴ�С
     *
     * @param downloadLength ��ǰ���صĴ�С
     */
    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }

    /**
     * ��ȡ���������̵߳Ľ�����Ϣ����
     *
     * @return ���������̵߳Ľ�����Ϣ����
     */
    public List<DownProgressItem> getProgressInfoList() {
        return mProgressInfoList;
    }

    /**
     * �������������̵߳Ľ�����Ϣ����
     *
     * @param progressInfoList ���������̵߳Ľ�����Ϣ����
     */
    public void setProgressInfoList(List<DownProgressItem> progressInfoList) {
        mProgressInfoList = progressInfoList;
    }

}