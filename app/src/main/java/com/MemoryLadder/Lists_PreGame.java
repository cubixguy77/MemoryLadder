package com.MemoryLadder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;
//import com.MemoryLadderFull.R;

public class Lists_PreGame extends Activity implements OnClickListener {
	
	private Button button1;
	private Button button2;
	private Button startGameButton;
	private Button aboutTestButton;
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
	private  int numRows;
	private  int numCols;
	private  int gameType;
	private int mode;
	private int step;
	private  int memTime;
	private  int recallTime;
	private int target;
	private static final int LISTS_WORDS  = Constants.LISTS_WORDS;
//	private static final int LISTS_EVENTS = Constants.LISTS_EVENTS;	
	private final static int STEPS  = Constants.STEPS;
	private final static int WMC    = Constants.WMC;
	private final static int CUSTOM = Constants.CUSTOM;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        
        aboutTestButton = (Button) findViewById(R.id.aboutTestButton);
        aboutTestButton.setOnClickListener(this);
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
    	if (gameType == LISTS_WORDS)
    		TitleText.setText("Random Words");
    	else
    		TitleText.setText("Historic Dates");
    }
    
    public void initGraphics() {
    	ImageView graphic = (ImageView) findViewById(R.id.graphic);
    	if (gameType == LISTS_WORDS)
    		graphic.setImageResource(R.drawable.pre_graphic_randomwords);
    	else
    		graphic.setImageResource(R.drawable.pre_graphic_historicdates);
    }
    
            
    public void onPreGameFinished() {
    	Intent i = getIntent();
    	i.setClass(this, Lists_Mem.class);

		i.putExtra("numRows", numRows);
		i.putExtra("numCols", numCols);
		i.putExtra("memTime", memTime);
		i.putExtra("recallTime", recallTime);
		i.putExtra("step", step);
		i.putExtra("target", target);
		
		this.startActivity(i);
    }
    
    
    public void getExtras() {
    	Intent i = getIntent();
        gameType        = i.getIntExtra("gameType",    -1);
        mode            = i.getIntExtra("mode",        -1);
        
        getSettings(mode);  
    }
    
    public void launchSettings() {
    	Intent i = getIntent();
    	i.setClass(this, Lists_Settings.class);		
		this.startActivity(i);     	
    }
    
    public void launchVideoDialog() {
		VideoDialog v = new VideoDialog(this, gameType);
		v.show();		
	}
    
	@Override
	public void onClick(View view) {
		if (view == button1)
			launchSettings();
		else if (view == button2)
			startActivity(new Intent(this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
		else if (view == startGameButton)
			onPreGameFinished();
		else if (view == aboutTestButton)
			launchVideoDialog();
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
	
	public void refreshGameSpecs() {
		
		initModeTitle();
		
		if (gameType == LISTS_WORDS) {
	    	Label0.setText("Words per column:");    Value0.setText(Integer.toString(numRows));
	    	Label1.setVisibility(View.VISIBLE);     Value1.setVisibility(View.VISIBLE);
	    	Label1.setText("Number of columns:");   Value1.setText(Integer.toString(numCols));
	    	Label2.setText("Memorization Time:");   Value2.setText(Utils.formatIntoHHMMSStruncated(memTime));
	    	Label3.setText("Recall Time");          Value3.setText(Utils.formatIntoHHMMSStruncated(recallTime));
		}
		else {
	    	Label0.setText("Number of dates:");   Value0.setText(Integer.toString(numRows));
	    	Label1.setVisibility(View.GONE);      Value1.setVisibility(View.GONE);
	    	Label2.setText("Memorization Time:"); Value2.setText(Utils.formatIntoHHMMSStruncated(memTime));
	    	Label3.setText("Recall Time");        Value3.setText(Utils.formatIntoHHMMSStruncated(recallTime));
		}
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
    	if (gameType == LISTS_WORDS) {
    		if (mode == STEPS) {    	        
    	        SharedPreferences settings = getSharedPreferences("Steps", 0);
    	        step      = settings.getInt("LISTS_WORDS", 1);
    			int[] specs = Constants.getSpecs_STEPS_RandomWords(step);
    			numRows         = specs[0];
    	        numCols         = specs[1];
    	        memTime         = specs[2];
    	        recallTime      = specs[3]; 
    	        target       = specs[4];
    		}
    		else if (mode == WMC) {
    			numRows         = Constants.wmc_words_numRows;
    	        numCols         = Constants.wmc_words_numCols;
    	        memTime         = Constants.wmc_words_memTime;
    	        recallTime      = Constants.wmc_words_recallTime;
    		}
    		else if (mode == CUSTOM) {
    			SharedPreferences settings = getSharedPreferences("List_Preferences", 0);
    			numRows         = settings.getInt("WORDS_numRows", Constants.default_words_numRows);
    	        numCols         = settings.getInt("WORDS_numCols", Constants.default_words_numCols);
    	        memTime         = settings.getInt("WORDS_memTime", Constants.default_words_memTime);
    	        recallTime      = settings.getInt("WORDS_recallTime", Constants.default_words_recallTime);
    		}
    	}
    	else {
    		if (mode == STEPS) {        
    	        SharedPreferences settings = getSharedPreferences("Steps", 0);
    	        step      = settings.getInt("LISTS_EVENTS", 1);
    			int[] specs = Constants.getSpecs_STEPS_HistoricDates(step);
    			numRows         = specs[0];
    	        numCols         = specs[1];
    	        memTime         = specs[2];
    	        recallTime      = specs[3];
    	        target       = specs[4];
    		}
    		else if (mode == WMC) {
    			numRows         = Constants.wmc_dates_numRows;
    	        numCols         = Constants.wmc_dates_numCols;
    	        memTime         = Constants.wmc_dates_memTime;
    	        recallTime      = Constants.wmc_dates_recallTime;
    		}
    		else if (mode == CUSTOM) {
    			SharedPreferences settings = getSharedPreferences("List_Preferences", 0);
    			numRows         = settings.getInt("DATES_numRows", Constants.default_dates_numRows);
    	        numCols         = settings.getInt("DATES_numCols", Constants.default_dates_numCols);
    	        memTime         = settings.getInt("DATES_memTime", Constants.default_dates_memTime);
    	        recallTime      = settings.getInt("DATES_recallTime", Constants.default_dates_recallTime);
    		}
    	}
    }
	
}