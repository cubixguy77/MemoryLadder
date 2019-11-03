package com.memoryladder.taketest.numbers.written.Keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TableLayout;

import com.mastersofmemory.memoryladder.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class NumericKeyboardView extends TableLayout {

    private KeyListener listener;
    
    public NumericKeyboardView(Context context) {
        super(context);
    }
    public NumericKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setKeyListener(KeyListener listener) {
        this.listener = listener;
    }
    
    public void show(int base) {

        /* Binary mode, only show 0 and 1 keys */
        if (base == 2) {
            findViewById(R.id.keyboardBinaryRow).setVisibility(View.VISIBLE);
            findViewById(R.id.keyboardRow1).setVisibility(View.GONE);
            findViewById(R.id.keyboardRow2).setVisibility(View.GONE);
            findViewById(R.id.keyboardRow3).setVisibility(View.GONE);
            findViewById(R.id.keyboardRow4).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.keyboardBinaryRow).setVisibility(View.GONE);
            findViewById(R.id.keyboardRow1).setVisibility(View.VISIBLE);
            findViewById(R.id.keyboardRow2).setVisibility(View.VISIBLE);
            findViewById(R.id.keyboardRow3).setVisibility(View.VISIBLE);
            findViewById(R.id.keyboardRow4).setVisibility(View.VISIBLE);
        }

        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }
    
    @OnClick(R.id.key_1) void on1Clicked() { if(listener != null) listener.onDigit('1'); }
    @OnClick(R.id.key_2) void on2Clicked() { if(listener != null) listener.onDigit('2'); }
    @OnClick(R.id.key_3) void on3Clicked() { if(listener != null) listener.onDigit('3'); }
    @OnClick(R.id.key_4) void on4Clicked() { if(listener != null) listener.onDigit('4'); }
    @OnClick(R.id.key_5) void on5Clicked() { if(listener != null) listener.onDigit('5'); }
    @OnClick(R.id.key_6) void on6Clicked() { if(listener != null) listener.onDigit('6'); }
    @OnClick(R.id.key_7) void on7Clicked() { if(listener != null) listener.onDigit('7'); }
    @OnClick(R.id.key_8) void on8Clicked() { if(listener != null) listener.onDigit('8'); }
    @OnClick(R.id.key_9) void on9Clicked() { if(listener != null) listener.onDigit('9'); }
    @OnClick(R.id.key_0) void on0Clicked() { if(listener != null) listener.onDigit('0'); }
    @OnClick(R.id.bin_key_0) void onBin0Clicked() { if(listener != null) listener.onDigit('0'); }
    @OnClick(R.id.bin_key_1) void onBin1Clicked() { if(listener != null) listener.onDigit('1'); }
    @OnClick(R.id.key_forward) void onKeyForwardClicked() { if(listener != null) listener.onForward(); }
    @OnClick(R.id.key_back) void onKeyBackClicked() { if(listener != null) listener.onBack(); }
    @OnClick(R.id.key_backspace) void onKeyBackspaceClicked() { if(listener != null) listener.onBackspace(); }

    @Optional @OnClick(R.id.bin_key_forward) void onBinKeyForwardClicked() { if(listener != null) listener.onForward(); }
    @Optional @OnClick(R.id.bin_key_back) void onBinKeyBackClicked() { if(listener != null) listener.onBack(); }
    @Optional @OnClick(R.id.bin_key_backspace) void onBinKeyBackspaceClicked() { if(listener != null) listener.onBackspace(); }
}
