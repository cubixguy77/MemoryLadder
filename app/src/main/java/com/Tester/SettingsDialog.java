package com.Tester;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
//import com.MemoryLadderFull.R;

public class SettingsDialog extends Dialog implements OnClickListener, OnSeekBarChangeListener {
	
	Context context;
	Button CancelButton;
	Button SaveButton;
	TextView SeekBarValue;
	String oldValue;
	String newValue;
	int[] hashmarks;
	
	OnMyDialogResult mDialogResult; // the callback
	
	public SettingsDialog(Context context, String oldValue, String LabelText, int[] hashmarks) {
		super(context);
		this.context = context;
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
	
	public void initText(String labeltext) {
		TextView Label = (TextView) findViewById(R.id.Label);
		Label.setText(labeltext);
		
		SeekBarValue = (TextView) findViewById(R.id.SeekBarValue);
	}
	
	public void initSeekBar() {
		SeekBar seekbar = (SeekBar) findViewById(R.id.SeekBar);
		seekbar.setOnSeekBarChangeListener(this);
		
		seekbar.setMax(hashmarks.length - 1);
		int i = getStartingIndex();
		System.out.println("i: " + i + " " + hashmarks[i]);
		seekbar.setProgress(i);
		SeekBarValue.setText(Integer.toString(hashmarks[i]));
	}
	
	public int getStartingIndex() {
		for (int i=0; i<hashmarks.length; i++) {
			System.out.println(i + " " + hashmarks[i] + " " + oldValue);
			if (hashmarks[i] == Integer.parseInt(oldValue))
				return i;
		}
		return -1;
	}
	
	public void initButtons() {
		CancelButton = (Button) findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(this);
		
		SaveButton = (Button) findViewById(R.id.SaveButton);
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