package com.MemoryLadder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;
//import com.MemoryLadderFull.R;

public class Numbers_Recall extends Activity implements OnClickListener {
	
	private GridView grid;
	private Button button1;
	private Button button2;
	private int numRows;
	private int numCols;
	private int numColsMEM;
	private int numItems;
	private int gameType;
	private int recallTime;
	private Boolean timeExpired = false;
	private Boolean userQuit = false;
	private Boolean openKeyboard = true;
	private char[][] guess;
	private char[][] answer;
	private String[] textBoxStrings;
	private Timer timer;
	private TextView timerText;
	
//	private final static int NUMBERS_SPEED   = Constants.NUMBERS_SPEED;
//	private final static int NUMBERS_LONG    = Constants.NUMBERS_LONG;
//	private final static int NUMBERS_BINARY  = Constants.NUMBERS_BINARY;
	private final static int NUMBERS_SPOKEN  = Constants.NUMBERS_SPOKEN;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getExtras();
                
        setContentView(R.layout.game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        
        
        grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(numCols);
        grid.setAdapter(new NumberAdapter(this));

		grid.setRecyclerListener(new AbsListView.RecyclerListener() {
			@Override
			public void onMovedToScrapHeap(View view) {
				if ( view.hasFocus()){
					view.clearFocus();
					if ( view instanceof EditText) {
						InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
				}
			}
		});
        
        initButtons();
        initText();
    }
    
    @Override
    public void onResume() {
    	super.onResume();    	
    	initTimer();
    	timer.start();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	if (timer != null)
    		timer.cancel();
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    	super.onWindowFocusChanged(hasFocus);
    	if (hasFocus && openKeyboard) {
    		openKeyboard();
    	}    		
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
/*
    public void setScreenOrientation() {
    	//int screenSize = getResources().getConfiguration().screenLayout;
    	if (gameType == NUMBERS_SPOKEN)     //(screenSize&Configuration.SCREENLAYOUT_SIZE_MASK) < Configuration.SCREENLAYOUT_SIZE_LARGE && gameType != NUMBERS_SPOKEN && numCols > 20)
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
  */  
    public void openKeyboard() {
    	openKeyboard = false;
    	
    	ViewGroup griditem = (ViewGroup) grid.getChildAt(grid.getFirstVisiblePosition()); //First item
        for(int i = 0; i < griditem.getChildCount(); i++) {
            if(griditem != null && griditem.getChildAt(i) instanceof EditText) {
                griditem.getChildAt(i).requestFocus();                             
            }
        }        
    }
    
    public void initButtons() {
    	button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button1.setText("Stop");
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button2.setText("Done");
    }
    
    public void initText() {
    	TextView TitleText = (TextView) findViewById(R.id.TitleText);
    	TitleText.setText("Recall");
    }
    
    public void initTimer() {
    	timerText = (TextView) findViewById(R.id.timerText);
    	timer = new Timer(recallTime * 1000, 1000, timerText);
    }
    
    public char[][] getGuessArray() {
    	char[][] charArray = new char[numRows][numColsMEM];
    	for (int row=0; row<numRows; row++) {
    		String output = "";
    		String old = textBoxStrings[row];
    		for (int i=0; i<numColsMEM; i++) {
    			if (old == null || i >= old.length())
    				output += " ";
    			else
    				output += old.charAt(i);
    		}
    		charArray[row] = output.toCharArray();
    		System.out.println(output);
    	}
    	return charArray;
    }
    
    public int getCol(int pos) {
    	return pos % numColsMEM;
    }
    public int getRow(int pos) {
    	return pos / numColsMEM;
    }
    
    public class NumberAdapter extends BaseAdapter {

        public NumberAdapter(Context context) {  	        
        	textBoxStrings = new String[numRows];
        }
        public int getCount() {           			 return numItems;        }
        public Object getItem(int position) {        return position;        }
        public long getItemId(int position) {        return position;        }
        
        
        public EditText setEditTextFilters(EditText et) {        	
        	int maxLength = numColsMEM;
        	InputFilter[] FilterArray = new InputFilter[1];
        	FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        	et.setFilters(FilterArray);
        	et.setInputType(InputType.TYPE_CLASS_PHONE);
        	et.setSingleLine();
        	return et;
        }
        
        public View getView(int position, View convertView, ViewGroup parent) {
            
            final ViewHolder holder;
            
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.number, null);
            	holder = new ViewHolder();            	
                holder.editText = (EditText) convertView.findViewById(R.id.number_edittext);
                holder.editText = setEditTextFilters(holder.editText);
                holder.editText.addTextChangedListener(new TextWatcher() {	                    
                	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    public void afterTextChanged(Editable s) {
                    	textBoxStrings[holder.row] = s.toString(); 
                    }
                });
                
                convertView.setTag(holder); 
            }
            else {
            	holder = (ViewHolder) convertView.getTag();
            }
            
            holder.row = position;
        	holder.editText.setText(textBoxStrings[holder.row]);
                            
        	TextView rowtext = (TextView) convertView.findViewById(R.id.number_rowtext);
        	rowtext.setText(Integer.toString(holder.row + 1));
            return convertView;
        }
    }
    
    
    static class ViewHolder {
    	EditText editText;
    	String string;
    	int row;
    }
    
    
    
    
    /* Returns an array with the first column removed */
    public char[][] sanitizeArray(char[][] array) {
    	int rows = array.length;
    	int cols = array[0].length;
    	char[][] temp = new char[rows][cols];
    	for (int row=0; row<rows; row++) {
    		for (int col=1; col<cols; col++) {
    			temp[row][col-1] = array[row][col];
    		}
    	}
    	return temp;
    }
    
    
    
	@Override
	public void onClick(View v) {
		if (v == button1)
			launchExitDialog();
		else if (v == button2 && !timeExpired && !userQuit)
			launchScoreActivity();
	}
	
	
    public void launchScoreActivity() {
    	timer.cancel();
    	guess = getGuessArray();
		Intent i = getIntent();
				
        for (int row=0; row<numRows; row++) 
			i.putExtra("guess" + row, guess[row]);			
        
        int[] analysis = Scoring.getScore(gameType, guess, sanitizeArray(answer));
        int percent;
        if (analysis[1] == 0)
        	percent = 0;
        else
        	percent = (int) (100 * ((double) analysis[0] / analysis[1]));
        int score = analysis[2];
        
        String[] scores = new String[4];
        scores[0] = "Recall Accuracy:" +  percent + "% (" +  analysis[0] + " / " +  analysis[1] + ")";
        if (gameType == NUMBERS_SPOKEN)
        	scores[1] = "hide";
        else
        	scores[1] = "Memorization Time:" + Utils.formatIntoHHMMSStruncated(i.getIntExtra("memTimeUsed", -1));
        scores[2] = "hide";
        scores[3] = "Score:" +  score;
        i.putExtra("scores", scores);
        
        i.setClass(this, ScoreActivity.class);
        this.startActivity(i);		
    }
	   
    public void getExtras() {
    	Intent intent = getIntent();
        numRows = intent.getIntExtra("numRows", -1);
        numColsMEM = intent.getIntExtra("numCols", -1);
        numCols = 1;
        numItems = numRows * numCols;
        recallTime = intent.getIntExtra("recallTime", -1);
        gameType = intent.getIntExtra("gameType", -1);
        
        int i=0;
        answer = new char[numRows][numColsMEM];
        while (intent.hasExtra("answer" + i)) {
        	answer[i] = intent.getCharArrayExtra("answer" + i);   // reconstruct our 2d array, one linear array at a time
        	i++;
        }
    }
    
    
    
    public void launchExitDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
    	builder.setMessage("Stop current game?")
    	       .setCancelable(false)
    	       .setPositiveButton("Stop Game", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   Numbers_Recall.this.startActivity(new Intent(Numbers_Recall.this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    	        	   userQuit = true;
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
    
    
    private class Timer extends CountDownTimer {
		private Boolean displayTime; // whether or not we should be setting the timer's time to a textview
		private TextView TimerText;	
		
		public Timer(long millisInFuture, long countDownInterval, TextView TimerText) {
				super(millisInFuture, countDownInterval);
				this.TimerText = TimerText;
				displayTime = true;
		}
		public Timer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			displayTime = false;
		}
		
		@Override
		public void onFinish() {
			timeExpired = true;
			TimerText.setText("00:00");
			
			final TextView timesUp = (TextView) findViewById(R.id.timesUp);
			grid.setVisibility(View.INVISIBLE);
			timesUp.setVisibility(View.VISIBLE);
			
			new Handler().postDelayed(new Runnable(){  
	    		public void run()  { launchScoreActivity(); }
	    	}, 3000);
		}

		@Override
		public void onTick(long millisUntilFinished) {
			if (displayTime)				
				TimerText.setText(Utils.formatIntoHHMMSStruncated(millisUntilFinished / 1000));
		}
	}
    
    
}