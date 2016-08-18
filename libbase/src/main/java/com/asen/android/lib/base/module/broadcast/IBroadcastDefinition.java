package com.asen.android.lib.base.module.broadcast;

/**
 * 广播接受者接口定义
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 15:12
 */
public interface IBroadcastDefinition {

    /**
     * 开始监听广播
     */
    public void startWatch();

    /**
     * 停止监听，注销广播
     */
    public void stopWatch();

}
