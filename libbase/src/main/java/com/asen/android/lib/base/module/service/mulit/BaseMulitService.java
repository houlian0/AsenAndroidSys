package com.asen.android.lib.base.module.service.mulit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.asen.android.lib.base.BaseApplication;
import com.asen.android.lib.base.core.network.task.SenTimingTask;
import com.asen.android.lib.base.module.service.BaseBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple to Introduction
 * 多定时任务的Service
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public abstract class BaseMulitService extends Service {

    protected Context mContext;

    protected BaseApplication mApplication;

    // 默认首次启动定时任务的时间间隔
    private static final int DEFAULT_START_TIME_INTERVAL_ITEM = 100; // 100, 200, 300 .....

    // 默认的定时任务时间间隔
    private static final int DEFAULT_RUNNABLE_TIME_INTERVEL = 60 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mApplication = (BaseApplication) getApplication();
        mTimingTaskList = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRunnable();
        return START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        startRunnable();
        return new BaseBinder(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRunnable();
        stopForeground(true); // 关掉所有的通知
    }

    private List<SenTimingTask<Void, Object, Void>> mTimingTaskList = null;

    private boolean isStart = false;

    private void startRunnable() { // 开启定时任务
        if (!isStart) {
            int number = timeTaskNumber();
            long[] startTimeIntervals = startTimeIntervals();
            long[] runnableTimeIntervals = initRunnableTimeIntervals();

            for (int i = 0; i < number; i++) {
                final int position = i;
                final long startTimeInterva = (startTimeIntervals == null || startTimeIntervals.length <= position) ? position * DEFAULT_START_TIME_INTERVAL_ITEM : startTimeIntervals[position];
                final long runnableTimeInterval = (runnableTimeIntervals == null || runnableTimeIntervals.length <= position) ? DEFAULT_RUNNABLE_TIME_INTERVEL : runnableTimeIntervals[position];

                SenTimingTask<Void, Object, Void> task = new SenTimingTask<Void, Object, Void>() {
                    @Override
                    protected long startTimeInterval() {
                        return startTimeInterva;
                    }

                    @Override
                    protected long runnableTimeInterval() {
                        return runnableTimeInterval;
                    }

                    @Override
                    protected Void processingData(Void... params) {
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Object... progresses) {
                        super.onProgressUpdate(progresses);
                        BaseMulitService.this.onProgressUpdate(position, progresses);
                    }

                    @Override
                    protected void doInBackground(Void params) throws RuntimeException {
                        doInBackgroud(position);
                    }
                };
                task.start();
                mTimingTaskList.add(task);
            }
            isStart = true;
        }
    }

    private void stopRunnable() { // 结束定时任务
        if (isStart) {
            for (SenTimingTask task : mTimingTaskList) {
                task.stop();
            }
            mTimingTaskList.clear();
            isStart = false;
        }
    }

    protected void publishProgress(int positon, Object... progresses) {
        if (mTimingTaskList.size() <= positon) return;
        SenTimingTask<Void, Object, Void> senTimingTask = mTimingTaskList.get(positon);
        senTimingTask.publishProgress(progresses);
    }

    /**
     * 更新数据到UI线程
     *
     * @param position
     * @param progresses
     */
    protected void onProgressUpdate(int position, Object... progresses) {

    }

    /**
     * 向Service设置数据
     *
     * @param type
     * @param objects
     */
    public abstract void setData(int type, Object... objects);

    /**
     * 获得定时任务个数
     *
     * @return
     */
    protected abstract int timeTaskNumber();

    /**
     * 起始的时间间隔
     *
     * @return
     */
    protected abstract long[] startTimeIntervals();

    /**
     * 中间的时间间隔
     *
     * @return
     */
    protected abstract long[] initRunnableTimeIntervals();

    /**
     * 后台运行
     *
     * @param position
     */
    protected abstract void doInBackgroud(int position);


//	Intent i = new Intent();
//	i.setClass(MainActivity.this, MainActivity.class);
//	//一定要Intent.FLAG_ACTIVITY_NEW_TASK
//	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//	//PendingIntent 是Intent的包装类
//	PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
//	NotificationCompat.Builder ncb = new NotificationCompat.Builder(MainActivity.this);
//	ncb.setTicker("第一个Notifiy");
//	ncb.setAutoCancel(true);
//	ncb.setContentIntent(contentIntent);
//	ncb.setDefaults(Notification.DEFAULT_ALL);
//	ncb.setContentTitle("hello Tby");
//	ncb.setContentText("ContentText");
//	ncb.setSmallIcon(R.drawable.load);
//	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//	notificationManager.notify(1, ncb.build());

}