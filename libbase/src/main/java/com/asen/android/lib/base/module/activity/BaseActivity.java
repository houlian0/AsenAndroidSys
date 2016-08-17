package com.asen.android.lib.base.module.activity;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.asen.android.lib.base.BaseApplication;
import com.asen.android.lib.base.tool.manage.action.ActionIntent;
import com.asen.android.lib.base.tool.manage.action.BaseAction;
import com.asen.android.lib.base.tool.manage.action.IActionManager;
import com.asen.android.lib.base.tool.manage.action.OperaActionManager;

/**
 * ������Activity������v7����������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * ˢ��ǰһ��ҳ��ķ���ֵ
     */
    public static final int RESULT_REFRESH = 2; // ˢ��ҳ��

    /**
     * ����ǰһ��ҳ��ķ���ֵ
     */
    public static final int RESULT_EXIT = -2; // �˳�

    public Context mContext;

    public BaseApplication mApplication;

    private IActionManager mActionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        mActionManager = new OperaActionManager(this);

        Application application = getApplication();
        if (application instanceof BaseApplication) {
            mApplication = (BaseApplication) application;
            mApplication.addActivity(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mApplication != null)
            mApplication.removeActivity(this);
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
     * ��õ�ǰ��Action
     *
     * @param cls Action��
     * @param <T> ����
     * @return �����ǰ��Action����������࣬�򷵻�Action���󣻷��򷵻�null
     */
    public <T extends BaseAction> T getAction(Class<T> cls) {
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

}
