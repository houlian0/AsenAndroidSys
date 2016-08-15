package com.asen.android.lib.base.core.network.download.bean;

/**
 * 下载单线程的进度信息
 *
 * @author Asen
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

    /**
     * 当前线程是否正在运行
     */
    private boolean isRunning = false;

    /**
     * 构造函数
     *
     * @param startSeek 文件下载的起始位置
     * @param endSeek   文件下载的终止位置
     */
    public DownProgressItem(long startSeek, long endSeek) {
        this.startSeek = startSeek;
        this.endSeek = endSeek;
    }

    /**
     * 构造函数
     *
     * @param startSeek 文件下载的起始位置
     * @param endSeek   文件下载的终止位置
     * @param downSize  当前线程已经下载的文件位置
     */
    public DownProgressItem(long startSeek, long endSeek, long downSize) {
        this.startSeek = startSeek;
        this.endSeek = endSeek;
        this.downSize = downSize;
    }

    /**
     * 获取文件下载的起始位置
     *
     * @return 文件下载的起始位置
     */
    public long getStartSeek() {
        return startSeek;
    }

    /**
     * 获取文件下载的终止位置
     *
     * @return 文件下载的终止位置
     */
    public long getEndSeek() {
        return endSeek;
    }

    /**
     * 获取当前线程已经下载的文件位置
     *
     * @return 当前线程已经下载的文件位置
     */
    public long getDownSize() {
        return downSize;
    }

    /**
     * 设置当前线程已经下载的文件位置
     *
     * @param downSize 当前线程已经下载的文件位置
     */
    public void setDownSize(long downSize) {
        this.downSize = downSize;
    }

    /**
     * 获取当前线程是否正在运行的状态
     *
     * @return 当前线程是否正在运行
     */
    public synchronized boolean isRunning() {
        return isRunning;
    }

    /**
     * 设置当前线程是否正在运行的状态
     *
     * @param running 当前线程是否正在运行
     */
    public synchronized void setRunning(boolean running) {
        isRunning = running;
    }
}
