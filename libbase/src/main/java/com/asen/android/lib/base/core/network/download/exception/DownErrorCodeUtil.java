package com.asen.android.lib.base.core.network.download.exception;

/**
 * ������봦����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownErrorCodeUtil {

    /**
     * ���ݴ�������ȡ������Ϣ
     *
     * @param code �������
     * @return ������Ϣ
     */
    public static String getErrorMessage(int code) {
        return getErrorMessage(code, new DefaultDownDownErrorCode());
    }

    /**
     * ���ݴ�������ȡ������Ϣ
     *
     * @param code         �������
     * @param errorCodeImp �������ת������Ϣʵ��
     * @return ������Ϣ
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
     * �������ת�쳣
     *
     * @param code �������
     * @return �쳣
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
