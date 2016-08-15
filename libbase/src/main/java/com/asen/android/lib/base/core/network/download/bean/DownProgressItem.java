package com.asen.android.lib.base.core.network.download.bean;

/**
 * ���ص��̵߳Ľ�����Ϣ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class DownProgressItem {

    /**
     * ��ǰ�̵߳��ļ�������ʼλ��
     */
    private long startSeek;

    /**
     * ��ǰ�̵߳��ļ������յ�λ��
     */
    private long endSeek;

    /**
     * �ý������Ѿ������ֽڴ�С
     */
    private long downSize;

    /**
     * ��ǰ�߳��Ƿ���������
     */
    private boolean isRunning = false;

    /**
     * ���캯��
     *
     * @param startSeek �ļ����ص���ʼλ��
     * @param endSeek   �ļ����ص���ֹλ��
     */
    public DownProgressItem(long startSeek, long endSeek) {
        this.startSeek = startSeek;
        this.endSeek = endSeek;
    }

    /**
     * ���캯��
     *
     * @param startSeek �ļ����ص���ʼλ��
     * @param endSeek   �ļ����ص���ֹλ��
     * @param downSize  ��ǰ�߳��Ѿ����ص��ļ�λ��
     */
    public DownProgressItem(long startSeek, long endSeek, long downSize) {
        this.startSeek = startSeek;
        this.endSeek = endSeek;
        this.downSize = downSize;
    }

    /**
     * ��ȡ�ļ����ص���ʼλ��
     *
     * @return �ļ����ص���ʼλ��
     */
    public long getStartSeek() {
        return startSeek;
    }

    /**
     * ��ȡ�ļ����ص���ֹλ��
     *
     * @return �ļ����ص���ֹλ��
     */
    public long getEndSeek() {
        return endSeek;
    }

    /**
     * ��ȡ��ǰ�߳��Ѿ����ص��ļ�λ��
     *
     * @return ��ǰ�߳��Ѿ����ص��ļ�λ��
     */
    public long getDownSize() {
        return downSize;
    }

    /**
     * ���õ�ǰ�߳��Ѿ����ص��ļ�λ��
     *
     * @param downSize ��ǰ�߳��Ѿ����ص��ļ�λ��
     */
    public void setDownSize(long downSize) {
        this.downSize = downSize;
    }

    /**
     * ��ȡ��ǰ�߳��Ƿ��������е�״̬
     *
     * @return ��ǰ�߳��Ƿ���������
     */
    public synchronized boolean isRunning() {
        return isRunning;
    }

    /**
     * ���õ�ǰ�߳��Ƿ��������е�״̬
     *
     * @param running ��ǰ�߳��Ƿ���������
     */
    public synchronized void setRunning(boolean running) {
        isRunning = running;
    }
}
