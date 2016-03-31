package com.asen.android.lib.base.module.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Simple to Introduction
 * Home键按下后的广播监听
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 15:12
 */
public class HomePressBroadcast implements IBroadcastDefinition {

    private Context mContext;

    private OnHomePressedListener mOnHomePressedListener;

    private HomeRecevier mRecevier;

    public HomePressBroadcast(Context context, OnHomePressedListener onHomePressedListener) {
        mContext = context;
        mOnHomePressedListener = onHomePressedListener;

        mRecevier = new HomeRecevier();
    }

    /**
     * Home键按下时的监听接口
     */
    public interface OnHomePressedListener {

        /**
         * 短按Home键
         */
        public void onHomePressed();

        /**
         * 长按Home键
         */
        public void onHomeLongPressed();
    }

    /**
     * 开始监听广播
     */
    @Override
    public void startWatch() {
        if (mRecevier != null) {
            mContext.registerReceiver(mRecevier, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }
    }

    /**
     * 停止监听，注销广播
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

    /**
     * 广播接收者
     */
    class HomeRecevier extends BroadcastReceiver {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        //        final String SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (mOnHomePressedListener != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {  // 短按home键
                            mOnHomePressedListener.onHomePressed();
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) { // 长按home键
                            mOnHomePressedListener.onHomeLongPressed();
                        }
                    }
                }
            }
        }
    }

}