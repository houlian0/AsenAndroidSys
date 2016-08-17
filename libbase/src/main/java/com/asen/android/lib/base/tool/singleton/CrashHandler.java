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
 * �������Ĵ�����־������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:19
 */
public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = CrashHandler.class.getSimpleName();

    //ϵͳĬ�ϵ�UncaughtException������
    private UncaughtExceptionHandler mDefaultHandler;
    //CrashHandlerʵ��
    private static volatile CrashHandler c = null;
    //�����Context����
    private Context mContext;
    //�����洢�豸��Ϣ���쳣��Ϣ
    private Map<String, String> infos = new HashMap<>();
    // �����쳣�˳�ʱ����
    private OnCaughtExceptionListener ocel = null;

    //���ڸ�ʽ������,��Ϊ��־�ļ�����һ����
    @SuppressLint("SimpleDateFormat")
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    /**
     * ��ֻ֤��һ��CrashHandlerʵ��
     */
    private CrashHandler() {
    }

    /**
     * ��ȡCrashHandlerʵ�� ,����ģʽ
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
     * ��ʼ����Ϣ
     *
     * @param context Android������
     */
    public void init(Context context) {
        mContext = context;
        //��ȡϵͳĬ�ϵ�UncaughtException������
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //���ø�CrashHandlerΪ�����Ĭ�ϴ�����
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * ��UncaughtException����ʱ��ת��ú���������
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //����û�û�д�������ϵͳĬ�ϵ��쳣������������
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            } finally {
                Log.e(TAG, "error : ", ex);
            }
            //�˳�����
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
     * ���ó������ʱ�ļ�������
     *
     * @param ocel ������ʵ��
     */
    public void setOnCaughtExceptionListener(OnCaughtExceptionListener ocel) {
        this.ocel = ocel;
    }

    /**
     * ��������ʱ�Ľӿڶ���
     */
    public interface OnCaughtExceptionListener {

        /**
         * �������ʱ����
         */
        public boolean onCaughtException();
    }

    // �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //ʹ��Toast����ʾ�쳣��Ϣ
        if (AppData.DEBUG) {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    Toast.makeText(mContext, "�ܱ�Ǹ����������쳣�������˳�.", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();
        }
        //�ռ��豸������Ϣ
        collectDeviceInfo(mContext);
        //������־�ļ�
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * �ռ��豸������Ϣ
     *
     * @param ctx Android������
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
     * ���������Ϣ���ļ���
     *
     * @param ex Android������
     * @return �����ļ�����, ���ڽ��ļ����͵�������
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
