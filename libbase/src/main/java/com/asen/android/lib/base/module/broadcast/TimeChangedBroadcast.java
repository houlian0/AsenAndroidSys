package com.asen.android.lib.base.module.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by ASEN on 2016/3/31.
 * 时间改变广播监听
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:04
 */
public class TimeChangedBroadcast implements IBroadcastDefinition {

    private Context mContext;

    private OnTimeChangeListener mOnTimeChangeListener;

    private TimeReceiver mRecevier;

    /**
     * 时间改变时的监听
     */
    public interface OnTimeChangeListener {

        /**
         * 时间监听
         */
        void timeChanged();
    }

    /**
     * 开始监听
     */
    @Override
    public void startWatch() {
        if (mRecevier != null) {
            mContext.registerReceiver(mRecevier, new IntentFilter(Intent.ACTION_TIME_TICK));
        }
    }

    /**
     * 结束监听
     */
    @Override
    public void stopWatch() {
        if (mRecevier != null) {
            try {
                mContext.unregisterReceiver(mRecevier);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public TimeChangedBroadcast(Context context, OnTimeChangeListener onTimeChangeListener) {
        mContext = context;
        mOnTimeChangeListener = onTimeChangeListener;
        mRecevier = new TimeReceiver();
    }

    class TimeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_TIME_TICK.equals(action)) {
                if (mOnTimeChangeListener != null)
                    mOnTimeChangeListener.timeChanged();
            }
        }
    }
}
