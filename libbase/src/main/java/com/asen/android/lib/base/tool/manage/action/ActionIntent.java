package com.asen.android.lib.base.tool.manage.action;


import android.os.Bundle;

/**
 * Actionִ����ͼ
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public class ActionIntent {

    private BaseAction mAction; // Action

    private Bundle mBundle; // Bundle����

    private Object mObject; // Object����

    /**
     * ���캯��
     *
     * @param action ִ�е�Action
     * @param object object ����
     * @param bundle bundle ����
     */
    public ActionIntent(BaseAction action, Object object, Bundle bundle) {
        this.mAction = action;
        this.mBundle = bundle;
        this.mObject = object;
    }

    /**
     * ���ִ�е�Action
     *
     * @return ����ִ�е�Action
     */
    public BaseAction getAction() {
        return mAction;
    }

    /**
     * ���object ����
     *
     * @return ����object ����
     */
    public Object getObject() {
        return mObject;
    }

    /**
     * ���bundle ����
     *
     * @return ����bundle ����
     */
    public Bundle getBundle() {
        return mBundle;
    }
}
