package com.MemoryLadder.TakeTest.WrittenNumbers.NumberGrid;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class MaxHeightScrollView extends ScrollView {

    private int maxHeight;

    public MaxHeightScrollView(Context context) {
        super(context);
    }
    public MaxHeightScrollView(Context context, AttributeSet attrs) {        super(context, attrs);    }
    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}