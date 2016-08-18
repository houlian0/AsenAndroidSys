package com.asen.android.lib.base.tool.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 此类所有的保存都采用 getDefaultSharedPreferences
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class SharePreferenceUtil {

    /**
     * 是否包含key值
     *
     * @param context Android上下文
     * @param key     键值
     * @return 如果包含所传入的键值，返回true；否则返回false
     */
    public static boolean hasKey(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    /**
     * 清除SP数据
     *
     * @param context Android上下文
     */
    public static void clearPref(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 移除key值
     *
     * @param context Android上下文
     * @param keys    键值集合
     */
    public static void removePrefKey(Context context, String... keys) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    /**
     * 获取对应键值的字符串
     *
     * @param context      Android上下文
     * @param key          key值
     * @param defaultValue 不存在时的默认值
     * @return 对应键值的字符串
     */
    public static String getPrefString(Context context, String key, String defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    /**
     * 保存对应键值的字符串
     *
     * @param context Android上下文
     * @param key     key值
     * @param value   值
     */
    public static void setPrefString(Context context, String key, String value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).apply();
    }

    /**
     * 获取对应键值的boolean值
     *
     * @param context      Android上下文
     * @param key          key值
     * @param defaultValue 不存在时的默认值
     * @return 对应键值的boolean值
     */
    public static boolean getPrefBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * 保存对应键值的boolean值
     *
     * @param context Android上下文
     * @param key     key值
     * @param value   值
     */
    public static void setPrefBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).apply();
    }

    /**
     * 获取对应键值的int值
     *
     * @param context      Android上下文
     * @param key          key值
     * @param defaultValue 不存在时的默认值
     * @return 对应键值的int值
     */
    public static int getPrefInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    /**
     * 保存对应键值的int值
     *
     * @param context Android上下文
     * @param key     key值
     * @param value   值
     */
    public static void setPrefInt(Context context, String key, int value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).apply();
    }

    /**
     * 获取对应键值的float值
     *
     * @param context      Android上下文
     * @param key          key值
     * @param defaultValue 不存在时的默认值
     * @return 对应键值的float值
     */
    public static float getPrefFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * 保存对应键值的float值
     *
     * @param context Android上下文
     * @param key     key值
     * @param value   值
     */
    public static void setPrefFloat(Context context, String key, float value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putFloat(key, value).apply();
    }

    /**
     * 获取对应键值的long值
     *
     * @param context      Android上下文
     * @param key          key值
     * @param defaultValue 不存在时的默认值
     * @return 对应键值的long值
     */
    public static long getPrefLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getLong(key, defaultValue);
    }

    /**
     * 保存对应键值的long值
     *
     * @param context Android上下文
     * @param key     key值
     * @param value   值
     */
    public static void setSettingLong(Context context, String key, long value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putLong(key, value).apply();
    }

}
