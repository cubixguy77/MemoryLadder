package com.MemoryLadder.Cards;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.MemoryLadder.Constants;
import com.MemoryLadder.SettingsDialog;
import com.MemoryLadder.TestDetailsScreen.TestDetailsActivity;
import com.MemoryLadder.TimePickerDialog;
import com.MemoryLadder.Utils;
import com.mastersofmemory.memoryladder.R;

public class Cards_Settings extends Activity implements OnClickListener{
	
	private Button CancelButton;
	private Button SaveButton;
	private Button defaultSettings;
	private  int gameType;
	private  int cardsperdeck;
	private  int numdecks;
	private  int memTime;
	private  int recallTime;
	private  int cardsPerGroup;
	private boolean mnemo_enabled;
	private TextView tv_cardsperdeck;
	private TextView tv_numdecks;
	private TextView tv_memtime;
	private TextView tv_recalltime;
	private TextView tv_mnemo;
	private TextView tv_cardspergroup;
	
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
    	CancelButton = findViewById(R.id.CancelButton);
    	CancelButton.setOnClickListener(this);
        
    	SaveButton = findViewById(R.id.SaveButton);
    	SaveButton.setOnClickListener(this);

    	defaultSettings = findViewById(R.id.defaultSettings);
    	defaultSettings.setOnClickListener(this);
    }   
    
    public void initTable() {
    	tv_cardsperdeck = findViewById(R.id.tv_cardsperdeck);
    	tv_numdecks     = findViewById(R.id.tv_numdecks);
    	tv_memtime      = findViewById(R.id.tv_memtime);
    	tv_recalltime   = findViewById(R.id.tv_recalltime);
    	tv_mnemo        = findViewById(R.id.tv_mnemo);
    	tv_cardspergroup        = findViewById(R.id.tv_cardspergroup);

    	tv_cardsperdeck.setOnClickListener(this);
    	tv_numdecks.setOnClickListener(this);
    	tv_memtime.setOnClickListener(this);
    	tv_recalltime.setOnClickListener(this);
    	tv_mnemo.setOnClickListener(this);
    	tv_cardspergroup.setOnClickListener(this);
    }
        
    public void getExtras() {
    	Intent i = getIntent();
        gameType = i.getIntExtra("gameType",    -1);
    }
    
    public void getPreferences() {
    	SharedPreferences prefs = getSharedPreferences("Card_Preferences", 0);
    	
    	tv_cardsperdeck.setText(prefs.getString   ("tv_cardsperdeck",    Integer.toString(Constants.default_cards_deckSize)));
    	tv_numdecks.setText(prefs.getString       ("tv_numdecks",        Integer.toString(Constants.default_cards_numDecks)));
    	tv_memtime.setText(prefs.getString        ("tv_memtime",         Utils.formatIntoHHMMSS(Constants.default_cards_memTime)));
    	tv_recalltime.setText(prefs.getString     ("tv_recalltime",      Utils.formatIntoHHMMSS(Constants.default_cards_recallTime))); 
    	tv_mnemo.setText(prefs.getString          ("tv_mnemo",           "Off")); 
    	tv_cardspergroup.setText(prefs.getString          ("tv_cardspergroup", Integer.toString(Constants.default_cards_cardsPerGroup)));
    }
    
    public void updateSettings() {	
    	cardsperdeck = Integer.parseInt( tv_cardsperdeck.getText().toString() );
    	numdecks =     Integer.parseInt( tv_numdecks.getText().toString() );
    	memTime =      Utils.getTotalSeconds( tv_memtime.getText().toString() );    
    	recallTime =   Utils.getTotalSeconds( tv_recalltime.getText().toString() );		        
    	cardsPerGroup =   Integer.parseInt( tv_cardspergroup.getText().toString() );

        mnemo_enabled = tv_mnemo.getText().toString().equals("On");

    	System.out.println("gametype: "       + gameType);
    	System.out.println("cards per deck: " + cardsperdeck);
    	System.out.println("num decks: "      + numdecks);
    	System.out.println("memtime:  "       + memTime);
    	System.out.println("recalltime: "     + recallTime);
    	System.out.println("mnemo_enabled: "  + mnemo_enabled);
    	System.out.println("cards per group: "  + cardsPerGroup);
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
        editor.putString("tv_cardspergroup", tv_cardspergroup.getText().toString());

        /* In game variable values */
        editor.putInt("deckSize",          cardsperdeck);
        editor.putInt("numDecks",          numdecks);
        editor.putInt("memTime",           memTime);
        editor.putInt("recallTime",        recallTime);
        editor.putBoolean("mnemo_enabled", mnemo_enabled);
        editor.putInt("numCardsPerGroup",  cardsPerGroup);

        editor.commit();
    }
    
    public void setDefaultSettings() {
    	tv_cardsperdeck.setText(Integer.toString(Constants.default_cards_deckSize));
    	tv_numdecks.setText(Integer.toString(Constants.default_cards_numDecks));
    	tv_memtime.setText(Integer.toString(Constants.default_cards_memTime));
    	tv_recalltime.setText(Integer.toString(Constants.default_cards_recallTime)); 
    	tv_cardspergroup.setText(Integer.toString(Constants.default_cards_cardsPerGroup));
    }
    
	@Override
	public void onClick(View view) {
		if (view == CancelButton)
			onSettingsFinished();
		else if (view == SaveButton) {
			onSaveSettings();
		}
		else if (view == defaultSettings)
			setDefaultSettings();
		
		else if (view == tv_cardsperdeck) {
			SettingsDialog dialog = new SettingsDialog(this, tv_cardsperdeck.getText().toString(), "Cards per deck:", Constants.cards_tv_cardsperdeck);
			dialog.setDialogResult(result -> tv_cardsperdeck.setText(result));
			dialog.show();
		}
		else if (view == tv_cardspergroup) {
			SettingsDialog dialog = new SettingsDialog(this, tv_cardspergroup.getText().toString(), "Cards per group:", Constants.cards_tv_cardspergroup);
			dialog.setDialogResult(result -> tv_cardspergroup.setText(result));
			dialog.show();
		}
		else if (view == tv_numdecks) {
			SettingsDialog dialog = new SettingsDialog(this, tv_numdecks.getText().toString(), "Number of decks:", Constants.cards_tv_numdecks);
			dialog.setDialogResult(result -> tv_numdecks.setText(result));
			dialog.show();
		}
		else if (view == tv_memtime) {
			TimePickerDialog dialog = new TimePickerDialog(this, tv_memtime.getText().toString(), "Memorization Time");
			dialog.setDialogResult(result -> tv_memtime.setText(result));
			dialog.show();
		}
		else if (view == tv_recalltime) {
			TimePickerDialog dialog = new TimePickerDialog(this, tv_recalltime.getText().toString(), "Recall Time");
			dialog.setDialogResult(result -> tv_recalltime.setText(result));
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

    @Override
    public void onBackPressed() {
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