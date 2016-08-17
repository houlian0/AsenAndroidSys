package com.asen.android.lib.base.tool.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * �������еı��涼���� getDefaultSharedPreferences
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class SharePreferenceUtil {

    /**
     * �Ƿ����keyֵ
     *
     * @param context Android������
     * @param key     ��ֵ
     * @return �������������ļ�ֵ������true�����򷵻�false
     */
    public static boolean hasKey(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    /**
     * ���SP����
     *
     * @param context Android������
     */
    public static void clearPref(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear();
        editor.apply();
    }

    /**
     * �Ƴ�keyֵ
     *
     * @param context Android������
     * @param keys    ��ֵ����
     */
    public static void removePrefKey(Context context, String... keys) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    /**
     * ��ȡ��Ӧ��ֵ���ַ���
     *
     * @param context      Android������
     * @param key          keyֵ
     * @param defaultValue ������ʱ��Ĭ��ֵ
     * @return ��Ӧ��ֵ���ַ���
     */
    public static String getPrefString(Context context, String key, String defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    /**
     * �����Ӧ��ֵ���ַ���
     *
     * @param context Android������
     * @param key     keyֵ
     * @param value   ֵ
     */
    public static void setPrefString(Context context, String key, String value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).apply();
    }

    /**
     * ��ȡ��Ӧ��ֵ��booleanֵ
     *
     * @param context      Android������
     * @param key          keyֵ
     * @param defaultValue ������ʱ��Ĭ��ֵ
     * @return ��Ӧ��ֵ��booleanֵ
     */
    public static boolean getPrefBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * �����Ӧ��ֵ��booleanֵ
     *
     * @param context Android������
     * @param key     keyֵ
     * @param value   ֵ
     */
    public static void setPrefBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).apply();
    }

    /**
     * ��ȡ��Ӧ��ֵ��intֵ
     *
     * @param context      Android������
     * @param key          keyֵ
     * @param defaultValue ������ʱ��Ĭ��ֵ
     * @return ��Ӧ��ֵ��intֵ
     */
    public static int getPrefInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    /**
     * �����Ӧ��ֵ��intֵ
     *
     * @param context Android������
     * @param key     keyֵ
     * @param value   ֵ
     */
    public static void setPrefInt(Context context, String key, int value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).apply();
    }

    /**
     * ��ȡ��Ӧ��ֵ��floatֵ
     *
     * @param context      Android������
     * @param key          keyֵ
     * @param defaultValue ������ʱ��Ĭ��ֵ
     * @return ��Ӧ��ֵ��floatֵ
     */
    public static float getPrefFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * �����Ӧ��ֵ��floatֵ
     *
     * @param context Android������
     * @param key     keyֵ
     * @param value   ֵ
     */
    public static void setPrefFloat(Context context, String key, float value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putFloat(key, value).apply();
    }

    /**
     * ��ȡ��Ӧ��ֵ��longֵ
     *
     * @param context      Android������
     * @param key          keyֵ
     * @param defaultValue ������ʱ��Ĭ��ֵ
     * @return ��Ӧ��ֵ��longֵ
     */
    public static long getPrefLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getLong(key, defaultValue);
    }

    /**
     * �����Ӧ��ֵ��longֵ
     *
     * @param context Android������
     * @param key     keyֵ
     * @param value   ֵ
     */
    public static void setSettingLong(Context context, String key, long value) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putLong(key, value).apply();
    }

}
