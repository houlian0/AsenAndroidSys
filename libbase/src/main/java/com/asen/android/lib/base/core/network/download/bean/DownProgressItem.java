package com.asen.android.lib.base.core.network.download.bean;

/**
 * Simple to Introduction
 * 下载单线程的进度信息
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownProgressItem {

    /**
     * 当前线程的文件下载起始位置
     */
    private long startSeek;

    /**
     * 当前线程的文件下载终点位置
     */
    private long endSeek;

    /**
     * 该进度中已经下载字节大小
     */
    private long downSize;

    private boolean isRunning = false;

    public DownProgressItem(long startSeek, long endSeek) {
        this.startSeek = startSeek;
        this.endSeek = endSeek;
    }

    public DownProgressItem(long startSeek, long endSeek, long downSize) {
        this.startSeek = startSeek;
        this.endSeek = endSeek;
        this.downSize = downSize;
    }

    public long getStartSeek() {
        return startSeek;
    }

    public long getEndSeek() {
        return endSeek;
    }

    public long getDownSize() {
        return downSize;
    }

    public void setDownSize(long downSize) {
        this.downSize = downSize;
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized void setRunning(boolean running) {
        isRunning = running;
    }
}
