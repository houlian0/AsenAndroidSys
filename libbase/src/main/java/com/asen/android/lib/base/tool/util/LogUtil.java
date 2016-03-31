package com.asen.android.lib.base.tool.util;

import android.util.Log;

import com.asen.android.lib.base.global.AppData;

/**
 * Simple to Introduction
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class LogUtil {

    private static final String TAG = LogUtil.class.getSimpleName();

    public static void v(String tag, String msg) {
        if (AppData.DEBUG) Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (AppData.DEBUG) Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (AppData.DEBUG) Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (AppData.DEBUG) Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (AppData.DEBUG) Log.e(tag, msg);
    }

    public static void println(String msg) {
        if (AppData.DEBUG) Log.i(TAG, msg);
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (AppData.DEBUG) Log.v(tag, msg, tr);
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (AppData.DEBUG) Log.d(tag, msg, tr);
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (AppData.DEBUG) Log.i(tag, msg, tr);
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (AppData.DEBUG) Log.w(tag, msg, tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (AppData.DEBUG) Log.e(tag, msg, tr);
    }

    public static void println(String msg, Throwable tr) {
        if (AppData.DEBUG) Log.e(TAG, msg, tr);
    }

}