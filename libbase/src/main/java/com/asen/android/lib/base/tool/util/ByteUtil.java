package com.asen.android.lib.base.tool.util;

import java.math.BigInteger;

/**
 * �ֽ�ת��������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class ByteUtil {

    /**
     * ת��shortΪbyte
     *
     * @param b     ������ֽ�����
     * @param s     ��Ҫת����short
     * @param index �ֽ������е���ʼλ��
     */
    public static void putShort(byte b[], short s, int index) {
        b[index + 1] = (byte) (s >> 8);
        b[index] = (byte) (s);
    }

    /**
     * ͨ��byte����ȡ��short
     *
     * @param b     ��Ҫȡֵ���ֽ�����
     * @param index �ڼ�λ��ʼȡ
     * @return ȡ����short����
     */
    public static short getShort(byte[] b, int index) {
        return (short) (((b[index + 1] << 8) | b[index] & 0xff));
    }

    /**
     * ת��intΪbyte����
     *
     * @param bb    ������ֽ�����
     * @param x     ��Ҫת����int
     * @param index �ֽ������е���ʼλ��
     */
    public static void putInt(byte[] bb, int x, int index) {
        bb[index + 3] = (byte) (x >> 24);
        bb[index + 2] = (byte) (x >> 16);
        bb[index + 1] = (byte) (x >> 8);
        bb[index] = (byte) (x);
    }

    /**
     * ͨ��byte����ȡ��int
     *
     * @param bb    ��Ҫȡֵ���ֽ�����
     * @param index �ڼ�λ��ʼȡ
     * @return ȡ����int����
     */
    public static int getInt(byte[] bb, int index) {
        return (((bb[index + 3] & 0xff) << 24)
                | ((bb[index + 2] & 0xff) << 16)
                | ((bb[index + 1] & 0xff) << 8)
                | ((bb[index] & 0xff)));
    }

    /**
     * ת��long��Ϊbyte����
     *
     * @param bb    ������ֽ�����
     * @param x     ��Ҫת����long
     * @param index �ֽ������е���ʼλ��
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
     * ͨ��byte����ȡ��long
     *
     * @param bb    ��Ҫȡֵ���ֽ�����
     * @param index �ڼ�λ��ʼȡ
     * @return ȡ����long����
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
     * �ַ����ֽ�ת��
     *
     * @param bb    ������ֽ�����
     * @param ch    ��Ҫת����char
     * @param index �ֽ������е���ʼλ��
     */
    public static void putChar(byte[] bb, char ch, int index) {
        int temp = (int) ch;
        for (int i = 0; i < 2; i++) {
            bb[index + i] = Integer.valueOf(temp & 0xff).byteValue(); // �����λ���������λ
            temp = temp >> 8; // ������8λ
        }
    }

    /**
     * �ֽڵ��ַ�ת��
     *
     * @param b     ��Ҫȡֵ���ֽ�����
     * @param index �ڼ�λ��ʼȡ
     * @return ȡ����char����
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
     * floatת��byte
     *
     * @param bb    ������ֽ�����
     * @param x     ��Ҫת����float
     * @param index �ֽ������е���ʼλ��
     */
    public static void putFloat(byte[] bb, float x, int index) {
        int l = Float.floatToIntBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = Integer.valueOf(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * ͨ��byte����ȡ��float
     *
     * @param b     ��Ҫȡֵ���ֽ�����
     * @param index �ڼ�λ��ʼȡ
     * @return ȡ����float����
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
     * doubleת��byte
     *
     * @param bb    ������ֽ�����
     * @param x     ��Ҫת����double
     * @param index �ֽ������е���ʼλ��
     */
    public static void putDouble(byte[] bb, double x, int index) {
        long l = Double.doubleToLongBits(x);
        for (int i = 0; i < 4; i++) {
            bb[index + i] = Long.valueOf(l).byteValue();
            l = l >> 8;
        }
    }

    /**
     * ͨ��byte����ȡ��float
     *
     * @param b     ��Ҫȡֵ���ֽ�����
     * @param index �ڼ�λ��ʼȡ
     * @return ȡ����double����
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
     * ��byte[]תΪ���ֽ��Ƶ��ַ���
     *
     * @param bytes �ֽ�����
     * @param radix ��������ת�����Ƶķ�Χ����Character.MIN_RADIX��Character.MAX_RADIX��
     *              ������Χ���Ϊ10����
     * @return ת������ַ���
     */
    public static String binary(byte[] bytes, int radix) {
        return new BigInteger(1, bytes).toString(radix);// �����1��������
    }

}
