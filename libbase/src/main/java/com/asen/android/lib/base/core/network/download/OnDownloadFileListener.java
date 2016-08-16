package com.asen.android.lib.base.core.network.download;

import java.io.File;

/**
 * ���ؼ���
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public interface OnDownloadFileListener {

    /**
     * ���صĽ�����Ϣ
     *
     * @param downloadSize ��ǰ�Ѿ����صĴ�С
     * @param totalSize    �ļ����ܴ�С
     * @param progress     �ļ����صĽ��ȣ�0~100��
     * @param speed        �ļ����ص��ٶȣ�byte/s��
     */
    void progress(long downloadSize, long totalSize, float progress, float speed);

    /**
     * ���سɹ�
     *
     * @param file ���سɹ����ļ�
     */
    void success(File file);

    /**
     * ����ʧ��
     *
     * @param errorCode ����ʧ�ܵĴ������
     * @param e         ����ʱ���쳣
     */
    void error(int errorCode, Exception e);

}
