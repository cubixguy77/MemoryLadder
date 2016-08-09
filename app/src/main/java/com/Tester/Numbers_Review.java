package com.Tester;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
//import com.MemoryLadderFull.R;

public class Numbers_Review extends Activity implements OnClickListener{
	
	private GridView grid;
	private Button button1;
	private Button button2;
	private int numRows;
	private int numCols;
	private int numItems;
	private int gameType;
	private int score;
	private int mode;
	private char[][] guess;
	private char[][] answer;
	
//	private final char ROW_MARKER = 'x';
//	private final static int NUMBERS_SPEED   = Constants.NUMBERS_SPEED;
//	private final static int NUMBERS_LONG    = Constants.NUMBERS_LONG;
//	private final static int NUMBERS_BINARY  = Constants.NUMBERS_BINARY;
	private final static int NUMBERS_SPOKEN  = Constants.NUMBERS_SPOKEN;

	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getExtras();
        setScreenOrientation();
        
        setContentView(R.layout.game);       
        
        initButtons();
        initText();
        
        numCols++;
        numItems = numRows * numCols;
        
        grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(numCols);
        grid.setAdapter(new NumberAdapter(this));

                        
        initButtons();
    }
    
    public void setScreenOrientation() {
    	int screenSize = getResources().getConfiguration().screenLayout;
    	if ((screenSize&Configuration.SCREENLAYOUT_SIZE_MASK) < Configuration.SCREENLAYOUT_SIZE_LARGE && gameType != NUMBERS_SPOKEN && numCols > 20)
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
    
    public void initButtons() {
    	button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button1.setText("Back");
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button2.setText("Play Again");
    }
    
    public void initText() {
    	TextView TitleText = (TextView) findViewById(R.id.TitleText);
    	TitleText.setText("Review");
    	
    	TextView timerText = (TextView) findViewById(R.id.timerText);
    	timerText.setVisibility(View.INVISIBLE);
    	
    	TextView ScoreText = (TextView) findViewById(R.id.PageNumber);
    	ScoreText.setVisibility(View.VISIBLE);
    	ScoreText.setTextSize(30);
    	ScoreText.setTextColor(Color.YELLOW);
    	ScoreText.setText("Score: " + Integer.toString(score));
    }        
        
    public int getCol(int pos) {
    	return pos % numCols;
    }
    public int getRow(int pos) {
    	return pos / numCols;
    }
    
    public class NumberAdapter extends BaseAdapter {

        private Context context;                
        public NumberAdapter(Context context) {  	 this.context = context;    }
        public int getCount() {           			 return numItems;        }
        public Object getItem(int position) {        return null;        }
        public long getItemId(int position) {        return 0;        }
        public View getView(int position, View convertView, ViewGroup parent) {
	    	int row = getRow(position);
	        int col = getCol(position);
    	
            TextView view;
            if (convertView == null) {
            	view = new TextView(context);	              
            	view.setLines(2);
            	
            	if (col == 0) {
            		view.setGravity(Gravity.CENTER);
            		view.setBackgroundResource(R.drawable.outline_whiteonblue);
            		view.setTextColor(Color.YELLOW);
            	}
            	else {
            		view.setGravity(Gravity.CENTER_HORIZONTAL);
            		view.setBackgroundResource(R.drawable.outline_whiteonblack);
            	}
            	
            }	            
            else 
            	view = (TextView) convertView;	 
            
            if (col == 0) {
            	view.setText(Integer.toString(row + 1));
            	view.setGravity(Gravity.CENTER);
            	view.setTextColor(Color.YELLOW);
        		view.setBackgroundResource(R.drawable.outline_whiteonblue);
            }
            else {
            	view.setText(Character.toString(answer[row][col]) + "\n" + guess[row][col-1]);
            	if (guess[row][col-1] == answer[row][col]) {
            		view.setTextColor(Color.BLACK);
            		view.setBackgroundResource(R.drawable.outline_whiteongreen);
            	}
            	else if (guess[row][col-1] != ' ') {
            		view.setTextColor(Color.BLACK);
            		view.setBackgroundResource(R.drawable.outline_whiteonred);
            	}
            	else {
            		view.setTextColor(Color.LTGRAY);
            		view.setBackgroundResource(R.drawable.outline_whiteonblack);
            	}
            }
            
            return view;	        	
    	}
	}
            
	@Override
	public void onClick(View view) {
		if (view == button1)
			launchScoreActivity();
		else
			launchTest();
	}
	
	
	public void launchScoreActivity() {
		finish();
    }
	
	public void launchTest() {
		Intent i = new Intent(this, Utils.getPreClass(gameType));
		i.putExtra("mode", mode);
		i.putExtra("gameType", gameType);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
	}

    public void getExtras() {
    	Intent i = getIntent();
        numRows         = i.getIntExtra("numRows",     -1);
        numCols         = i.getIntExtra("numCols",     -1);
        mode            = i.getIntExtra("mode", -1);
        gameType        = i.getIntExtra("gameType", -1);
        
        score           = i.getIntExtra("score", -1);
        
        getGuessArray(i);
        getAnswerArray(i);
    }
   
    public void getGuessArray(Intent intent) {
    	int i=0;
        guess = new char[numRows][numCols];
        while (intent.hasExtra("guess" + i)) {
        	guess[i] = intent.getCharArrayExtra("guess" + i);   // reconstruct our 2d array, one linear array at a time
        	i++;
        }
    }
    
    public void getAnswerArray(Intent intent) {
    	int i=0;
        answer = new char[numRows][numCols];
        while (intent.hasExtra("answer" + i)) {
        	answer[i] = intent.getCharArrayExtra("answer" + i);   // reconstruct our 2d array, one linear array at a time
        	i++;
        }
    }
}