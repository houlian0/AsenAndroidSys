package com.asen.android.lib.base.tool.manage;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple to Introduction
 * 管理Fragment的类
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:25
 */
class FragmentManager4Activity extends FragmentManager {

    private FragmentActivity mActivity;

    private List<Fragment> fragmentList;

    private Handler mHandler = null;

    /**
     * 新建管理Fragment的类
     *
     * @param activity Activity
     */
    public FragmentManager4Activity(FragmentActivity activity) {
        mActivity = activity;
        fragmentList = new ArrayList<Fragment>();
        mHandler = new Handler();
    }

    @Override
    public List<Fragment> getList() {
        return fragmentList;
    }

    /**
     * 向一个FrameLayout加入一个Fragment，并设置其可见
     *
     * @param fragment      Fragment
     * @param frameLayoutId FrameLayout的id
     */
    @Override
    public void add(Fragment fragment, int frameLayoutId) {
        this.add(fragment, frameLayoutId, true);
    }

    /**
     * 向一个FrameLayout加入一个Fragment，并设置其可见属性
     *
     * @param fragment      Fragment
     * @param frameLayoutId FrameLayout的id
     * @param visible       true可见，false隐藏
     */
    @Override
    public void add(final Fragment fragment, int frameLayoutId, boolean visible) {
        this.add(fragment, frameLayoutId, visible, null);
    }

    /**
     * 向一个FrameLayout加入一个Fragment，并设置其可见属性
     *
     * @param fragment      Fragment
     * @param frameLayoutId FrameLayout的id
     * @param visible       true可见，false隐藏
     * @param data          数据
     */
    @Override
    public void add(final Fragment fragment, int frameLayoutId, boolean visible, final Bundle data) {
        if (fragment == null || mActivity == null)
            return;

        if (!fragmentList.contains(fragment)) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager()
                    .beginTransaction();
            ft.add(frameLayoutId, fragment);
            if (visible) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commitAllowingStateLoss();

            fragmentList.add(fragment);
        }

        if (visible && fragment instanceof OnFragmentRefreshListener) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((OnFragmentRefreshListener) fragment).onRefresh(mActivity, data);
                }
            });
        }
    }

    /**
     * @param fragment
     * @param frameLayoutId
     * @param data
     * @Title: replace
     * @Description:
     */
    @Override
    public void replace(final Fragment fragment, int frameLayoutId, final Bundle data) {
        if (fragment == null || mActivity == null)
            return;

        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        ft.replace(frameLayoutId, fragment);
        ft.commitAllowingStateLoss();

        if (fragment instanceof OnFragmentRefreshListener) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((OnFragmentRefreshListener) fragment).onRefresh(mActivity, data);

                }
            });
        }
    }


    /**
     * 向一个FrameLayout加入多个Fragment，并显示第一个Fragment，其余隐藏
     *
     * @param list          Fragment列表
     * @param frameLayoutId FrameLayout的id
     */

    public void addList(List<Fragment> list, int frameLayoutId) {
        this.addList(list, frameLayoutId, null);
    }

    /**
     * 向一个FrameLayout加入多个Fragment，并显示第一个Fragment，其余隐藏
     *
     * @param list          Fragment列表
     * @param frameLayoutId FrameLayout的id
     * @param data          数据
     */
    public void addList(List<Fragment> list, int frameLayoutId, final Bundle data) {
        if (list == null)
            return;

        this.fragmentList.addAll(list);

        Fragment showFragment = null;
        FragmentTransaction ft = mActivity.getSupportFragmentManager()
                .beginTransaction();
        for (Fragment f : fragmentList) {
            ft.add(frameLayoutId, f);
            if (showFragment == null) {
                showFragment = f;
                ft.show(f);
            } else {
                ft.hide(f);
            }
        }
        ft.commitAllowingStateLoss();

        if (showFragment != null && showFragment instanceof OnFragmentRefreshListener) {
            final Fragment f = showFragment;
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    ((OnFragmentRefreshListener) f).onRefresh(mActivity, data);
                }
            });
        }
    }

    /**
     * 隐藏一个Fragment(该工具类集合中包含的Fragment)
     *
     * @param fragment Fragment
     */
    public void hide(final Fragment fragment) {
        if (fragment == null || mActivity == null)
            return;

        if (fragmentList.contains(fragment)) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            ft.hide(fragment);
            ft.commitAllowingStateLoss();
        }

        if (fragment != null && fragment instanceof OnFragmentHideListener) {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    ((OnFragmentHideListener) fragment).onHide();

                }
            });
        }
    }

    /**
     * 隐藏一个Fragment(该工具类集合中包含的Fragment)
     *
     * @param cla
     */
    public void hide(Class<?> cla) {
        if (mActivity == null) return;

        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();

        for (Fragment fragment : fragmentList) {
            if (fragment.getClass().getName().equals(cla.getName())) {
                ft.hide(fragment);

                final Fragment f = fragment;
                if (f != null && f instanceof OnFragmentHideListener) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((OnFragmentHideListener) f).onHide();
                        }
                    });
                }
                break;
            }
        }

        ft.commitAllowingStateLoss();
    }

    /**
     * 显示一个Fragment(该工具类集合中包含的Fragment)
     *
     * @param fragment Fragment
     */
    public void show(final Fragment fragment) {
        this.show(fragment, null);
    }

    /**
     * 显示一个Fragment(该工具类集合中包含的Fragment)
     *
     * @param fragment Fragment
     * @param data     数据
     */
    public void show(final Fragment fragment, final Bundle data) {
        if (fragment == null || mActivity == null)
            return;

        if (fragmentList.contains(fragment)) {
            FragmentTransaction ft = mActivity.getSupportFragmentManager()
                    .beginTransaction();
            ft.show(fragment);
            ft.commitAllowingStateLoss();
        }

        if (fragment instanceof OnFragmentRefreshListener) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((OnFragmentRefreshListener) fragment).onRefresh(mActivity, data);
                }
            });
        }
    }

    /**
     * 移除一个Fragment(该工具类集合中包含的Fragment)
     *
     * @param fragment Fragment
     */
    public void remove(Fragment fragment) {
        if (fragment == null || mActivity == null)
            return;

        if (fragmentList.contains(fragment)) {
            fragmentList.remove(fragment);
        }
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 移除所有该集合类中的Fragment
     */
    public void removeAll() {
        if (mActivity == null)
            return;

        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        for (Fragment fragment : fragmentList) {
            ft.remove(fragment);
        }
        ft.commitAllowingStateLoss();

        fragmentList.clear();
    }

    /**
     * 根据Class<?> 显示指定的Fragment，并隐藏其他所有集合中的Fragment
     *
     * @param cla 要显示的 Fragment的Class类
     */
    public void showFragment(Class<?> cla) {
        this.showFragment(cla, null);
    }

    /**
     * 根据Class<?> 显示指定的Fragment，并隐藏其他所有集合中的Fragment
     *
     * @param cla  要显示的 Fragment的Class类
     * @param data 数据
     */
    public void showFragment(Class<?> cla, final Bundle data) {
        if (cla == null || mActivity == null)
            return;

        android.support.v4.app.FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        Fragment showFragment = null;
        for (Fragment fragment : fragmentList) {
            if (fragment.getClass().getName().equals(cla.getName())) {
                showFragment = fragment;
                ft.show(fragment);
            } else {
                ft.hide(fragment);

                final Fragment f = fragment;
                if (f != null && f instanceof OnFragmentHideListener) {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            ((OnFragmentHideListener) f).onHide();

                        }
                    });
                }
            }
        }
        ft.commitAllowingStateLoss();

        if (showFragment != null && showFragment instanceof OnFragmentRefreshListener) {
            final Fragment f = showFragment;
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    ((OnFragmentRefreshListener) f).onRefresh(mActivity, data);

                }
            });
        }
    }

    /**
     * 根据Class<?> 显示指定的Fragment，不做別的操作
     *
     * @param cla 要显示的 Fragment的Class类
     */
    public void showFragmentOnly(Class<?> cla) {
        this.showFragmentOnly(cla, null);
    }

    /**
     * 根据Class<?> 显示指定的Fragment，不做別的操作
     *
     * @param cla  要显示的 Fragment的Class类
     * @param data 数据
     */
    public void showFragmentOnly(Class<?> cla, final Bundle data) {
        if (cla == null || mActivity == null)
            return;

        android.support.v4.app.FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();

        Fragment showFragment = null;
        for (Fragment fragment : fragmentList) {
            if (fragment.getClass().getName().equals(cla.getName())) {
                showFragment = fragment;
                ft.show(fragment);
                break;
            }
        }
        ft.commitAllowingStateLoss();

        if (showFragment != null && showFragment instanceof OnFragmentRefreshListener) {
            final Fragment f = showFragment;
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    ((OnFragmentRefreshListener) f).onRefresh(mActivity, data);

                }
            });
        }
    }

    @Override
    public <T extends Fragment> T getFragment(Class<T> cla) {
        for (Fragment fragment : fragmentList) {
            if (fragment.getClass().getName().equals(cla.getName())) {
                return (T) fragment;
            }
        }
        return null;
    }

}
