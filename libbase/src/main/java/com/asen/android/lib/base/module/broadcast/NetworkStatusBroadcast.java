package com.asen.android.lib.base.module.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * ����״̬�����㲥
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 15:12
 */
public class NetworkStatusBroadcast implements IBroadcastDefinition {

    private Context mContext; // Android������

    private OnNetworkStatusListener mOnNetworkStatusListener; // ����״̬�ļ����ӿ�

    private NetworkRecevier mRecevier; // �㲥

    /**
     * ���캯��
     *
     * @param context                 Android������
     * @param onNetworkStatusListener ����״̬�ļ����ӿ�
     */
    public NetworkStatusBroadcast(Context context, OnNetworkStatusListener onNetworkStatusListener) {
        mContext = context;
        mOnNetworkStatusListener = onNetworkStatusListener;
        mRecevier = new NetworkRecevier();
    }

    /**
     * ����״̬�����ļ����ӿ�
     */
    public interface OnNetworkStatusListener {

        /**
         * ���ӵ�wifi����
         */
        public void connToWifi();

        /**
         * ���ӵ��ƶ�����
         */
        public void connToMobile();

        /**
         * δ��������
         */
        public void unConnNetwork();
    }

    /**
     * ��ʼ�����㲥
     */
    @Override
    public void startWatch() {
        if (mRecevier != null) {
            mContext.registerReceiver(mRecevier, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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
    class NetworkRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null) { // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    // connected to wifi
                    if (mOnNetworkStatusListener != null) mOnNetworkStatusListener.connToWifi();
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile provider's data plan
                    if (mOnNetworkStatusListener != null) mOnNetworkStatusListener.connToMobile();
                }
            } else {
                // not connected to the internet
                if (mOnNetworkStatusListener != null) mOnNetworkStatusListener.unConnNetwork();
            }

        }
    }

}