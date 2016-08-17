package com.asen.android.lib.base.tool.manage;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * View 单选管理类，基于selector
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public class ViewManager4Selector {

    private List<View> list = null; // 保存View集合

    private int currentPressedIndex = -1;

    public ViewManager4Selector() {
        list = new ArrayList<>();
    }

    /**
     * 新增View
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
     * 新增View集
     *
     * @param views view集
     */
    public void addView(View... views) {
        for (View v : views) {
            addView(v);
        }
    }

    /**
     * 获得View的集合
     *
     * @return View的集合
     */
    public List<View> getViewList() {
        return list;
    }

    /**
     * 判断是否包含View
     *
     * @param view view
     * @return true 包含，false 不包含
     */
    public boolean contains(View view) {
        return list.contains(view);
    }

    /**
     * 增加选中的View
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
     * 移除所有View
     */
    public void removeAllViews() {
        list.clear();
    }

    /**
     * 选中View
     *
     * @param view select this view, if it's selected, do nothing.
     * @return 是否选中
     */
    public boolean selectView(View view) {
        return this.selectView(view, false);
    }

    /**
     * 选中或取消选中View
     *
     * @param view select this view, if it's selected, cancel selected;
     * @return 是否选中
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
        } else if (currentPressedIndex == index) {// 当前已经按下
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
     * 选中View
     *
     * @param viewId select this view, if it's selected, do nothing.
     * @return 是否选中
     */
    public boolean selectView(int viewId) {
        return this.selectView(viewId, false);
    }

    /**
     * 选中或取消选中View
     *
     * @param viewId select this view, if it's selected, cancel selected;
     * @return 是否选中
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
        } else if (currentPressedIndex == index) {// 当前已经按下
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
     * 取消当前选中的View
     */
    public void cancelSelect() {
        if (currentPressedIndex != -1 && currentPressedIndex < list.size()) {
            list.get(currentPressedIndex).setSelected(false);
        }
        currentPressedIndex = -1;
    }

    /**
     * 选中View
     *
     * @param tag tag标签
     * @return 是否选中
     */
    public boolean selectViewByTag(Object tag) {
        return this.selectViewByTag(tag, false);
    }

    /**
     * 选中或取消选中View
     *
     * @param tag tag标签
     * @return 是否选中
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
        } else if (currentPressedIndex == index) {// 当前已经按下
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
     * 获得当前选中的View
     *
     * @return 返回当前选中的View，没有则返回null
     */
    public View getSelectView() {
        if (currentPressedIndex != -1 && currentPressedIndex < list.size()) {
            return list.get(currentPressedIndex);
        }
        return null;
    }

}
