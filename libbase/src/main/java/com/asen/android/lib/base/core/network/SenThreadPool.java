package com.asen.android.lib.base.core.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * �̳߳�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class SenThreadPool {

    private static final int DEFAULT_POOL_SIZE = 6; // Ĭ�ϵ��̳߳ش�С

    private ExecutorService pool;

    /**
     * ����̳߳�
     *
     * @return �̳߳�
     */
    public ExecutorService getPool() {
        return pool;
    }

    /**
     * ���캯��-��ʼ��Ĭ��size��С{@link SenThreadPool#DEFAULT_POOL_SIZE}���̳߳�
     */
    public SenThreadPool() {
        pool = Executors.newScheduledThreadPool(DEFAULT_POOL_SIZE);
    }

    /**
     * ���캯��-��ʼ��size��С���̳߳�
     *
     * @param size �̳߳ش�С
     */
    public SenThreadPool(int size) {
        pool = Executors.newScheduledThreadPool(size);
    }

    /**
     * ִ���߳�
     *
     * @param command �߳�
     */
    public void execute(Runnable command) {
        pool.execute(command);
    }

}
