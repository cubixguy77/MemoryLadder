package com.memoryladder.taketest.numbers.written.numbercarousel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class CustomNumberCarousel extends LinearLayout {

    private TextView prevText;
    private TextView curText;
    private TextView nextText;
    private TextView mnemoText;
    private TextView rowIndicator;
    private TextView closeButton;
    public LinearLayout carouselContainer;
    private FrameLayout mnemoTextContainer;
    private static final int duration = 100;
    private static TimeInterpolator interpolator = new AccelerateDecelerateInterpolator();

    /* If the animations are kicked off while they're already in progress, the views get all jumbled */
    private boolean animationsInProgress = false;
    private boolean isDisplayMnemo = false;

    public CustomNumberCarousel(Context context) {        super(context);    }
    public CustomNumberCarousel(Context context, AttributeSet attrs) {        super(context, attrs);    }
    public CustomNumberCarousel(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        prevText = findViewById(R.id.prevGroup);
        curText = findViewById(R.id.curGroup);
        nextText = findViewById(R.id.nextGroup);
        mnemoText = findViewById(R.id.mnemoText);
        rowIndicator = findViewById(R.id.rowIndicator);
        closeButton = findViewById(R.id.closeButton);
        carouselContainer = findViewById(R.id.carouselContainer);
        mnemoTextContainer = findViewById(R.id.mnemoTextContainer);
    }

    public void display(String prev, String cur, String next) {
        prevText.setText(prev);
        curText.setText(cur);
        nextText.setText(next);
    }

    public boolean animationsInProgress() {
        return this.animationsInProgress;
    }

    public void transitionForward(String prev, String cur, String next) {
        if (animationsInProgress()) {
            display(prev, cur, next);
            animationsInProgress = false;
            return;
        }

        animationsInProgress = true;

        prevText.animate().alpha(0).setDuration(duration).setInterpolator(interpolator).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                prevText.setText(prev);
                prevText.animate().alpha(1).setDuration(duration).setInterpolator(interpolator).setListener(null).start();
            }
        }).start();

        curText.animate().alpha(0).scaleX(0.5f).scaleY(0.5f).setDuration(duration).setInterpolator(interpolator).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                curText.setText(cur);
                curText.animate().alpha(1).scaleX(1).scaleY(1).setDuration(duration).setInterpolator(interpolator).setListener(null).start();
            }
        }).start();

        nextText.animate().alpha(0).setDuration(duration).setInterpolator(interpolator).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                nextText.setText(next);
                nextText.animate().alpha(1).setDuration(duration).setInterpolator(interpolator).setListener(null).start();
                animationsInProgress = false;
            }
        }).start();
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
        mnemoText.setText("");
    }

    public boolean isExpanded() {
        return getVisibility() == View.VISIBLE && carouselContainer.getVisibility() == View.VISIBLE;
    }

    public void collapse() {
        carouselContainer.setVisibility(View.GONE);
        mnemoTextContainer.setVisibility(View.GONE);
        closeButton.setText(R.string.expand);
        setHeight(0);
    }

    public void setHeight(int height) {
        if (height <= 0) {
            LayoutParams collapseParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.0f);
            setLayoutParams(collapseParams);
        } else {
            LayoutParams expandParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height, 0.0f);
            setLayoutParams(expandParams);
        }
    }

    public void expand() {
        if (isExpanded())
            return;

        carouselContainer.setVisibility(View.VISIBLE);
        mnemoTextContainer.setVisibility(this.isDisplayMnemo ? View.VISIBLE : View.GONE);
        closeButton.setText(R.string.hide);
    }

    public void setRowNum(int begin, int end, int numRows) {
        rowIndicator.setText(String.format(getContext().getString(R.string.Row_Num_Indicator), begin + 1, begin == end ? "" : "-" + (end + 1), numRows));
    }

    public void showMnemo() {
        this.isDisplayMnemo = true;
        mnemoTextContainer.setVisibility(isExpanded() ? View.VISIBLE : View.GONE);
    }

    public void setMnemoText(String text) {
        mnemoText.setText(String.format("\"%s\"", text));
    }

    public void hideMnemo() {
        this.isDisplayMnemo = false;
        mnemoTextContainer.setVisibility(View.GONE);
    }


}
