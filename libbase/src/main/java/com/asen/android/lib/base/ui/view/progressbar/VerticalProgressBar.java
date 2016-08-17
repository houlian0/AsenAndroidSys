package com.asen.android.lib.base.ui.view.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * ��ֱ��ProgressBar
 *
 * @author Asen
 * @version v1.0
 * @date 2016/3/31 17:04
 */
public class VerticalProgressBar extends ProgressBar {

    public VerticalProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VerticalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalProgressBar(Context context) {
        super(context);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // canvas.rotate(-90);// ��ת90�ȣ���ˮƽProgressBar������
        // canvas.translate(-getHeight(), 0);//
        // ��������ת��õ���VerticalProgressBar�Ƶ���ȷ��λ��,ע�⾭��ת<span
        // style="white-space: pre;">
        // </span> ����ֵ����

        canvas.rotate(90);// ��ת90�ȣ���ˮƽProgressBar������
        canvas.translate(0, -getWidth());

        super.onDraw(canvas);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());// �������ֵ
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldw, oldh);// �������ֵ
        // super.onSizeChanged(w, h, oldw, oldh);
    }
}
