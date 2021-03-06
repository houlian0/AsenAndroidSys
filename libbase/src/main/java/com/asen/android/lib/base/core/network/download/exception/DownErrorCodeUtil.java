package com.asen.android.lib.base.core.network.download.exception;

/**
 * 错误代码处理类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownErrorCodeUtil {

    /**
     * 根据错误代码获取错误信息
     *
     * @param code 错误代码
     * @return 错误信息
     */
    public static String getErrorMessage(int code) {
        return getErrorMessage(code, new DefaultDownDownErrorCode());
    }

    /**
     * 根据错误代码获取错误信息
     *
     * @param code         错误代码
     * @param errorCodeImp 错误代码转错误信息实例
     * @return 错误信息
     */
    public static String getErrorMessage(int code, IDownErrorCode errorCodeImp) {
        if (IDownErrorCode.DOWNLOAD_IS_STARTED == code) {
            return errorCodeImp.getDownloadIsStartedStr();
        } else if (IDownErrorCode.FILE_CREATE_ERROR == code) {
            return errorCodeImp.getFileCreateErrorStr();
        } else if (IDownErrorCode.NETWORK_CONN_ERROR == code) {
            return errorCodeImp.getNetworkConnErrorStr();
        } else {
            return errorCodeImp.getUnknownErrorStr();
        }
    }

    /**
     * 错误代码转异常
     *
     * @param code 错误代码
     * @return 异常
     */
    public static DownloadFileException getErrorException(int code) {
        if (IDownErrorCode.DOWNLOAD_IS_STARTED == code) {
            return new DownloadFileException("DownLoad is started or finish!!!");
        } else if (IDownErrorCode.FILE_CREATE_ERROR == code) {
            return new DownloadFileException("SaveFolder create error!!!");
        } else if (IDownErrorCode.NETWORK_CONN_ERROR == code) {
            return new DownloadFileException("IO or network error!!!");
        } else {
            return new DownloadFileException("Unknown error!!!");
        }
    }

}
