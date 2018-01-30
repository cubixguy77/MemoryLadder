package com.MemoryLadder.Settings;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class TimeSettingDialog extends Dialog implements OnClickListener {

	private Button CancelButton;
	private Button SaveButton;

	private int oldValue;
	private int newValue;

	private int hours;
	private int minutes;
	private int seconds;

	final private static int MAX_HOUR = 5;

	private ValueChangeListener listener;

	public interface ValueChangeListener {
		void onValueChanged(int newValue);
	}

	public TimeSettingDialog(Context context, String LabelText, int oldValue) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.picker_time);
		
		this.oldValue = oldValue;
		this.newValue = oldValue;
		
		initTimeVariables(oldValue);
		initControls();
		initText(LabelText);

		setCancelable(true);	
	}

    public void setValueChangeListener(ValueChangeListener listener){
        this.listener = listener;
    }

	private void initTimeVariables(int totalSeconds) {
        hours = (totalSeconds / 3600);
        int remainder = (totalSeconds % 3600);
        minutes = remainder / 60;
        seconds = remainder % 60;
	}

	private void initText(String text) {
        ((TextView) findViewById(R.id.numberPickerDialogTitle)).setText(text);
	}
	
	private void initControls() {
		CancelButton = findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(this);
		
		SaveButton = findViewById(R.id.SaveButton);
		SaveButton.setOnClickListener(this);

        NumberPicker hoursPicker = findViewById(R.id.numberPickerHours);
        hoursPicker.setMinValue(0);
        hoursPicker.setMaxValue(MAX_HOUR);
        hoursPicker.setValue(hours);
        hoursPicker.setWrapSelectorWheel(false);
        hoursPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            hours = newVal;
            refresh();
        });

        NumberPicker minutesPicker = findViewById(R.id.numberPickerMinutes);
        minutesPicker.setMinValue(0);
        minutesPicker.setMaxValue(59);
        minutesPicker.setValue(minutes);
        minutesPicker.setWrapSelectorWheel(false);
        minutesPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            minutes = newVal;
            refresh();
        });

        NumberPicker secondsPicker = findViewById(R.id.numberPickerSeconds);
        secondsPicker.setMinValue(0);
        secondsPicker.setMaxValue(59);
        secondsPicker.setValue(seconds);
        secondsPicker.setWrapSelectorWheel(false);
        secondsPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            seconds = newVal;
            refresh();
        });
	}

	private void refresh() {
		newValue = (hours*3600) + (minutes*60) + (seconds);
	}

	@Override
	public void onClick(View v) {
		if (v == CancelButton) 
			dismiss();
		else if (v == SaveButton) {
			if(listener != null && oldValue != newValue && newValue > 0){
                listener.onValueChanged(newValue);
            }

			dismiss();
		}
	}	
}