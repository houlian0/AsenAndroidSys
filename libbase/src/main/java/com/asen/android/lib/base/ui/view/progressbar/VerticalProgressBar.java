package com.asen.android.lib.base.ui.view.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by ASEN on 2016/3/31.
 * 垂直的ProgressBar
 *
 * @author ASEN
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
		// canvas.rotate(-90);// 反转90度，将水平ProgressBar竖起来
		// canvas.translate(-getHeight(), 0);//
		// 将经过旋转后得到的VerticalProgressBar移到正确的位置,注意经旋转<span
		// style="white-space: pre;">
		// </span> 后宽高值互换
		
		canvas.rotate(90);// 正转90度，将水平ProgressBar竖起来
		canvas.translate(0, -getWidth());

		super.onDraw(canvas);
	}

	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, widthMeasureSpec);
		setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());// 互换宽高值
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(h, w, oldw, oldh);// 互换宽高值
		// super.onSizeChanged(w, h, oldw, oldh);
	}
}
