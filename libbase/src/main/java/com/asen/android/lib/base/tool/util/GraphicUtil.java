package com.asen.android.lib.base.tool.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.widget.TextView;

/**
 * ����תͼƬ ������
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class GraphicUtil {

    /**
     * �����������ݺ����ִ�С ������ֵĸ߿����Ϣ�������ڵ������֣�
     *
     * @param textSize ���ִ�С
     * @return ���ָ߿�
     */
    public static Paint.FontMetrics getFontMetrics(float textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        return paint.getFontMetrics();
    }

    /**
     * ����TextView������ֵĸ߿����Ϣ�������ڵ������֣�
     *
     * @param textView TextView �ؼ�
     * @return ���ָ߿�
     */
    public static Paint.FontMetrics getFontMetrics(TextView textView) {
        return textView.getPaint().getFontMetrics();
    }

    /**
     * ���ݻ��� ������ֵĸ߿����Ϣ�������ڵ������֣�
     *
     * @param paint ����
     * @return ���ָ߿�
     */
    public static Paint.FontMetrics getFontMetrics(Paint paint) {
        return paint.getFontMetrics();
    }

    /**
     * ������ת��BitmapͼƬ�������ڵ������֣�
     *
     * @param content   ��������
     * @param textSize  ���ִ�С
     * @param textColor ������ɫ
     * @return BitmapͼƬ
     */
    public static Bitmap getTextBitmap(String content, float textSize, int textColor) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setAntiAlias(true);
        Paint.FontMetrics metrics = getFontMetrics(paint);

        Rect rect = new Rect();
        paint.getTextBounds(content, 0, content.length(), rect);
        Bitmap bitmap = Bitmap.createBitmap(rect.width(), (int) Math.ceil(metrics.descent - metrics.ascent), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawText(content, 0, (float) Math.abs(Math.ceil(metrics.ascent)), paint);

        return bitmap;
    }

    /**
     * ������ת��BitmapͼƬ��֧�ֶ������֣�
     *
     * @param contents  ��������
     * @param textSize  ���ִ�С
     * @param textColor ������ɫ
     * @return BitmapͼƬ
     */
    public static Bitmap getTextBitmap(String[] contents, float textSize, int textColor) {
        if (contents == null || contents.length == 0)
            return null;

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setAntiAlias(true);
        Paint.FontMetrics metrics = getFontMetrics(paint);

        int maxWidth = 0; // ���
        int heigh = 0; // �߶�
        for (String content : contents) {
            if (content == null)
                content = "";
            Rect rect = new Rect();
            paint.getTextBounds(content, 0, content.length(), rect);
            int width = rect.width();
            if (maxWidth < width) {
                maxWidth = width;
            }
            if (heigh == 0) {
                heigh = (int) Math.ceil(metrics.descent - metrics.ascent);
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(maxWidth, heigh * contents.length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        for (int i = 0; i < contents.length; i++) {
            canvas.drawText(contents[i], 0, (float) Math.abs(Math.ceil(metrics.ascent)) + heigh * i, paint);
        }

        return bitmap;
    }


//    /**
//     * ���㵥�����ָ߿�
//     *
//     * @param text ��������
//     * @param size ���ִ�С
//     * @return ���и߶ȣ�������
//     */
//    private int[] measureText(String text, float size) {
//        if (text == null) text = "";
//
//        Paint paint = new Paint();
//        paint.setTextSize(size);
//
//        Paint.FontMetrics metrics = paint.getFontMetrics();
//        double f = Math.ceil(metrics.descent - metrics.ascent);
//        Rect rect = new Rect();
//        paint.getTextBounds(text, 0, text.length(), rect);
//
//        return new int[]{rect.width(), (int) Math.ceil(f)};
//    }
//
//    /**
//     * ��������ȡ�ַ�������λ�ã������ػ�ȡ��������ɵģ�.
//     *
//     * @param str    ָ�����ַ���
//     * @param maxPix Ҫȡ����λ�ã����أ�
//     * @param paint  the paint
//     * @return �ַ�������λ��
//     */
//    public static int subStringLength(String str, int maxPix, Paint paint) {
//        if (TextUtils.isEmpty(str)) {
//            return 0;
//        }
//        int currentIndex = 0;
//        for (int i = 0; i < str.length(); i++) {
//            //��ȡһ���ַ�
//            String temp = str.substring(0, i + 1);
//            float valueLength = paint.measureText(temp);
//            if (valueLength > maxPix) {
//                currentIndex = i - 1;
//                break;
//            } else if (valueLength == maxPix) {
//                currentIndex = i;
//                break;
//            }
//        }
//        //����������ط������һ���ַ�λ��
//        if (currentIndex == 0) {
//            currentIndex = str.length() - 1;
//        }
//        return currentIndex;
//    }
//
//    /**
//     * ��������ȡ���ֵ����ؿ�.
//     *
//     * @param str   the str
//     * @param paint the paint
//     * @return the string width
//     */
//    public static float getStringWidth(String str, Paint paint) {
//        return paint.measureText(str);
//    }
//
//    /**
//     * ������ֵĿ��
//     *
//     * @param str   the str
//     * @param paint the paint
//     * @return the string width
//     */
//    public static float getDesiredWidth(String str, TextPaint paint) {
//        return Layout.getDesiredWidth(str, paint);
//    }
//
//    /**
//     * ��ȡ���ֵĸ߶�
//     *
//     * @param paint the Paint
//     * @return the string height
//     */
//    public static float getDesiredHeight(Paint paint) {
//        Paint.FontMetrics fm = paint.getFontMetrics();
//        return (float) Math.ceil(fm.descent - fm.ascent);
//    }
//
//    /**
//     * ��������.
//     *
//     * @param text    the text
//     * @param maxWPix the max w pix
//     * @param paint   the paint
//     * @return the draw row count
//     */
//    public static List<String> getDrawRowStr(String text, int maxWPix, Paint paint) {
//        String[] texts = null;
//        if (text.contains("\n")) {
//            texts = text.split("\n");
//        } else {
//            texts = new String[1];
//            texts[0] = text;
//        }
//        //��������
//        List<String> mStrList = new ArrayList<String>();
//
//        for (int i = 0; i < texts.length; i++) {
//            String textLine = texts[i];
//            //��������ı���ʾΪ����
//            while (true) {
//                //�����ɵ����һ���ֵ�λ��
//                int endIndex = subStringLength(textLine, maxWPix, paint);
//                if (endIndex <= 0) {
//                    mStrList.add(textLine);
//                } else {
//                    if (endIndex == textLine.length() - 1) {
//                        mStrList.add(textLine);
//                    } else {
//                        mStrList.add(textLine.substring(0, endIndex + 1));
//                    }
//
//                }
//                //��ȡʣ�µ�
//                if (textLine.length() > endIndex + 1) {
//                    //����ʣ�µ�
//                    textLine = textLine.substring(endIndex + 1);
//                } else {
//                    break;
//                }
//            }
//        }
//
//        return mStrList;
//    }
//
//    /**
//     * ��������ȡ����ı�������.
//     *
//     * @param text
//     * @param maxWPix
//     * @param paint
//     * @return
//     */
//    public static int getDrawRowCount(String text, int maxWPix, Paint paint) {
//        String[] texts = null;
//        if (text.contains("\n")) {
//            texts = text.split("\n");
//        } else {
//            texts = new String[1];
//            texts[0] = text;
//        }
//        //��������
//        List<String> mStrList = new ArrayList<String>();
//
//        for (int i = 0; i < texts.length; i++) {
//            String textLine = texts[i];
//            //��������ı���ʾΪ����
//            while (true) {
//                //�����ɵ����һ���ֵ�λ��
//                int endIndex = subStringLength(textLine, maxWPix, paint);
//                if (endIndex <= 0) {
//                    mStrList.add(textLine);
//                } else {
//                    if (endIndex == textLine.length() - 1) {
//                        mStrList.add(textLine);
//                    } else {
//                        mStrList.add(textLine.substring(0, endIndex + 1));
//                    }
//
//                }
//                //��ȡʣ�µ�
//                if (textLine.length() > endIndex + 1) {
//                    //����ʣ�µ�
//                    textLine = textLine.substring(endIndex + 1);
//                } else {
//                    break;
//                }
//            }
//        }
//
//        return mStrList.size();
//    }
//
//    /**
//     * �����������ı���֧�ֻ���.
//     *
//     * @param canvas  the canvas
//     * @param text    the text
//     * @param maxWPix the max w pix
//     * @param paint   the paint
//     * @param left    the left
//     * @param top     the top
//     * @return the int
//     */
//    public static int drawText(Canvas canvas, String text, int maxWPix, Paint paint, int left, int top) {
//        if (TextUtils.isEmpty(text)) {
//            return 1;
//        }
//        //��Ҫ�������ֳ��ȿ��ƻ���
//        //�������ֵĳ���
//        List<String> mStrList = getDrawRowStr(text, maxWPix, paint);
//
//        int hSize = (int) getDesiredHeight(paint);
//
//        for (int i = 0; i < mStrList.size(); i++) {
//            //��������
//            int x = left;
//            int y = top + hSize / 2 + hSize * i;
//            String textLine = mStrList.get(i);
//            canvas.drawText(textLine, x, y, paint);
//        }
//        return mStrList.size();
//    }

}
