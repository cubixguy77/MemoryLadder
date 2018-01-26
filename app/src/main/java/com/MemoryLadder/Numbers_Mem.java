package com.MemoryLadder;

import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.MemoryLadder.Timer.CountDownTimerPausable;
import com.mastersofmemory.memoryladder.R;
//import com.MemoryLadderFull.R;

public class Numbers_Mem extends Activity implements OnClickListener, android.content.DialogInterface.OnDismissListener {
	
	private GridView grid;
	private NumberAdapter adapter;
	private Button button1;
	private Button button2;
	private Button StartButton;
    private Button InstructionsButton;
    private Button NextGroupButton;
    
    private TextView MnemoText;
	private Boolean mnemo_enabled = false;
	private CellShader shader;
    
    private int numRows;
	private int numCols;
	private int numGridCols;
	private int numItems;
	private int gameType;
	private int memTime;
	private double secondsPerDigit;
	private char[][] answer;
	private Boolean timeExpired = false;
	private Boolean userQuit = false;
	private Boolean countDownComplete = false;
	final private static char ROW_MARKER = 'x';
	
	final private static int NUMBERS_SPEED   = Constants.NUMBERS_SPEED;
//	final private static int NUMBERS_LONG    = Constants.NUMBERS_LONG;
	final private static int NUMBERS_BINARY  = Constants.NUMBERS_BINARY;
	final private static int NUMBERS_SPOKEN  = Constants.NUMBERS_SPOKEN;
	
//	final private static int STEPS  = Constants.STEPS;
//	final private static int WMC    = Constants.WMC;
	final private static int CUSTOM = Constants.CUSTOM;
		
	private SoundManager soundManager;
	private Timer timer;
	private TextView timerText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getExtras();       
        setScreenOrientation();

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.game);

        initButtons();
        initMnemonics();
        initSounds();
        initTimer();
    }

	@Override
	public void onResume() {
		super.onResume();

		if (timer != null && timer.getSecondsElapsed() > 0) {
			timer.start();
		}
	}
    
    @Override
    public void onPause() {
    	super.onPause();
    	if (gameType == NUMBERS_SPOKEN) {
    		userQuit = true;
    		soundManager.stop();
    	}
    	else if (timer != null && !timer.isPaused())
    		timer.pause();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    
    public void setScreenOrientation() {
    	int screenSize = getResources().getConfiguration().screenLayout;
    	if ((screenSize&Configuration.SCREENLAYOUT_SIZE_MASK) < Configuration.SCREENLAYOUT_SIZE_LARGE && gameType != NUMBERS_SPOKEN && numCols > 20)
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    
    public void startMem() {
    	StartButton.setVisibility(View.INVISIBLE);
    	InstructionsButton.setVisibility(View.INVISIBLE);
        
        if (mnemo_enabled) {
        	MnemoText.setVisibility(View.VISIBLE);
        	NextGroupButton.setVisibility(View.VISIBLE);
        }
        
        initGrid();
       
        if (mnemo_enabled) {
        	RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) grid.getLayoutParams();
    		params.addRule(RelativeLayout.ABOVE, R.id.MnemoText);
    		grid.setLayoutParams(params);
        }  	
    	    	
    	CountDownDialog count = new CountDownDialog(this);
    	count.setOnDismissListener(this);
    	count.show();
    }
    
    public void setListener() {
        button2.setOnClickListener(this);
    }
    
    public void initMnemonics() {
    	if ((gameType == NUMBERS_SPEED || gameType == NUMBERS_BINARY) && 
    	    getIntent().getIntExtra("mode", -1) == CUSTOM && 
    	    getIntent().getBooleanExtra("mnemo", false)) {
    			mnemo_enabled = true;
    			MnemoText.setText("\"\"");
    			if (gameType == NUMBERS_SPEED)
    				shader = new CellShader(2, numRows, numCols + 1, gameType, this);
    			else
    				shader = new CellShader(6, numRows, numCols + 1, gameType, this);
    	}    		
    	else
    		mnemo_enabled = false;
    }
    
    public void initGrid() {
    	if (gameType == NUMBERS_SPOKEN) {
        	numGridCols = 1;
        	numItems = 1;
        }
        else {
        	numGridCols = numCols + 1;
            numItems = numRows * numGridCols;
        }        
        grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(numGridCols);
        adapter = new NumberAdapter(this);
        grid.setAdapter(adapter);
        
        
    }
    
    public void initTimer() {
    	timerText = (TextView) findViewById(R.id.timerText);
    	if (gameType == NUMBERS_SPOKEN)
    		timerText.setVisibility(View.INVISIBLE);
    	else
    		timer = new Timer(memTime * 1000, 1000, timerText);
    }
    public void startTimer() {
    	timer.start();
    }
    
    public void initButtons() {
    	button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button1.setText("Stop");
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setText("Recall");
        if (gameType == NUMBERS_SPOKEN)
        	button2.setVisibility(View.GONE);
        
        StartButton = (Button) findViewById(R.id.StartButton);
        StartButton.setOnClickListener(this);
        StartButton.setVisibility(View.VISIBLE);
        
        InstructionsButton = (Button) findViewById(R.id.InstructionsButton);
        InstructionsButton.setOnClickListener(this);
        InstructionsButton.setVisibility(View.VISIBLE);
        
        NextGroupButton = (Button) findViewById(R.id.NextGroupButton);
        NextGroupButton.setOnClickListener(this);
        
        MnemoText = (TextView) findViewById(R.id.MnemoText);
    }
    
    public void initSounds() {
    	if (gameType == NUMBERS_SPOKEN)
    		soundManager = new SoundManager(this, secondsPerDigit);
    }
    
    public void playSound(int digit) {
    	soundManager.playSound(digit);
    }
    
    public void launchSpokenNumbers() {
    	nextSpokenDigit(0);
    }
    
    public void nextSpokenDigit(final int index) {
    	if (index >= numRows * numCols && !userQuit)
    		launchRecallActivity();
    	else if (!userQuit) {
    		char digit = answer[index / numCols][(index % numCols) + 1];
    		playSound(Character.getNumericValue(digit));
    		new Handler().postDelayed(new Runnable(){ public void run()  { 
        		nextSpokenDigit(index + 1);
        	}
        	} , (int) (secondsPerDigit * 1000));
    	}
    }
    
    public char[][] getWrittenNumberArray() {
    	char[][] array = new char[numRows][numCols + 1];
    	Random rand = new Random();
    	int numDigits;
		if (gameType == NUMBERS_BINARY)
			numDigits = 2;
		else
			numDigits = 10;
    	for (int i=0; i<numRows; i++) {
    		for (int j=0; j<numCols + 1; j++) {
    			if (j > 0) { 
    				char nextchar  = (char) (rand.nextInt(numDigits) + 48);
    				array[i][j] = nextchar;
    			}
    			else
    				array[i][j] = ROW_MARKER;
    		}
    	}
    	return array;
    }
    
    public char[][] getSpokenNumberArray() {
    	char[][] array = new char[numRows][numCols];
    	Random rand = new Random();
    	int numDigits = 10;
    	for (int i=0; i<numRows; i++) {
    		for (int j=0; j<numCols; j++) {    			
    			array[i][j] = (char) (rand.nextInt(numDigits) + 48);
    		}
    	}			
    	Utils.print(array);
    	return array;
    }
    
    public int getCol(int pos) {
    	return pos % (numCols+1);
    }
    public int getRow(int pos) {
    	return pos / (numCols+1);
    }
    
    public class NumberAdapter extends BaseAdapter {

        private Context context;                
        public NumberAdapter(Context context) {  	 this.context = context;    }
        public int getCount() {           			 return numItems;        }
        public Object getItem(int position) {        return position;        }
        public long getItemId(int position) {        return position;        }
        public View getView(int position, View convertView, ViewGroup parent) {
        	int row = getRow(position);
            int col = getCol(position);
        	if (gameType != NUMBERS_SPOKEN) {
        		TextView view;
	            if (convertView == null) {
	            	view = new TextView(context, null);
	            	view.setGravity(Gravity.CENTER);	            	
	            }	            
	            else 
	            	view = (TextView) convertView;
	            
	            if (answer[row][col] == ROW_MARKER) {
	            	view.setTextColor(Color.YELLOW);
	        		view.setBackgroundResource(R.drawable.outline_whiteonblue);
	            	view.setText(Integer.toString(getRow(position) + 1));
	            }
	            else if (mnemo_enabled && shader.shouldHighlight(position)) {
	            	view.setTextColor(Color.BLACK);
            		view.setBackgroundResource(R.drawable.outline_whiteonyellow);
            		if (countDownComplete)
            			view.setText(Character.toString(answer[row][col]));
            		else
            			view.setText("");
	            }
	            else {
	            	view.setTextColor(Color.WHITE);
            		view.setBackgroundResource(0);
            		if (countDownComplete)
            			view.setText(Character.toString(answer[row][col]));
            		else
            			view.setText("");
	            }
	            return view;
	        }
        	else {  // spoken numbers
        		ImageView view;
        		if (convertView == null) {
        			view = new ImageView(context);
        			view.setImageResource(R.drawable.pre_graphic_spokennumbers);
        			view.setVerticalScrollBarEnabled(false);
        		}
        		else 
        			view = (ImageView) convertView;   
        		return view;
        	}
        }
    }
    
    
    public void launchRecallActivity() {
    	int secondsElapsed = 0;
    	if (gameType != NUMBERS_SPOKEN) {
    		timer.cancel();
    		secondsElapsed = timer.getSecondsElapsed();
    	}
    	Intent i = getIntent();
		
    	for (int row=0; row<numRows; row++) 
			i.putExtra("answer" + row, answer[row]);
    	
    	if (gameType != NUMBERS_SPOKEN)
    		i.putExtra("memTimeUsed", secondsElapsed);
		
		i.setClass(this, Numbers_Recall.class);
		this.startActivity(i);
		finish();
    }

    public void getExtras() {
    	Intent i = getIntent();
        gameType        = i.getIntExtra("gameType",    -1);
        numRows         = i.getIntExtra("numRows",     -1);
        numCols         = i.getIntExtra("numCols",     -1);
        memTime         = i.getIntExtra("memTime",     -1);
        secondsPerDigit = i.getFloatExtra("secondsPerDigit", 1.0f);
        
        getAnswerArray();
    }
    
    public void getAnswerArray() {
    	answer = getWrittenNumberArray();
    }
    
    public void advancePegPositions() {
    	shader.advancePositions();
    }
    
    public void updateMnemoText() {    	
    	MnemoText.setText(shader.getMnemoText(answer));
    }
    
    public void showNextGroup() {
    	advancePegPositions();
    	adapter.notifyDataSetChanged();    	
    	updateMnemoText();
    }
    
    
	@Override
	public void onClick(View v) {
		if (v == button1)
			launchExitDialog();
		else if (v == button2 && !timeExpired && !userQuit) {
			launchRecallActivity();
		}
		else if (v == StartButton)
			startMem();
		else if (v == InstructionsButton) {
			InstructionsDialog dialog = new InstructionsDialog(this, gameType);
			dialog.show();
		}
		else if (v == NextGroupButton) {
			showNextGroup();
		}
	}
	
	
	
	
	public void launchExitDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
    	builder.setMessage("Stop current game?")
    	       .setCancelable(false)
    	       .setPositiveButton("Stop Game", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   Numbers_Mem.this.startActivity(new Intent(Numbers_Mem.this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    	        	   userQuit = true;
    	        	   if (gameType != NUMBERS_SPOKEN)
    	        		   timer.cancel();
    	           }
    	       })
    	       .setNegativeButton("Continue Game", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	                dialog.cancel();
    	           }
    	       });
    	AlertDialog alert = builder.create();
    	alert.show();
    }
	
	
	private class Timer extends CountDownTimerPausable {
		private Boolean displayTime; // whether or not we should be setting the timer's time to a textview
		private TextView TimerText;	
		private int secondsElapsed = 0;
		
		public Timer(long millisInFuture, long countDownInterval, TextView TimerText) {
			super(millisInFuture, countDownInterval);
			this.TimerText = TimerText;
			displayTime = true;
			TimerText.setText(Utils.formatIntoHHMMSStruncated(millisInFuture / 1000));
		}
		public Timer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			displayTime = false;
		}
		
		public int getSecondsElapsed() {
			return secondsElapsed;
		}
		
		@Override
		public void onFinish() {
			timeExpired = true; 
			secondsElapsed = memTime;
			TimerText.setText("00:00");
			
			final TextView timesUp = (TextView) findViewById(R.id.timesUp);
			grid.setVisibility(View.INVISIBLE);
			
			if (mnemo_enabled) {
				MnemoText.setVisibility(View.GONE);
				NextGroupButton.setVisibility(View.GONE);
			}
				
			
			timesUp.setVisibility(View.VISIBLE);
			
			
			new Handler().postDelayed(new Runnable(){  
	    		public void run()  { launchRecallActivity(); }
	    	}, 3000);
		}
		
		@Override
		public void onTick(long millisUntilFinished) {
			secondsElapsed = (int) (memTime - (millisUntilFinished / 1000));
			if (displayTime)				
				TimerText.setText(Utils.formatIntoHHMMSStruncated(millisUntilFinished / 1000));
		}
	}
	
	@Override
	public void onDismiss(DialogInterface arg0) {
    	setListener();
 		countDownComplete = true;
    	if (gameType == NUMBERS_SPOKEN) {
        	launchSpokenNumbers();
        }   
    	else {  	   
        	adapter.notifyDataSetChanged(); // refresh the views so that they display their digit
     		startTimer();  
     		if (mnemo_enabled)
     			updateMnemoText();
     	}	 
	}
	
}