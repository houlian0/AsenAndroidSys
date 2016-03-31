package com.asen.android.lib.base.tool.manage;

import android.os.Bundle;

/**
 * Fragment显示时的监听接口
 *
 * @param <T>
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public interface OnFragmentRefreshListener<T> {

    /**
     * 显示时调用
     *
     * @param parent
     * @param data
     */
    public void onRefresh(T parent, Bundle data);

}
