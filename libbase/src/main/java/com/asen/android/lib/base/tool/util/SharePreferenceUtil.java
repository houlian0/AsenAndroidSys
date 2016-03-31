package com.asen.android.lib.base.tool.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by HL_SEN on 2015/9/22.
 * 此类所有的保存都采用 getDefaultSharedPreferences
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class SharePreferenceUtil {

    /**
     * 是否包含key值
     *
     * @param context
     * @param key     key值
     * @return
     */
    public static boolean hasKey(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    /**
     * 清除SP数据
     *
     * @param context
     */
    public static void clearPref(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 移除key值
     *
     * @param context
     * @param keys
     */
    public static void removePrefKey(Context context, String... keys) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.commit();
    }

    /**
     * @param context
     * @param key          key值
     * @param defaultValue 不存在时的默认值
     * @return
     */
    public static String getPrefString(Context context, String key, String defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    /**
     * @param context
     * @param key     key值
     * @param value   值
     */
    public static void setPrefString(Context context, String key, String value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).commit();
    }

    /**
     * @param context
     * @param key          key值
     * @param defaultValue 不存在时的默认值
     * @return
     */
    public static boolean getPrefBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }


    /**
     * @param context
     * @param key     key值
     * @param value   值
     */
    public static void setPrefBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).commit();
    }

    /**
     * @param context
     * @param key          key值
     * @param defaultValue 不存在时的默认值
     * @return
     */
    public static int getPrefInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    /**
     * @param context
     * @param key     key值
     * @param value   值
     */
    public static void setPrefInt(Context context, String key, int value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).commit();
    }

    /**
     * @param context
     * @param key          key值
     * @param defaultValue 不存在时的默认值
     * @return
     */
    public static float getPrefFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * @param context
     * @param key     key值
     * @param value   值
     */
    public static void setPrefFloat(Context context, String key, float value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putFloat(key, value).commit();
    }

    /**
     * @param context
     * @param key          key值
     * @param defaultValue 不存在时的默认值
     * @return
     */
    public static long getPrefLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getLong(key, defaultValue);
    }

    /**
     * @param context
     * @param key     key值
     * @param value   值
     */
    public static void setSettingLong(Context context, String key, long value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putLong(key, value).commit();
    }


}
