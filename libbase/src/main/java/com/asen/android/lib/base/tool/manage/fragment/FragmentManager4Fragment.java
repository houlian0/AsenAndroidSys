package com.asen.android.lib.base.tool.manage.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理Fragment的类（基于Fragment）
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
class FragmentManager4Fragment extends FragmentManager {

    private Fragment mParent;

    private ArrayList<Fragment> fragmentList;

    private Handler mHandler = null;

    public FragmentManager4Fragment(Fragment activity) {
        mParent = activity;
        fragmentList = new ArrayList<>();
        mHandler = new Handler();
    }

    @Override
    public List<Fragment> getList() {
        return fragmentList;
    }

    @Override
    public void add(Fragment fragment, int frameLayoutId) {
        this.add(fragment, frameLayoutId, true);
    }

    @Override
    public void add(final Fragment fragment, int frameLayoutId, boolean visible) {
        this.add(fragment, frameLayoutId, visible, null);
    }

    @Override
    public void add(final Fragment fragment, int frameLayoutId, boolean visible, final Bundle data) {
        if (fragment == null || mParent == null)
            return;

        if (!fragmentList.contains(fragment)) {
            FragmentTransaction ft = mParent.getChildFragmentManager().beginTransaction();
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
                    ((OnFragmentRefreshListener) fragment).onRefresh(mParent, data);
                }
            });
        }
    }

    @Override
    public void replace(final Fragment fragment, int frameLayoutId, final Bundle data) {
        if (fragment == null || mParent == null)
            return;

        FragmentTransaction ft = mParent.getChildFragmentManager().beginTransaction();
        ft.replace(frameLayoutId, fragment);
        ft.commitAllowingStateLoss();

        if (fragment instanceof OnFragmentRefreshListener) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((OnFragmentRefreshListener) fragment).onRefresh(mParent, data);
                }
            });
        }
    }

    @Override
    public void addList(List<Fragment> list, int frameLayoutId) {
        this.addList(list, frameLayoutId, null);
    }

    @Override
    public void addList(List<Fragment> list, int frameLayoutId, final Bundle data) {
        if (list == null)
            return;

        this.fragmentList.addAll(list);

        Fragment showFragment = null;
        FragmentTransaction ft = mParent.getChildFragmentManager().beginTransaction();
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
                    ((OnFragmentRefreshListener) f).onRefresh(mParent, data);
                }
            });
        }
    }

    @Override
    public void hide(final Fragment fragment) {
        if (fragment == null || mParent == null)
            return;

        if (fragmentList.contains(fragment)) {
            FragmentTransaction ft = mParent.getChildFragmentManager().beginTransaction();
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

    @Override
    public void hide(Class<?> cla) {
        if (mParent == null) return;

        FragmentTransaction ft = mParent.getChildFragmentManager().beginTransaction();

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

    @Override
    public void show(final Fragment fragment) {
        this.show(fragment, null);
    }

    @Override
    public void show(final Fragment fragment, final Bundle data) {
        if (fragment == null || mParent == null)
            return;

        if (fragmentList.contains(fragment)) {
            FragmentTransaction ft = mParent.getChildFragmentManager().beginTransaction();
            ft.show(fragment);
            ft.commitAllowingStateLoss();
        }

        if (fragment instanceof OnFragmentRefreshListener) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((OnFragmentRefreshListener) fragment).onRefresh(mParent, data);
                }
            });
        }
    }

    @Override
    public void remove(Fragment fragment) {
        if (fragment == null || mParent == null)
            return;

        if (fragmentList.contains(fragment)) {
            fragmentList.remove(fragment);
        }
        FragmentTransaction ft = mParent.getChildFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void removeAll() {
        if (mParent == null)
            return;

        FragmentTransaction ft = mParent.getChildFragmentManager().beginTransaction();
        for (Fragment fragment : fragmentList) {
            ft.remove(fragment);
        }
        ft.commitAllowingStateLoss();

        fragmentList.clear();
    }

    @Override
    public void showFragment(Class<?> cla) {
        this.showFragment(cla, null);
    }

    @Override
    public void showFragment(Class<?> cla, final Bundle data) {
        if (cla == null || mParent == null)
            return;

        android.support.v4.app.FragmentManager fragmentManager = mParent.getChildFragmentManager();
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
                    ((OnFragmentRefreshListener) f).onRefresh(mParent, data);

                }
            });
        }
    }

    @Override
    public void showFragmentOnly(Class<?> cla) {
        this.showFragmentOnly(cla, null);
    }

    @Override
    public void showFragmentOnly(Class<?> cla, final Bundle data) {
        if (cla == null || mParent == null)
            return;

        android.support.v4.app.FragmentManager fragmentManager = mParent.getChildFragmentManager();
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
                    ((OnFragmentRefreshListener) f).onRefresh(mParent, data);

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
