package com.asen.android.lib.base.ui.view.tree.adapter;

import com.asen.android.lib.base.ui.view.tree.TreeNode;
import com.asen.android.lib.base.ui.view.tree.TreeNodeId;
import com.asen.android.lib.base.ui.view.tree.TreeNodePid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Simple to Introduction
 * 多级树节点帮助类（一次性获取所有的节点信息，所以）
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:04
 */
public class TreeNodeHelper<T> {

    private List<T> datas;

    private List<TreeNode<T>> treeNodeList;

    private int defaultLevel = 0;

    /**
     * 多级树帮助类
     *
     * @param datas        原始数据
     * @param defaultLevel 默认展开的层数，小于等于该数字展开
     */
    public TreeNodeHelper(List<T> datas, int defaultLevel) {
        this.datas = datas;
        this.defaultLevel = defaultLevel < 0 ? 0 : defaultLevel;
        treeNodeList = reassemblingNodeList(); // 重组节点集合
    }

    /**
     * 获得所有可见的node集合
     *
     * @return 返回所有展看的待显示的集合
     */
    public List<TreeNode<T>> getExpandedNodeList() {
        List<TreeNode<T>> nodeList = new ArrayList<>();

        for (TreeNode<T> node : treeNodeList) {
            add2ExpandedNodeList(nodeList, node);
        }

        return nodeList;
    }

    // 向总集合上增加数据
    private void add2ExpandedNodeList(List<TreeNode<T>> nodeList, TreeNode<T> node) {
        nodeList.add(node);
        if (node.isExpanded()) {
            List<TreeNode<T>> childList = node.getChildList();
            for (TreeNode<T> child : childList) {
                add2ExpandedNodeList(nodeList, child);
            }
        }
    }

    /**
     * 重新装配节点集合
     *
     * @return 重新装配后的结果
     */
    private List<TreeNode<T>> reassemblingNodeList() {
        List<TreeNode<T>> nodeList = datas2NodeList();
        initRootNodeList(nodeList);
        initLevel(nodeList);
        return nodeList;
    }

    /**
     * 给所有的节点设置层级信息
     *
     * @param nodeList 根节点信息
     */
    private void initLevel(List<TreeNode<T>> nodeList) {
        for (TreeNode<T> node : nodeList) {
            if (node.getParent() == null) {
                node.setLevel(1);
            } else {
                node.setLevel(node.getParent().getLevel() + 1);
            }
            List<TreeNode<T>> childList = node.getChildList();
            if (!childList.isEmpty()) {
                initLevel(childList);
            }

            if (node.getLevel() <= defaultLevel)
                node.setIsExpanded(true);
        }
    }

    /**
     * 处理出根节点
     *
     * @param nodeList 所有的节点信息
     */
    private void initRootNodeList(List<TreeNode<T>> nodeList) {
        ListIterator<TreeNode<T>> iterator = nodeList.listIterator();
        while (iterator.hasNext()) {
            TreeNode<T> next = iterator.next();
            if (next.getParent() != null) {
                iterator.remove();
            }
        }
    }

    /**
     * 将原始数据转成节点型数据
     *
     * @return 节点型数据
     */
    private List<TreeNode<T>> datas2NodeList() {
        List<TreeNode<T>> nodeList = new ArrayList<>();

        for (T data : datas) {
            nodeList.add(new TreeNode<T>(data));
        }

        int size = nodeList.size();
        for (int i = 0; i < size; i++) {
            try {
                TreeNode<T> node1 = nodeList.get(i);
                Object[] objects1 = getObjects(node1);

                for (int j = i + 1; j < size; j++) {
                    TreeNode<T> node2 = nodeList.get(j);
                    Object[] objects2 = getObjects(node2);

                    if (equals(objects1[0], objects2[1])) {
                        node1.getChildList().add(node2);
                        node2.setParent(node1);
                    } else if (equals(objects1[1], objects2[0])) {
                        node2.getChildList().add(node1);
                        node1.setParent(node2);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return nodeList;
    }

    // 比较两个Object类
    private boolean equals(Object obj1, Object obj2) {
        return obj1 == null ? null == obj2 : obj1.equals(obj2);
    }

    // 获得id和pid
    private Object[] getObjects(TreeNode<T> node) throws IllegalAccessException {
        T object = node.getObject();
        Field[] fields = object.getClass().getDeclaredFields();

        Object id = null, pid = null;
        for (Field f : fields) {
            if (f.getAnnotation(TreeNodeId.class) != null) {
                f.setAccessible(true);
                id = f.get(object);
            }
            if (f.getAnnotation(TreeNodePid.class) != null) {
                f.setAccessible(true);
                pid = f.get(object);
            }
        }

        if (id == null) {
            throw new IllegalAccessException("ID cannot be null!!!");
        }

        return new Object[]{id, pid};
    }

}
