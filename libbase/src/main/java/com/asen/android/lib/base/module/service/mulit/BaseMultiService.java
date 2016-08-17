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
 * �ඨʱ�����Service
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public abstract class BaseMultiService extends Service {

    protected Context mContext; // Android��������

    protected BaseApplication mApplication;

    // Ĭ���״�������ʱ�����ʱ����
    private static final int DEFAULT_START_TIME_INTERVAL_ITEM = 100; // 100, 200, 300 .....

    // Ĭ�ϵĶ�ʱ����ʱ����
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
        stopForeground(true); // �ص����е�֪ͨ
    }

    private List<SenTimingTask<Void, Object, Void>> mTimingTaskList = null;

    private boolean isStart = false;

    // ������ʱ����
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

    // ������ʱ����
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
     * �����ݷ��͵����߳��д���
     *
     * @param position   ��ʱ���񼯺ϵ��±꣨���� position+1 ����ʱ����
     * @param progresses Ҫ���͵�������Ϣ
     */
    protected void publishProgress(int position, Object... progresses) {
        if (mTimingTaskList.size() <= position) return;
        SenTimingTask<Void, Object, Void> senTimingTask = mTimingTaskList.get(position);
        senTimingTask.publishProgress(progresses);
    }

    /**
     * ���շ��͵����߳��е����ݣ��������߳��д���
     *
     * @param position   ��ʱ���񼯺ϵ��±꣨���� position+1 ����ʱ����
     * @param progresses ���շ��͹�����������Ϣ
     */
    protected void onProgressUpdate(int position, Object... progresses) {

    }

    /**
     * ��ö�ʱ�������
     *
     * @return ��ʱ������ܸ���
     */
    protected abstract int timeTaskNumber();

    /**
     * ��ʼ��ʱ����
     *
     * @return ��������ʼִ�е�ʱ����������ֵ��
     */
    protected abstract long[] startTimeIntervals();

    /**
     * �м��ʱ����
     *
     * @return �������м�ÿ��ִ�е�ʱ����������ֵ��
     */
    protected abstract long[] initRunnableTimeIntervals();

    /**
     * ��̨����
     *
     * @param position ��ʱ���񼯺ϵ��±꣨���� position+1 ����ʱ����
     */
    protected abstract void doInBackground(int position);

//	Intent i = new Intent();
//	i.setClass(MainActivity.this, MainActivity.class);
//	//һ��ҪIntent.FLAG_ACTIVITY_NEW_TASK
//	i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//	//PendingIntent ��Intent�İ�װ��
//	PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
//	NotificationCompat.Builder ncb = new NotificationCompat.Builder(MainActivity.this);
//	ncb.setTicker("��һ��Notifiy");
//	ncb.setAutoCancel(true);
//	ncb.setContentIntent(contentIntent);
//	ncb.setDefaults(Notification.DEFAULT_ALL);
//	ncb.setContentTitle("hello Tby");
//	ncb.setContentText("ContentText");
//	ncb.setSmallIcon(R.drawable.load);
//	NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//	notificationManager.notify(1, ncb.build());

}