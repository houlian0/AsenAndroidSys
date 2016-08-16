package com.asen.android.lib.base.module.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Home�����º�Ĺ㲥����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 15:12
 */
public class HomePressBroadcast implements IBroadcastDefinition {

    private Context mContext; // Android������

    private OnHomePressedListener mOnHomePressedListener; // Home�����º�ļ���

    private HomeRecevier mRecevier; // �㲥

    /**
     * ���캯��
     *
     * @param context               Android������
     * @param onHomePressedListener Home�����º�ļ���
     */
    public HomePressBroadcast(Context context, OnHomePressedListener onHomePressedListener) {
        mContext = context;
        mOnHomePressedListener = onHomePressedListener;

        mRecevier = new HomeRecevier();
    }

    /**
     * Home������ʱ�ļ����ӿ�
     */
    public interface OnHomePressedListener {

        /**
         * �̰�Home��
         */
        public void onHomePressed();

        /**
         * ����Home��
         */
        public void onHomeLongPressed();
    }

    /**
     * ��ʼ�����㲥
     */
    @Override
    public void startWatch() {
        if (mRecevier != null) {
            mContext.registerReceiver(mRecevier, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        }
    }

    /**
     * ֹͣ������ע���㲥
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
     * �㲥������
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
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {  // �̰�home��
                            mOnHomePressedListener.onHomePressed();
                        } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) { // ����home��
                            mOnHomePressedListener.onHomeLongPressed();
                        }
                    }
                }
            }
        }
    }

}