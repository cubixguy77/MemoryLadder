package com.memoryladder.settings;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.memoryladder.taketest.numbers.spoken.settings.DigitSpeed;
import com.mastersofmemory.memoryladder.R;

public class DigitSpeedSettingDialog extends Dialog implements OnClickListener {

	private Button CancelButton;
	private Button SaveButton;
	private LinearLayout optionsContainer;
	private int initValue;
	private int newValue;

	private ValueChangeListener listener;

	public DigitSpeedSettingDialog(Context context, int initValue) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.picker_digit_speed);
		
		this.initValue = initValue;
		this.newValue = initValue;
		
		initButtons();
        initOptions();
        refresh(initValue);
		
		setCancelable(true);	
	}
	
	public interface ValueChangeListener {
	   void onValueChanged(int newValue);
	}
	
	public void setValueChangedListener(ValueChangeListener listener){
		this.listener = listener;
    }
	
	public void initButtons() {
		CancelButton = findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(this);
		
		SaveButton = findViewById(R.id.SaveButton);
		SaveButton.setOnClickListener(this);
	}	
		
	@Override
	public void onClick(View v) {
		if (v == CancelButton) {
            dismiss();
        }
		else if (v == SaveButton) {
			if(listener != null && initValue != newValue) {
                listener.onValueChanged(newValue);
            }

			dismiss();
		}
	}

	private void setBackground(View view, boolean isSelected) {
	    view.setBackgroundResource(isSelected ? R.color.colorPrimary : 0);
    }

	private TextView getOption(int index) {
		return (TextView) optionsContainer.getChildAt(index);
	}

	private void refresh(int value) {
        setBackground(getOption(1), value == DigitSpeed.DIGIT_SPEED_SUPER_SLOW);
        setBackground(getOption(2), value == DigitSpeed.DIGIT_SPEED_SLOW);
        setBackground(getOption(3), value == DigitSpeed.DIGIT_SPEED_STANDARD);
        setBackground(getOption(4), value == DigitSpeed.DIGIT_SPEED_FAST);
        setBackground(getOption(5), value == DigitSpeed.DIGIT_SPEED_SUPER_FAST);
    }

    private void initOptions() {
        optionsContainer = findViewById(R.id.digitSpeedOptionContainer);
        
        getOption(1).setOnClickListener(v -> {
            newValue = DigitSpeed.DIGIT_SPEED_SUPER_SLOW;
            refresh(newValue);
        });

        getOption(2).setOnClickListener(v -> {
            newValue = DigitSpeed.DIGIT_SPEED_SLOW;
            refresh(newValue);
        });

        getOption(3).setOnClickListener(v -> {
            newValue = DigitSpeed.DIGIT_SPEED_STANDARD;
            refresh(newValue);
        });

        getOption(4).setOnClickListener(v -> {
            newValue = DigitSpeed.DIGIT_SPEED_FAST;
            refresh(newValue);
        });

        getOption(5).setOnClickListener(v -> {
            newValue = DigitSpeed.DIGIT_SPEED_SUPER_FAST;
            refresh(newValue);
        });
	}
}