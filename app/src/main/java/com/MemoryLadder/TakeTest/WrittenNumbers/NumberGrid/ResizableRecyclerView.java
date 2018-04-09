package com.MemoryLadder.TakeTest.WrittenNumbers.NumberGrid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class ResizableRecyclerView extends RecyclerView {

    private int maxHeight;

    public ResizableRecyclerView(Context context) {
        super(context);
    }
    public ResizableRecyclerView(Context context, AttributeSet attrs) {        super(context, attrs);    }
    public ResizableRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}