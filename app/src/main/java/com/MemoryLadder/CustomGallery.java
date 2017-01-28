package com.MemoryLadder;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;
//import com.MemoryLadderFull.R;


public class CustomGallery extends Gallery {

	private long mLastScrollEvent;
	private float mFlingMultiplier;
	
	public CustomGallery(Context context) {
		super(context);
	}
	public CustomGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public CustomGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
    
	@Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
      long now = SystemClock.uptimeMillis();
      if (Math.abs(now - mLastScrollEvent) > 500 * mFlingMultiplier) {	    
    	  super.onLayout(changed, l, t, r, b);	  
      }
    }
    
     
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
        float distanceY)
    {
      mLastScrollEvent = SystemClock.uptimeMillis();
      mFlingMultiplier = 1;
      return super.onScroll(e1, e2, distanceX, distanceY);
    }
    
	
    
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    	mFlingMultiplier = Math.abs(velocityX/100);
    	return super.onFling(e1, e2, velocityX, velocityY);
    }
	
}