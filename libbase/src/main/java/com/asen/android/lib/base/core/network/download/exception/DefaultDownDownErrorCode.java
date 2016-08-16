package com.asen.android.lib.base.core.network.download.exception;

/**
 * ���������Ϣʵ��
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DefaultDownDownErrorCode implements IDownErrorCode {

    @Override
    public String getNetworkConnErrorStr() {
        return "��������ʧ��";
    }

    @Override
    public String getFileLengthZeroStr() {
        return "�ļ���СΪ��";
    }

    @Override
    public String getFileCreateErrorStr() {
        return "�ļ�����ʧ��";
    }

    @Override
    public String getDownloadIsStartedStr() {
        return "�������ڽ�����";
    }

    @Override
    public String getFileConfigErrorStr() {
        return "�ļ�������Ϣ����";
    }

    @Override
    public String getUnknownErrorStr() {
        return "δ֪�Ĵ�����Ϣ";
    }

}
