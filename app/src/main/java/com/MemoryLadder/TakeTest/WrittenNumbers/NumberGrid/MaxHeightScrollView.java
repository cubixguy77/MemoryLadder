package com.MemoryLadder.TakeTest.WrittenNumbers.NumberGrid;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

public class MaxHeightScrollView extends NestedScrollView {

    private int maxHeight;

    public MaxHeightScrollView(Context context) {
        super(context);
    }
    public MaxHeightScrollView(Context context, AttributeSet attrs) {        super(context, attrs);    }
    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}