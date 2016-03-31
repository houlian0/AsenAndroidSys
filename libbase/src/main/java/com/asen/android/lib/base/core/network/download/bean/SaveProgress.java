package com.asen.android.lib.base.core.network.download.bean;

import java.util.List;

/**
 * Simple to Introduction
 * 下载文件的进度信息
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class SaveProgress {

    /**
     * 当前下载的大小
     */
    private long downloadLength;

    private List<DownProgressItem> mProgressInfoList;

    public SaveProgress(long downloadLength, List<DownProgressItem> progressInfoList) {
        this.downloadLength = downloadLength;
        mProgressInfoList = progressInfoList;
    }

    public long getDownloadLength() {
        return downloadLength;
    }

    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }

    public List<DownProgressItem> getProgressInfoList() {
        return mProgressInfoList;
    }

    public void setProgressInfoList(List<DownProgressItem> progressInfoList) {
        mProgressInfoList = progressInfoList;
    }
}