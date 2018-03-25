package com.MemoryLadder.TakeTest.WrittenNumbers.NumberCarousel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class CustomNumberCarousel extends FrameLayout {

    private TextView prevText;
    private TextView curText;
    private TextView nextText;
    private TextView extraText;

    /* If the animations are kicked off while they're already in progress, the views get all jumbled */
    private boolean animationsInProgress = false;

    public static final String EMPTY_STRING = "-";

    public CustomNumberCarousel(Context context) {        super(context);    }
    public CustomNumberCarousel(Context context, AttributeSet attrs) {        super(context, attrs);    }
    public CustomNumberCarousel(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        prevText = findViewById(R.id.prevGroup);
        curText = findViewById(R.id.curGroup);
        nextText = findViewById(R.id.nextGroup);
        extraText = findViewById(R.id.extraGroup);
    }

    public void display(String prev, String cur, String next) {
        prevText.setText(prev);
        curText.setText(cur);
        nextText.setText(next);
    }

    public boolean animationsInProgress() {
        return this.animationsInProgress;
    }

    public void transitionForward(String next) {
        float prevPos = prevText.getX();
        float curPos = curText.getX();
        float nextPos = nextText.getX();

        float smallTextSize = prevText.getTextSize();
        float largeTextSize = curText.getTextSize();

        final int duration = 200;
        animationsInProgress = true;

        prevText.animate().x(-prevText.getWidth()).setDuration(duration);

        curText.animate().x(prevPos).setDuration(duration);
        ValueAnimator curTextSizeAnim = ObjectAnimator.ofFloat(largeTextSize, smallTextSize);
        curTextSizeAnim.addUpdateListener(animation -> curText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) animation.getAnimatedValue()));
        curTextSizeAnim.setDuration(duration);
        curTextSizeAnim.start();

        nextText.animate().x(curPos).setDuration(duration);
        ValueAnimator nextTextSizeAnim = ObjectAnimator.ofFloat(smallTextSize, largeTextSize);
        nextTextSizeAnim.addUpdateListener(animation -> nextText.setTextSize(TypedValue.COMPLEX_UNIT_PX, (float) animation.getAnimatedValue()));
        nextTextSizeAnim.setDuration(duration);
        nextTextSizeAnim.start();
        nextTextSizeAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                TextView temp = prevText;
                prevText = curText;
                curText = nextText;
                nextText = extraText;
                extraText = temp;
                extraText.setX(getWidth());
                animationsInProgress = false;
            }
        });

        extraText.setX(getWidth());
        extraText.setText(next);
        extraText.setVisibility(View.VISIBLE);
        extraText.animate().x(nextPos).setDuration(duration);
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }
}
