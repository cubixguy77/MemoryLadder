package com.MemoryLadder;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class TimePickerDialog extends Dialog implements OnClickListener {

	private Button CancelButton;
	private Button SaveButton;
	private String oldValue;
	private String newValue;
	
	private TextView Hours;
	private TextView Minutes;
	private TextView Seconds;
	
	private Button HoursUp;
	private Button HoursDown;
	private Button MinutesUp;
	private Button MinutesDown;
	private Button SecondsUp;
	private Button SecondsDown;
	
	private int hours;
	private int minutes;
	private int seconds;
	
	final private static int UP = 0;
	final private static int DOWN = 1;
	
	final private static int MAX_HOUR = 5;
	
	private OnMyDialogResultTime mDialogResult; // the callback
	
	public TimePickerDialog(Context context, String oldValue, String LabelText) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.timepickerdialog);
		
		this.oldValue = oldValue;
		this.newValue = oldValue;
		
		initTimeVariables();	
		initButtons();
		initText(LabelText);
		
		
		setCancelable(true);	
	}
	
	private void initTimeVariables() {
		hours = Utils.getHours(oldValue);
		minutes = Utils.getMinutes(oldValue);
		seconds = Utils.getSeconds(oldValue);
	}

	public interface OnMyDialogResultTime{
	       void finish(String result);
	}
	
	public void setDialogResult(OnMyDialogResultTime dialogResult){
        mDialogResult = dialogResult;
    }
	
	private void initText(String labeltext) {
		TextView Label = findViewById(R.id.Label);
		Label.setText(labeltext);		
		Hours = findViewById(R.id.Hours);
		Minutes = findViewById(R.id.Minutes);
		Seconds = findViewById(R.id.Seconds);
		
		refreshTimeDisplay();
	}
	
	public void initButtons() {
		CancelButton = findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(this);
		
		SaveButton = findViewById(R.id.SaveButton);
		SaveButton.setOnClickListener(this);
		
		HoursUp = findViewById(R.id.HoursUp);
		HoursUp.setOnClickListener(this);
		HoursDown = findViewById(R.id.HoursDown);
		HoursDown.setOnClickListener(this);
		
		MinutesUp = findViewById(R.id.MinutesUp);
		MinutesUp.setOnClickListener(this);
		MinutesDown = findViewById(R.id.MinutesDown);
		MinutesDown.setOnClickListener(this);
		
		SecondsUp = findViewById(R.id.SecondsUp);
		SecondsUp.setOnClickListener(this);
		SecondsDown = findViewById(R.id.SecondsDown);
		SecondsDown.setOnClickListener(this);		
	}	
	
	private void refreshTimeDisplay() {
		String hours_string;
		String minutes_string;
		String seconds_string;
		
		hours_string = "0" + hours;
		
		if (minutes < 10)
			minutes_string = "0" + minutes;
		else
			minutes_string = Integer.toString(minutes);
		
		if (seconds < 10)
			seconds_string = "0" + seconds;
		else
			seconds_string = Integer.toString(seconds);
		
		Hours.setText(hours_string);
		Minutes.setText(minutes_string);
		Seconds.setText(seconds_string);
		
		newValue = hours_string + ":" + minutes_string + ":" + seconds_string;
	}
	
	private void updateHours(int button) {
		if (button == UP && hours < MAX_HOUR) 
			hours++;		
		else if (button == DOWN && hours > 0)
			hours--;
		refreshTimeDisplay();
	}
	
	private void updateMinutes(int button) {
		if (button == UP && minutes < 59) 
			minutes++;
		else if (button == UP && minutes == 59)
			minutes = 0;
		else if (button == DOWN && minutes > 0)
			minutes--;
		else if (button == DOWN && minutes == 0)
			minutes = 59;
		refreshTimeDisplay();
	}
	
	private void updateSeconds(int button) {
		if (button == UP && seconds < 59) 
			seconds++;	
		else if (button == UP && seconds == 59)
			seconds = 0;
		else if (button == DOWN && seconds > 0)
			seconds--;
		else if (button == DOWN && seconds == 0)
			seconds = 59;
		refreshTimeDisplay();
	}
		
	@Override
	public void onClick(View v) {
		if (v == CancelButton) 
			dismiss();
		else if (v == SaveButton) {
			if( mDialogResult != null ){
                mDialogResult.finish(newValue);
            }
			TimePickerDialog.this.dismiss();
		}
		else if (v == HoursUp)
			updateHours(UP);
		else if (v == HoursDown)
			updateHours(DOWN);
		else if (v == MinutesUp)
			updateMinutes(UP);
		else if (v == MinutesDown)
			updateMinutes(DOWN);
		else if (v == SecondsUp)
			updateSeconds(UP);
		else if (v == SecondsDown)
			updateSeconds(DOWN);
	}	
}