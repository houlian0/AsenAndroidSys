package com.asen.android.lib.base.ui.view.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple to Introduction
 * 多级树节点
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:04
 */
public class TreeNode<T> {

    /**
     * 父节点（为null则无父节点）
     */
    private TreeNode<T> parent;

    /**
     * 子节点
     */
    private List<TreeNode<T>> childList;

    /**
     * 节点数据
     */
    private T object;

    /**
     * 是否展开的
     */
    private boolean isExpanded;

    /**
     * 级数
     */
    private int level;

    public TreeNode(T object) {
        this.childList = new ArrayList<>();
        this.object = object;
    }

    /**
     * 是否为跟节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null) return false;
        return parent.isExpanded();
    }

    /**
     * 是否是叶子界点
     *
     * @return
     */
    public boolean isLeaf() {
        return childList.size() == 0;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public List<TreeNode<T>> getChildList() {
        return childList;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded = isExpanded;
        if (!isExpanded) {
            for (TreeNode<T> node : childList) {
                node.setIsExpanded(false);
            }
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
