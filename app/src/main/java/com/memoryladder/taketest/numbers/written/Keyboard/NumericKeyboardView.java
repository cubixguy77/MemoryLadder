package com.memoryladder.taketest.numbers.written.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.mastersofmemory.memoryladder.R;

public class NumericKeyboardView extends TableLayout {

    private KeyListener listener;
    private Button key1, key2, key3, key4, key5, key6, key7, key8, key9, key0;
    private Button binKey0, binKey1;
    private View keyForward, keyBack, keyBackspace;
    private View binKeyForward, binKeyBack, binKeyBackspace;

    public NumericKeyboardView(Context context) {
        super(context);
    }
    public NumericKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        key1 = findViewById(R.id.key_1);
        key2 = findViewById(R.id.key_2);
        key3 = findViewById(R.id.key_3);
        key4 = findViewById(R.id.key_4);
        key5 = findViewById(R.id.key_5);
        key6 = findViewById(R.id.key_6);
        key7 = findViewById(R.id.key_7);
        key8 = findViewById(R.id.key_8);
        key9 = findViewById(R.id.key_9);
        key0 = findViewById(R.id.key_0);
        keyForward = findViewById(R.id.key_forward);
        keyBack = findViewById(R.id.key_back);
        keyBackspace = findViewById(R.id.key_backspace);

        binKey0 = findViewById(R.id.bin_key_0);
        binKey1 = findViewById(R.id.bin_key_1);
        binKeyForward = findViewById(R.id.bin_key_forward);
        binKeyBack = findViewById(R.id.bin_key_back);
        binKeyBackspace = findViewById(R.id.bin_key_backspace);

        setClickListeners();
    }

    public void setKeyListener(KeyListener listener) {
        this.listener = listener;
    }

    private void setClickListeners() {
        setClickListener(key1, '1');
        setClickListener(key2, '2');
        setClickListener(key3, '3');
        setClickListener(key4, '4');
        setClickListener(key5, '5');
        setClickListener(key6, '6');
        setClickListener(key7, '7');
        setClickListener(key8, '8');
        setClickListener(key9, '9');
        setClickListener(key0, '0');
        setClickListener(keyForward, () -> listener.onForward());
        setClickListener(keyBack, () -> listener.onBack());
        setClickListener(keyBackspace, () -> listener.onBackspace());

        setClickListener(binKey0, '0');
        setClickListener(binKey1, '1');
        setClickListener(binKeyForward, () -> listener.onForward());
        setClickListener(binKeyBack, () -> listener.onBack());
        setClickListener(binKeyBackspace, () -> listener.onBackspace());
    }

    private void setClickListener(View view, char digit) {
        view.setOnClickListener(v -> {
            if (listener != null) listener.onDigit(digit);
        });
    }

    private void setClickListener(View view, Runnable action) {
        view.setOnClickListener(v -> {
            if (listener != null) action.run();
        });
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
}
