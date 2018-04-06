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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class CustomNumberCarousel extends LinearLayout {

    private TextView prevText;
    private TextView curText;
    private TextView nextText;
    private TextView extraText;
    private TextView centerHiddenGroup;
    private TextView mnemoText;
    private TextView rowIndicator;
    private TextView closeButton;
    private FrameLayout closeButtonContainer;
    private FrameLayout carouselContainer;
    private FrameLayout mnemoTextContainer;

    /* If the animations are kicked off while they're already in progress, the views get all jumbled */
    private boolean animationsInProgress = false;
    private int visibleHeight;

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
        mnemoText = findViewById(R.id.mnemoText);
        centerHiddenGroup = findViewById(R.id.centerHiddenGroup);
        rowIndicator = findViewById(R.id.rowIndicator);
        closeButton = findViewById(R.id.closeButton);
        closeButtonContainer = findViewById(R.id.closeButtonContainer);
        carouselContainer = findViewById(R.id.carouselContainer);
        mnemoTextContainer = findViewById(R.id.mnemoTextContainer);
    }

    public void display(String prev, String cur, String next) {
        prevText.setText(prev);
        curText.setText(cur);
        //centerHiddenGroup.setText(cur);
        //curText.setX(centerHiddenGroup.getX());
        nextText.setText(next);
    }

    public boolean animationsInProgress() {
        return this.animationsInProgress;
    }

    public void transitionForward(String next) {
        display(curText.getText().toString(), nextText.getText().toString(), next);

        /*
        animationsInProgress = true;

        //System.out.println("Hidden x before: " + centerHiddenGroup.getX());
        //centerHiddenGroup.setText(nextText.getText());
        //centerHiddenGroup.invalidate();
        //System.out.println("Hidden x after: " + centerHiddenGroup.getX());

        float prevPos = prevText.getX();
        //float curPos = centerHiddenGroup.getX();
        float curPos = curText.getX();
        float nextPos = nextText.getX();

        float smallTextSize = prevText.getTextSize();
        float largeTextSize = curText.getTextSize();

        final int duration = 50;

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
        */
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
        mnemoText.setText("");
    }

    public void toggle() {
        if (isExpanded())
            collapse();
        else
            expand();
    }

    public boolean isExpanded() {
        return getVisibility() == View.VISIBLE && carouselContainer.getVisibility() == View.VISIBLE;
    }

    private void collapse() {
        carouselContainer.setVisibility(View.GONE);
        mnemoTextContainer.setVisibility(View.GONE);
        closeButton.setText("+Expand");

        LayoutParams collapseParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 0.0f);
        setLayoutParams(collapseParams);
    }

    private void expand() {
        carouselContainer.setVisibility(View.VISIBLE);
        mnemoTextContainer.setVisibility(View.VISIBLE);
        closeButton.setText("—Hide");

        LayoutParams expandParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1.0f);
        setLayoutParams(expandParams);
    }

    public void setRowNum(int begin, int end, int numRows) {
        rowIndicator.setText("Row " + (begin+1) + (begin == end ? "" : "-" + (end+1)) + "/" + numRows);
    }

    public int getVisibleHeight() {
        return getVisibility() == View.VISIBLE ? closeButtonContainer.getHeight() + (isExpanded() ? carouselContainer.getHeight() + mnemoTextContainer.getHeight() : 0) : 0;
    }

    public void setMnemo(String mnemo) {
        mnemoText.setText("\"" + mnemo + "\"");
    }

    public void hideMnemo() {
        mnemoTextContainer.setVisibility(View.GONE);
    }
}
