package com.asen.android.lib.base.tool.util;

import android.graphics.Color;

import java.util.Random;

/**
 * ��ɫ�Ĺ�����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class ColorUtil {

    /**
     * �����ɫ���ĸ�ֵ��˳��ֱ�Ϊ���졢�̡�����͸����
     *
     * @param color ��ɫ��intֵ
     * @return �졢�̡�����͸���� �ĸ�ֵ��int����
     */
    public static int[] colorToRGBA(int color) {
        int[] result = new int[4];

        result[0] = color >> 16 & 0xFF;
        result[1] = color >> 8 & 0xFF;
        result[2] = color & 0xFF;
        result[3] = color >> 24 & 0xFF;

        return result;
    }

    /**
     * �෴����ɫ����͸����ɫ��
     *
     * @param color ԭʼ����ɫֵ
     * @return ת�����ԭɫֵ
     */
    public static int oppositeOpacityColor(int color) {
        int[] rgba = ColorUtil.colorToRGBA(~color);
        return Color.rgb(rgba[0], rgba[1], rgba[2]);
    }

    /**
     * �෴����ɫ������͸���ȣ�
     *
     * @param color ԭʼ����ɫֵ
     * @return ת�����ԭɫֵ
     */
    public static int oppositeColor(int color) {
        int[] rgba = ColorUtil.colorToRGBA(~color);
        return Color.argb(rgba[3], rgba[0], rgba[1], rgba[2]);
    }

    /**
     * ��ȡ�������ɫֵ
     *
     * @return �����ɫֵ������͸����
     */
    public static int getRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return r << 16 & g << 8 & b;
    }
    
}
