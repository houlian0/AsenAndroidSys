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
 * ����ʱ����Service
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public abstract class BaseSingleService extends android.app.Service {

    static final int HANDLER_RESTART_RUNNABLE = 0x9001;

    protected Context mContext; // Android������

    protected BaseApplication mApplication;

    private ExecutorService pool = Executors.newFixedThreadPool(6); // �̳߳�

    private BaseSingleServiceRunnable runnable; // ��ʱ����ִ�е��߳�

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
        isStart = false; // ������ʱ����
        stopForeground(true);
        mHandler.removeMessages(HANDLER_RESTART_RUNNABLE);
    }

    // ��ʼ��ʱ����
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
     * ���̨������̣߳��������ݣ���������Դ��
     *
     * @param objects ����Դ
     */
    public void setData(Object... objects) {
        this.objects = objects;
    }

    /**
     * ��ʱ�������ʼʱ��
     *
     * @return ��ʼʱ�䣨����ֵ��
     */
    protected abstract long startTimeInterval();

    /**
     * ��ʼ������м���ʱ��
     *
     * @return �м���ʱ�䣨����ֵ��
     */
    protected abstract long initRunnableTimeInterval();

    /**
     * ��̨��������
     *
     * @param objects ����Դ
     */
    protected abstract void doInBackground(Object... objects);

    void sendToRunnable() {
        if (isStart)
            mHandler.sendEmptyMessageDelayed(HANDLER_RESTART_RUNNABLE, initRunnableTimeInterval());
    }


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
