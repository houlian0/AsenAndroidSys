package com.asen.android.lib.base.tool.manage.action;


import android.os.Bundle;

/**
 * Action执行意图
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public class ActionIntent {

    private BaseAction mAction; // Action

    private Bundle mBundle; // Bundle数据

    private Object mObject; // Object数据

    /**
     * 构造函数
     *
     * @param action 执行的Action
     * @param object object 数据
     * @param bundle bundle 数据
     */
    public ActionIntent(BaseAction action, Object object, Bundle bundle) {
        this.mAction = action;
        this.mBundle = bundle;
        this.mObject = object;
    }

    /**
     * 获得执行的Action
     *
     * @return 返回执行的Action
     */
    public BaseAction getAction() {
        return mAction;
    }

    /**
     * 获得object 数据
     *
     * @return 返回object 数据
     */
    public Object getObject() {
        return mObject;
    }

    /**
     * 获得bundle 数据
     *
     * @return 返回bundle 数据
     */
    public Bundle getBundle() {
        return mBundle;
    }
}
