package com.asen.android.lib.base.tool.manage.action;

import android.os.Bundle;

/**
 * 可销毁式Action
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
     * 首次执行时会调用
     *
     * @param object object数据
     * @param bundle bundle数据
     */
    public abstract void onStart(Object object, Bundle bundle);

    /**
     * 非首次执行的其他多次执行时会调用
     *
     * @param object object数据
     * @param bundle bundle数据
     */
    public abstract void onResume(Object object, Bundle bundle);

}