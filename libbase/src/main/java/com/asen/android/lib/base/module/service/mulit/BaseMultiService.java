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
 * 多定时任务的Service
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public abstract class BaseMultiService extends Service {

    protected Context mContext; // Android的上下文

    protected BaseApplication mApplication;

    // 默认首次启动定时任务的时间间隔
    private static final int DEFAULT_START_TIME_INTERVAL_ITEM = 100; // 100, 200, 300 .....

    // 默认的定时任务时间间隔
    private static final int DEFAULT_RUNNABLE_TIME_INTERVAL = 60 * 1000;

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

    // 开启定时任务
    private void startRunnable() {
        if (!isStart) {
            int number = timeTaskNumber();
            long[] startTimeIntervals = startTimeIntervals();
            long[] runnableTimeIntervals = initRunnableTimeIntervals();

            for (int i = 0; i < number; i++) {
                final int position = i;
                final long startTimeInterval = (startTimeIntervals == null || startTimeIntervals.length <= position) ? position * DEFAULT_START_TIME_INTERVAL_ITEM : startTimeIntervals[position];
                final long runnableTimeInterval = (runnableTimeIntervals == null || runnableTimeIntervals.length <= position) ? DEFAULT_RUNNABLE_TIME_INTERVAL : runnableTimeIntervals[position];

                SenTimingTask<Void, Object, Void> task = new SenTimingTask<Void, Object, Void>() {
                    @Override
                    protected long startTimeInterval() {
                        return startTimeInterval;
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
                        BaseMultiService.this.onProgressUpdate(position, progresses);
                    }

                    @Override
                    protected void doInBackground(Void params) throws RuntimeException {
                        BaseMultiService.this.doInBackground(position);
                    }
                };
                task.start();
                mTimingTaskList.add(task);
            }
            isStart = true;
        }
    }

    // 结束定时任务
    private void stopRunnable() {
        if (isStart) {
            for (SenTimingTask task : mTimingTaskList) {
                task.stop();
            }
            mTimingTaskList.clear();
            isStart = false;
        }
    }

    /**
     * 将数据发送到主线程中处理
     *
     * @param position   定时任务集合的下标（即第 position+1 个定时任务）
     * @param progresses 要发送的数据信息
     */
    protected void publishProgress(int position, Object... progresses) {
        if (mTimingTaskList.size() <= position) return;
        SenTimingTask<Void, Object, Void> senTimingTask = mTimingTaskList.get(position);
        senTimingTask.publishProgress(progresses);
    }

    /**
     * 接收发送到主线程中的数据，并在主线程中处理
     *
     * @param position   定时任务集合的下标（即第 position+1 个定时任务）
     * @param progresses 接收发送过来的数据信息
     */
    protected void onProgressUpdate(int position, Object... progresses) {

    }

    /**
     * 获得定时任务个数
     *
     * @return 定时任务的总个数
     */
    protected abstract int timeTaskNumber();

    /**
     * 起始的时间间隔
     *
     * @return 多任务起始执行的时间间隔（毫秒值）
     */
    protected abstract long[] startTimeIntervals();

    /**
     * 中间的时间间隔
     *
     * @return 多任务中间每次执行的时间间隔（毫秒值）
     */
    protected abstract long[] initRunnableTimeIntervals();

    /**
     * 后台运行
     *
     * @param position 定时任务集合的下标（即第 position+1 个定时任务）
     */
    protected abstract void doInBackground(int position);

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