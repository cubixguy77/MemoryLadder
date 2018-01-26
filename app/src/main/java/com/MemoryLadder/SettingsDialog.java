package com.MemoryLadder;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class SettingsDialog extends Dialog implements OnClickListener, OnSeekBarChangeListener {

	private Button CancelButton;
	private Button SaveButton;
	private TextView SeekBarValue;
	private String oldValue;
	private String newValue;
	private int[] hashmarks;
	
	private OnMyDialogResult mDialogResult; // the callback
	
	public SettingsDialog(Context context, String oldValue, String LabelText, int[] hashmarks) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settingsdialog);
		
		this.oldValue = oldValue;
		this.newValue = oldValue;
		this.hashmarks = hashmarks;
		
		initButtons();
		initText(LabelText);
		initSeekBar();
		
		setCancelable(true);	
	}
	
	public interface OnMyDialogResult{
	       void finish(String result);
	}
	
	public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }
	
	private void initText(String labeltext) {
		TextView Label = findViewById(R.id.Label);
		Label.setText(labeltext);
		
		SeekBarValue = findViewById(R.id.SeekBarValue);
	}
	
	private void initSeekBar() {
		SeekBar seekbar = findViewById(R.id.SeekBar);
		seekbar.setOnSeekBarChangeListener(this);
		
		seekbar.setMax(hashmarks.length - 1);
		int i = getStartingIndex();
		System.out.println("i: " + i + " " + hashmarks[i]);
		seekbar.setProgress(i);
		SeekBarValue.setText(Integer.toString(hashmarks[i]));
	}
	
	private int getStartingIndex() {
		for (int i=0; i<hashmarks.length; i++) {
			System.out.println(i + " " + hashmarks[i] + " " + oldValue);
			if (hashmarks[i] == Integer.parseInt(oldValue))
				return i;
		}
		return -1;
	}
	
	public void initButtons() {
		CancelButton = findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(this);
		
		SaveButton = findViewById(R.id.SaveButton);
		SaveButton.setOnClickListener(this);
	}	
		
	@Override
	public void onClick(View v) {
		if (v == CancelButton) 
			dismiss();
		else if (v == SaveButton) {
			if( mDialogResult != null ){
                mDialogResult.finish(newValue);
            }
			SettingsDialog.this.dismiss();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekbar, int value, boolean arg2) {
		newValue = Integer.toString(hashmarks[value]);
		SeekBarValue.setText(newValue);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {}	
}