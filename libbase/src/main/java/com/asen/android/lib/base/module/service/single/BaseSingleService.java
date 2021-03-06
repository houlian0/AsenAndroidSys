package com.asen.android.lib.base.module.service.single;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.asen.android.lib.base.BaseApplication;
import com.asen.android.lib.base.module.service.BaseBinder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 单定时任务Service
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public abstract class BaseSingleService extends android.app.Service {

    static final int HANDLER_RESTART_RUNNABLE = 0x9001;

    protected Context mContext; // Android上下文

    protected BaseApplication mApplication;

    private ExecutorService pool = Executors.newFixedThreadPool(6); // 线程池

    private BaseSingleServiceRunnable runnable; // 定时任务执行的线程

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mApplication = (BaseApplication) getApplication();
        runnable = new BaseSingleServiceRunnable(this);
    }

    private boolean isStart = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRunnable();
        return START_REDELIVER_INTENT;
    }

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
        isStart = false; // 结束定时任务
        stopForeground(true);
        mHandler.removeMessages(HANDLER_RESTART_RUNNABLE);
    }

    // 开始定时任务
    private void startRunnable() {
        if (!isStart) {
            isStart = true;
            mHandler.sendEmptyMessageDelayed(HANDLER_RESTART_RUNNABLE, startTimeInterval());
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case HANDLER_RESTART_RUNNABLE:
                    pool.execute(runnable);
                    break;
                default:
                    break;
            }
        }
    };

    Object[] objects;

    /**
     * 向后台处理的线程，发送数据（设置数据源）
     *
     * @param objects 数据源
     */
    public void setData(Object... objects) {
        this.objects = objects;
    }

    /**
     * 定时任务的起始时间
     *
     * @return 起始时间（毫秒值）
     */
    protected abstract long startTimeInterval();

    /**
     * 起始任务的中间间隔时间
     *
     * @return 中间间隔时间（毫秒值）
     */
    protected abstract long initRunnableTimeInterval();

    /**
     * 后台处理数据
     *
     * @param objects 数据源
     */
    protected abstract void doInBackground(Object... objects);

    void sendToRunnable() {
        if (isStart)
            mHandler.sendEmptyMessageDelayed(HANDLER_RESTART_RUNNABLE, initRunnableTimeInterval());
    }


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
