package com.asen.android.lib.base.module.activity;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.asen.android.lib.base.BaseApplication;
import com.asen.android.lib.base.tool.manage.action.ActionIntent;
import com.asen.android.lib.base.tool.manage.action.BaseAction;
import com.asen.android.lib.base.tool.manage.action.IActionManager;
import com.asen.android.lib.base.tool.manage.action.OperActionManager;

/**
 * Created by ASEN on 2016/3/31.
 * 基础的Activity，基于v7包的新特性
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:57
 */
public class BaseActivity extends AppCompatActivity {

    /**
     * 刷新前一个页面的返回值
     */
    public static final int RESULT_REFRESH = 2; // 刷新页面

    /**
     * 销毁前一个页面的返回值
     */
    public static final int RESULT_EXIT = -2; // 退出

    public Context mContext;

    public BaseApplication mApplication;

    private IActionManager mActionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        mActionManager = new OperActionManager(this);

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
     * 获得当前的Action
     * @param cls
     * @param <T>
     * @return
     */
    public<T extends BaseAction> T getAction(Class<T> cls) {
        ActionIntent intent = mActionManager.getCurrentIntent();

        if(intent == null) return null;

        try {
            BaseAction action = intent.getAction();
            if(action.getClass().getName().equals(cls.getName())) {
                return (T)action;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
