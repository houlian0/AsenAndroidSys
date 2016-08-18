package com.asen.android.lib.base.core.network.download.exception;

/**
 * 错误代码信息实例
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DefaultDownDownErrorCode implements IDownErrorCode {

    @Override
    public String getNetworkConnErrorStr() {
        return "网络连接失败";
    }

    @Override
    public String getFileLengthZeroStr() {
        return "文件大小为零";
    }

    @Override
    public String getFileCreateErrorStr() {
        return "文件创建失败";
    }

    @Override
    public String getDownloadIsStartedStr() {
        return "下载正在进行中";
    }

    @Override
    public String getFileConfigErrorStr() {
        return "文件配置信息报错";
    }

    @Override
    public String getUnknownErrorStr() {
        return "未知的错误信息";
    }

}
