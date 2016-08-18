package com.asen.android.lib.base.tool.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.widget.TextView;

/**
 * 文字转图片 工具类
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 16:09
 */
public class GraphicUtil {

    /**
     * 根据文字内容和文字大小 获得文字的高宽等信息（适用于单行文字）
     *
     * @param textSize 文字大小
     * @return 文字高宽
     */
    public static Paint.FontMetrics getFontMetrics(float textSize) {
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        return paint.getFontMetrics();
    }

    /**
     * 根据TextView获得文字的高宽等信息（适用于单行文字）
     *
     * @param textView TextView 控件
     * @return 文字高宽
     */
    public static Paint.FontMetrics getFontMetrics(TextView textView) {
        return textView.getPaint().getFontMetrics();
    }

    /**
     * 根据画笔 获得文字的高宽等信息（适用于单行文字）
     *
     * @param paint 画笔
     * @return 文字高宽
     */
    public static Paint.FontMetrics getFontMetrics(Paint paint) {
        return paint.getFontMetrics();
    }

    /**
     * 将文字转成Bitmap图片（适用于单行文字）
     *
     * @param content   文字内容
     * @param textSize  文字大小
     * @param textColor 文字颜色
     * @return Bitmap图片
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
     * 将文字转成Bitmap图片（支持多行文字）
     *
     * @param contents  文字内容
     * @param textSize  文字大小
     * @param textColor 文字颜色
     * @return Bitmap图片
     */
    public static Bitmap getTextBitmap(String[] contents, float textSize, int textColor) {
        if (contents == null || contents.length == 0)
            return null;

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setAntiAlias(true);
        Paint.FontMetrics metrics = getFontMetrics(paint);

        int maxWidth = 0; // 宽度
        int heigh = 0; // 高度
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
//     * 计算单行文字高宽
//     *
//     * @param text 单行文字
//     * @param size 文字大小
//     * @return 单行高度，整体宽度
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
//     * 描述：获取字符的所在位置（按像素获取最大能容纳的）.
//     *
//     * @param str    指定的字符串
//     * @param maxPix 要取到的位置（像素）
//     * @param paint  the paint
//     * @return 字符的所在位置
//     */
//    public static int subStringLength(String str, int maxPix, Paint paint) {
//        if (TextUtils.isEmpty(str)) {
//            return 0;
//        }
//        int currentIndex = 0;
//        for (int i = 0; i < str.length(); i++) {
//            //获取一个字符
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
//        //短于最大像素返回最后一个字符位置
//        if (currentIndex == 0) {
//            currentIndex = str.length() - 1;
//        }
//        return currentIndex;
//    }
//
//    /**
//     * 描述：获取文字的像素宽.
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
//     * 获得文字的宽度
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
//     * 获取文字的高度
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
//     * 解析成行.
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
//        //共多少行
//        List<String> mStrList = new ArrayList<String>();
//
//        for (int i = 0; i < texts.length; i++) {
//            String textLine = texts[i];
//            //计算这个文本显示为几行
//            while (true) {
//                //可容纳的最后一个字的位置
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
//                //获取剩下的
//                if (textLine.length() > endIndex + 1) {
//                    //还有剩下的
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
//     * 描述：获取这段文本多少行.
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
//        //共多少行
//        List<String> mStrList = new ArrayList<String>();
//
//        for (int i = 0; i < texts.length; i++) {
//            String textLine = texts[i];
//            //计算这个文本显示为几行
//            while (true) {
//                //可容纳的最后一个字的位置
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
//                //获取剩下的
//                if (textLine.length() > endIndex + 1) {
//                    //还有剩下的
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
//     * 描述：绘制文本，支持换行.
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
//        //需要根据文字长度控制换行
//        //测量文字的长度
//        List<String> mStrList = getDrawRowStr(text, maxWPix, paint);
//
//        int hSize = (int) getDesiredHeight(paint);
//
//        for (int i = 0; i < mStrList.size(); i++) {
//            //计算坐标
//            int x = left;
//            int y = top + hSize / 2 + hSize * i;
//            String textLine = mStrList.get(i);
//            canvas.drawText(textLine, x, y, paint);
//        }
//        return mStrList.size();
//    }

}
