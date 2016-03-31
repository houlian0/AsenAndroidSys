package com.asen.android.lib.base.tool.manage.action;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Simple to Introduction
 * 基本的Action
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public abstract class BaseAction<T> {

    /**
     * 调用Action的执行类（Activity或Fragment）
     */
    protected T mParent; // 调用Action的执行类（Activity或Fragment）

    /**
     * Android 上下文
     */
    protected Context mContext; // Android 上下文

    /**
     * 设置 调用Action的执行类（Activity或Fragment）
     *
     * @param parent 调用Action的执行类（Activity或Fragment）
     */
    void setParent(T parent) {
        mParent = parent;
        if (mParent instanceof Activity) {
            mContext = ((Activity) mParent).getApplicationContext();
        } else if (mParent instanceof Fragment) {
            mContext = ((Fragment) mParent).getActivity().getApplicationContext();
        }
    }

    /**
     * 创建
     */
    public abstract void onCreate();

    /**
     * 执行
     *
     * @param isResume 是否重复执行
     * @param object   object数据
     * @param bundle   bundle数据
     */
    public abstract void execute(boolean isResume, Object object, Bundle bundle);

    /**
     * 销毁
     */
    public abstract void onDestroy();

}