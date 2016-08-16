package com.asen.android.lib.base.core.network.download;

/**
 * 下载文件接口定义
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public interface IDownloadFileService {

    /**
     * 开始下载
     */
    void startDownload();

    /**
     * 结束下载，断点记录将被删除
     */
    void stopDownload();

    /**
     * 暂停下载，断点记录不会被删除
     */
    void pauseDownload();

}
