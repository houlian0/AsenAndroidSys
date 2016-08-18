package com.asen.android.lib.base.ui.quick.adapter;

import android.content.Context;

import com.asen.android.lib.base.ui.quick.findview.AFindView;
import com.asen.android.lib.base.ui.quick.findview.FindViewUtil;

import java.util.List;

/**
 * 快速BaseAdapter，不需要findView
 *
 * @param <T> 数据实例类
 * @param <H> Holder类
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:08
 */
public abstract class QuickHolderBaseAdapter<T, H> extends QuickBaseAdapter<T> {

    /**
     * 构造函数
     *
     * @param context     Android上下文
     * @param layoutResId 布局资源
     * @param data        数据源
     */
    public QuickHolderBaseAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }

    @Override
    public final void convert(HolderHelper helper, T info, int position) {
        Object tag = helper.getTag();
        if (tag == null) {
            tag = getInstance();
            FindViewUtil.getInstance(mContext).findViews(helper, tag);
            helper.setTag(tag);
        }
        convert((H) tag, info, position);
    }

    /**
     * 获得Holder的实例化对象
     * {@link AFindView}、{@link HolderHelper}
     *
     * @return new 自定义的holder，并将控件加上AFindView注解
     */
    public abstract H getInstance();

    /**
     * 填充内容
     *
     * @param holder   自定义的holder
     * @param info     数据信息（当position > size-1 时，为null）
     * @param position 数据下标
     */
    public abstract void convert(H holder, T info, int position);

}