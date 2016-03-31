package com.asen.android.lib.base.module.service.single;

/**
 * Simple to Introduction
 * 单定时任务Service的线程
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public class BaseSingleServiceRunnable implements Runnable {

    private BaseSingleService mService;

    public BaseSingleServiceRunnable(BaseSingleService service) {
        mService = service;
    }

    @Override
    public void run() {
        mService.sendToRunnable();
        mService.doInBackgroud(mService.objects);
    }

}
