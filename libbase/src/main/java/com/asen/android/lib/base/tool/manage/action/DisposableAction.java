package com.asen.android.lib.base.tool.manage.action;

import android.os.Bundle;

/**
 * Simple to Introduction
 * 可销毁式Action
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public abstract class DisposableAction extends BaseAction {

    /**
     * 创建
     */
    public abstract void onCreate();

    @Override
    public final void execute(boolean isResume, Object object, Bundle bundle) {
        if (isResume) {
            onResume(object, bundle);
        } else {
            onStart(object, bundle);
        }
    }

    /**
     * 首次执行
     *
     * @param object object数据
     * @param bundle bundle数据
     */
    public abstract void onStart(Object object, Bundle bundle);

    /**
     * 多次执行
     *
     * @param object object数据
     * @param bundle bundle数据
     */
    public abstract void onResume(Object object, Bundle bundle);

    /**
     * 销毁
     */
    public abstract void onDestroy();

}