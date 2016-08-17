package com.asen.android.lib.base.tool.manage.action;

/**
 * ActionManager�ӿڶ���
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public interface IActionManager {

    /**
     * ִ��Action
     *
     * @param action action��ͼ
     */
    public void executeIntent(ActionIntent action);

    /**
     * ���ٵ�ǰAction
     */
    public void cancelCurrentIntent();

    /**
     * ��õ�ǰAction��ͼ
     *
     * @return ���ص�ǰAction��ͼ�������ǰû���κ�Actionִ�й����򷵻�null
     */
    public ActionIntent getCurrentIntent();

}
