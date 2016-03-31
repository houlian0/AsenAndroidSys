package com.asen.android.lib.base.ui.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * <p>
 * <com.gisinfo.mobileoffice.view.GridViewForScroll
 * <p>
 * android:id="@+id/gridView"
 * <p>
 * android:layout_width="wrap_content"
 * <p>
 * android:layout_height="match_parent"
 * <p>
 * android:layout_marginBottom="10dip"
 * <p>
 * android:layout_marginLeft="10dip"
 * <p>
 * android:layout_marginRight="10dip"
 * <p>
 * android:listSelector="@color/transparent"
 * <p>
 * android:numColumns="3"
 * <p>
 * android:cacheColorHint="#00000000"
 * <p>
 * android:scrollbars="none" >
 * <p>
 * </com.gisinfo.mobileoffice.view.GridViewForScroll>
 *
 * @author ASEN
 * @version v1.0
 * @date 2016/3/31 17:04
 */
public class GridViewForScroll extends GridView {

	public GridViewForScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GridViewForScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GridViewForScroll(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
