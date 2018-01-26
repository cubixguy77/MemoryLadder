package com.MemoryLadder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TableRow;
import android.widget.TextView;

import com.MemoryLadder.TestDetailsScreen.TestDetailsActivity;
import com.mastersofmemory.memoryladder.R;
//import com.MemoryLadderFull.R;

public class Lists_Review extends Activity implements OnClickListener {
	
	private GridView grid;
	private Button button1;
	private Button button2;
	private Button Previous;
	private Button Next;
	private  int numRows;
	private  int numCols;
	private  int numItems;
	private  int gameType;
	private int mode;
	private int numPages = 1;
	private int currentPage = 0;
	private int score;
	private  String[][] guess;
	private  String[][] answer;
	private  String[] strings;
	private TextView PageNumber;
	private ListAdapter adapter;
	
	private static final int LISTS_WORDS  = Constants.LISTS_WORDS;
	private static final int LISTS_EVENTS = Constants.LISTS_EVENTS;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getExtras();   
        setScreenOrientation();

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game);
                             
        initButtons();
        initText();
        
        grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(numCols);
        grid.setVerticalSpacing(0);
        adapter = new ListAdapter(this);
        grid.setAdapter(adapter);
        
    }
            
    public void initButtons() {
    	button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button1.setText("Back");
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button2.setText("Play Again");
        
        if (numPages > 1) {
        	Previous = (Button) findViewById(R.id.Previous);
        	Previous.setVisibility(View.VISIBLE);
        	Previous.setOnClickListener(this);
            
            Next = (Button) findViewById(R.id.Next);
            Next.setVisibility(View.VISIBLE);
            Next.setOnClickListener(this);
            
            PageNumber = (TextView) findViewById(R.id.PageNumber);
            PageNumber.setVisibility(View.VISIBLE);
            updatePageNumberText();
        }
    }
    
    public void setScreenOrientation() {
    	int screenSize = getResources().getConfiguration().screenLayout;
    	if ((screenSize&Configuration.SCREENLAYOUT_SIZE_MASK) == 4)
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    public void updatePageNumberText() {
    	PageNumber.setText("Page " + (currentPage+1) + " of " + numPages);
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
    
    private class ListAdapter extends BaseAdapter {

        public ListAdapter(Context context) {}
        public int getCount() {           			 return numItems;        }
        public Object getItem(int position) {        return null;        }
        public long getItemId(int position) {        return 0;        }
                        
        public View getView(int position, View convertView, ViewGroup parent) {
        	View MyView = convertView;
        	
        	TextView numtext;
            TextView wordtext;
            
            if (convertView == null) {            	
                MyView = getLayoutInflater().inflate(R.layout.list, null);                                 
                EditText datebox = (EditText)  MyView.findViewById(R.id.datebox); datebox.setVisibility(View.GONE);
                EditText wordbox = (EditText)  MyView.findViewById(R.id.wordbox); wordbox.setVisibility(View.GONE);
                
                numtext  = (TextView) MyView.findViewById(R.id.numtext);
            	wordtext = (TextView) MyView.findViewById(R.id.wordtext);
                if (gameType == LISTS_EVENTS){
                	numtext.setTextSize(20);
                	wordtext.setTextSize(20); 
                }
            }
            else {
            	numtext = (TextView)  MyView.findViewById(R.id.numtext);
            	wordtext = (TextView) MyView.findViewById(R.id.wordtext);                                        
            }
            
            position = position + ( numItems * currentPage );
            int row = getRow(position);
        	int col = getCol(position);
        	
            String s = strings[position];
            String num =  s.substring(0, s.indexOf(" "));
            String rest = s.substring(s.indexOf(" ") + 1, s.length());          
         
            
            if (gameType == LISTS_WORDS) {
            	
            	TableRow tablerow = (TableRow) MyView.findViewById(R.id.tableRow1);
            	            	
            	numtext.setLines(1);
	            numtext.setText(num);
	            wordtext.setText(rest + "\n" + guess[row][col]);
	            
	            if (rest.equals(guess[row][col])) {
	            	tablerow.setBackgroundResource(R.drawable.outline_whiteongreenlong);
	            	numtext.setTextColor(Color.BLACK);
	                wordtext.setTextColor(Color.BLACK);
	            }
	            else if (!guess[row][col].equals("")) {
	            	tablerow.setBackgroundResource(R.drawable.outline_whiteonredlong);
	            	numtext.setTextColor(Color.BLACK);
	                wordtext.setTextColor(Color.BLACK);
	            }
	            else {
	            	tablerow.setBackgroundResource(R.drawable.outline_whiteonblacklong);
	            	numtext.setTextColor(Color.WHITE);
	                wordtext.setTextColor(Color.WHITE);
	            }
	            
            }
          
            else {
            	TableRow tablerow = (TableRow) MyView.findViewById(R.id.tableRow1);
            	//numtext.setLines(1);
	            numtext.setText(num + "\n" + guess[row][col]);
            	//numtext.setText(num);
	            wordtext.setText(rest);
	            
	            if (num.equals(guess[row][col])) {
	            	//wordtext.setBackgroundColor(Color.GREEN);
	            	tablerow.setBackgroundResource(R.drawable.outline_whiteongreenlong);
	            	numtext.setTextColor(Color.BLACK);
	                wordtext.setTextColor(Color.BLACK);
	            }
	            else if (!guess[row][col].equals("")) {
	            	//wordtext.setBackgroundColor(Color.RED);
	            	tablerow.setBackgroundResource(R.drawable.outline_whiteonredlong);
	            	numtext.setTextColor(Color.BLACK);
	                wordtext.setTextColor(Color.BLACK);
	            }
	            else {
	            	//wordtext.setBackgroundColor(Color.BLACK);
	            	tablerow.setBackgroundResource(R.drawable.outline_whiteonblacklong);
	            	numtext.setTextColor(Color.WHITE);
	                wordtext.setTextColor(Color.WHITE);
	            }
	            
            }    
            return MyView;        
        }             
    }
    
    
    public void getExtras() {
    	Intent intent = getIntent();
        numRows = intent.getIntExtra("numRows", -1);
        numCols = intent.getIntExtra("numCols", -1);
        
        gameType = intent.getIntExtra("gameType", -1);
        mode     = intent.getIntExtra("mode", -1);
        strings  = intent.getStringArrayExtra("strings"); 
        score    = intent.getIntExtra("score", -1);
        
        if (gameType == LISTS_WORDS) {
        	if (numCols <= 5)
        		numPages = 1;
        	else {
        		numPages = numCols / 5;
        		numCols = 5;
        	}
        }
        
        numItems = numRows * numCols;
        
        getGuessArray(intent);
        getAnswerArray(intent);
    }
    
    public void getGuessArray(Intent intent) {
    	int i=0;
        guess = new String[numRows * numPages][numCols];
        while (intent.hasExtra("guess" + i)) {
        	guess[i] = intent.getStringArrayExtra("guess" + i);   // reconstruct our 2d array, one linear array at a time
        	i++;
        }
    }
    
    public void getAnswerArray(Intent intent) {
    	int i=0;
        answer = new String[numRows * numPages][numCols];
        while (intent.hasExtra("answer" + i)) {
        	answer[i] = intent.getStringArrayExtra("answer" + i);   // reconstruct our 2d array, one linear array at a time
        	i++;
        }
    }
    
    
    public void launchScoreActivity() {
    	finish();
    }
    
    public void launchTest() {
		Intent i = new Intent(this, TestDetailsActivity.class);
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
		else if (v == Previous) {
			if (currentPage > 0) {
				currentPage--;
				adapter.notifyDataSetChanged();
				updatePageNumberText();
			}
		}
		else if (v == Next) {		
			if (currentPage < (numPages - 1)) {
				currentPage++;
				adapter.notifyDataSetChanged();
				updatePageNumberText();
			}
		}
	}	
}