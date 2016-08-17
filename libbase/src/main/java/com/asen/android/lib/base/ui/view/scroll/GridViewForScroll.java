package com.asen.android.lib.base.ui.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * ¼æÈÝScrollViewµÄGridView
 *
 * @author Asen
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
