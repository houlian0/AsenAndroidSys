package com.asen.android.lib.base.tool.manage.action;

/**
 * Simple to Introduction
 * ActionManager接口定义
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public interface IActionManager {

    /**
     * 执行Action
     *
     * @param action action意图
     */
    public void executeIntent(ActionIntent action);

    /**
     * 销毁当前Action
     */
    public void cancelCurrentIntent();

    /**
     * 获得当前Action意图
     *
     * @return 当前Action意图，or null
     */
    public ActionIntent getCurrentIntent();

}
