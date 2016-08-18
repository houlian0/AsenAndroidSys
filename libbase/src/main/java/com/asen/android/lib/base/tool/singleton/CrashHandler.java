package com.asen.android.lib.base.tool.singleton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.asen.android.lib.base.global.AppData;
import com.asen.android.lib.base.global.AppPath;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 程序奔溃的错误日志捕获类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = CrashHandler.class.getSimpleName();

    //系统默认的UncaughtException处理类
    private UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static volatile CrashHandler c = null;
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private Map<String, String> infos = new HashMap<>();
    // 程序异常退出时监听
    private OnCaughtExceptionListener ocel = null;

    //用于格式化日期,作为日志文件名的一部分
    @SuppressLint("SimpleDateFormat")
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        if (null == c) {
            synchronized (CrashHandler.class) {
                if (null == c) {
                    c = new CrashHandler();
                }
            }
        }
        return c;
    }

    /**
     * 初始化信息
     *
     * @param context Android上下文
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            } finally {
                Log.e(TAG, "error : ", ex);
            }
            //退出程序
            if (ocel != null) {
                boolean result = ocel.onCaughtException();
                if (!result) {
                    mDefaultHandler.uncaughtException(thread, ex);
                }
            } else {
                mDefaultHandler.uncaughtException(thread, ex);
            }
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(1);
        }
    }

    /**
     * 设置程序崩溃时的监听处理
     *
     * @param ocel 监听的实现
     */
    public void setOnCaughtExceptionListener(OnCaughtExceptionListener ocel) {
        this.ocel = ocel;
    }

    /**
     * 监听崩溃时的接口定义
     */
    public interface OnCaughtExceptionListener {

        /**
         * 程序崩溃时调用
         */
        public boolean onCaughtException();
    }

    // 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        if (AppData.DEBUG) {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出.", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
        }
        //收集设备参数信息
        collectDeviceInfo(mContext);
        //保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx Android上下文
     */
    void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Log.d(TAG, field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info", e);
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex Android上下文
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    String saveCrashInfo2File(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = formatter.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File dir = AppPath.getAppErrorFile(mContext);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                if (dir.exists() && dir.canWrite()) {
                    FileOutputStream fos = new FileOutputStream(new File(dir.getAbsolutePath(), fileName));
                    fos.write(sb.toString().getBytes());
                    fos.close();
                } else {
                    return null;
                }
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }
}
