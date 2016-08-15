package com.asen.android.lib.base.core.network.download.bean;

/**
 * �������õ���Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownConfigInfo {

    /**
     * �Ƿ�ԭ�ļ������
     */
    private boolean isOriginal;

    /**
     * �߳�������Ĭ��3���߳���
     */
    private int threadNumber;

    /**
     * �չ��캯����Ĭ��isOriginal = true��threadNumber = 3
     */
    public DownConfigInfo() {
        isOriginal = true;
        threadNumber = 3;
    }

    /**
     * ��ȡ�Ƿ�ԭ�ļ��������״̬
     *
     * @return true��ԭ�ļ��������false��Ψһ���ļ���
     */
    public boolean isOriginal() {
        return isOriginal;
    }

    /**
     * �����Ƿ�ԭ�ļ��������״̬
     *
     * @param original true��ԭ�ļ��������false��Ψһ���ļ���
     */
    public void setOriginal(boolean original) {
        isOriginal = original;
    }

    /**
     * ��ȡͬʱ���ص��߳�����
     *
     * @return ͬʱ���ص��߳�����
     */
    public int getThreadNumber() {
        return threadNumber;
    }

    /**
     * ����ͬʱ���ص��߳�����
     *
     * @param threadNumber �߳�����
     */
    public void setThreadNumber(int threadNumber) {
        this.threadNumber = threadNumber;
    }

}
