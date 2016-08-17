package com.asen.android.lib.base.tool.manage.action;

import android.os.Bundle;

/**
 * ������ʽAction
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public abstract class DisposableAction extends BaseAction {

    @Override
    public final void execute(boolean isResume, Object object, Bundle bundle) {
        if (isResume) {
            onResume(object, bundle);
        } else {
            onStart(object, bundle);
        }
    }

    /**
     * �״�ִ��ʱ�����
     *
     * @param object object����
     * @param bundle bundle����
     */
    public abstract void onStart(Object object, Bundle bundle);

    /**
     * ���״�ִ�е��������ִ��ʱ�����
     *
     * @param object object����
     * @param bundle bundle����
     */
    public abstract void onResume(Object object, Bundle bundle);

}