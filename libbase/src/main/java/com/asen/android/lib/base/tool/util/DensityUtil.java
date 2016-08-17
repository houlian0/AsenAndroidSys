package com.asen.android.lib.base.tool.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * �������ܶ���صĹ�����
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class DensityUtil {

    /**
     * ������dipת��Ϊpx.
     *
     * @param context  the context
     * @param dipValue the dip value
     * @return pxֵ
     */
    public static float dip2px(Context context, float dipValue) {
        DisplayMetrics mDisplayMetrics = AppUtil.getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, mDisplayMetrics);
    }

    /**
     * ������pxת��Ϊdip.
     *
     * @param context the context
     * @param pxValue the px value
     * @return dipֵ
     */
    public static float px2dip(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = AppUtil.getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.density;
    }

    /**
     * ������spת��Ϊpx.
     *
     * @param context the context
     * @param spValue the sp value
     * @return spֵ
     */
    public static float sp2px(Context context, float spValue) {
        DisplayMetrics mDisplayMetrics = AppUtil.getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue,
                mDisplayMetrics);
    }

    /**
     * ������pxת��Ϊsp.
     *
     * @param context the context
     * @param pxValue the px value
     * @return spֵ
     */
    public static float px2sp(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = AppUtil.getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.scaledDensity;
    }

    /**
     * TypedValue�ٷ�Դ���е��㷨�����ⵥλת��ΪPX��λ
     *
     * @param unit    TypedValue.COMPLEX_UNIT_DIP
     * @param value   ��Ӧ��λ��ֵ
     * @param metrics �ܶ�
     * @return pxֵ
     */
    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }

}
