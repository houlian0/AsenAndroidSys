package com.asen.android.lib.base.ui.quick.adapter.tree;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * ΪListView��������޼��� ��������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:04
 */
public abstract class BaseTreeAdapter<T> extends BaseAdapter {

    private TreeNodeHelper<T> mNodeHelper;

    private List<TreeNode<T>> mNodeList;

    protected ListView mListView;

    protected Context mContext;

    private OnTreeNodeClickListener onTreeNodeClickListener = null;

    public interface OnTreeNodeClickListener<T> {

        void onClick(TreeNode<T> data, boolean isLeaf);
    }

    public BaseTreeAdapter(ListView listView, List<T> datas, OnTreeNodeClickListener onTreeNodeClickListener) {
        this(listView, datas, 0, onTreeNodeClickListener);
    }

    public BaseTreeAdapter(ListView listView, List<T> datas, int defaultLevel, OnTreeNodeClickListener onTreeNodeClickListener) {
        this.mListView = listView;
        this.mContext = mListView.getContext();
        mNodeHelper = new TreeNodeHelper<>(datas, defaultLevel);
        this.onTreeNodeClickListener = onTreeNodeClickListener;
        mNodeList = mNodeHelper.getExpandedNodeList();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TreeNode<T> node = mNodeList.get(position);
                boolean isLeaf = node.isLeaf();
                if (!isLeaf) {
                    node.setIsExpanded(!node.isExpanded());
                    mNodeList = mNodeHelper.getExpandedNodeList();
                    notifyDataSetChanged();
                }
                if (BaseTreeAdapter.this.onTreeNodeClickListener != null) {
                    BaseTreeAdapter.this.onTreeNodeClickListener.onClick(node, isLeaf);
                }
            }
        });
    }

    @Override
    public int getCount() {
        return mNodeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNodeList.get(position).getObject();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TreeNode<T> node = mNodeList.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        convertView.setPadding((int) ((node.getLevel() - 1) * paddingLeftSize()), 0, 0, 0);
        return convertView;
    }

    /**
     * ����ÿ���������һ������������С
     *
     * @return ������С
     */
    public abstract float paddingLeftSize();

    /**
     * ��ʼ������
     *
     * @param node        ����Դ
     * @param position    �����±�
     * @param convertView ����View
     * @param parent      ViewGroup
     * @return ����View
     */
    public abstract View getConvertView(TreeNode<T> node, int position, View convertView, ViewGroup parent);

}
