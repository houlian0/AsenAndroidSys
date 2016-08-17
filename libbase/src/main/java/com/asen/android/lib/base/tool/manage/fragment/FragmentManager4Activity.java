package com.asen.android.lib.base.tool.manage.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理Fragment的类（基于Activity）
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:25
 */
class FragmentManager4Activity extends FragmentManager {

    private FragmentActivity mActivity;

    private List<Fragment> fragmentList;

    private Handler mHandler = null;

    FragmentManager4Activity(FragmentActivity activity) {
        mActivity = activity;
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

    public void addList(List<Fragment> list, int frameLayoutId) {
        this.addList(list, frameLayoutId, null);
    }

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

    public void show(final Fragment fragment) {
        this.show(fragment, null);
    }

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

    public void showFragment(Class<?> cla) {
        this.showFragment(cla, null);
    }

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

    public void showFragmentOnly(Class<?> cla) {
        this.showFragmentOnly(cla, null);
    }

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
