package com.Tester;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
//import com.MemoryLadderFull.R;

public class Shapes_Review extends Activity implements OnClickListener {
	
	private GridView grid;
	private Button button1;
	private Button button2;
	private int numRows;
	private int numCols;
	private int numItems;
	private int gameType;
	private int mode;
	private int score;
	private String[][] guess;
	private String[][] answer;
	private int[] images;
	
//	final private static int SHAPES_FACES    = Constants.SHAPES_FACES;
	final private static int SHAPES_ABSTRACT = Constants.SHAPES_ABSTRACT;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getExtras();                        
        setScreenOrientation();
        
        setContentView(R.layout.game);
                
        initButtons();
        initText();
        
        grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(numCols);
        grid.setBackgroundColor(Color.WHITE);
        grid.setAdapter(new ShapeAdapter(this));
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
    
    public void setScreenOrientation() {
    	int screenSize = getResources().getConfiguration().screenLayout;
    	if ((screenSize&Configuration.SCREENLAYOUT_SIZE_MASK) == 4)
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
            
    public int getCol(int pos) {
    	return pos % numCols;
    }
    public int getRow(int pos) {
    	return pos / numCols;
    }
    
    private class ShapeAdapter extends BaseAdapter {

        public ShapeAdapter(Context context) {}
        public int getCount() {           			 return numItems;        }
        public Object getItem(int position) {        return null;        }
        public long getItemId(int position) {        return 0;        }
                        
        public View getView(int position, View convertView, ViewGroup parent) {
            View MyView = convertView;
            if (convertView == null) {            	
            	LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.shape, null);
                
                EditText textbox = (EditText) MyView.findViewById(R.id.shape_edittext);
            	textbox.setVisibility(View.GONE);                                      	            	
            }
            ImageView image = (ImageView) MyView.findViewById(R.id.shape_image);
            image.setBackgroundResource(images[position]);
            
            int row = getRow(position);
        	int col = getCol(position);
            TextView text = (TextView) MyView.findViewById(R.id.shape_text);
            
        	if (gameType == SHAPES_ABSTRACT)
        		text.setText("Seq: " + answer[row][col] + "\n" + 
        				     "Seq: " + guess [row][col]);
        	else {
        		text.setText(answer[row][col] + "\n" + guess[row][col]);        		
        	}
        	
        	if (guess[row][col].equalsIgnoreCase(answer[row][col]))
        		text.setBackgroundColor(Color.GREEN);
        	else if (!guess[row][col].equals(""))
        		text.setBackgroundColor(Color.RED);
        	else
        		text.setBackgroundColor(Color.WHITE);
            
            return MyView;
        }
    }
    
    
    public void getExtras() {
    	Intent intent = getIntent();
        numRows = intent.getIntExtra("numRows", -1);
        numCols = intent.getIntExtra("numCols", -1);
        numItems = numRows * numCols;
        gameType = intent.getIntExtra("gameType", -1);
        mode            = intent.getIntExtra("mode", -1);
        images = intent.getIntArrayExtra("images");
        score    = intent.getIntExtra("score", -1);
        getGuessArray(intent);
        getAnswerArray(intent);
    }
    
    public void getGuessArray(Intent intent) {
    	int i=0;
        guess = new String[numRows][numCols];
        while (intent.hasExtra("guess" + i)) {
        	guess[i] = intent.getStringArrayExtra("guess" + i);   // reconstruct our 2d array, one linear array at a time
        	i++;
        }
    }
    
    public void getAnswerArray(Intent intent) {
    	int i=0;
        answer = new String[numRows][numCols];
        while (intent.hasExtra("answer" + i)) {
        	answer[i] = intent.getStringArrayExtra("answer" + i);   // reconstruct our 2d array, one linear array at a time
        	i++;
        }
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
    
	@Override
	public void onClick(View v) {
		if (v == button1) 
			launchScoreActivity();
		else if (v == button2)
			launchTest();
	}	
}