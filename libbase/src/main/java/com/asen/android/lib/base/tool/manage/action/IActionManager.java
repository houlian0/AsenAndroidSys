package com.asen.android.lib.base.tool.manage.action;

/**
 * ActionManager接口定义
 *
 * @author Asen
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
     * @return 返回当前Action意图，如果当前没有任何Action执行过，则返回null
     */
    public ActionIntent getCurrentIntent();

}
