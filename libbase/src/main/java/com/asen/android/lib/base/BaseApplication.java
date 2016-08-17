package com.asen.android.lib.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.asen.android.lib.base.tool.singleton.CrashHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * 基础的Application
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
     * Activity关闭时，删除Activity列表中的Activity对象
     *
     * @param a 要移除的activity
     */
    public void removeActivity(Activity a) {
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     *
     * @param a 要添加的activity
     */
    public void addActivity(Activity a) {
        list.add(a);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
    }

    /**
     * 关闭Activity列表中的除最后一个Activity之外的所有Activity
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
     * 程序异常退出时执行
     *
     * @return false，则以系统默认的方式处理异常；true，则不执行系统默认处理异常的方法
     */
    protected boolean abnormalExit() {
        return false;
    }

}

