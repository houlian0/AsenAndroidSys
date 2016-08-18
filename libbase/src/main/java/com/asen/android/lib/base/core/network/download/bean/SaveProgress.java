package com.asen.android.lib.base.core.network.download.bean;

import java.util.List;

/**
 * 下载文件的进度信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class SaveProgress {

    /**
     * 当前下载的大小
     */
    private long downloadLength;

    /**
     * 所有下载线程的进度信息集合
     */
    private List<DownProgressItem> mProgressInfoList;

    /**
     * 构造函数
     *
     * @param downloadLength   当前下载的大小
     * @param progressInfoList 所有下载线程的进度信息集合
     */
    public SaveProgress(long downloadLength, List<DownProgressItem> progressInfoList) {
        this.downloadLength = downloadLength;
        mProgressInfoList = progressInfoList;
    }

    /**
     * 获取当前下载的大小
     *
     * @return 当前下载的大小
     */
    public long getDownloadLength() {
        return downloadLength;
    }

    /**
     * 设置当前下载的大小
     *
     * @param downloadLength 当前下载的大小
     */
    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }

    /**
     * 获取所有下载线程的进度信息集合
     *
     * @return 所有下载线程的进度信息集合
     */
    public List<DownProgressItem> getProgressInfoList() {
        return mProgressInfoList;
    }

    /**
     * 设置所有下载线程的进度信息集合
     *
     * @param progressInfoList 所有下载线程的进度信息集合
     */
    public void setProgressInfoList(List<DownProgressItem> progressInfoList) {
        mProgressInfoList = progressInfoList;
    }

}