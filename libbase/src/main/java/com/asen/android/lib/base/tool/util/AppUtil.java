package com.asen.android.lib.base.tool.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * APP工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class AppUtil {

    /**
     * 获得唯一ID
     *
     * @return uuid
     */
    public static String getUUid() {
        UUID uuid = UUID.randomUUID();
        return String.valueOf(uuid).replace("-", "");
    }

    /**
     * 获得Android设备的IMEI号（DeviceId：IMEI（International Mobile Equipment Identity）是国际移动设备标识的缩写）
     *
     * @param context Android上下文
     * @return Android设备的IMEI号
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 获得手机号码（现在的SIM卡是拿不到手机号的）
     *
     * @param context Android上下文
     * @return Android手机号
     */
    public static String getLine1Number(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    /**
     * 获得手机SIM卡的ICCID号（Integrate circuit card identity 集成电路卡识别码，固化在手机SIM卡中）
     *
     * @param context Android上下文
     * @return 手机SIM卡的ICCID号（手机号的唯一值）
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();
    }

    /**
     * 获得手机SIM卡的IMSI号 国际移动用户识别码（IMSI：International Mobile Subscriber Identification Number）
     *
     * @param context Android上下文
     * @return 手机SIM卡的IMSI号
     */
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    /**
     * 1s后重启程序，并进入到指定Activity
     *
     * @param context     Android上下文
     * @param activityCls 需要重新打开的Activity页面类
     */
    public static void exitAndRestart(Context context, Class<?> activityCls) {
        Intent intent = new Intent(context, activityCls);
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT); // FLAG_ACTIVITY_NEW_TASK
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
    }

    /**
     * 描述：打开并安装文件.
     *
     * @param context Android上下文
     * @param file    apk文件路径
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 描述：卸载程序.
     *
     * @param context     Android上下文
     * @param packageName 程序包名
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * 获得APP版本号
     *
     * @param context Android上下文
     * @return APP的版本号
     */
    public static int getAppVersionCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 获得APP版本名称
     *
     * @param context Android上下文
     * @return APP的版本名称
     */
    public static String getAppVersionName(Context context) {
        String verCode = null;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    /**
     * 获得APP包名
     *
     * @param context Android上下文
     * @return APP的程序包名
     */
    public static String getAppPackageName(Context context) {
        String pkName = null;
        try {
            pkName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pkName;
    }

    /**
     * 根据资源id获得资源文件名
     *
     * @param context Android上下文
     * @param resId   资源ID
     * @return 资源文件名称
     */
    public static String getResourceNameById(Context context, int resId) {
        return context.getResources().getResourceName(resId);
    }

    /**
     * 根据资源文件名称获得资源id
     *
     * @param context Android上下文
     * @param name    资源文件名称
     * @param defType 资源文件类型，如："drawable"
     * @return 资源ID
     */
    public static int getResourcesIdByName(Context context, String name, String defType) {
        return context.getResources().getIdentifier(name, defType, context.getPackageName());
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context Android上下文
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 判断是否为平板-根据屏幕的尺寸判定（建议采用{@link AppUtil#isPad(Context)}方法）
     *
     * @return Android上下文
     */
    public static boolean isPad2(Context context) {
        // 大于6尺寸则为Pad
        return getScreenInches(context) >= 6.0;
    }

    /**
     * 根据是平板还是手机，切换显示状态（横屏还是竖屏）
     * 如果是平板的话，设为横屏；如果是手机的话，设为竖屏
     *
     * @param activity 当前显示的页面
     */
    public static void initScreenOrientation(Activity activity) {
        if (isPad(activity.getApplicationContext())) {
            if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else {
            if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    /**
     * 判断当前的屏幕是否竖屏
     *
     * @param activity 当前显示的页面
     * @return 为竖屏的话，返回true；否则返回false
     */
    public static boolean isPortrait(Activity activity) {
        return activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    /**
     * 获得设备尺寸
     *
     * @param context Android上下文
     * @return 设备尺寸(单位英寸)
     */
    public static double getScreenInches(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        return Math.sqrt(x + y);
    }

    /**
     * 获得手机wifi的mac地址
     *
     * @param context     Android上下文
     * @param isLowerCase 是否小写字符串输出。true则小写，false则大写
     * @return 返回Mac地址
     */
    public String getLocalMacAddress(Context context, boolean isLowerCase) {
        String macAddress = "";
        if (Version.hasMarshmallow()) {
            // 从"/sys/class/net/wlan0/address"文件中获取Mac地址信息
            try {
                String str = "";
                Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
                InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                for (; null != str; ) {
                    str = input.readLine();
                    if (str != null) {
                        macAddress = str.trim();// 去空格
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if ("".equals(macAddress)) {
                // 从"/sys/class/net/eth0/address"文件中获取Mac地址信息
                try {
                    FileReader reader = new FileReader("/sys/class/net/eth0/address");
                    StringBuilder builder = new StringBuilder();
                    char[] buffer = new char[4096];
                    int readLength = reader.read(buffer);
                    while (readLength >= 0) {
                        builder.append(buffer, 0, readLength);
                        readLength = reader.read(buffer);
                    }
                    reader.close();
                    macAddress = builder.toString().trim().substring(0, 17);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if (info != null)
                macAddress = info.getMacAddress();
        }
        return isLowerCase ? macAddress.toLowerCase() : macAddress.toUpperCase();
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param ctx       Android上下文
     * @param className 服务的类名
     * @return true 在运行，false 不在运行
     */
    public static boolean isServiceRunning(Context ctx, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 停止服务
     *
     * @param ctx       Android的上下文
     * @param className 服务的类型
     * @return 如果成功停止服务，则返回true，否则返回false
     */
    public static boolean stopRunningService(Context ctx, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(ctx, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = ctx.stopService(intent_service);
        }
        return ret;
    }

    /**
     * 通过系统文件"/sys/devices/system/cpu"路径下的内容，查询cpu的个数信息
     *
     * @return 查询CPU的个数，如果失败的话，返回1
     */
    public static int getNumCores() {
        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    // Check if filename is "cpu", followed by a single digit number
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }
            });
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            return 1;
        }
    }

    /**
     * 描述：判断网络是否连接
     *
     * @param context Android上下文
     * @return 如果网络可用，返回true；否则返回false
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 判断Gps是否打开 需要 android.permission.ACCESS_FINE_LOCATION 权限
     *
     * @param context Android上下文
     * @return 如果GPS定位可用，返回true；否则返回false
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断设备是否含有GPS定位功能（是否存在相应的硬件设施）
     *
     * @param context Android上下文
     * @return 如果包含GPS定位功能，返回true；否则返回false
     */
    public static boolean hasGPSDevice(Context context) {
        LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断设备是否含有网络定位功能（是否存在相应的硬件设施）
     *
     * @param context Android上下文
     * @return 如果包含网络定位功能，返回true；否则返回false
     */
    public static boolean hasNetworkDevice(Context context) {
        LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * wifi是否打开
     *
     * @param context Android上下文
     * @return 如果wifi打开的情况，则返回true；否则返回false
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断当前网络是否是wifi网络
     *
     * @param context Android上下文
     * @return 如果当前的网络是wifi的情况，返回true；否则返回false
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 判断当前网络是否是移动网络
     *
     * @param context Android上下文
     * @return 如果当前的网络是移动网络的情况，返回true；否则返回false
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 打开软键盘
     *
     * @param context Android上下文
     */
    public static void showSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 关闭软键盘
     *
     * @param activity Android四大组件之一的Activity
     */
    public static void closeSoftInput(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取屏幕尺寸与密度等信息
     *
     * @param context Android上下文
     * @return 屏幕尺寸与密度等信息
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        DisplayMetrics outSize = new DisplayMetrics();
        display.getMetrics(outSize);
        return outSize;
    }

}