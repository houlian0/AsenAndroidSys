package com.asen.android.lib.base.tool.manage.fragment;

import android.os.Bundle;

/**
 * Fragment显示时的监听接口
 *
 * @param <T>
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public interface OnFragmentRefreshListener<T> {

    /**
     * 显示Fragment时调用
     *
     * @param parent 父类信息（Activity或者Fragment）
     * @param data   传递的数据信息
     */
    public void onRefresh(T parent, Bundle data);

}
