package com.asen.android.lib.base.core.network.download.bean;

/**
 * Simple to Introduction
 * 下载配置等信息
 *
 * @author ASEN
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

    public DownConfigInfo() {
        isOriginal = true;
        threadNumber = 3;
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

}
