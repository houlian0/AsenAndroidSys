package com.asen.android.lib.base.ui.quick.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * 快速holder帮助类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:08
 */
public class HolderHelper {

    private View mView;

    private SparseArray<View> mViewArray;

    public Object mTag;

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    HolderHelper(View view) {
        mView = view;
        mViewArray = new SparseArray<>();
    }

    /**
     * 根据Id找到对应的View
     *
     * @param viewId 控件id
     * @param <T>    返回结果定义
     * @return 返回View
     */
    public <T extends View> T findViewById(int viewId) {
        View view = mViewArray.get(viewId);
        if (view == null) {
            view = mView.findViewById(viewId);
            mViewArray.put(viewId, view);
        }
        return (T) view;
    }

}