package com.MemoryLadder;

import com.MemoryLadder.SettingsDialog.OnMyDialogResult;
import com.MemoryLadder.TestDetailsScreen.TestDetailsActivity;
import com.MemoryLadder.TimePickerDialog.OnMyDialogResultTime;
import com.mastersofmemory.memoryladder.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Numbers_Settings extends Activity implements OnClickListener{
	
	private Button CancelButton;
	private Button SaveButton;
	private Button wmcSettings;
	private Button defaultSettings;
	
	private int gameType;
	private Boolean showMnemo;
	
	private int WRITTEN_numRows;
	private int WRITTEN_numCols;
	private int WRITTEN_memTime;
	private int WRITTEN_recallTime;
	private Boolean WRITTEN_mnemo;
	
	private int SPOKEN_numRows;
	private int SPOKEN_numCols;
	private int SPOKEN_recallTime;
	private float SPOKEN_secondsPerDigit;	
	
	
	
	TableLayout table;
	TextView tv_numlines;
	TextView tv_digitsperline;
	TextView tv_memtime;
	TextView tv_numdigits;
	TextView tv_digitsperminute;
	TextView tv_recalltime;
	TextView tv_mnemo;
	Spinner spinner_digitspeed;
	
	final private static int NUMBERS_SPEED   = Constants.NUMBERS_SPEED;
//	final private static int NUMBERS_LONG    = Constants.NUMBERS_LONG;
	final private static int NUMBERS_BINARY  = Constants.NUMBERS_BINARY;
	final private static int NUMBERS_SPOKEN  = Constants.NUMBERS_SPOKEN;
	
	final static int SLOW   = 0;
	final static int MEDIUM = 1;
	final static int FAST   = 2;
	final static int SUPER_FAST = 3;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_numbers);
        
        getExtras();
        initButtons();
        initTable();        
        getPreferences();

    }
    
    public void initButtons() {
    	CancelButton = (Button) findViewById(R.id.CancelButton);
    	CancelButton.setOnClickListener(this);
        
    	SaveButton = (Button) findViewById(R.id.SaveButton);
    	SaveButton.setOnClickListener(this);
    	
    	wmcSettings = (Button) findViewById(R.id.wmcSettings);
    	wmcSettings.setOnClickListener(this);
    	
    	defaultSettings = (Button) findViewById(R.id.defaultSettings);
    	defaultSettings.setOnClickListener(this);
    }   
    
    public void initTable() {
    	TableRow row1 = (TableRow) findViewById(R.id.tablerow1);
    	TableRow row2 = (TableRow) findViewById(R.id.tablerow2);
    	TableRow row3 = (TableRow) findViewById(R.id.tablerow3);
    	TableRow row4 = (TableRow) findViewById(R.id.tablerow4);
    	TableRow row5 = (TableRow) findViewById(R.id.tablerow5);
    	TableRow row7 = (TableRow) findViewById(R.id.tablerow7);
    	
    	tv_numlines        = (TextView) findViewById(R.id.tv_numlines);
    	tv_digitsperline   = (TextView) findViewById(R.id.tv_digitsperline);
    	tv_memtime         = (TextView) findViewById(R.id.tv_memtime);
    	tv_numdigits       = (TextView) findViewById(R.id.tv_numdigits);
    	tv_recalltime      = (TextView) findViewById(R.id.tv_recalltime);
    	tv_mnemo           = (TextView) findViewById(R.id.tv_mnemo);
    	spinner_digitspeed = (Spinner) findViewById(R.id.spinner_digitSpeed);
    	
    	tv_numlines.setOnClickListener(this);
    	tv_digitsperline.setOnClickListener(this);
    	tv_memtime.setOnClickListener(this);
    	tv_numdigits.setOnClickListener(this);
    	tv_recalltime.setOnClickListener(this);
    	tv_mnemo.setOnClickListener(this);
    	
    	if (gameType == NUMBERS_SPOKEN) {
    		row1.setVisibility(View.GONE);
    		row2.setVisibility(View.GONE);
    		row3.setVisibility(View.GONE);
    		row7.setVisibility(View.GONE);
    	}
    	else if (showMnemo) {
    		row4.setVisibility(View.GONE);
    		row5.setVisibility(View.GONE);
    	}
    	else {
    		row4.setVisibility(View.GONE);
    		row5.setVisibility(View.GONE);
    		row7.setVisibility(View.GONE);
    	}
    }
        
    public void updateSettings() {
    	
    	if (gameType == NUMBERS_SPOKEN) {
    		SPOKEN_recallTime = Utils.getTotalSeconds( tv_recalltime.getText().toString() );
    		int numDigits = Integer.parseInt( tv_numdigits.getText().toString() );
    		if (numDigits <= 40)
    			SPOKEN_numCols = numDigits;
    		else
    			SPOKEN_numCols = 10;
    		SPOKEN_numRows = numDigits / SPOKEN_numCols;
    		
    		String digitSpeed = spinner_digitspeed.getSelectedItem().toString();
			switch (digitSpeed) {
				case "Slow":
					SPOKEN_secondsPerDigit = 3;
					break;
				case "Medium":
					SPOKEN_secondsPerDigit = 2;
					break;
				case "Fast":
					SPOKEN_secondsPerDigit = 1;
					break;
				case "Super Fast":
					SPOKEN_secondsPerDigit = 0.5f;
					break;
			}
    	}
    	else {
        	WRITTEN_numRows = Integer.parseInt( tv_numlines.getText().toString() );
        	WRITTEN_numCols = Integer.parseInt( tv_digitsperline.getText().toString() );
        	WRITTEN_memTime = Utils.getTotalSeconds( tv_memtime.getText().toString() );    
        	WRITTEN_recallTime = Utils.getTotalSeconds( tv_recalltime.getText().toString() );
        	
        	if (showMnemo) {
        		if (tv_mnemo.getText().toString().equals("On"))
        			WRITTEN_mnemo = true;
        		else
        			WRITTEN_mnemo = false;
        	}
        		
    	}
    	
    	System.out.println("numrows: "      + WRITTEN_numRows);
    	System.out.println("numcols: "      + WRITTEN_numCols);
    	System.out.println("gametype: "     + gameType);
    	System.out.println("memtime:  "     + WRITTEN_memTime);
    	System.out.println("recalltime: "   + WRITTEN_recallTime);
    	System.out.println("mnemo assist: " + WRITTEN_mnemo);
    }
    
    public void getExtras() {
    	Intent i = getIntent();
        gameType = i.getIntExtra("gameType",    -1);        
        if ((gameType == NUMBERS_SPEED || gameType == NUMBERS_BINARY) && getIntent().getIntExtra("mode", -1) == Constants.CUSTOM)
        	showMnemo = true;
        else
        	showMnemo = false;
    }
    
    public void getPreferences() {
    	SharedPreferences prefs = getSharedPreferences("Number_Preferences", 0);
    	
    	if (gameType == NUMBERS_SPEED) {
	    	tv_numlines.setText(prefs.getString          ("WRITTEN_tv_numlines",        Integer.toString(Constants.default_written_numRows)));
	    	tv_digitsperline.setText(prefs.getString     ("WRITTEN_tv_digitsperline",   Integer.toString(Constants.default_written_numCols)));
	    	tv_memtime.setText(prefs.getString           ("WRITTEN_tv_memtime",         Utils.formatIntoHHMMSS(Constants.default_written_memTime)));
	    	tv_recalltime.setText(prefs.getString        ("WRITTEN_tv_recalltime",      Utils.formatIntoHHMMSS(Constants.default_written_recallTime)));
	    	tv_mnemo.setText(prefs.getString             ("WRITTEN_tv_mnemo",           "Off"));
    	}
    	else if (gameType == NUMBERS_BINARY) {
    		tv_numlines.setText(prefs.getString          ("BINARY_tv_numlines",        Integer.toString(Constants.default_binary_numRows)));
	    	tv_digitsperline.setText(prefs.getString     ("BINARY_tv_digitsperline",   Integer.toString(Constants.default_binary_numCols)));
	    	tv_memtime.setText(prefs.getString           ("BINARY_tv_memtime",         Utils.formatIntoHHMMSS(Constants.default_binary_memTime)));
	    	tv_recalltime.setText(prefs.getString        ("BINARY_tv_recalltime",      Utils.formatIntoHHMMSS(Constants.default_binary_recallTime)));
	    	tv_mnemo.setText(prefs.getString             ("BINARY_tv_mnemo",           "Off"));
	    }
    	else {
	    	tv_numlines.setText(prefs.getString          ("SPOKEN_tv_numlines",        Integer.toString(Constants.default_spoken_numRows)));
	    	tv_digitsperline.setText(prefs.getString     ("SPOKEN_tv_digitsperline",   Integer.toString(Constants.default_spoken_numCols)));
	    	tv_numdigits.setText(prefs.getString         ("SPOKEN_tv_numdigits",       Integer.toString(Constants.default_spoken_numDigits)));
	    	tv_recalltime.setText(prefs.getString        ("SPOKEN_tv_recalltime",      Utils.formatIntoHHMMSS(Constants.default_spoken_recallTime)));
	    	spinner_digitspeed.setSelection(prefs.getInt ("SPOKEN_spinner_digitspeed", Constants.default_spoken_digitSpeed));
    	}
    }
    
    public void commitPreferences() {
    	SharedPreferences settings = getSharedPreferences("Number_Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        
        /* EditText Strings */
        if (gameType == NUMBERS_SPEED) {
	        editor.putString("WRITTEN_tv_numlines",      tv_numlines.getText().toString());
	        editor.putString("WRITTEN_tv_digitsperline", tv_digitsperline.getText().toString());
	        editor.putString("WRITTEN_tv_memtime",       tv_memtime.getText().toString());
	        editor.putString("WRITTEN_tv_numdigits",     tv_numdigits.getText().toString());
	        editor.putString("WRITTEN_tv_recalltime",    tv_recalltime.getText().toString());
	        editor.putInt("WRITTEN_spinner_digitspeed",  spinner_digitspeed.getSelectedItemPosition());
	        editor.putString("WRITTEN_tv_mnemo", tv_mnemo.getText().toString());
        }
        else if (gameType == NUMBERS_BINARY) {
        	editor.putString("BINARY_tv_numlines",      tv_numlines.getText().toString());
	        editor.putString("BINARY_tv_digitsperline", tv_digitsperline.getText().toString());
	        editor.putString("BINARY_tv_memtime",       tv_memtime.getText().toString());
	        editor.putString("BINARY_tv_numdigits",     tv_numdigits.getText().toString());
	        editor.putString("BINARY_tv_recalltime",    tv_recalltime.getText().toString());
	        editor.putInt("BINARY_spinner_digitspeed",  spinner_digitspeed.getSelectedItemPosition());
	        editor.putString("BINARY_tv_mnemo",         tv_mnemo.getText().toString());
        }
        else {
	        editor.putString("SPOKEN_tv_numlines",      tv_numlines.getText().toString());
	        editor.putString("SPOKEN_tv_digitsperline", tv_digitsperline.getText().toString());
	        editor.putString("SPOKEN_tv_memtime",       tv_memtime.getText().toString());
	        editor.putString("SPOKEN_tv_numdigits",     tv_numdigits.getText().toString());
	        editor.putString("SPOKEN_tv_recalltime",    tv_recalltime.getText().toString());
	        editor.putInt("SPOKEN_spinner_digitspeed",  spinner_digitspeed.getSelectedItemPosition());
        }
        
        /* In game variable values */
        if (gameType == NUMBERS_SPEED) {
	        editor.putInt("WRITTEN_numRows",    WRITTEN_numRows);
	        editor.putInt("WRITTEN_numCols",    WRITTEN_numCols);
	        editor.putInt("WRITTEN_memTime",    WRITTEN_memTime);
	        editor.putInt("WRITTEN_recallTime", WRITTEN_recallTime);
	        editor.putBoolean("WRITTEN_mnemo",  WRITTEN_mnemo);
        }
        else if (gameType == NUMBERS_BINARY) {
        	editor.putInt("BINARY_numRows",    WRITTEN_numRows);
	        editor.putInt("BINARY_numCols",    WRITTEN_numCols);
	        editor.putInt("BINARY_memTime",    WRITTEN_memTime);
	        editor.putInt("BINARY_recallTime", WRITTEN_recallTime);
	        editor.putBoolean("BINARY_mnemo",  WRITTEN_mnemo);
        }
        else {
	        editor.putInt("SPOKEN_numRows",    SPOKEN_numRows);
	        editor.putInt("SPOKEN_numCols",    SPOKEN_numCols);
	        editor.putInt("SPOKEN_recallTime", SPOKEN_recallTime);
	        editor.putFloat("SPOKEN_secondsPerDigit",    SPOKEN_secondsPerDigit);
        }
        editor.commit();
    }
    
    public void setWMCsettings() {
    	if (gameType != NUMBERS_SPOKEN) {
    		tv_numlines.setText("15");
        	tv_digitsperline.setText("40");
        	tv_memtime.setText("00:05:00");
        	tv_recalltime.setText("00:15:00");
    	}
    	else {
        	tv_numdigits.setText("200");
        	tv_recalltime.setText("00:10:00");
        	spinner_digitspeed.setSelection(MEDIUM);
    	}
    }
    
    public void setDefaultSettings() {
    	if (gameType != NUMBERS_SPOKEN) {
    		tv_numlines.setText(Integer.toString(Constants.default_written_numRows));
        	tv_digitsperline.setText(Integer.toString(Constants.default_written_numCols));
        	tv_memtime.setText(Utils.formatIntoHHMMSS(Constants.default_written_memTime));
        	tv_recalltime.setText(Utils.formatIntoHHMMSS(Constants.default_written_recallTime));
    	}
    	else {
        	tv_numdigits.setText(Integer.toString(Constants.default_spoken_numDigits));
        	tv_recalltime.setText(Utils.formatIntoHHMMSS(Constants.default_spoken_recallTime));
        	spinner_digitspeed.setSelection(Constants.default_spoken_digitSpeed);
    	}
    }
    
    
	@Override
	public void onClick(View view) {
		if (view == CancelButton)
			onSettingsFinished();
		else if (view == SaveButton) {
			onSaveSettings();
		}
		else if (view == wmcSettings)
			setWMCsettings();
		else if (view == defaultSettings)
			setDefaultSettings();
		else if (view == tv_numlines) {
			SettingsDialog dialog = new SettingsDialog(this, tv_numlines.getText().toString(), "Number of lines:", Constants.numbers_tv_numlines);
			dialog.setDialogResult(new OnMyDialogResult() {
			    public void finish(String result){  tv_numlines.setText(result);   }
			});
			dialog.show();
		}
		else if (view == tv_digitsperline) {
			SettingsDialog dialog = new SettingsDialog(this, tv_digitsperline.getText().toString(), "Digits per line:", Constants.numbers_tv_digitsperline);
			dialog.setDialogResult(new OnMyDialogResult() {
			    public void finish(String result){  tv_digitsperline.setText(result);   }
			});
			dialog.show();
		}
		else if (view == tv_numdigits) {
			SettingsDialog dialog = new SettingsDialog(this, tv_numdigits.getText().toString(), "Number of digits:", Constants.numbers_tv_numdigits);
			dialog.setDialogResult(new OnMyDialogResult() {
			    public void finish(String result){  tv_numdigits.setText(result);   }
			});
			dialog.show();
		}
		
		else if (view == tv_memtime) {
			TimePickerDialog dialog = new TimePickerDialog(this, tv_memtime.getText().toString(), "Memorization Time");
			dialog.setDialogResult(new OnMyDialogResultTime() {
			    public void finish(String result){  tv_memtime.setText(result);   }
			});
			dialog.show();
		}
		else if (view == tv_recalltime) {
			TimePickerDialog dialog = new TimePickerDialog(this, tv_recalltime.getText().toString(), "Recall Time");
			dialog.setDialogResult(new OnMyDialogResultTime() {
			    public void finish(String result){  tv_recalltime.setText(result);   }
			});
			dialog.show();
		}
		else if (view == tv_mnemo) {
			if (tv_mnemo.getText().toString().equals("On"))
				tv_mnemo.setText("Off");
			else
				tv_mnemo.setText("On");
		}
	}

    public void onSaveSettings() {    	
    	
    		updateSettings();
    		commitPreferences();
    		onSettingsFinished();
    	
    }
    
    
    public void onSettingsFinished() {
    	Intent i = getIntent();
		i.setClass(this, TestDetailsActivity.class);
		i.putExtra("gameType", gameType);
		startActivity(i);
		finish();
    }
}