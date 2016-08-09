package com.Tester;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;
//import com.MemoryLadderFull.R;


public class CountDownDialog extends Dialog {
		
	public CountDownDialog(Context context) {
		super(context, R.style.fullscreen);
		setContentView(R.layout.countdown);
		TextView text = (TextView) findViewById(R.id.text);
		countDown(text, 3);
	}
	
	private void countDown(final TextView tv, final int count) {
		   if (count == 0) { 
			   tv.setVisibility(View.INVISIBLE);
			   dismiss();
		   } 
		   tv.setText(Integer.toString(count));
		   AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
		   animation.setDuration(1000);
		   animation.setAnimationListener(new AnimationListener() {
		     public void onAnimationEnd(Animation anim) {
		       countDown(tv, count - 1);
		     }

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationStart(Animation animation) {}
		   });
		   tv.startAnimation(animation);
	}
}