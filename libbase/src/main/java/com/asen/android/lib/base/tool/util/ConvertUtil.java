package com.asen.android.lib.base.tool.util;

import android.text.TextUtils;

import com.asen.android.lib.base.global.AppData;

import java.util.Date;

/**
 * Simple to Introduction
 * 字符串 与 基本类型转换 工具类
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class ConvertUtil {

    /**
     * 将String类型转成byte字节
     *
     * @param str 待转换的字符串
     * @return 返回byte，默认值0
     */
    public static byte stringToByte(String str) {
        return stringToByte(str, (byte) 0);
    }

    /**
     * 将String类型转成byte字节
     *
     * @param str         待转换的字符串
     * @param defaultData 默认值
     * @return 返回byte
     */
    public static byte stringToByte(String str, byte defaultData) {
        try {
            return Byte.valueOf(str);
        } catch (NumberFormatException e) {
            if (AppData.DEBUG) e.printStackTrace();
            return defaultData;
        }
    }

    /**
     * 将String类型转成short
     *
     * @param str 待转换的字符串
     * @return 返回short ，默认值0
     */
    public static short stringToShort(String str) {
        return stringToShort(str, (short) 0);
    }

    /**
     * 将String类型转成short
     *
     * @param str         待转换的字符串
     * @param defaultData 默认值
     * @return 返回short
     */
    public static short stringToShort(String str, short defaultData) {
        try {
            return Short.valueOf(str);
        } catch (NumberFormatException e) {
            if (AppData.DEBUG) e.printStackTrace();
            return defaultData;
        }
    }

    /**
     * 将String类型转成int
     *
     * @param str 待转换的字符串
     * @return 返回int，默认值0
     */
    public static int stringToInt(String str) {
        return stringToInt(str, 0);
    }

    /**
     * 将String类型转成int
     *
     * @param str         待转换的字符串
     * @param defaultData 默认值
     * @return 返回int
     */
    public static int stringToInt(String str, int defaultData) {
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            if (AppData.DEBUG) e.printStackTrace();
            return defaultData;
        }
    }

    /**
     * 将String类型转成long
     *
     * @param str 待转换的字符串
     * @return 返回long，默认值0
     */
    public static long stringToLong(String str) {
        return stringToLong(str, 0);
    }

    /**
     * 将String类型转成long
     *
     * @param str         待转换的字符串
     * @param defaultData 默认值
     * @return 返回long
     */
    public static long stringToLong(String str, long defaultData) {
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            if (AppData.DEBUG) e.printStackTrace();
            return defaultData;
        }
    }

    /**
     * 将String类型转成float
     *
     * @param str 待转换的字符串
     * @return 返回float，默认值0
     */
    public static float stringToFloat(String str) {
        return stringToFloat(str, 0);
    }

    /**
     * 将String类型转成float
     *
     * @param str         待转换的字符串
     * @param defaultData 默认值
     * @return 返回float
     */
    public static float stringToFloat(String str, float defaultData) {
        try {
            return Float.valueOf(str);
        } catch (NumberFormatException e) {
            if (AppData.DEBUG) e.printStackTrace();
            return defaultData;
        }
    }

    /**
     * 将String类型转成double
     *
     * @param str 待转换的字符串
     * @return 返回double，默认值0
     */
    public static double stringToDouble(String str) {
        return stringToDouble(str, 0);
    }

    /**
     * 将String类型转成double
     *
     * @param str         待转换的字符串
     * @param defaultData 默认值
     * @return 返回double
     */
    public static double stringToDouble(String str, double defaultData) {
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            if (AppData.DEBUG) e.printStackTrace();
            return defaultData;
        }
    }

    /**
     * 将String类型转成boolean
     *
     * @param str 待转换的字符串
     * @return 返回boolean，默认值false
     */
    public static boolean stringToBoolean(String str) {
        return stringToBoolean(str, false);
    }

    /**
     * 将String类型转成boolean
     *
     * @param str         待转换的字符串
     * @param defaultData 默认值
     * @return 返回boolean
     */
    public static boolean stringToBoolean(String str, boolean defaultData) {
        try {
            return Boolean.valueOf(str);
        } catch (NumberFormatException e) {
            if (AppData.DEBUG) e.printStackTrace();
            return defaultData;
        }
    }

    /**
     * 将String类型转成char
     *
     * @param str 待转换的字符串
     * @return 返回char，默认值0
     */
    public static char stringToChar(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        } else {
            return str.charAt(0);
        }
    }

    /**
     * 将String类型转成char
     *
     * @param str         待转换的字符串
     * @param defaultData 默认值
     * @return 返回char
     */
    public static char stringToChar(String str, char defaultData) {
        if (TextUtils.isEmpty(str)) {
            return defaultData;
        } else {
            return str.charAt(0);
        }
    }

    /**
     * 将String类型转成long毫秒值
     *
     * @param str 待转换的字符串
     * @return 返回long毫秒值，默认值0
     */
    public static long stringToDateTime(String str) {
        return stringToDateTime(str, 0);
    }

    /**
     * 将String类型转成long毫秒值
     *
     * @param str         待转换的字符串
     * @param defaultData 默认值
     * @return 返回long毫秒值
     */
    public static long stringToDateTime(String str, long defaultData) {
        return stringToLong(str, defaultData);
    }

    /**
     * 将String类型转成Date
     *
     * @param str 待转换的字符串
     * @return 返回Date，默认值 new Date(0)
     */
    public static Date stringToDate(String str) {
        return stringToDate(str, new Date(0));
    }

    /**
     * 将String类型转成Date
     *
     * @param str         待转换的字符串
     * @param defaultData 默认值
     * @return 返回Date
     */
    public static Date stringToDate(String str, Date defaultData) {
        try {
            return new Date(Long.valueOf(str));
        } catch (NumberFormatException e) {
            if (AppData.DEBUG) e.printStackTrace();
            return defaultData;
        }
    }

}