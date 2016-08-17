package com.asen.android.lib.base.tool.manage.action;

/**
 * ActionManager实现类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public class OperaActionManager<T> implements IActionManager {

    private T mParent;

    private ActionIntent currentIntent = null;

    public OperaActionManager(T parent) {
        mParent = parent;
    }

    @Override
    public void executeIntent(ActionIntent intent) {
        BaseAction action = intent.getAction();
        boolean isExecuteAction = action instanceof ExecuteAction; // 是否是执行式Action

        boolean isResume = currentIntent != null && currentIntent.getAction().getClass().getName().equals(action.getClass().getName());
        if (!isResume) { // 首次 执行
            action.setParent(mParent);
            if (!isExecuteAction) release(); // 销毁上一个Action(执行式Action不做此操作)
            action.onCreate(); // 首次
            if (!isExecuteAction) currentIntent = intent;
        } else { // 重复执行，取上次执行的Action
            action = currentIntent.getAction();
        }

        action.execute(isResume, intent.getObject(), intent.getBundle());
    }

    @Override
    public void cancelCurrentIntent() {
        release();
    }

    private void release() {
        if (currentIntent != null) {
            currentIntent.getAction().onDestroy();
            currentIntent = null;
        }
    }

    public ActionIntent getCurrentIntent() {
        return currentIntent;
    }

}
