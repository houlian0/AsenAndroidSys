package com.asen.android.lib.base.ui.quick.adapter.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * �༶���ڵ�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:04
 */
public class TreeNode<T> {

    /**
     * ���ڵ㣨Ϊnull���޸��ڵ㣩
     */
    private TreeNode<T> parent;

    /**
     * �ӽڵ�
     */
    private List<TreeNode<T>> childList;

    /**
     * �ڵ�����
     */
    private T object;

    /**
     * �Ƿ�չ����
     */
    private boolean isExpanded;

    /**
     * ����
     */
    private int level;

    public TreeNode(T object) {
        this.childList = new ArrayList<>();
        this.object = object;
    }

    /**
     * �Ƿ�Ϊ���ڵ�
     *
     * @return ����Ǹ��ڵ㣬����true�����򷵻�false
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * �жϸ��ڵ��Ƿ�չ��
     *
     * @return ����Ǹ��ڵ���չ�����򷵻�true�����򷵻�false
     */
    public boolean isParentExpand() {
        return parent != null && parent.isExpanded();
    }

    /**
     * �Ƿ���Ҷ�ӽڵ�
     *
     * @return �����Ҷ�ӽڵ㣬����true�����򷵻�false
     */
    public boolean isLeaf() {
        return childList.size() == 0;
    }

    /**
     * ��ȡ���ڵ�Ľڵ���
     *
     * @return ������ڸ��ڵ㣬���ظ��ڵ���Ϣ�����򷵻�null
     */
    public TreeNode<T> getParent() {
        return parent;
    }

    // ���ø��ڵ�
    void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    /**
     * ��ȡ���е��ӽڵ���Ϣ
     *
     * @return ���е��ӽڵ���Ϣ
     */
    public List<TreeNode<T>> getChildList() {
        return childList;
    }

    /**
     * ��ȡ�ڵ��е��Զ�������
     *
     * @return �Զ�������
     */
    public T getObject() {
        return object;
    }

    // ���ýڵ��е��Զ�������
    void setObject(T object) {
        this.object = object;
    }

    /**
     * �жϵ�ǰ�ڵ��Ƿ�չ��
     *
     * @return ��ǰ�ڵ��Ƿ�չ����չ������true�����򷵻�false
     */
    public boolean isExpanded() {
        return isExpanded;
    }

    /**
     * ���õ�ǰ�ڵ��Ƿ�չ��
     *
     * @param isExpanded true��չ����false����չ��
     */
    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
        if (!isExpanded) {
            for (TreeNode<T> node : childList) {
                node.setIsExpanded(false);
            }
        }
    }

    /**
     * ��ȡ��ǰ�ڵ�ļ���
     *
     * @return ��ǰ�ڵ�ļ���
     */
    public int getLevel() {
        return level;
    }

    void setLevel(int level) {
        this.level = level;
    }

}
