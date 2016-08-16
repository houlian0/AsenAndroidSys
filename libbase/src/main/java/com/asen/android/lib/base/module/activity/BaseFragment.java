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
 * ������Fragment�����õ�v4����Fragment
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
     * ��Fragment����onShow���ܻ����Ӧ��ֵ
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

    private boolean isFirstShow = true; // �Ƿ��״�ִ��onShow

    @Override
    public final void onRefresh(Object parent, Bundle data) {
        if (mParentFragment == null && parent instanceof Fragment) {
            mParentFragment = (Fragment) parent;
        }
        if (data != null) {
            isFirstShow = false; // �ı��״�ִ�е�״̬
        }
        onShow(data, isFirstShow);
    }

    /**
     * ����{@link com.asen.android.lib.base.tool.manage.FragmentManager}��ʾFragmentʱִ��
     *
     * @param data    ����Ĳ���
     * @param isFirst Ĭ��isFirst��true�����data��Ϊnullʱ��isFirst�����false
     */
    public void onShow(Bundle data, boolean isFirst) {

    }

    /**
     * ���� {@link com.asen.android.lib.base.tool.manage.FragmentManager}����Fragmentʱִ��
     */
    @Override
    public void onHide() {

    }


    /**
     * ִ��Action
     *
     * @param actionIntent ��ǰ��Action��ͼ
     */
    public void executeIntent(ActionIntent actionIntent) {
        mActionManager.executeIntent(actionIntent);
    }

    /**
     * ���ٵ�ǰ��Action
     */
    public void cancelCurrentIntent() {
        mActionManager.cancelCurrentIntent();
    }

    /**
     * ��õ�ǰFragment��Action
     *
     * @param cls Action��
     * @param <T> ����
     * @return �����ǰFragment��Action����������࣬�򷵻�Action���󣻷��򷵻�null
     */
    public <T extends BaseAction> T getFragmentAction(Class<T> cls) {
        ActionIntent intent = mActionManager.getCurrentIntent();

        if (intent == null) return null;

        try {
            BaseAction action = intent.getAction();
            Class<?> currentCls = action.getClass(); // ��ǰ��ʾ��Action
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
     * ��õ�ǰActivity�ϵ�Action
     *
     * @param cls Action��
     * @param <T> ����
     * @return �����ǰActivity�ϵ�Action����������࣬�򷵻�Action���󣻷��򷵻�null
     */
    public <T extends BaseAction> T getActivityAction(Class<T> cls) {
        if (mActivity instanceof BaseActivity) {
            return ((BaseActivity) mActivity).getAction(cls);
        }
        return null;
    }


}
