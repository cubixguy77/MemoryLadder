package com.MemoryLadder;

//import com.MemoryLadderFull.R;
import com.MemoryLadder.SettingsDialog.OnMyDialogResult;
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

public class Cards_Settings extends Activity implements OnClickListener{
	
	private Button CancelButton;
	private Button SaveButton;
	private Button wmc_CARDS_SPEED;
	private Button wmc_CARDS_LONG;
	private Button defaultSettings;
	private  int gameType;
	private  int cardsperdeck;
	private  int numdecks;
	private  int memTime;
	private  int recallTime;
	private Boolean mnemo_enabled;
	private Boolean showMnemo;
	private TextView tv_cardsperdeck;
	private TextView tv_numdecks;
	private TextView tv_memtime;
	private TextView tv_recalltime;
	private TextView tv_mnemo;
	private final int CARDS_SPEED  = Constants.CARDS_SPEED;
	private final int CARDS_LONG   = Constants.CARDS_LONG;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_cards);
        
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
    	
    	wmc_CARDS_SPEED = (Button) findViewById(R.id.wmc_CARDS_SPEED);
    	wmc_CARDS_SPEED.setOnClickListener(this);
    	
    	wmc_CARDS_LONG = (Button) findViewById(R.id.wmc_CARDS_LONG);
    	wmc_CARDS_LONG.setOnClickListener(this);
    	
    	defaultSettings = (Button) findViewById(R.id.defaultSettings);
    	defaultSettings.setOnClickListener(this);
    }   
    
    public void initTable() {
    	tv_cardsperdeck = (TextView) findViewById(R.id.tv_cardsperdeck);
    	tv_numdecks     = (TextView) findViewById(R.id.tv_numdecks);
    	tv_memtime      = (TextView) findViewById(R.id.tv_memtime);
    	tv_recalltime   = (TextView) findViewById(R.id.tv_recalltime);
    	tv_mnemo        = (TextView) findViewById(R.id.tv_mnemo);
    	
    	tv_cardsperdeck.setOnClickListener(this);
    	tv_numdecks.setOnClickListener(this);
    	tv_memtime.setOnClickListener(this);
    	tv_recalltime.setOnClickListener(this);
    	tv_mnemo.setOnClickListener(this);
    	
    	if (!showMnemo) {
    		TableRow row = (TableRow) findViewById(R.id.cards_tablerow5);
    		row.setVisibility(View.GONE);
    	}
    		
    }
        
    public void getExtras() {
    	Intent i = getIntent();
        gameType = i.getIntExtra("gameType",    -1);
        if (getIntent().getIntExtra("mode", -1) == Constants.CUSTOM)
        	showMnemo = true;
        else
        	showMnemo = false;
    }
    
    public void getPreferences() {
    	SharedPreferences prefs = getSharedPreferences("Card_Preferences", 0);
    	
    	tv_cardsperdeck.setText(prefs.getString   ("tv_cardsperdeck",    Integer.toString(Constants.default_cards_deckSize)));
    	tv_numdecks.setText(prefs.getString       ("tv_numdecks",        Integer.toString(Constants.default_cards_numDecks)));
    	tv_memtime.setText(prefs.getString        ("tv_memtime",         Utils.formatIntoHHMMSS(Constants.default_cards_memTime)));
    	tv_recalltime.setText(prefs.getString     ("tv_recalltime",      Utils.formatIntoHHMMSS(Constants.default_cards_recallTime))); 
    	tv_mnemo.setText(prefs.getString          ("tv_mnemo",           "Off")); 
    } 
    
    public void updateSettings() {	
    	cardsperdeck = Integer.parseInt( tv_cardsperdeck.getText().toString() );
    	numdecks =     Integer.parseInt( tv_numdecks.getText().toString() );
    	memTime =      Utils.getTotalSeconds( tv_memtime.getText().toString() );    
    	recallTime =   Utils.getTotalSeconds( tv_recalltime.getText().toString() );		        
    	
    	if (showMnemo) {
    		if (tv_mnemo.getText().toString().equals("On"))
    			mnemo_enabled = true;
    		else
    			mnemo_enabled = false;
    	}
    	
    	System.out.println("gametype: "       + gameType);
    	System.out.println("cards per deck: " + cardsperdeck);
    	System.out.println("num decks: "      + numdecks);
    	System.out.println("memtime:  "       + memTime);
    	System.out.println("recalltime: "     + recallTime);
    	System.out.println("mnemo_enabled: "  + mnemo_enabled);
    }
    
    public void commitPreferences() {
    	SharedPreferences settings = getSharedPreferences("Card_Preferences", 0);
        SharedPreferences.Editor editor = settings.edit();
        
        /* EditText Strings */
        editor.putString("tv_cardsperdeck",  tv_cardsperdeck.getText().toString());
        editor.putString("tv_numdecks",      tv_numdecks.getText().toString());
        editor.putString("tv_memtime",       tv_memtime.getText().toString());
        editor.putString("tv_recalltime",    tv_recalltime.getText().toString());
        editor.putString("tv_mnemo",         tv_mnemo.getText().toString());

        /* In game variable values */
        editor.putInt("deckSize",          cardsperdeck);
        editor.putInt("numDecks",          numdecks);
        editor.putInt("memTime",           memTime);
        editor.putInt("recallTime",        recallTime);
        editor.putBoolean("mnemo_enabled", mnemo_enabled);

        editor.commit();
    }
    
    public void setWMCsettings(int newGameType) {
    	gameType = newGameType;
    	if (gameType == CARDS_SPEED) {
	    	tv_cardsperdeck.setText(Integer.toString(Constants.wmc_cards_speed_deckSize));
	    	tv_numdecks.setText(Integer.toString(Constants.wmc_cards_speed_numDecks));
	    	tv_memtime.setText(Integer.toString(Constants.wmc_cards_speed_memTime));
	    	tv_recalltime.setText(Integer.toString(Constants.wmc_cards_speed_recallTime)); 
	    }
    	else {
    		tv_cardsperdeck.setText(Integer.toString(Constants.wmc_cards_long_deckSize));
	    	tv_numdecks.setText(Integer.toString(Constants.wmc_cards_long_numDecks));
	    	tv_memtime.setText(Integer.toString(Constants.wmc_cards_long_memTime));
	    	tv_recalltime.setText(Integer.toString(Constants.wmc_cards_long_recallTime));
    	}
    }
    
    public void setDefaultSettings() {
    	tv_cardsperdeck.setText(Integer.toString(Constants.default_cards_deckSize));
    	tv_numdecks.setText(Integer.toString(Constants.default_cards_numDecks));
    	tv_memtime.setText(Integer.toString(Constants.default_cards_memTime));
    	tv_recalltime.setText(Integer.toString(Constants.default_cards_recallTime)); 
    }
    
	@Override
	public void onClick(View view) {
		if (view == CancelButton)
			onSettingsFinished();
		else if (view == SaveButton) {
			onSaveSettings();
		}
		else if (view == wmc_CARDS_SPEED)
			setWMCsettings(CARDS_SPEED);
		else if (view == wmc_CARDS_LONG)
			setWMCsettings(CARDS_LONG);
		else if (view == defaultSettings)
			setDefaultSettings();
		
		else if (view == tv_cardsperdeck) {
			SettingsDialog dialog = new SettingsDialog(this, tv_cardsperdeck.getText().toString(), "Cards per deck:", Constants.cards_tv_cardsperdeck);
			dialog.setDialogResult(new OnMyDialogResult() {
			    public void finish(String result){  tv_cardsperdeck.setText(result);   }
			});
			dialog.show();
		}
		else if (view == tv_numdecks) {
			SettingsDialog dialog = new SettingsDialog(this, tv_numdecks.getText().toString(), "Number of decks:", Constants.cards_tv_numdecks);
			dialog.setDialogResult(new OnMyDialogResult() {
			    public void finish(String result){  tv_numdecks.setText(result);   }
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
    	i.setClass(this, Cards_PreGame.class);
		this.startActivity(i);
		finish();
    }
}