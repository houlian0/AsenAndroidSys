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
 * APP������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class AppUtil {

    /**
     * ���ΨһID
     *
     * @return uuid
     */
    public static String getUUid() {
        UUID uuid = UUID.randomUUID();
        return String.valueOf(uuid).replace("-", "");
    }

    /**
     * ���Android�豸��IMEI�ţ�DeviceId��IMEI��International Mobile Equipment Identity���ǹ����ƶ��豸��ʶ����д��
     *
     * @param context Android������
     * @return Android�豸��IMEI��
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * ����ֻ����루���ڵ�SIM�����ò����ֻ��ŵģ�
     *
     * @param context Android������
     * @return Android�ֻ���
     */
    public static String getLine1Number(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    /**
     * ����ֻ�SIM����ICCID�ţ�Integrate circuit card identity ���ɵ�·��ʶ���룬�̻����ֻ�SIM���У�
     *
     * @param context Android������
     * @return �ֻ�SIM����ICCID�ţ��ֻ��ŵ�Ψһֵ��
     */
    public static String getSimSerialNumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimSerialNumber();
    }

    /**
     * ����ֻ�SIM����IMSI�� �����ƶ��û�ʶ���루IMSI��International Mobile Subscriber Identification Number��
     *
     * @param context Android������
     * @return �ֻ�SIM����IMSI��
     */
    public static String getSubscriberId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    /**
     * 1s���������򣬲����뵽ָ��Activity
     *
     * @param context     Android������
     * @param activityCls ��Ҫ���´򿪵�Activityҳ����
     */
    public static void exitAndRestart(Context context, Class<?> activityCls) {
        Intent intent = new Intent(context, activityCls);
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT); // FLAG_ACTIVITY_NEW_TASK
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1���Ӻ�����Ӧ��
    }

    /**
     * �������򿪲���װ�ļ�.
     *
     * @param context Android������
     * @param file    apk�ļ�·��
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * ������ж�س���.
     *
     * @param context     Android������
     * @param packageName �������
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    /**
     * ���APP�汾��
     *
     * @param context Android������
     * @return APP�İ汾��
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
     * ���APP�汾����
     *
     * @param context Android������
     * @return APP�İ汾����
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
     * ���APP����
     *
     * @param context Android������
     * @return APP�ĳ������
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
     * ������Դid�����Դ�ļ���
     *
     * @param context Android������
     * @param resId   ��ԴID
     * @return ��Դ�ļ�����
     */
    public static String getResourceNameById(Context context, int resId) {
        return context.getResources().getResourceName(resId);
    }

    /**
     * ������Դ�ļ����ƻ����Դid
     *
     * @param context Android������
     * @param name    ��Դ�ļ�����
     * @param defType ��Դ�ļ����ͣ��磺"drawable"
     * @return ��ԴID
     */
    public static int getResourcesIdByName(Context context, String name, String defType) {
        return context.getResources().getIdentifier(name, defType, context.getPackageName());
    }

    /**
     * �жϵ�ǰ�豸���ֻ�����ƽ�壬�������� Google I/O App for Android
     *
     * @param context Android������
     * @return ƽ�巵�� True���ֻ����� False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * �ж��Ƿ�Ϊƽ��-������Ļ�ĳߴ��ж����������{@link AppUtil#isPad(Context)}������
     *
     * @return Android������
     */
    public static boolean isPad2(Context context) {
        // ����6�ߴ���ΪPad
        return getScreenInches(context) >= 6.0;
    }

    /**
     * ������ƽ�廹���ֻ����л���ʾ״̬����������������
     * �����ƽ��Ļ�����Ϊ������������ֻ��Ļ�����Ϊ����
     *
     * @param activity ��ǰ��ʾ��ҳ��
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
     * �жϵ�ǰ����Ļ�Ƿ�����
     *
     * @param activity ��ǰ��ʾ��ҳ��
     * @return Ϊ�����Ļ�������true�����򷵻�false
     */
    public static boolean isPortrait(Activity activity) {
        return activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    /**
     * ����豸�ߴ�
     *
     * @param context Android������
     * @return �豸�ߴ�(��λӢ��)
     */
    public static double getScreenInches(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        return Math.sqrt(x + y);
    }

    /**
     * ����ֻ�wifi��mac��ַ
     *
     * @param context     Android������
     * @param isLowerCase �Ƿ�Сд�ַ��������true��Сд��false���д
     * @return ����Mac��ַ
     */
    public String getLocalMacAddress(Context context, boolean isLowerCase) {
        String macAddress = "";
        if (Version.hasMarshmallow()) {
            // ��"/sys/class/net/wlan0/address"�ļ��л�ȡMac��ַ��Ϣ
            try {
                String str = "";
                Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
                InputStreamReader ir = new InputStreamReader(pp.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                for (; null != str; ) {
                    str = input.readLine();
                    if (str != null) {
                        macAddress = str.trim();// ȥ�ո�
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if ("".equals(macAddress)) {
                // ��"/sys/class/net/eth0/address"�ļ��л�ȡMac��ַ��Ϣ
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
     * �����жϷ����Ƿ�����.
     *
     * @param ctx       Android������
     * @param className ���������
     * @return true �����У�false ��������
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
     * ֹͣ����
     *
     * @param ctx       Android��������
     * @param className ���������
     * @return ����ɹ�ֹͣ�����򷵻�true�����򷵻�false
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
     * ͨ��ϵͳ�ļ�"/sys/devices/system/cpu"·���µ����ݣ���ѯcpu�ĸ�����Ϣ
     *
     * @return ��ѯCPU�ĸ��������ʧ�ܵĻ�������1
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
     * �������ж������Ƿ�����
     *
     * @param context Android������
     * @return ���������ã�����true�����򷵻�false
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
     * �ж�Gps�Ƿ�� ��Ҫ android.permission.ACCESS_FINE_LOCATION Ȩ��
     *
     * @param context Android������
     * @return ���GPS��λ���ã�����true�����򷵻�false
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * �ж��豸�Ƿ���GPS��λ���ܣ��Ƿ������Ӧ��Ӳ����ʩ��
     *
     * @param context Android������
     * @return �������GPS��λ���ܣ�����true�����򷵻�false
     */
    public static boolean hasGPSDevice(Context context) {
        LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * �ж��豸�Ƿ������綨λ���ܣ��Ƿ������Ӧ��Ӳ����ʩ��
     *
     * @param context Android������
     * @return ����������綨λ���ܣ�����true�����򷵻�false
     */
    public static boolean hasNetworkDevice(Context context) {
        LocationManager mgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * wifi�Ƿ��
     *
     * @param context Android������
     * @return ���wifi�򿪵�������򷵻�true�����򷵻�false
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * �жϵ�ǰ�����Ƿ���wifi����
     *
     * @param context Android������
     * @return �����ǰ��������wifi�����������true�����򷵻�false
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
     * �жϵ�ǰ�����Ƿ����ƶ�����
     *
     * @param context Android������
     * @return �����ǰ���������ƶ���������������true�����򷵻�false
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
     * �������
     *
     * @param context Android������
     */
    public static void showSoftInput(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * �ر������
     *
     * @param activity Android�Ĵ����֮һ��Activity
     */
    public static void closeSoftInput(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * ��ȡ��Ļ�ߴ����ܶȵ���Ϣ
     *
     * @param context Android������
     * @return ��Ļ�ߴ����ܶȵ���Ϣ
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = mWindowManager.getDefaultDisplay();
        DisplayMetrics outSize = new DisplayMetrics();
        display.getMetrics(outSize);
        return outSize;
    }

}