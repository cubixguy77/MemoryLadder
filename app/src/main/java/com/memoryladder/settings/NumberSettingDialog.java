package com.memoryladder.settings;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class NumberSettingDialog extends Dialog implements OnClickListener {

	private Button CancelButton;
	private Button SaveButton;
    private int initValue;
	private int newValue;

	private ValueChangeListener listener;

	public NumberSettingDialog(Context context, String label, int initValue, int min, int max) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.picker_number);
		
		this.initValue = initValue;
		this.newValue = initValue;
		
		initButtons();
		initText(label);
		initNumberPicker(min, max);
		
		setCancelable(true);	
	}
	
	public interface ValueChangeListener {
	   void onValueChanged(int newValue);
	}
	
	public void setValueChangedListener(ValueChangeListener listener){
		this.listener = listener;
    }
	
	private void initText(String labelText) {
		TextView Label = findViewById(R.id.numberPickerLabel);
		Label.setText(labelText);
	}
	
	private void initNumberPicker(int min, int max) {
        NumberPicker numberPicker = findViewById(R.id.numberPicker);
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> newValue = newVal);

        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(initValue);

        numberPicker.setWrapSelectorWheel(false);
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
}