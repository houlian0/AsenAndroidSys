package com.asen.android.lib.base.core.network.download;

/**
 * �����ļ��ӿڶ���
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public interface IDownloadFileService {

    /**
     * ��ʼ����
     */
    void startDownload();

    /**
     * �������أ��ϵ��¼����ɾ��
     */
    void stopDownload();

    /**
     * ��ͣ���أ��ϵ��¼���ᱻɾ��
     */
    void pauseDownload();

}
