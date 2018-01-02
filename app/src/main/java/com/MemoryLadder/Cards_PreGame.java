package com.MemoryLadder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.MemoryLadder.Cards.CardPrototype;
import com.mastersofmemory.memoryladder.R;
//import com.MemoryLadderFull.R;

public class Cards_PreGame extends Activity implements OnClickListener  {
	private Button button1;
	private Button button2;
	private TextView Label0;
	private TextView Label1;
	private TextView Label2;
	private TextView Label3;
	private TextView Value0;
	private TextView Value1;
	private TextView Value2;
	private TextView Value3;
	private TextView TargetTextLabel;
	private TextView TargetTextValue; 
	private Button startGameButton;
	private int numDecks;
	private int deckSize;
	private int cardsPerGroup;
	private int gameType;
	private int mode;
	private int step;
	private int memTime;
	private int recallTime;
	private Boolean mnemo_enabled;
	private int target;
	private final int CARDS_SPEED  = Constants.CARDS_SPEED;
///	private final int CARDS_LONG   = Constants.CARDS_LONG;
	
	private final static int STEPS  = Constants.STEPS;
	private final static int WMC    = Constants.WMC;
	private final static int CUSTOM = Constants.CUSTOM;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pre_game);
        
        getExtras();
        initButtons();
        initText();
        initGraphics();
        refreshGameSpecs();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }
    
    @Override
    public void onRestart() {
    	super.onRestart();
    	getExtras();
    	refreshGameSpecs();
    	initTitle();
    	initGraphics();
    	if (mode == CUSTOM)
        	button1.setVisibility(View.VISIBLE);
    	else
    		button1.setVisibility(View.GONE);
    }

    
    public void initButtons() {    	
    	button1 = (Button) findViewById(R.id.button1);
    	if (mode == CUSTOM)
        	button1.setOnClickListener(this);
        else
        	button1.setVisibility(View.GONE);
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        
        startGameButton = (Button) findViewById(R.id.startGameButton);
        startGameButton.setOnClickListener(this);
    }
    
    public void initText() {
    	initTitle();
    	
    	Label0 = (TextView) findViewById(R.id.Label0);
    	Label1 = (TextView) findViewById(R.id.Label1);
    	Label2 = (TextView) findViewById(R.id.Label2);
    	Label3 = (TextView) findViewById(R.id.Label3);
    	
    	Value0 = (TextView) findViewById(R.id.Value0);
    	Value1 = (TextView) findViewById(R.id.Value1);
    	Value2 = (TextView) findViewById(R.id.Value2);
    	Value3 = (TextView) findViewById(R.id.Value3);
    	
    	TargetTextLabel = (TextView) findViewById(R.id.TargetTextLabel);
    	TargetTextValue = (TextView) findViewById(R.id.TargetTextValue);
    }
    
    public void initTitle() {
    	TextView TitleText = (TextView) findViewById(R.id.TitleText);
    	TitleText.setText("Cards");
    }
    
    public void initGraphics() {
    	ImageView graphic = (ImageView) findViewById(R.id.graphic);
    	graphic.setImageResource(R.drawable.pre_graphic_cards);
    }
    
    public void initModeTitle() {
    	ImageView ModeImage = (ImageView) findViewById(R.id.ModeImage);
    	TextView ModeText = (TextView) findViewById(R.id.ModeText);
    	if (mode == STEPS) {
			ModeText.setText("Step " + step);
			switch (step) {
				case 1: ModeImage.setImageResource(R.drawable.icon_mode_step1); break;
				case 2: ModeImage.setImageResource(R.drawable.icon_mode_step2); break;
				case 3: ModeImage.setImageResource(R.drawable.icon_mode_step3); break;
				case 4: ModeImage.setImageResource(R.drawable.icon_mode_step4); break;
				case 5: ModeImage.setImageResource(R.drawable.icon_mode_step5); break;
				default: ModeImage.setImageResource(R.drawable.icon_mode_step1); break;
			}
		}
		else if (mode == WMC) {
			ModeText.setText("Compete");
			ModeImage.setImageResource(R.drawable.icon_mode_wmc);
		}
		else {
			ModeText.setText("Train");
			ModeImage.setImageResource(R.drawable.icon_mode_custom);
		}
    }

	@Override
	public void onClick(View v) {
		if (v == button1)
			launchSettings();
		else if (v == button2)
			startActivity(new Intent(this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		else if (v == startGameButton)
			launchMemActivity();
	}
	
	public void launchSettings() {
		Intent i = getIntent();
    	i.setClass(this, Cards_Settings.class);		
		this.startActivity(i);    	
    }
	
	public void launchMemActivity() {
		Intent i = getIntent();
    	i.setClass(this, CardPrototype.class);

		i.putExtra("deckSize", deckSize);
		i.putExtra("numDecks", numDecks);
		i.putExtra("memTime", memTime);
		i.putExtra("recallTime", recallTime);
		i.putExtra("mnemo_enabled", mnemo_enabled);
		i.putExtra("numCardsPerGroup", cardsPerGroup);
		i.putExtra("step", step);
		i.putExtra("target", target);
		
		this.startActivity(i);
	}
	
	public void getExtras() {
		Intent i = getIntent();
        gameType  = i.getIntExtra("gameType",    -1);
        mode      = i.getIntExtra("mode",        -1);
                       
        getSettings(mode);  
    }
	
	
	public void refreshGameSpecs() {
    	
    	initModeTitle();
    	
    	Label0.setText("Cards per deck:");    Value0.setText(Integer.toString(deckSize));
    	Label1.setText("Number of decks:");   Value1.setText(Integer.toString(numDecks));
    	Label2.setText("Memorization Time:"); Value2.setText(Utils.formatIntoHHMMSStruncated(memTime));
    	Label3.setText("Recall Time");        Value3.setText(Utils.formatIntoHHMMSStruncated(recallTime));
    	
    	if (mode == STEPS) {
    		TargetTextLabel.setVisibility(View.VISIBLE);
    		TargetTextValue.setVisibility(View.VISIBLE);
    		TargetTextValue.setText(Double.toString(Utils.getTargetScore(gameType, step)));
    	}
    	else {
    		TargetTextLabel.setVisibility(View.GONE);
    		TargetTextValue.setVisibility(View.GONE);
    	}
    }	
	
	public void getSettings(int mode) {
    	if (gameType == CARDS_SPEED) {
    		if (mode == STEPS) {
    			SharedPreferences settings = getSharedPreferences("Steps", 0);
    	        step      = settings.getInt("CARDS_SPEED", 1);
    			int[] specs = Constants.getSpecs_STEPS_Cards(step);
    			deckSize     = specs[0];
    			numDecks     = specs[1];
    			memTime      = specs[2];
    			recallTime	 = specs[3];
    			target       = specs[4];
    			cardsPerGroup = 2;
    		}
    		else if (mode == WMC) {
    			deckSize         = Constants.wmc_cards_speed_deckSize;
    			numDecks         = Constants.wmc_cards_speed_numDecks;
    			cardsPerGroup    = Constants.wmc_cards_speed_cardsPerGroup;
    	        memTime          = Constants.wmc_cards_speed_memTime;
    	        recallTime       = Constants.wmc_cards_speed_recallTime;
    		}
    		else if (mode == CUSTOM) {
    			SharedPreferences settings = getSharedPreferences("Card_Preferences", 0);
    			deckSize         = settings.getInt("deckSize",   Constants.default_cards_deckSize);
    	        numDecks         = settings.getInt("numDecks",   Constants.default_cards_numDecks);
    	        cardsPerGroup    = settings.getInt("numCardsPerGroup", Constants.default_cards_cardsPerGroup);
    	        memTime          = settings.getInt("memTime",    Constants.default_cards_memTime);
    	        recallTime       = settings.getInt("recallTime", Constants.default_cards_recallTime);
    	        mnemo_enabled    = settings.getBoolean("mnemo_enabled", false);
    		}
    	}
    	else { 
    		if (mode == STEPS) {
    			SharedPreferences settings = getSharedPreferences("Steps", 0);
    	        step      = settings.getInt("CARDS_LONG", 1);
    			int[] specs = Constants.getSpecs_STEPS_Cards(step);
    			deckSize     = specs[0];
    			numDecks     = specs[1];
    			memTime      = specs[2];
    			recallTime	 = specs[3];
    			target       = specs[4];
				cardsPerGroup = 2;
    		}
    		else if (mode == WMC) { 
    			deckSize         = Constants.wmc_cards_long_deckSize;
    			numDecks         = Constants.wmc_cards_long_numDecks;
				cardsPerGroup    = Constants.wmc_cards_long_cardsPerGroup;
    	        memTime          = Constants.wmc_cards_long_memTime;
    	        recallTime       = Constants.wmc_cards_long_recallTime;
    		}
    		else if (mode == CUSTOM) {
    			SharedPreferences settings = getSharedPreferences("Card_Preferences", 0);
    			deckSize         = settings.getInt("deckSize",   Constants.default_cards_deckSize);
    	        numDecks         = settings.getInt("numDecks",   Constants.default_cards_numDecks);
    	        cardsPerGroup    = settings.getInt("numCardsPerGroup",   Constants.default_cards_cardsPerGroup);
    	        memTime          = settings.getInt("memTime",    Constants.default_cards_memTime);
    	        recallTime       = settings.getInt("recallTime", Constants.default_cards_recallTime);
    	        mnemo_enabled    = settings.getBoolean("mnemo_enabled", false);
    		}
    	}
    }
	
	
	
}