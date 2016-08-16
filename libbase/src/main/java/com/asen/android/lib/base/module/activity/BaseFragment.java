package com.asen.android.lib.base.module.activity;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.asen.android.lib.base.tool.manage.OnFragmentHideListener;
import com.asen.android.lib.base.tool.manage.OnFragmentRefreshListener;
import com.asen.android.lib.base.tool.manage.action.ActionIntent;
import com.asen.android.lib.base.tool.manage.action.BaseAction;
import com.asen.android.lib.base.tool.manage.action.IActionManager;
import com.asen.android.lib.base.tool.manage.action.OperActionManager;

/**
 * 基本的Fragment，采用的v4包的Fragment
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public class BaseFragment extends Fragment implements OnFragmentHideListener, OnFragmentRefreshListener {

    protected FragmentActivity mActivity;

    protected Context mContext;

    protected Application mApplication;

    private IActionManager mActionManager;

    /**
     * 父Fragment，在onShow中能获得相应的值
     */
    protected Fragment mParentFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = mActivity.getApplicationContext();
        mApplication = mActivity.getApplication();

        mActionManager = new OperActionManager(this);
    }

    private boolean isFirstShow = true; // 是否首次执行onShow

    @Override
    public final void onRefresh(Object parent, Bundle data) {
        if (mParentFragment == null && parent instanceof Fragment) {
            mParentFragment = (Fragment) parent;
        }
        if (data != null) {
            isFirstShow = false; // 改变首次执行的状态
        }
        onShow(data, isFirstShow);
    }

    /**
     * 利用{@link com.asen.android.lib.base.tool.manage.FragmentManager}显示Fragment时执行
     *
     * @param data    传入的参数
     * @param isFirst 默认isFirst是true，如果data不为null时，isFirst被设成false
     */
    public void onShow(Bundle data, boolean isFirst) {

    }

    /**
     * 利用 {@link com.asen.android.lib.base.tool.manage.FragmentManager}隐藏Fragment时执行
     */
    @Override
    public void onHide() {

    }


    /**
     * 执行Action
     *
     * @param actionIntent 当前的Action意图
     */
    public void executeIntent(ActionIntent actionIntent) {
        mActionManager.executeIntent(actionIntent);
    }

    /**
     * 销毁当前的Action
     */
    public void cancelCurrentIntent() {
        mActionManager.cancelCurrentIntent();
    }

    /**
     * 获得当前Fragment的Action
     *
     * @param cls Action类
     * @param <T> 泛型
     * @return 如果当前Fragment的Action是所传入的类，则返回Action对象；否则返回null
     */
    public <T extends BaseAction> T getFragmentAction(Class<T> cls) {
        ActionIntent intent = mActionManager.getCurrentIntent();

        if (intent == null) return null;

        try {
            BaseAction action = intent.getAction();
            Class<?> currentCls = action.getClass(); // 当前显示的Action
            while (currentCls != null) {
                if (currentCls.getName().equals(cls.getName())) {
                    return (T) action;
                }
                currentCls = currentCls.getSuperclass();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获得当前Activity上的Action
     *
     * @param cls Action类
     * @param <T> 泛型
     * @return 如果当前Activity上的Action是所传入的类，则返回Action对象；否则返回null
     */
    public <T extends BaseAction> T getActivityAction(Class<T> cls) {
        if (mActivity instanceof BaseActivity) {
            return ((BaseActivity) mActivity).getAction(cls);
        }
        return null;
    }


}
