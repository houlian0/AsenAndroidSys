package com.asen.android.lib.base.core.network.download.exception;

/**
 * Simple to Introduction
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public interface IErrorCode {

    /**
     * 网络连接失败
     */
    int NETWORK_CONN_ERROR = 0x01;

    /**
     * 文件大小为0
     */
    int FILE_LENGTH_ZERO = 0x02;

    /**
     * 文件创建失败
     */
    int FILE_CREATE_ERROR = 0x03;

    /**
     * 下载正在进行中
     */
    int DOWNLOAD_IS_STARTED = 0x04;

    /**
     * 文件配置信息报错
     */
    int FILE_CONFIG_ERROR = 0x05;

    /**
     * 网络连接失败
     */
    String getNetworkConnErrorStr();

    /**
     * 文件大小为零
     */
    String getFileLengthZeroStr();

    /**
     * 文件创建失败
     */
    String getFileCreateErrorStr();

    /**
     * 下载正在进行中
     */
    String getDownloadIsStartedStr();

    /**
     * 文件配置信息报错
     */
    String getFileConfigErrorStr();

    /**
     * 未知错误信息
     */
    String getUnknownErrorStr();

}
