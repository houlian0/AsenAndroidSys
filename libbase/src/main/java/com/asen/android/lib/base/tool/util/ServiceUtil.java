package com.asen.android.lib.base.tool.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import java.util.List;

/**
 * 服务管理类
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class ServiceUtil {

    /**
     * @param context
     * @param serviceAction
     * @param service
     * @return 如果本次成功打开返回true，如果服务当前处理开启状态则返回false
     * @Title: startService
     * @Description: start方式打开线程
     */
    public static boolean startService(Context context, String serviceAction, Class<?> service) {
        if (!isServiceRunning(context, service.getName())) {
            Intent intent = new Intent(serviceAction);
            intent.setPackage(AppUtil.getAppPackageName(context));
            context.startService(intent);
            return true;
        }
        return false;
    }

    /**
     * @param context
     * @param serviceAction
     * @param service
     * @return 如果本次成功关闭返回true
     * @Title: stopService
     * @Description:
     */
    public static boolean stopService(Context context, String serviceAction, Class<?> service) {
        if (isServiceRunning(context, service.getName())) {
            Intent intent = new Intent(serviceAction);
            intent.setPackage(AppUtil.getAppPackageName(context));
            context.stopService(intent);
            return true;
        }
        return false;
    }

    /**
     * @param context
     * @param serviceAction
     * @param conn
     * @Title: bindService
     * @Description: 绑定式服务
     */
    public static void bindService(Context context, String serviceAction, ServiceConnection conn) {
//        Intent intent = new Intent(serviceAction);
//        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        Intent intent = new Intent(serviceAction);
        intent.setPackage(AppUtil.getAppPackageName(context));
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    /**
     * @param context
     * @param conn
     * @Title: unBindService
     * @Description: 解绑服务
     */
    public static void unBindService(Context context, ServiceConnection conn) {
        context.unbindService(conn);
    }

    private static boolean isServiceRunning(Context ctx, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

}
