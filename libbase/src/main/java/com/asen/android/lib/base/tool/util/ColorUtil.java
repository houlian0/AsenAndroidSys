package com.asen.android.lib.base.tool.util;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by HL_SEN on 2015/9/21.
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class ColorUtil {

    /**
     * 拆分颜色的四个值
     *
     * @param color
     * @return 红、绿、蓝、透明度 四个值得int数组
     */
    public static int[] colorToRGBA(int color) {
        int[] result = new int[4];

        result[0] = color >> 16 & 0xFF;
        result[1] = color >> 8 & 0xFF;
        result[2] = color >> 0 & 0xFF;
        result[3] = color >> 24 & 0xFF;

        return result;
    }

    /**
     * 相反的颜色（不透明的色）
     *
     * @param color
     * @return
     */
    public static int oppositeOpacityColor(int color) {
        int[] rgba = ColorUtil.colorToRGBA(~color);
        return Color.rgb(rgba[0], rgba[1], rgba[2]);
    }

    /**
     * 相反的颜色（包括透明度）
     *
     * @param color
     * @return
     */
    public static int oppositeColor(int color) {
        int[] rgba = ColorUtil.colorToRGBA(~color);
        return Color.argb(rgba[3], rgba[0], rgba[1], rgba[2]);
    }

    /**
     * 获取随机的颜色值
     *
     * @return 随机颜色值，不含透明度
     */
    public static int getRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return r << 16 & g << 8 & b;
    }
}
