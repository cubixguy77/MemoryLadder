package com.MemoryLadder;

//import com.MemoryLadderFull.R;
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
import android.widget.TableRow;
import android.widget.TextView;

public class Lists_Settings extends Activity implements OnClickListener{
	
	private Button CancelButton;
	private Button SaveButton;
	private Button wmcSettings;
	private Button defaultSettings;
	private int gameType;

	private int WORDS_numRows;
	private int WORDS_numCols;
	private int WORDS_memTime;
	private int WORDS_recallTime;
	
	private int DATES_numRows;
	private int DATES_numCols;
	private int DATES_memTime;
	private int DATES_recallTime;
	
	
	private TextView tv_numcolumns;
	private TextView tv_wordspercolumn;
	private TextView tv_numdates;	
	private TextView tv_memtime;
	private TextView tv_recalltime;
	private static final int LISTS_WORDS  = Constants.LISTS_WORDS;
//	private static final int LISTS_EVENTS = Constants.LISTS_EVENTS;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_lists);
        
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
    	TableRow row1 = (TableRow) findViewById(R.id.lists_tablerow1);
    	TableRow row2 = (TableRow) findViewById(R.id.lists_tablerow2);
    	TableRow row3 = (TableRow) findViewById(R.id.lists_tablerow3);
    	
    	tv_numcolumns     = (TextView) findViewById(R.id.tv_numcolumns);
    	tv_wordspercolumn = (TextView) findViewById(R.id.tv_wordspercolumn);
    	tv_numdates       = (TextView) findViewById(R.id.tv_numdates);
    	tv_memtime        = (TextView) findViewById(R.id.tv_memtime);
    	tv_recalltime     = (TextView) findViewById(R.id.tv_recalltime);
    	
    	tv_numcolumns.setOnClickListener(this);
    	tv_wordspercolumn.setOnClickListener(this);
    	tv_numdates.setOnClickListener(this);
    	tv_memtime.setOnClickListener(this);
    	tv_recalltime.setOnClickListener(this);
    	
    	if (gameType == LISTS_WORDS)
    		row3.setVisibility(View.GONE);    	
    	else {
    		row1.setVisibility(View.GONE);
    		row2.setVisibility(View.GONE);
    	}
    }
        
    public void getExtras() {
    	Intent i = getIntent();
        gameType = i.getIntExtra("gameType",    -1);
    }
    
    public void getPreferences() {
    	SharedPreferences prefs = getSharedPreferences("List_Preferences", 0);
    	
    	if (gameType == LISTS_WORDS) {
	    	tv_numcolumns.setText(prefs.getString     ("WORDS_tv_numcolumns",      Integer.toString(Constants.default_words_numCols)));
	    	tv_wordspercolumn.setText(prefs.getString ("WORDS_tv_wordspercolumn",  Integer.toString(Constants.default_words_numRows)));
	    	tv_memtime.setText(prefs.getString        ("WORDS_tv_memtime",         Utils.formatIntoHHMMSS(Constants.default_words_memTime)));
	    	tv_recalltime.setText(prefs.getString     ("WORDS_tv_recalltime",      Utils.formatIntoHHMMSS(Constants.default_words_recallTime)));
    	}
    	else {
	    	tv_numcolumns.setText(prefs.getString     ("DATES_tv_numcolumns",      Integer.toString(Constants.default_dates_numCols)));
	    	tv_numdates.setText(prefs.getString       ("DATES_tv_numdates",        Integer.toString(Constants.default_dates_numRows)));
	    	tv_memtime.setText(prefs.getString        ("DATES_tv_memtime",         Utils.formatIntoHHMMSS(Constants.default_dates_memTime)));
	    	tv_recalltime.setText(prefs.getString     ("DATES_tv_recalltime",      Utils.formatIntoHHMMSS(Constants.default_dates_recallTime)));
    	}
    }
    
    public void updateSettings() {	
    	if (gameType == LISTS_WORDS) {
    		WORDS_numCols = Integer.parseInt( tv_numcolumns.getText().toString() );
    		WORDS_numRows = Integer.parseInt( tv_wordspercolumn.getText().toString() );
    		WORDS_memTime    =   Utils.getTotalSeconds( tv_memtime.getText().toString() ); 
    		WORDS_recallTime =   Utils.getTotalSeconds( tv_recalltime.getText().toString() );	
    	}
    	else {
    		DATES_numCols = 1;
        	DATES_numRows = Integer.parseInt( tv_numdates.getText().toString() );
        	DATES_memTime    =   Utils.getTotalSeconds( tv_memtime.getText().toString() );   
        	DATES_recallTime =   Utils.getTotalSeconds( tv_recalltime.getText().toString() );	
        	
    	}        
    	
    	System.out.println("gametype: "       + gameType);
    	System.out.println("num rows: "       + WORDS_numRows);
    	System.out.println("num cols: "       + WORDS_numCols);
    	System.out.println("memtime:  "       + WORDS_memTime);
    	System.out.println("recalltime: "     + WORDS_recallTime);
    }
    
    public void commitPreferences() {
    	SharedPreferences settings = getSharedPreferences("List_Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        
        /* EditText Strings */
        if (gameType == LISTS_WORDS) {
	        editor.putString("WORDS_tv_numcolumns",     tv_numcolumns.getText().toString());
	        editor.putString("WORDS_tv_wordspercolumn", tv_wordspercolumn.getText().toString());
	        editor.putString("WORDS_tv_numdates",       tv_numdates.getText().toString());
	        editor.putString("WORDS_tv_memtime",        tv_memtime.getText().toString());
	        editor.putString("WORDS_tv_recalltime",     tv_recalltime.getText().toString());
        }
        else {
	        editor.putString("DATES_tv_numcolumns",     tv_numcolumns.getText().toString());
	        editor.putString("DATES_tv_wordspercolumn", tv_wordspercolumn.getText().toString());
	        editor.putString("DATES_tv_numdates",       tv_numdates.getText().toString());
	        editor.putString("DATES_tv_memtime",        tv_memtime.getText().toString());
	        editor.putString("DATES_tv_recalltime",     tv_recalltime.getText().toString());
        }
        /* In game variable values */
        if (gameType == LISTS_WORDS) {
	        editor.putInt("WORDS_numRows",     WORDS_numRows);
	        editor.putInt("WORDS_numCols",     WORDS_numCols);
	        editor.putInt("WORDS_memTime",     WORDS_memTime);
	        editor.putInt("WORDS_recallTime",  WORDS_recallTime);
        }
        else {
	        editor.putInt("DATES_numRows",     DATES_numRows);
	        editor.putInt("DATES_numCols",     DATES_numCols);
	        editor.putInt("DATES_memTime",     DATES_memTime);
	        editor.putInt("DATES_recallTime",  DATES_recallTime);
        }
        editor.commit();
    }
    
    public void setWMCsettings() {
    	if (gameType == LISTS_WORDS) {
    		tv_numcolumns.setText(Integer.toString(Constants.wmc_words_numCols));
        	tv_wordspercolumn.setText(Integer.toString(Constants.wmc_words_numRows));
        	tv_memtime.setText(Integer.toString(Constants.wmc_words_memTime));
        	tv_recalltime.setText(Integer.toString(Constants.wmc_words_recallTime));  
    	}
    	else {
        	tv_numdates.setText(Integer.toString(Constants.wmc_dates_numRows));
        	tv_memtime.setText(Integer.toString(Constants.wmc_dates_memTime));
        	tv_recalltime.setText(Integer.toString(Constants.wmc_dates_recallTime));  
    	}
    }
    
    public void setDefaultSettings() {
    	if (gameType == LISTS_WORDS) {
    		tv_numcolumns.setText(Integer.toString(Constants.default_words_numCols));
        	tv_wordspercolumn.setText(Integer.toString(Constants.default_words_numRows));
        	tv_memtime.setText(Integer.toString(Constants.default_words_memTime));
        	tv_recalltime.setText(Integer.toString(Constants.default_words_recallTime));  
    	}
    	else {
        	tv_numdates.setText(Integer.toString(Constants.default_dates_numRows));
        	tv_memtime.setText(Integer.toString(Constants.default_dates_memTime));
        	tv_recalltime.setText(Integer.toString(Constants.default_dates_recallTime));
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
		else if (view == tv_numcolumns) {
			SettingsDialog dialog = new SettingsDialog(this, tv_numcolumns.getText().toString(), "Number of columns:", Constants.lists_tv_numcolumns);
			dialog.setDialogResult(new OnMyDialogResult() {
			    public void finish(String result){  tv_numcolumns.setText(result);   }
			});
			dialog.show();
		}
		else if (view == tv_wordspercolumn) {
			SettingsDialog dialog = new SettingsDialog(this, tv_wordspercolumn.getText().toString(), "Words per column:", Constants.lists_tv_wordspercolumn);
			dialog.setDialogResult(new OnMyDialogResult() {
			    public void finish(String result){  tv_wordspercolumn.setText(result);   }
			});
			dialog.show();
		}
		else if (view == tv_numdates) {
			SettingsDialog dialog = new SettingsDialog(this, tv_numdates.getText().toString(), "Number of dates:", Constants.lists_tv_numdates);
			dialog.setDialogResult(new OnMyDialogResult() {
			    public void finish(String result){  tv_numdates.setText(result);   }
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
        i.putExtra("mode", Constants.CUSTOM);
		this.startActivity(i);
		finish();
    }
}