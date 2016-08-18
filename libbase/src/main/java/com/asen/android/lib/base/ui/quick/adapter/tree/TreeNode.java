package com.asen.android.lib.base.ui.quick.adapter.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 多级树节点
 *
 * @author Asen
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
     * @return 如果是根节点，返回true，否则返回false
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return 如果是父节点且展开，则返回true，否则返回false
     */
    public boolean isParentExpand() {
        return parent != null && parent.isExpanded();
    }

    /**
     * 是否是叶子节点
     *
     * @return 如果是叶子节点，返回true，否则返回false
     */
    public boolean isLeaf() {
        return childList.size() == 0;
    }

    /**
     * 获取父节点的节点新
     *
     * @return 如果存在父节点，返回父节点信息，否则返回null
     */
    public TreeNode<T> getParent() {
        return parent;
    }

    // 设置父节点
    void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    /**
     * 获取所有的子节点信息
     *
     * @return 所有的子节点信息
     */
    public List<TreeNode<T>> getChildList() {
        return childList;
    }

    /**
     * 获取节点中的自定义数据
     *
     * @return 自定义数据
     */
    public T getObject() {
        return object;
    }

    // 设置节点中的自定义数据
    void setObject(T object) {
        this.object = object;
    }

    /**
     * 判断当前节点是否展开
     *
     * @return 当前节点是否展开，展开返回true，否则返回false
     */
    public boolean isExpanded() {
        return isExpanded;
    }

    /**
     * 设置当前节点是否展开
     *
     * @param isExpanded true，展开；false，不展开
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
     * 获取当前节点的级数
     *
     * @return 当前节点的级数
     */
    public int getLevel() {
        return level;
    }

    void setLevel(int level) {
        this.level = level;
    }

}
