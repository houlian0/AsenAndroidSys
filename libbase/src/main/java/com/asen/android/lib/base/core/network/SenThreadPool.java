package com.asen.android.lib.base.core.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 线程池
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:20
 */
public class SenThreadPool {

    private static final int DEFAULT_POOL_SIZE = 6;

    private ExecutorService pool;

    /**
     * 获得线程池
     *
     * @return 线程池
     */
    public ExecutorService getPool() {
        return pool;
    }

    /**
     * 获得默认size大小的线程池
     */
    public SenThreadPool() {
        pool = Executors.newScheduledThreadPool(DEFAULT_POOL_SIZE);
    }

    /**
     * 获得size大小的线程池
     *
     * @param size 线程池大小
     */
    public SenThreadPool(int size) {
        pool = Executors.newScheduledThreadPool(size);
    }

    public void execute(Runnable command) {
        pool.execute(command);
    }

}
