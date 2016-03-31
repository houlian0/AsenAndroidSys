package com.asen.android.lib.base.ui.quick.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple to Introduction
 * 快速BaseAdapter（需要自己去findView）
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:08
 */
public abstract class QuickBaseAdapter<T> extends BaseAdapter {

    /**
     * Android上下文
     */
    protected Context mContext;

    /**
     * 布局资源
     */
    private int mLayoutResId;

    /**
     * 数据源
     */
    private List<T> mDataList; // 数据源

    /**
     * 构造函数
     *
     * @param context     Android上下文
     * @param layoutResId 布局资源
     * @param data        数据源
     */
    public QuickBaseAdapter(Context context, int layoutResId, List<T> data) {
        mContext = context;
        mLayoutResId = layoutResId;
        mDataList = data == null ? new ArrayList<T>() : data;
    }

    /**
     * 获得数据集
     *
     * @return 数据集
     */
    protected List<T> getData() {
        return mDataList;
    }

    /**
     * 替换数据并刷新
     *
     * @param data 数据集
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
     * 填充内容
     *
     * @param helper   快速holder帮助类
     * @param info     数据信息（当position > size-1 时，为null）
     * @param position 数据下标
     */
    public abstract void convert(HolderHelper helper, T info, int position);

}