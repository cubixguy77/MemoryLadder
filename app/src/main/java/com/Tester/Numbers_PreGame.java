package com.Tester;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
//import com.MemoryLadderFull.R;

public class Numbers_PreGame extends Activity implements OnClickListener{

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
	private int numRows;
	private int numCols;
	private int gameType;
	private int mode;
	private int step;
	private int memTime;
	private int recallTime;
	private int target;
	private double secondsPerDigit;
	private Boolean mnemo;
	
	final private static int NUMBERS_SPEED   = Constants.NUMBERS_SPEED;
	final private static int NUMBERS_LONG    = Constants.NUMBERS_LONG;
	final private static int NUMBERS_BINARY  = Constants.NUMBERS_BINARY;
	final private static int NUMBERS_SPOKEN  = Constants.NUMBERS_SPOKEN;
	
	final private static int STEPS  = Constants.STEPS;
	final private static int WMC    = Constants.WMC;
	final private static int CUSTOM = Constants.CUSTOM;
	
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
    	if (mode == CUSTOM) {
        	button1.setVisibility(View.VISIBLE);
        	button1.setOnClickListener(this);
    	}
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
    	if (gameType == NUMBERS_SPEED)
    		TitleText.setText("Speed Numbers");
    	else if (gameType == NUMBERS_LONG)
    		TitleText.setText("Long Numbers");
    	else if (gameType == NUMBERS_BINARY)
    		TitleText.setText("Binary Numbers");
    	else if (gameType == NUMBERS_SPOKEN)
    		TitleText.setText("Spoken Numbers");
    }
    
    public void initGraphics() {
    	ImageView graphic = (ImageView) findViewById(R.id.graphic);
    	if (gameType == NUMBERS_SPOKEN)
    		graphic.setImageResource(R.drawable.pre_graphic_spokennumbers);
    	else if (gameType == NUMBERS_BINARY)
    		graphic.setImageResource(R.drawable.pre_graphic_binary);
    	else
    		graphic.setImageResource(R.drawable.pre_graphic_writtennumbers);
    }
    
    
    public void onPreGameFinished() {
    	Intent i = getIntent();
    	i.setClass(this, Numbers_Mem.class);
    	
		i.putExtra("numRows", numRows);
		i.putExtra("numCols", numCols);
		i.putExtra("memTime", memTime);
		i.putExtra("recallTime", recallTime);		
		i.putExtra("secondsPerDigit", secondsPerDigit);	
		i.putExtra("step", step);
		i.putExtra("target", target);
		i.putExtra("mnemo", mnemo);
		
		this.startActivity(i);
    }

    public void launchSettings() {
    	Intent i = getIntent();
    	i.setClass(this, Numbers_Settings.class);		
		this.startActivity(i);    	
    }
    
    public void launchVideoDialog() {
		VideoDialog v = new VideoDialog(this, gameType);
		v.show();		
	}
        
    
    public void getExtras() {
    	Intent i = getIntent();
    	
        gameType  = i.getIntExtra("gameType",    -1);
        mode      = i.getIntExtra("mode",        -1);
        
        getSettings(mode);
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
	
	public String getDigitSpeedString() {
		if (secondsPerDigit == 1)
			return "Fast";
		else if (secondsPerDigit == 2)
			return "Medium";
		else if (secondsPerDigit == 3)
			return "Slow";
		return "NULL";
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
		
		if (gameType == NUMBERS_SPOKEN) {
	    	Label0.setText("Number of digits:");    Value0.setText(Integer.toString(numRows * numCols));
	    	Label1.setVisibility(View.GONE);        Value1.setVisibility(View.GONE);
	    	Label2.setText("Digit Speed:");         Value2.setText(getDigitSpeedString());
	    	Label3.setText("Recall Time");          Value3.setText(Utils.formatIntoHHMMSStruncated(recallTime));
		}
		else {
	    	Label0.setText("Digits per line:");    Value0.setText(Integer.toString(numCols));
	    	Label1.setText("Number of lines:");    Value1.setText(Integer.toString(numRows));
	    	Label1.setVisibility(View.VISIBLE);    Value1.setVisibility(View.VISIBLE);
	    	Label2.setText("Memorization Time:");  Value2.setText(Utils.formatIntoHHMMSStruncated(memTime));
	    	Label3.setText("Recall Time");         Value3.setText(Utils.formatIntoHHMMSStruncated(recallTime));
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
    	if (gameType == NUMBERS_SPOKEN) {
    		if (mode == STEPS) {        
    			
    	        SharedPreferences settings = getSharedPreferences("Steps", 0);
    	        step      = settings.getInt("NUMBERS_SPOKEN", 1);
    	        
    			int[] specs = Constants.getSpecs_STEPS_Spoken(step);
    			numRows         = specs[0];
    	        numCols         = specs[1];
    	        recallTime      = specs[2];
    	        secondsPerDigit = specs[3];
    	        target       = specs[4];
    		}
    		else if (mode == WMC) {
    			numRows         = Constants.wmc_spoken_numRows;
    	        numCols         = Constants.wmc_spoken_numCols;
    	        recallTime      = Constants.wmc_spoken_recallTime;
    	        secondsPerDigit = Constants.wmc_spoken_secondsPerDigit;
    		}
    		else if (mode == CUSTOM) {
    			SharedPreferences settings = getSharedPreferences("Number_Preferences", 0);
    			numRows         = settings.getInt("SPOKEN_numRows", Constants.default_spoken_numRows);
    	        numCols         = settings.getInt("SPOKEN_numCols", Constants.default_spoken_numCols);
    	        recallTime      = settings.getInt("SPOKEN_recallTime", Constants.default_spoken_recallTime);
    	        secondsPerDigit = settings.getFloat("SPOKEN_secondsPerDigit", Constants.default_spoken_secondsPerDigit);
    		}
    	}
    	else {
    		if (mode == STEPS) {        
    	        SharedPreferences settings = getSharedPreferences("Steps", 0);
    	        int[] specs;
    	        if (gameType == NUMBERS_SPEED) {
    	        	step      = settings.getInt("NUMBERS_SPEED", 1);
    				specs = Constants.getSpecs_STEPS_Numbers(step);
    				System.out.println("getting specs for setp " + step);
    	        }
    	        else {
    	        	step      = settings.getInt("NUMBERS_BINARY", 1);
    				specs = Constants.getSpecs_STEPS_Binary(step);
    	        }
    			numRows         = specs[0];
    	        numCols         = specs[1];
    	        memTime         = specs[2];
    	        recallTime      = specs[3];    	        
    	        target          = specs[4];
    		}
    		else if (mode == WMC) {
    			if (gameType == NUMBERS_SPEED) {
    				numRows         = Constants.wmc_written_speed_numRows;
    	        	numCols         = Constants.wmc_written_speed_numCols;
    	        	memTime         = Constants.wmc_written_speed_memTime;
    	        	recallTime      = Constants.wmc_written_speed_recallTime;
    			}
    			else if (gameType == NUMBERS_LONG) {
    				numRows         = Constants.wmc_written_long_numRows;
    	        	numCols         = Constants.wmc_written_long_numCols;
    	        	memTime         = Constants.wmc_written_long_memTime;
    	        	recallTime      = Constants.wmc_written_long_recallTime;
    			}
    			else if (gameType == NUMBERS_BINARY) {
    				numRows         = Constants.wmc_written_binary_numRows;
    	        	numCols         = Constants.wmc_written_binary_numCols;
    	        	memTime         = Constants.wmc_written_binary_memTime;
    	        	recallTime      = Constants.wmc_written_binary_recallTime;
    			}
    		}
    		else if (mode == CUSTOM) {
    			SharedPreferences settings = getSharedPreferences("Number_Preferences", 0);
    			if (gameType == NUMBERS_SPEED) {
	    			numRows         = settings.getInt("WRITTEN_numRows", Constants.default_written_numRows);
	    	        numCols         = settings.getInt("WRITTEN_numCols", Constants.default_written_numCols);
	    	        memTime         = settings.getInt("WRITTEN_memTime", Constants.default_written_memTime);
	    	        recallTime      = settings.getInt("WRITTEN_recallTime", Constants.default_written_recallTime);
	    	        mnemo           = settings.getBoolean("WRITTEN_mnemo", false);
    			}
    			else {
	    			numRows         = settings.getInt("BINARY_numRows", Constants.default_binary_numRows);
	    	        numCols         = settings.getInt("BINARY_numCols", Constants.default_binary_numCols);
	    	        memTime         = settings.getInt("BINARY_memTime", Constants.default_binary_memTime);
	    	        recallTime      = settings.getInt("BINARY_recallTime", Constants.default_binary_recallTime);
	    	        mnemo           = settings.getBoolean("BINARY_mnemo", false);
    			}    	        
    		}
    	}
    }
	
	
	
}