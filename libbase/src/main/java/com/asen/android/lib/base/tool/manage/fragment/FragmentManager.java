package com.asen.android.lib.base.tool.manage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.List;

/**
 * 管理Fragment的抽象类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
public abstract class FragmentManager {

    /**
     * 创建FragmentManager
     *
     * @param parent 执行的父类（FragmentActivity or Fragment）
     * @param <T>    FragmentActivity or Fragment
     * @return 创建FragmentManager，不属于FragmentActivity or Fragment时，返回null
     */
    public static <T> FragmentManager createFragmentManager(T parent) {
        if (parent instanceof FragmentActivity) {
            return new FragmentManager4Activity((FragmentActivity) parent);
        } else if (parent instanceof Fragment) {
            return new FragmentManager4Fragment((Fragment) parent);
        } else {
            return null;
        }
    }

    /**
     * 获得所有Fragment的集合
     *
     * @return 所有Fragment的集合
     */
    public abstract List<Fragment> getList();

    /**
     * 添加Fragment
     *
     * @param fragment      要添加的Fragment
     * @param frameLayoutId frameLayoutId
     */
    public abstract void add(Fragment fragment, int frameLayoutId);

    /**
     * 添加Fragment
     *
     * @param fragment      要添加的Fragment
     * @param frameLayoutId frameLayoutId
     * @param visible       是否可见
     */
    public abstract void add(Fragment fragment, int frameLayoutId, boolean visible);

    /**
     * 添加Fragment
     *
     * @param fragment      要添加的Fragment
     * @param frameLayoutId frameLayoutId
     * @param visible       是否可见
     * @param data          刷新数据
     */
    public abstract void add(Fragment fragment, int frameLayoutId, boolean visible, Bundle data);

    /**
     * 替换Fragment
     *
     * @param fragment      要替换的Fragment
     * @param frameLayoutId frameLayoutId
     * @param data          刷新数据
     */
    public abstract void replace(Fragment fragment, int frameLayoutId, Bundle data);

    /**
     * 添加Fragment集合
     *
     * @param list          要添加的Fragment集合 （只会显示集合中第一个Fragment）
     * @param frameLayoutId frameLayoutId
     */
    public abstract void addList(List<Fragment> list, int frameLayoutId);

    /**
     * 添加Fragment集合
     *
     * @param list          要添加的Fragment集合（只会显示集合中第一个Fragment）
     * @param frameLayoutId frameLayoutId
     * @param data          刷新数据
     */
    public abstract void addList(List<Fragment> list, int frameLayoutId, Bundle data);

    /**
     * 隐藏Fragment
     *
     * @param fragment 隐藏的Fragment
     */
    public abstract void hide(Fragment fragment);

    /**
     * 隐藏Fragment
     *
     * @param cla 隐藏的Fragment类
     */
    public abstract void hide(Class<?> cla);

    /**
     * 显示Fragment
     *
     * @param fragment 显示的Fragment
     */
    public abstract void show(Fragment fragment);

    /**
     * 显示Fragment
     *
     * @param fragment 显示的Fragment
     * @param data     刷新数据
     */
    public abstract void show(Fragment fragment, Bundle data);

    /**
     * 移除Fragment
     *
     * @param fragment 移除的Fragment
     */
    public abstract void remove(Fragment fragment);

    /**
     * 移除所有Fragment
     */
    public abstract void removeAll();

    /**
     * 显示Fragment
     *
     * @param cla 显示的Fragment类
     */
    public abstract void showFragment(Class<?> cla);

    /**
     * 显示Fragment
     *
     * @param cla  显示的Fragment类
     * @param data 刷新数据
     */
    public abstract void showFragment(Class<?> cla, Bundle data);

    /**
     * 仅显示Fragment，不隐藏
     *
     * @param cla 显示的Fragment类
     */
    public abstract void showFragmentOnly(Class<?> cla);

    /**
     * 仅显示Fragment，不隐藏
     *
     * @param cla  显示的Fragment类
     * @param data 刷新数据
     */
    public abstract void showFragmentOnly(Class<?> cla, Bundle data);

    /**
     * 获得Fragment
     *
     * @param cla 要获得的Fragment类，不存在返回null
     */
    public abstract <T extends Fragment> T getFragment(Class<T> cla);

}
