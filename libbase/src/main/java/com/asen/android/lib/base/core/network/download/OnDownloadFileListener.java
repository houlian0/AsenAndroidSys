package com.asen.android.lib.base.core.network.download;

import java.io.File;

/**
 * 下载监听
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public interface OnDownloadFileListener {

    /**
     * 下载的进度信息
     *
     * @param downloadSize 当前已经下载的大小
     * @param totalSize    文件的总大小
     * @param progress     文件下载的进度（0~100）
     * @param speed        文件下载的速度（byte/s）
     */
    void progress(long downloadSize, long totalSize, float progress, float speed);

    /**
     * 下载成功
     *
     * @param file 下载成功的文件
     */
    void success(File file);

    /**
     * 下载失败
     *
     * @param errorCode 下载失败的错误代码
     * @param e         下载时的异常
     */
    void error(int errorCode, Exception e);

}
