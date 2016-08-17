package com.asen.android.lib.base.ui.quick.adapter;

import android.content.Context;

import com.asen.android.lib.base.ui.quick.findview.AFindView;
import com.asen.android.lib.base.ui.quick.findview.FindViewUtil;

import java.util.List;

/**
 * ����BaseAdapter������ҪfindView
 *
 * @param <T> ����ʵ����
 * @param <H> Holder��
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:08
 */
public abstract class QuickHolderBaseAdapter<T, H> extends QuickBaseAdapter<T> {

    /**
     * ���캯��
     *
     * @param context     Android������
     * @param layoutResId ������Դ
     * @param data        ����Դ
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
     * ���Holder��ʵ��������
     * {@link AFindView}��{@link HolderHelper}
     *
     * @return new �Զ����holder�������ؼ�����AFindViewע��
     */
    public abstract H getInstance();

    /**
     * �������
     *
     * @param holder   �Զ����holder
     * @param info     ������Ϣ����position > size-1 ʱ��Ϊnull��
     * @param position �����±�
     */
    public abstract void convert(H holder, T info, int position);

}