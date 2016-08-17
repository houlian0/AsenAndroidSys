package com.asen.android.lib.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.asen.android.lib.base.tool.singleton.CrashHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * ������Application
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 15:12
 */
public abstract class BaseApplication extends Application {

    protected Context mContext;

    private List<Activity> list = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(mContext);
        crashHandler.setOnCaughtExceptionListener(new CrashHandler.OnCaughtExceptionListener() {
            public boolean onCaughtException() {
                return abnormalExit();
            }
        });
    }

    /**
     * Activity�ر�ʱ��ɾ��Activity�б��е�Activity����
     *
     * @param a Ҫ�Ƴ���activity
     */
    public void removeActivity(Activity a) {
        list.remove(a);
    }

    /**
     * ��Activity�б������Activity����
     *
     * @param a Ҫ��ӵ�activity
     */
    public void addActivity(Activity a) {
        list.add(a);
    }

    /**
     * �ر�Activity�б��е�����Activity
     */
    public void finishActivity() {
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
    }

    /**
     * �ر�Activity�б��еĳ����һ��Activity֮�������Activity
     */
    public void finishActivityWithoutLastActivity() {
        ListIterator<Activity> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (listIterator.nextIndex() == list.size() - 1) {
                return;
            }
            Activity next = listIterator.next();
            next.finish();
            listIterator.remove();
        }

    }

    /**
     * �����쳣�˳�ʱִ��
     *
     * @return false������ϵͳĬ�ϵķ�ʽ�����쳣��true����ִ��ϵͳĬ�ϴ����쳣�ķ���
     */
    protected boolean abnormalExit() {
        return false;
    }

}

