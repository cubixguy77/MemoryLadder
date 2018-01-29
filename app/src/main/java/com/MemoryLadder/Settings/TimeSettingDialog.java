package com.MemoryLadder.Settings;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class TimeSettingDialog extends Dialog implements OnClickListener {

	private Button CancelButton;
	private Button SaveButton;
	private int oldValue;
	private int newValue;

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

	private ValueChangeListener listener; // the callback

	public interface ValueChangeListener {
		void onValueChanged(int newValue);
	}

	public TimeSettingDialog(Context context, String LabelText, int oldValue) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.timepickerdialog);
		
		this.oldValue = oldValue;
		this.newValue = oldValue;
		
		initTimeVariables(oldValue);
		initButtons();
		initText(LabelText);

		setCancelable(true);	
	}
	
	private void initTimeVariables(int totalSeconds) {
        hours = (totalSeconds / 3600);
        int remainder = (totalSeconds % 3600);
        minutes = remainder / 60;
        seconds = remainder % 60;
	}

	public void setValueChangeListener(ValueChangeListener listener){
        this.listener = listener;
    }
	
	private void initText(String text) {
		TextView Label = findViewById(R.id.Label);
		Label.setText(text);
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

		newValue = (hours*3600) + (minutes*60) + (seconds);
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
			if(listener != null && oldValue != newValue){
                listener.onValueChanged(newValue);
            }

			dismiss();
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