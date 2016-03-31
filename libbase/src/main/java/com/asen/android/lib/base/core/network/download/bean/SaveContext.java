package com.asen.android.lib.base.core.network.download.bean;

/**
 * Simple to Introduction
 * 下载的上下文信息
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class SaveContext {

    /**
     * 下载地址
     */
    private String url;

    /**
     * 文件唯一值
     */
    private String featid;

    /**
     * 上次文件下载的时间
     */
    private long time;

    /**
     * 上次文件下载的大小
     */
    private long fileSize;

    /**
     * 当前下载线程总共分配的字节长度
     */
    private long currentLength;

//    /**
//     * 当前下载已经下载的文件总大小
//     */
//    private long downloadLength;

    /**
     * 是否原文件名输出
     */
    private boolean isOriginal;

    /**
     * 线程数量，默认3个线程数
     */
    private int threadNumber;

    public SaveContext(String url, String featid, long time, long fileSize, long currentLength, boolean isOriginal, int threadNumber) {
        this.url = url;
        this.featid = featid;
        this.time = time;
        this.fileSize = fileSize;
        this.currentLength = currentLength;
//        this.downloadLength = downloadLength;
        this.isOriginal = isOriginal;
        this.threadNumber = threadNumber;
    }

    public boolean isOriginal() {
        return isOriginal;
    }

    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    public int getThreadNumber() {
        return threadNumber;
    }

    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    public long getCurrentLength() {
        return currentLength;
    }

    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

//    public long getDownloadLength() {
//        return downloadLength;
//    }

//    public void setDownloadLength(long downloadLength) {
//        this.downloadLength = downloadLength;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFeatid() {
        return featid;
    }

    public void setFeatid(String featid) {
        this.featid = featid;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
