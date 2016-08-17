package com.asen.android.lib.base.ui.quick.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * ����BaseAdapter����Ҫ�Լ�ȥfindView��
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:08
 */
abstract class QuickBaseAdapter<T> extends BaseAdapter {

    /**
     * Android������
     */
    protected Context mContext;

    /**
     * ������Դ
     */
    private int mLayoutResId;

    /**
     * ����Դ
     */
    private List<T> mDataList; // ����Դ

    /**
     * ���캯��
     *
     * @param context     Android������
     * @param layoutResId ������Դ
     * @param data        ����Դ
     */
    QuickBaseAdapter(Context context, int layoutResId, List<T> data) {
        mContext = context;
        mLayoutResId = layoutResId;
        mDataList = data == null ? new ArrayList<T>() : data;
    }

    /**
     * ������ݼ�
     *
     * @return ���ݼ�
     */
    protected List<T> getData() {
        return mDataList;
    }

    /**
     * �滻���ݲ�ˢ��
     *
     * @param data ���ݼ�
     */
    public void replaceData(List<T> data) {
        if (data == null) return;
        mDataList.clear();
        mDataList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        HolderHelper helper = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(mLayoutResId, null);
            helper = new HolderHelper(view);
            view.setTag(helper);
        } else {
            helper = (HolderHelper) view.getTag();
        }

        if (position < mDataList.size()) {
            T t = mDataList.get(position);
            convert(helper, t, position);
        } else {
            convert(helper, null, position);
        }

        return view;
    }

    /**
     * �������
     *
     * @param helper   ����holder������
     * @param info     ������Ϣ����position > size-1 ʱ��Ϊnull��
     * @param position �����±�
     */
    public abstract void convert(HolderHelper helper, T info, int position);

}