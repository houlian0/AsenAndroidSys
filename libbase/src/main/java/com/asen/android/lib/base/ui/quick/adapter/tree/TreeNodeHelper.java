package com.asen.android.lib.base.ui.quick.adapter.tree;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * �༶���ڵ�����ࣨһ���Ի�ȡ���еĽڵ���Ϣ�����ԣ�
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:04
 */
class TreeNodeHelper<T> {

    private List<T> datas;

    private List<TreeNode<T>> treeNodeList;

    private int defaultLevel = 0;

    /**
     * �༶��������
     *
     * @param datas        ԭʼ����
     * @param defaultLevel Ĭ��չ���Ĳ�����С�ڵ��ڸ�����չ��
     */
    TreeNodeHelper(List<T> datas, int defaultLevel) {
        this.datas = datas;
        this.defaultLevel = defaultLevel < 0 ? 0 : defaultLevel;
        treeNodeList = reassemblingNodeList(); // ����ڵ㼯��
    }

    /**
     * ������пɼ���node����
     *
     * @return ��������չ���Ĵ���ʾ�ļ���
     */
    public List<TreeNode<T>> getExpandedNodeList() {
        List<TreeNode<T>> nodeList = new ArrayList<>();

        for (TreeNode<T> node : treeNodeList) {
            add2ExpandedNodeList(nodeList, node);
        }

        return nodeList;
    }

    // ���ܼ�������������
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
     * ����װ��ڵ㼯��
     *
     * @return ����װ���Ľ��
     */
    private List<TreeNode<T>> reassemblingNodeList() {
        List<TreeNode<T>> nodeList = datas2NodeList();
        initRootNodeList(nodeList);
        initLevel(nodeList);
        return nodeList;
    }

    /**
     * �����еĽڵ����ò㼶��Ϣ
     *
     * @param nodeList ���ڵ���Ϣ
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
     * ��������ڵ�
     *
     * @param nodeList ���еĽڵ���Ϣ
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
     * ��ԭʼ����ת�ɽڵ�������
     *
     * @return �ڵ�������
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

    // �Ƚ�����Object��
    private boolean equals(Object obj1, Object obj2) {
        return obj1 == null ? null == obj2 : obj1.equals(obj2);
    }

    // ���id��pid
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
