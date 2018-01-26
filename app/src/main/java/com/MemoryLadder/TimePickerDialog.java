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
	
	Context context;
	Button CancelButton;
	Button SaveButton;
	String oldValue;
	String newValue;
	
	TextView Hours;
	TextView Minutes;
	TextView Seconds;
	
	Button HoursUp;
	Button HoursDown;
	Button MinutesUp;
	Button MinutesDown;	
	Button SecondsUp;
	Button SecondsDown;
	
	private int hours;
	private int minutes;
	private int seconds;
	
	final private static int UP = 0;
	final private static int DOWN = 1;
	
	final private static int MAX_HOUR = 5;
	
	OnMyDialogResultTime mDialogResult; // the callback
	
	public TimePickerDialog(Context context, String oldValue, String LabelText) {
		super(context);
		this.context = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.timepickerdialog);
		
		this.oldValue = oldValue;
		this.newValue = oldValue;
		
		initTimeVariables();	
		initButtons();
		initText(LabelText);
		
		
		setCancelable(true);	
	}
	
	public void initTimeVariables() {
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
	
	public void initText(String labeltext) {
		TextView Label = (TextView) findViewById(R.id.Label);
		Label.setText(labeltext);		
		Hours = (TextView) findViewById(R.id.Hours);
		Minutes = (TextView) findViewById(R.id.Minutes);
		Seconds = (TextView) findViewById(R.id.Seconds);
		
		refreshTimeDisplay();
	}
	
	public void initButtons() {
		CancelButton = (Button) findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(this);
		
		SaveButton = (Button) findViewById(R.id.SaveButton);
		SaveButton.setOnClickListener(this);
		
		HoursUp = (Button) findViewById(R.id.HoursUp);
		HoursUp.setOnClickListener(this);
		HoursDown = (Button) findViewById(R.id.HoursDown);
		HoursDown.setOnClickListener(this);
		
		MinutesUp = (Button) findViewById(R.id.MinutesUp);
		MinutesUp.setOnClickListener(this);
		MinutesDown = (Button) findViewById(R.id.MinutesDown);
		MinutesDown.setOnClickListener(this);
		
		SecondsUp = (Button) findViewById(R.id.SecondsUp);
		SecondsUp.setOnClickListener(this);
		SecondsDown = (Button) findViewById(R.id.SecondsDown);
		SecondsDown.setOnClickListener(this);		
	}	
	
	public void refreshTimeDisplay() {
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
	
	public void updateHours(int button) {
		if (button == UP && hours < MAX_HOUR) 
			hours++;		
		else if (button == DOWN && hours > 0)
			hours--;
		refreshTimeDisplay();
	}
	
	public void updateMinutes(int button) {
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
	
	public void updateSeconds(int button) {
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