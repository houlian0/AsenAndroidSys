package com.asen.android.lib.base.tool.manage;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * View ��ѡ�����࣬����selector
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public class ViewManager4Selector {

    private List<View> list = null; // ����View����

    private int currentPressedIndex = -1;

    public ViewManager4Selector() {
        list = new ArrayList<>();
    }

    /**
     * ����View
     *
     * @param view view
     */
    public void addView(View view) {
        if (!list.contains(view))
            list.add(view);

        if (view.isSelected())
            view.setSelected(false);
    }

    /**
     * ����View��
     *
     * @param views view��
     */
    public void addView(View... views) {
        for (View v : views) {
            addView(v);
        }
    }

    /**
     * ���View�ļ���
     *
     * @return View�ļ���
     */
    public List<View> getViewList() {
        return list;
    }

    /**
     * �ж��Ƿ����View
     *
     * @param view view
     * @return true ������false ������
     */
    public boolean contains(View view) {
        return list.contains(view);
    }

    /**
     * ����ѡ�е�View
     *
     * @param view view
     */
    public void addSelectedView(View view) {
        if (view == null) return;
        if (!list.contains(view))
            list.add(view);

        if (currentPressedIndex != -1) {
            list.get(currentPressedIndex).setSelected(false);
        }
        currentPressedIndex = list.size() - 1;

        if (view.isSelected())
            view.setSelected(true);
    }

    /**
     * �Ƴ�����View
     */
    public void removeAllViews() {
        list.clear();
    }

    /**
     * ѡ��View
     *
     * @param view select this view, if it's selected, do nothing.
     * @return �Ƿ�ѡ��
     */
    public boolean selectView(View view) {
        return this.selectView(view, false);
    }

    /**
     * ѡ�л�ȡ��ѡ��View
     *
     * @param view select this view, if it's selected, cancel selected;
     * @return �Ƿ�ѡ��
     */
    public boolean selectOrCancel(View view) {
        return this.selectView(view, true);
    }

    private boolean selectView(View view, boolean flag) {
        if (view == null) return false;

        boolean result = false;

        int index = list.indexOf(view);

        if (currentPressedIndex != -1 && currentPressedIndex != index) {
            list.get(currentPressedIndex).setSelected(false);
        }

        if (index == -1) {
            addSelectedView(view);
            view.setSelected(true);
            result = true;
        } else if (!view.isSelected()) {
            view.setSelected(true);
            result = true;
        } else if (currentPressedIndex == index) {// ��ǰ�Ѿ�����
            if (flag) {
                list.get(index).setSelected(false);
                result = false;
            } else {
                result = true;
            }
        } else {
            list.get(index).setSelected(true);
            result = true;
        }

        currentPressedIndex = index;

        return result;
    }

    /**
     * ѡ��View
     *
     * @param viewId select this view, if it's selected, do nothing.
     * @return �Ƿ�ѡ��
     */
    public boolean selectView(int viewId) {
        return this.selectView(viewId, false);
    }

    /**
     * ѡ�л�ȡ��ѡ��View
     *
     * @param viewId select this view, if it's selected, cancel selected;
     * @return �Ƿ�ѡ��
     */
    public boolean selectOrCancel(int viewId) {
        return this.selectView(viewId, true);
    }

    private boolean selectView(int viewId, boolean flag) {
        boolean result = false;

        View view = getViewById(viewId);
        if (view == null) return false;

        int index = list.indexOf(view);

        if (currentPressedIndex != -1 && currentPressedIndex != index) {
            list.get(currentPressedIndex).setSelected(false);
        }

        if (!view.isSelected()) {
            view.setSelected(true);
            result = true;
        } else if (index == -1) {
            addSelectedView(view);
            view.setSelected(true);
            result = true;
        } else if (currentPressedIndex == index) {// ��ǰ�Ѿ�����
            if (flag) {
                list.get(index).setSelected(false);
                result = false;
            } else {
                result = true;
            }
        } else {
            list.get(index).setSelected(true);
            result = true;
        }

        currentPressedIndex = index;

        return result;
    }

    private View getViewById(int id) {
        for (View v : list) {
            if (id == v.getId())
                return v;
        }
        return null;
    }

    /**
     * ȡ����ǰѡ�е�View
     */
    public void cancelSelect() {
        if (currentPressedIndex != -1 && currentPressedIndex < list.size()) {
            list.get(currentPressedIndex).setSelected(false);
        }
        currentPressedIndex = -1;
    }

    /**
     * ѡ��View
     *
     * @param tag tag��ǩ
     * @return �Ƿ�ѡ��
     */
    public boolean selectViewByTag(Object tag) {
        return this.selectViewByTag(tag, false);
    }

    /**
     * ѡ�л�ȡ��ѡ��View
     *
     * @param tag tag��ǩ
     * @return �Ƿ�ѡ��
     */
    public boolean selectOrCancelByTag(Object tag) {
        return this.selectViewByTag(tag, true);
    }

    private boolean selectViewByTag(Object tag, boolean flag) {
        boolean result = false;

        View view = getViewByTag(tag);
        if (view == null) return false;

        int index = list.indexOf(view);

        if (currentPressedIndex != -1 && currentPressedIndex != index) {
            list.get(currentPressedIndex).setSelected(false);
        }

        if (!view.isSelected()) {
            view.setSelected(true);
            result = true;
        } else if (index == -1) {
            addSelectedView(view);
            view.setSelected(true);
            result = true;
        } else if (currentPressedIndex == index) {// ��ǰ�Ѿ�����
            if (flag) {
                list.get(index).setSelected(false);
                result = false;
            } else {
                result = true;
            }
        } else {
            list.get(index).setSelected(true);
            result = true;
        }

        currentPressedIndex = index;

        return result;
    }

    private View getViewByTag(Object tag) {
        for (View v : list) {
            if (tag.equals(v.getTag()))
                return v;
        }
        return null;
    }

    /**
     * ��õ�ǰѡ�е�View
     *
     * @return ���ص�ǰѡ�е�View��û���򷵻�null
     */
    public View getSelectView() {
        if (currentPressedIndex != -1 && currentPressedIndex < list.size()) {
            return list.get(currentPressedIndex);
        }
        return null;
    }

}
