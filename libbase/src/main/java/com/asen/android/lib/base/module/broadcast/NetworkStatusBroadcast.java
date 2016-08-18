package com.asen.android.lib.base.module.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络状态监听广播
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 15:12
 */
public class NetworkStatusBroadcast implements IBroadcastDefinition {

    private Context mContext; // Android上下文

    private OnNetworkStatusListener mOnNetworkStatusListener; // 网络状态的监听接口

    private NetworkRecevier mRecevier; // 广播

    /**
     * 构造函数
     *
     * @param context                 Android上下文
     * @param onNetworkStatusListener 网络状态的监听接口
     */
    public NetworkStatusBroadcast(Context context, OnNetworkStatusListener onNetworkStatusListener) {
        mContext = context;
        mOnNetworkStatusListener = onNetworkStatusListener;
        mRecevier = new NetworkRecevier();
    }

    /**
     * 网络状态监听的监听接口
     */
    public interface OnNetworkStatusListener {

        /**
         * 连接到wifi网络
         */
        public void connToWifi();

        /**
         * 连接到移动网络
         */
        public void connToMobile();

        /**
         * 未连接网络
         */
        public void unConnNetwork();
    }

    /**
     * 开始监听广播
     */
    @Override
    public void startWatch() {
        if (mRecevier != null) {
            mContext.registerReceiver(mRecevier, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
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