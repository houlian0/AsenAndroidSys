package com.asen.android.lib.base.module.service.single;

/**
 * 单定时任务Service的线程
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public class BaseSingleServiceRunnable implements Runnable {

    private BaseSingleService mService; // 单定时任务Service

    public BaseSingleServiceRunnable(BaseSingleService service) {
        mService = service;
    }

    @Override
    public void run() {
        mService.sendToRunnable();
        mService.doInBackground(mService.objects);
    }

}
