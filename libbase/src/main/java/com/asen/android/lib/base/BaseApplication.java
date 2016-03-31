package com.asen.android.lib.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.asen.android.lib.base.tool.singleton.CrashHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by ASEN on 2016/3/31.
 * 基础的Application
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 15:12
 */
public abstract class BaseApplication extends Application {

    protected Context mContext;

    private List<Activity> list = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(mContext);
        crashHandler.setOcel(new CrashHandler.OnCaughtExceptionListener() {

            public void onCaughtException() {
                abnormalExit();
            }

        });
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     *
     * @param a
     */
    public void removeActivity(Activity a) {
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     *
     * @param a
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
     */
    protected abstract void abnormalExit();

}

