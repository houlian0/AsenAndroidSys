package com.asen.android.lib.base.core.network.download.bean;

/**
 * 下载配置等信息
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownConfigInfo {

    /**
     * 是否原文件名输出
     */
    private boolean isOriginal;

    /**
     * 线程数量，默认3个线程数
     */
    private int threadNumber;

    /**
     * 空构造函数，默认isOriginal = true，threadNumber = 3
     */
    public DownConfigInfo() {
        isOriginal = true;
        threadNumber = 3;
    }

    /**
     * 获取是否原文件名输出的状态
     *
     * @return true：原文件名输出；false：唯一的文件名
     */
    public boolean isOriginal() {
        return isOriginal;
    }

    /**
     * 设置是否原文件名输出的状态
     *
     * @param original true：原文件名输出；false：唯一的文件名
     */
    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    /**
     * 获取同时下载的线程数量
     *
     * @return 同时下载的线程数量
     */
    public int getThreadNumber() {
        return threadNumber;
    }

    /**
     * 设置同时下载的线程数量
     *
     * @param threadNumber 线程数量
     */
    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

}
