package com.asen.android.lib.base.tool.manage.action;

/**
 * ActionManagerʵ����
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
        boolean isExecuteAction = action instanceof ExecuteAction; // �Ƿ���ִ��ʽAction

        boolean isResume = currentIntent != null && currentIntent.getAction().getClass().getName().equals(action.getClass().getName());
        if (!isResume) { // �״� ִ��
            action.setParent(mParent);
            if (!isExecuteAction) release(); // ������һ��Action(ִ��ʽAction�����˲���)
            action.onCreate(); // �״�
            if (!isExecuteAction) currentIntent = intent;
        } else { // �ظ�ִ�У�ȡ�ϴ�ִ�е�Action
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
