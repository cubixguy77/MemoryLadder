package com.MemoryLadder.TakeTest.Timer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class TimerView extends androidx.appcompat.widget.AppCompatTextView implements ITimer.View {

    public TimerView(Context context) {
        super(context);
    }
    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TimerView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }

    @Override
    public void show() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void hide() {
        setVisibility(View.INVISIBLE);
    }

    @Override
    public void displayTime(long seconds) {
        setText(TimeFormat.formatIntoHHMMSStruncated(seconds));
    }
}