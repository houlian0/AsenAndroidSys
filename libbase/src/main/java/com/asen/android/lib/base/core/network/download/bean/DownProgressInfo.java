package com.asen.android.lib.base.core.network.download.bean;

import com.asen.android.lib.base.core.network.urlconn.bean.FileInfo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple to Introduction
 * 下载过程的信息（进度等信息）
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownProgressInfo {

    /**
     * 下载的URL地址
     */
    private URL url;

    /**
     * 文件信息
     */
    private FileInfo fileInfo;

    /**
     * 临时文件
     */
    private File tmpFile;

    /**
     * 当前下载线程总共分配的字节长度
     */
    private long currentLength;

    /**
     * 当前下载已经下载的文件总大小
     */
    private long downloadLength;

    /**
     * 下载的各个线程段的信息
     */
    private List<DownProgressItem> progressItems;

    private RandomAccessFile fileOutputStream;

    public RandomAccessFile getFileOutputStream() {
        return fileOutputStream;
    }

    public DownProgressInfo(URL url, FileInfo fileInfo, File tmpFile) throws IOException {
        this.url = url;
        this.fileInfo = fileInfo;
        this.tmpFile = tmpFile;
        progressItems = new ArrayList<>();

        if (!tmpFile.exists()) tmpFile.createNewFile();
        fileOutputStream = new RandomAccessFile(tmpFile, "rws");
    }

    /**
     * 获取下载的URL地址
     *
     * @return URL地址
     */
    public URL getUrl() {
        return url;
    }

    /**
     * 获取文件的信息
     *
     * @return 文件的信息
     */
    public FileInfo getFileInfo() {
        return fileInfo;
    }

    /**
     * 临时文件
     *
     * @return 临时文件
     */
    public File getTmpFile() {
        return tmpFile;
    }

    /**
     * 获取当前所有线程执行完时，文件的大小
     *
     * @return 文件的大小
     */
    public long getCurrentLength() {
        return currentLength;
    }

    /**
     * 获取当前已下载完成的文件大小
     *
     * @return 文件大小
     */
    public long getDownloadLength() {
        return downloadLength;
    }

    /**
     * 设置当前所有线程执行完时，文件的大小
     *
     * @param currentLength 文件的大小
     */
    public void setCurrentLength(long currentLength) {
        this.currentLength = currentLength;
    }

    /**
     * 设置当前已下载完成的文件大小
     *
     * @param downloadLength 文件大小
     */
    public void setDownloadLength(long downloadLength) {
        this.downloadLength = downloadLength;
    }

    /**
     * 添加文件进度信息
     *
     * @param item 文件进度信息
     */
    public void addDownProgressItem(DownProgressItem item) {
        this.progressItems.add(item);
    }

    /**
     * 判断进度信息列表是否为空
     *
     * @return 空的话，返回true，否则返回false
     */
    public boolean isProgressItemsEmpty() {
        return progressItems.isEmpty();
    }

    /**
     * 获取进度信息列表
     *
     * @return 进度信息列表
     */
    public List<DownProgressItem> getProgressItems() {
        return progressItems;
    }

}
