package com.asen.android.lib.base.core.network.download.bean;

/**
 * 下载的上下文信息
 *
 * @author Asen
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

    /**
     * 是否原文件名输出
     */
    private boolean isOriginal;

    /**
     * 线程数量，默认3个线程数
     */
    private int threadNumber;

    /**
     * 构造函数
     *
     * @param url           下载的URL地址
     * @param featid        唯一编码（每次有新地址下载时，生成一个唯一的编码值）
     * @param time          上次文件下载的时间
     * @param fileSize      上次文件下载的大小
     * @param currentLength 当前下载线程总共分配的字节长度
     * @param isOriginal    是否原文件名输出
     * @param threadNumber  线程数量，默认3个线程数
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
     * 是否原文件名输出
     *
     * @return true，按原文件名输出；false，以uuid命名的文件名输出
     */
    public boolean isOriginal() {
        return isOriginal;
    }

    /**
     * 设置是否按照原文件名输出
     *
     * @param original true，按原文件名输出；false，以uuid命名的文件名输出
     */
    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    /**
     * 获取同时下载单个文件的线程数量
     *
     * @return 同时下载单个文件的线程数量
     */
    public int getThreadNumber() {
        return threadNumber;
    }

    /**
     * 设置同时下载单个文件的线程数量
     *
     * @param threadNumber 同时下载单个文件的线程数量
     */
    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

    /**
     * 获取当前下载线程总共分配的字节长度
     *
     * @return 当前下载线程总共分配的字节长度
     */
    public long getCurrentLength() {
        return currentLength;
    }

    /**
     * 设置当前下载线程总共分配的字节长度
     *
     * @param currentLength 当前下载线程总共分配的字节长度
     */
    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    /**
     * 获取下载地址
     *
     * @return 下载地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置下载地址
     *
     * @param url 下载地址
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取下载的唯一编码
     *
     * @return 唯一编码
     */
    public String getFeatid() {
        return featid;
    }

    /**
     * 设置下载的唯一编码
     *
     * @param featid 唯一编码
     */
    public void setFeatid(String featid) {
        this.featid = featid;
    }

    /**
     * 获取上次文件下载的时间
     *
     * @return 上次文件下载的时间
     */
    public long getTime() {
        return time;
    }

    /**
     * 设置上次文件下载的时间
     *
     * @param time 上次文件下载的时间
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * 获取上次文件下载的大小
     *
     * @return 上次文件下载的大小
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * 设置上次文件下载的大小
     *
     * @param fileSize 上次文件下载的大小
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
