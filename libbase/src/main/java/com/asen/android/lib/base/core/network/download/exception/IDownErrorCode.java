package com.asen.android.lib.base.core.network.download.exception;

/**
 * ���صĴ������ͽӿڶ���
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public interface IDownErrorCode {

    /**
     * ��������ʧ��
     */
    int NETWORK_CONN_ERROR = 0x01;

    /**
     * �ļ���СΪ0
     */
    int FILE_LENGTH_ZERO = 0x02;

    /**
     * �ļ�����ʧ��
     */
    int FILE_CREATE_ERROR = 0x03;

    /**
     * �������ڽ�����
     */
    int DOWNLOAD_IS_STARTED = 0x04;

    /**
     * �ļ�������Ϣ����
     */
    int FILE_CONFIG_ERROR = 0x05;

    /**
     * ��������ʧ��
     */
    String getNetworkConnErrorStr();

    /**
     * �ļ���СΪ��
     */
    String getFileLengthZeroStr();

    /**
     * �ļ�����ʧ��
     */
    String getFileCreateErrorStr();

    /**
     * �������ڽ�����
     */
    String getDownloadIsStartedStr();

    /**
     * �ļ�������Ϣ����
     */
    String getFileConfigErrorStr();

    /**
     * δ֪������Ϣ
     */
    String getUnknownErrorStr();

}
