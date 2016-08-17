package com.asen.android.lib.base.tool.util;

import android.text.TextUtils;

import com.asen.android.lib.base.global.AppData;

import java.util.Date;

/**
 * �ַ��� �� ��������ת�� ������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class ConvertUtil {

    /**
     * ��String����ת��byte�ֽ�
     *
     * @param str ��ת�����ַ���
     * @return ����byte��Ĭ��ֵ0
     */
    public static byte stringToByte(String str) {
        return stringToByte(str, (byte) 0);
    }

    /**
     * ��String����ת��byte�ֽ�
     *
     * @param str         ��ת�����ַ���
     * @param defaultData Ĭ��ֵ
     * @return ����byte
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
     * ��String����ת��short
     *
     * @param str ��ת�����ַ���
     * @return ����short ��Ĭ��ֵ0
     */
    public static short stringToShort(String str) {
        return stringToShort(str, (short) 0);
    }

    /**
     * ��String����ת��short
     *
     * @param str         ��ת�����ַ���
     * @param defaultData Ĭ��ֵ
     * @return ����short
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
     * ��String����ת��int
     *
     * @param str ��ת�����ַ���
     * @return ����int��Ĭ��ֵ0
     */
    public static int stringToInt(String str) {
        return stringToInt(str, 0);
    }

    /**
     * ��String����ת��int
     *
     * @param str         ��ת�����ַ���
     * @param defaultData Ĭ��ֵ
     * @return ����int
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
     * ��String����ת��long
     *
     * @param str ��ת�����ַ���
     * @return ����long��Ĭ��ֵ0
     */
    public static long stringToLong(String str) {
        return stringToLong(str, 0);
    }

    /**
     * ��String����ת��long
     *
     * @param str         ��ת�����ַ���
     * @param defaultData Ĭ��ֵ
     * @return ����long
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
     * ��String����ת��float
     *
     * @param str ��ת�����ַ���
     * @return ����float��Ĭ��ֵ0
     */
    public static float stringToFloat(String str) {
        return stringToFloat(str, 0);
    }

    /**
     * ��String����ת��float
     *
     * @param str         ��ת�����ַ���
     * @param defaultData Ĭ��ֵ
     * @return ����float
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
     * ��String����ת��double
     *
     * @param str ��ת�����ַ���
     * @return ����double��Ĭ��ֵ0
     */
    public static double stringToDouble(String str) {
        return stringToDouble(str, 0);
    }

    /**
     * ��String����ת��double
     *
     * @param str         ��ת�����ַ���
     * @param defaultData Ĭ��ֵ
     * @return ����double
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
     * ��String����ת��boolean
     *
     * @param str ��ת�����ַ���
     * @return ����boolean��Ĭ��ֵfalse
     */
    public static boolean stringToBoolean(String str) {
        return stringToBoolean(str, false);
    }

    /**
     * ��String����ת��boolean
     *
     * @param str         ��ת�����ַ���
     * @param defaultData Ĭ��ֵ
     * @return ����boolean
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
     * ��String����ת��char
     *
     * @param str ��ת�����ַ���
     * @return ����char��Ĭ��ֵ0
     */
    public static char stringToChar(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        } else {
            return str.charAt(0);
        }
    }

    /**
     * ��String����ת��char
     *
     * @param str         ��ת�����ַ���
     * @param defaultData Ĭ��ֵ
     * @return ����char
     */
    public static char stringToChar(String str, char defaultData) {
        if (TextUtils.isEmpty(str)) {
            return defaultData;
        } else {
            return str.charAt(0);
        }
    }

    /**
     * ��String����ת��long����ֵ
     *
     * @param str ��ת�����ַ���
     * @return ����long����ֵ��Ĭ��ֵ0
     */
    public static long stringToDateTime(String str) {
        return stringToDateTime(str, 0);
    }

    /**
     * ��String����ת��long����ֵ
     *
     * @param str         ��ת�����ַ���
     * @param defaultData Ĭ��ֵ
     * @return ����long����ֵ
     */
    public static long stringToDateTime(String str, long defaultData) {
        return stringToLong(str, defaultData);
    }

    /**
     * ��String����ת��Date
     *
     * @param str ��ת�����ַ���
     * @return ����Date��Ĭ��ֵ new Date(0)
     */
    public static Date stringToDate(String str) {
        return stringToDate(str, new Date(0));
    }

    /**
     * ��String����ת��Date
     *
     * @param str         ��ת�����ַ���
     * @param defaultData Ĭ��ֵ
     * @return ����Date
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