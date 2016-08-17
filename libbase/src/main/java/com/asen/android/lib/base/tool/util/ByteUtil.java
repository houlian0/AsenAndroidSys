package com.asen.android.lib.base.tool.util;

import java.math.BigInteger;

/**
 * 字节转换工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class ByteUtil {

    /**
     * 转换short为byte
     *
     * @param b     输出的字节数组
     * @param s     需要转换的short
     * @param index 字节数组中的起始位置
     */
    public static void putShort(byte b[], short s, int index) {
        b[index + 1] = (byte) (s >> 8);
        b[index] = (byte) (s);
    }

    /**
     * 通过byte数组取到short
     *
     * @param b     需要取值的字节数组
     * @param index 第几位开始取
     * @return 取出的short内容
     */
    public static short getShort(byte[] b, int index) {
        return (short) (((b[index + 1] << 8) | b[index] & 0xff));
    }

    /**
     * 转换int为byte数组
     *
     * @param bb    输出的字节数组
     * @param x     需要转换的int
     * @param index 字节数组中的起始位置
     */
    public static void putInt(byte[] bb, int x, int index) {
        bb[index + 3] = (byte) (x >> 24);
        bb[index + 2] = (byte) (x >> 16);
        bb[index + 1] = (byte) (x >> 8);
        bb[index] = (byte) (x);
    }

    /**
     * 通过byte数组取到int
     *
     * @param bb    需要取值的字节数组
     * @param index 第几位开始取
     * @return 取出的int内容
     */
    public static int getInt(byte[] bb, int index) {
        return (((bb[index + 3] & 0xff) << 24)
                | ((bb[index + 2] & 0xff) << 16)
                | ((bb[index + 1] & 0xff) << 8)
                | ((bb[index] & 0xff)));
    }

    /**
     * 转换long型为byte数组
     *
     * @param bb    输出的字节数组
     * @param x     需要转换的long
     * @param index 字节数组中的起始位置
     */
    public static void putLong(byte[] bb, long x, int index) {
        bb[index + 7] = (byte) (x >> 56);
        bb[index + 6] = (byte) (x >> 48);
        bb[index + 5] = (byte) (x >> 40);
        bb[index + 4] = (byte) (x >> 32);
        bb[index + 3] = (byte) (x >> 24);
        bb[index + 2] = (byte) (x >> 16);
        bb[index + 1] = (byte) (x >> 8);
        bb[index] = (byte) (x);
    }

    /**
     * 通过byte数组取到long
     *
     * @param bb    需要取值的字节数组
     * @param index 第几位开始取
     * @return 取出的long内容
     */
    public static long getLong(byte[] bb, int index) {
        return ((((long) bb[index + 7] & 0xff) << 56)
                | (((long) bb[index + 6] & 0xff) << 48)
                | (((long) bb[index + 5] & 0xff) << 40)
                | (((long) bb[index + 4] & 0xff) << 32)
                | (((long) bb[index + 3] & 0xff) << 24)
                | (((long) bb[index + 2] & 0xff) << 16)
                | (((long) bb[index + 1] & 0xff) << 8)
                | (((long) bb[index] & 0xff)));
    }

    /**
     * 字符到字节转换
     *
     * @param bb    输出的字节数组
     * @param ch    需要转换的char
     * @param index 字节数组中的起始位置
     */
    public static void putChar(byte[] bb, char ch, int index) {
        int temp = (int) ch;
        for (int i = 0; i < 2; i++) {
            bb[index + i] = Integer.valueOf(temp & 0xff).byteValue(); // 将最高位保存在最低位
            temp = temp >> 8; // 向右移8位
        }
    }

    /**
     * 字节到字符转换
     *
     * @param b     需要取值的字节数组
     * @param index 第几位开始取
     * @return 取出的char内容
     */
    public static char getChar(byte[] b, int index) {
        int s = 0;
        if (b[index + 1] > 0)
            s += b[index + 1];
        else
            s += 256 + b[index];
        s *= 256;
        if (b[index] > 0)
            s += b[index + 1];
        else
            s += 256 + b[index];
        return (char) s;
    }

    /**
     * float转换byte
     *
     * @param bb    输出的字节数组
     * @param x     需要转换的float
     * @param index 字节数组中的起始位置
     */
    public static void putFloat(byte[] bb, float x, int index) {
        int l = Float.floatToIntBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = Integer.valueOf(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param b     需要取值的字节数组
     * @param index 第几位开始取
     * @return 取出的float内容
     */
    public static float getFloat(byte[] b, int index) {
        int l;
        l = b[index];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * double转换byte
     *
     * @param bb    输出的字节数组
     * @param x     需要转换的double
     * @param index 字节数组中的起始位置
     */
    public static void putDouble(byte[] bb, double x, int index) {
        long l = Double.doubleToLongBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = Long.valueOf(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * 通过byte数组取得float
     *
     * @param b     需要取值的字节数组
     * @param index 第几位开始取
     * @return 取出的double内容
     */
    public static double getDouble(byte[] b, int index) {
        long l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffL;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffL;
        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffL;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffL;
        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }

    /**
     * 将byte[]转为各种进制的字符串
     *
     * @param bytes 字节数组
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，
     *              超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

}
