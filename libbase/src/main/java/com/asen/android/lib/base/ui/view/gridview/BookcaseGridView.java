package com.asen.android.lib.base.ui.view.gridview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * Simple to Introduction 书架 gridView，内容不超出屏幕时正常
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:04
 */
public class BookcaseGridView extends GridView {

	private Bitmap background;

	public BookcaseGridView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
	}

	/**
	 * bitmap pay attention to out of memory
	 * 
	 * @param background
	 */
	public void setBackGround(Bitmap background) {
		this.background = background;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (background != null) {
			int backgroundWidth = background.getWidth();
			int width = getWidth();
			double scale = width * 1.0 / backgroundWidth;
			Matrix matrix = new Matrix();
			matrix.postScale((float) scale, (float) scale);
			background = Bitmap.createBitmap(background, 0, 0, background.getWidth(), background.getHeight(), matrix, true);

			int numColumns = getNumColumns();
			int count = getChildCount();
			int top = 0;
			int height_2 = background.getHeight();

			int num = count == 0 ? 0 : count / numColumns + 1;
			for (int i = 0; i < num; i++) {
				int index = i * numColumns;
				top = count > index ? getChildAt(index).getBottom() : 0;
				canvas.drawBitmap(background, 0, top - height_2 / 6.0f, null);
			}
		}
		super.dispatchDraw(canvas);
	}

}
