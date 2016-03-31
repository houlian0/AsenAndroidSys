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
 * Created by ASEN on 2016/3/31.
 * 采用的v4包的Fragment
 *
 * @author ASEN
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

    @Override
    public final void onRefresh(Object parent, Bundle data) {
        if (mParentFragment == null && parent instanceof Fragment) {
            mParentFragment = (Fragment) parent;
        }
        onShow(data);
    }

    public void onShow(Bundle data) {

    }

    @Override
    public void onHide() {

    }

    /**
     * 执行Action
     *
     * @param actionIntent
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
     * 获得当前Fragment上的Action
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends BaseAction> T getFragmentAction(Class<T> cls) {
        ActionIntent intent = mActionManager.getCurrentIntent();

        if (intent == null) return null;
        try {
            BaseAction action = intent.getAction();
            if (action.getClass().getName().equals(cls.getName())) {
                return (T) action;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获得当前Activity上的Action
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T extends BaseAction> T getActivityAction(Class<T> cls) {
        if (mActivity instanceof BaseActivity) {
            return ((BaseActivity) mActivity).getAction(cls);
        }
        return null;
    }


}
