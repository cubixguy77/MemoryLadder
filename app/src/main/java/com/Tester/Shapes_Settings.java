package com.Tester;

//import com.MemoryLadderFull.R;
import com.Tester.SettingsDialog.OnMyDialogResult;
import com.Tester.TimePickerDialog.OnMyDialogResultTime;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Shapes_Settings extends Activity implements OnClickListener{
	
	private Button CancelButton;
	private Button SaveButton;
	private Button wmcSettings;
	private Button defaultSettings;
	
	private  int gameType;

	
	private  int FACES_numimages;
	private  int FACES_numRows;
	private  int FACES_numCols;
	private  int FACES_memTime;
	private  int FACES_recallTime;
	
	private  int ABSTRACT_numimages;
	private  int ABSTRACT_numRows;
	private  int ABSTRACT_numCols;
	private  int ABSTRACT_memTime;
	private  int ABSTRACT_recallTime;
	
	
	
	
	TextView text_numimages;
	TextView tv_numimages;
	TextView tv_memtime;
	TextView tv_recalltime;
	final private static int SHAPES_FACES    = Constants.SHAPES_FACES;
//	final private static int SHAPES_ABSTRACT = Constants.SHAPES_ABSTRACT;	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_shapes);
        
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
    	text_numimages =  (TextView) findViewById(R.id.text_numimages);
    	tv_numimages    = (TextView) findViewById(R.id.tv_numimages);
    	tv_memtime      = (TextView) findViewById(R.id.tv_memtime);
    	tv_recalltime   = (TextView) findViewById(R.id.tv_recalltime);
    	
    	tv_numimages.setOnClickListener(this);
    	tv_memtime.setOnClickListener(this);
    	tv_recalltime.setOnClickListener(this);
    	
    	if (gameType == SHAPES_FACES)
    		text_numimages.setText("Number of faces:      ");
    	else
    		text_numimages.setText("Number of images:     ");
    }
            
    public void getExtras() {
    	Intent i = getIntent();
        gameType = i.getIntExtra("gameType",    -1);
    }
    
    public void getPreferences() {
    	SharedPreferences prefs = getSharedPreferences("Shape_Preferences", 0);
    	
    	if (gameType == SHAPES_FACES) { 
	    	tv_numimages.setText(prefs.getString      ("FACES_tv_numimages",       Integer.toString(Constants.default_faces_numImages)));
	    	tv_memtime.setText(prefs.getString        ("FACES_tv_memtime",         Utils.formatIntoHHMMSS(Constants.default_faces_memTime)));
	    	tv_recalltime.setText(prefs.getString     ("FACES_tv_recalltime",      Utils.formatIntoHHMMSS(Constants.default_faces_recallTime)));
    	}
    	else {
	    	tv_numimages.setText(prefs.getString      ("ABSTRACT_tv_numimages",       Integer.toString(Constants.default_abstract_numImages)));
	    	tv_memtime.setText(prefs.getString        ("ABSTRACT_tv_memtime",         Utils.formatIntoHHMMSS(Constants.default_abstract_memTime)));
	    	tv_recalltime.setText(prefs.getString     ("ABSTRACT_tv_recalltime",      Utils.formatIntoHHMMSS(Constants.default_abstract_recallTime))); 
    	}
    }
    
    public void updateSettings() {
    	
    		        
    	
    	if (gameType == SHAPES_FACES) { 
    		FACES_numCols = 3;
    		FACES_numimages    = Integer.parseInt( tv_numimages.getText().toString() );
    		FACES_memTime =      Utils.getTotalSeconds( tv_memtime.getText().toString() );    
    		FACES_recallTime =   Utils.getTotalSeconds( tv_recalltime.getText().toString() );	
    		
    		FACES_numRows = FACES_numimages / FACES_numCols;
    		if (!(FACES_numimages % FACES_numCols == 0))
    			FACES_numRows++;
    	}
    	else {
    		ABSTRACT_numCols = 5;
    		ABSTRACT_numimages    = Integer.parseInt( tv_numimages.getText().toString() );
    		ABSTRACT_memTime =      Utils.getTotalSeconds( tv_memtime.getText().toString() );    
    		ABSTRACT_recallTime =   Utils.getTotalSeconds( tv_recalltime.getText().toString() );	
    		
    		ABSTRACT_numRows = ABSTRACT_numimages / ABSTRACT_numCols;
    		if (!(ABSTRACT_numimages % ABSTRACT_numCols == 0))
    			ABSTRACT_numRows++;
    	}
    	
    	System.out.println("gametype: "       + gameType);
    	System.out.println("num images: "     + FACES_numimages);
    	System.out.println("num rows: "       + FACES_numRows);
    	System.out.println("num cols: "       + FACES_numCols);
    	System.out.println("memtime:  "       + FACES_memTime);
    	System.out.println("recalltime: "     + FACES_recallTime);
    }
    
    public void commitPreferences() {
    	SharedPreferences settings = getSharedPreferences("Shape_Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        
        /* EditText Strings */
        if (gameType == SHAPES_FACES) {
	        editor.putString("FACES_tv_numimages",     tv_numimages.getText().toString());
	        editor.putString("FACES_tv_memtime",       tv_memtime.getText().toString());
	        editor.putString("FACES_tv_recalltime",    tv_recalltime.getText().toString());
        }
        else {
	        editor.putString("ABSTRACT_tv_numimages",     tv_numimages.getText().toString());
	        editor.putString("ABSTRACT_tv_memtime",       tv_memtime.getText().toString());
	        editor.putString("ABSTRACT_tv_recalltime",    tv_recalltime.getText().toString());
        }
        
        /* In game variable values */
        if (gameType == SHAPES_FACES) {
	        editor.putInt("FACES_numRows",     FACES_numRows);
	        editor.putInt("FACES_numCols",     FACES_numCols);
	        editor.putInt("FACES_memTime",     FACES_memTime);
	        editor.putInt("FACES_recallTime",  FACES_recallTime);
        }
        else {
	        editor.putInt("ABSTRACT_numRows",     ABSTRACT_numRows);
	        editor.putInt("ABSTRACT_numCols",     ABSTRACT_numCols);
	        editor.putInt("ABSTRACT_memTime",     ABSTRACT_memTime);
	        editor.putInt("ABSTRACT_recallTime",  ABSTRACT_recallTime);
        }
        editor.commit();
    }
    
    public void setWMCsettings() {
    	if (gameType == SHAPES_FACES) {
    		tv_numimages.setText(Integer.toString(Constants.wmc_faces_numImages));
    		tv_memtime.setText(Integer.toString(Constants.wmc_faces_memTime));
    		tv_recalltime.setText(Integer.toString(Constants.wmc_faces_recallTime));
    	}
    	else {
    		tv_numimages.setText(Integer.toString(Constants.wmc_abstract_numImages));
    		tv_memtime.setText(Integer.toString(Constants.wmc_abstract_memTime));
    		tv_recalltime.setText(Integer.toString(Constants.wmc_abstract_recallTime)); 
    	}
    }
    
    public void setDefaultSettings() {
    	if (gameType == SHAPES_FACES) {
    		tv_numimages.setText(Integer.toString(Constants.default_faces_numImages));
    		tv_memtime.setText(Integer.toString(Constants.default_faces_memTime));
    		tv_recalltime.setText(Integer.toString(Constants.default_faces_recallTime));
    	}
    	else {
    		tv_numimages.setText(Integer.toString(Constants.default_abstract_numImages));
    		tv_memtime.setText(Integer.toString(Constants.default_abstract_memTime));
    		tv_recalltime.setText(Integer.toString(Constants.default_abstract_recallTime)); 
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
		else if (view == tv_numimages) {
			SettingsDialog dialog;
			if (gameType == SHAPES_FACES)
				dialog = new SettingsDialog(this, tv_numimages.getText().toString(), "Number of faces:", Constants.shapes_faces_tv_numimages);
			else
				dialog = new SettingsDialog(this, tv_numimages.getText().toString(), "Number of images:", Constants.shapes_abstract_tv_numimages);
			dialog.setDialogResult(new OnMyDialogResult() {
			    public void finish(String result){  tv_numimages.setText(result);   }
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
    	i.setClass(this, Shapes_PreGame.class);		
		this.startActivity(i);
		finish();
    }
}