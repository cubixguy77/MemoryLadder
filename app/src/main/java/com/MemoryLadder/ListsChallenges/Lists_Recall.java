package com.memoryladder.listschallenges;

import java.util.Arrays;
import java.util.Collections;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TableRow;
import android.widget.TextView;

import com.memoryladder.Constants;
import com.memoryladder.scoring.ScoreActivity;
import com.memoryladder.scoring.Scoring;
import com.memoryladder.testdetailsscreen.TestDetailsActivity;
import com.memoryladder.taketest.timer.CountDownTimerPausable;
import com.memoryladder.Utils;
import com.mastersofmemory.memoryladder.R;

public class Lists_Recall extends Activity implements OnClickListener {
	
	private GridView grid;
	private Button button1;
	private Button button2;
	private Button Previous;
	private Button Next;
	private  int numRows;
	private  int numCols;
	private  int numItems;
	private  int recallTime;
	private  int gameType;
	private  int mode;
	private int numPages = 1;
	private int currentPage = 0;
	private  String[][] guess;
	private  String[][] answer;
	private  String[] strings;
	private Boolean timeExpired = false;
	private Boolean userQuit = false;
	private Timer timer;
	private TextView timerText;
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
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.game);

        
        if (gameType == LISTS_WORDS)
        	getAnswers_WORDS();
        else
        	getAnswers_EVENTS();
        
        initButtons();
        initText();
        
        grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(numCols);
        grid.setVerticalSpacing(0);
        adapter = new ListAdapter(this);
        grid.setAdapter(adapter);

		/* the following resolves a crash caused by scrolling of a listview full of edittexts.
		 * see http://stackoverflow.com/a/40659632/215141 for more info */
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
    }
    
    @Override
    public void onResume() {
    	super.onResume();

		if (timer == null) {
			initTimer();
		}

    	timer.start();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	if (timer != null)
    		timer.pause();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void initTimer() {
    	timerText = (TextView) findViewById(R.id.timerText);
    	timer = new Timer(recallTime * 1000, 1000, timerText);
    }
    
    public void initButtons() {
    	button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button1.setText("Stop");
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button2.setText("Submit");
                
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
    
    public void showPageSelectors(int vis) {
    	if (numPages > 1) {
        	Previous = (Button) findViewById(R.id.Previous);
        	Previous.setVisibility(vis);        	
            Next = (Button) findViewById(R.id.Next);
            Next.setVisibility(vis);            
            PageNumber = (TextView) findViewById(R.id.PageNumber);
            PageNumber.setVisibility(vis);
            updatePageNumberText();
        }
    }
    
    public void updatePageNumberText() {
    	PageNumber.setText("Page " + (currentPage+1) + " of " + numPages);
    }
    
    public void initText() {
    	TextView TitleText = (TextView) findViewById(R.id.TitleText);
    	TitleText.setText("Recall");
    }    
    
    public void getAnswers_WORDS() {
    	guess = new String[numRows * numPages][numCols];
    	answer = new String[numRows * numPages][numCols];
    	for (int i=0; i<numItems * numPages; i++) {
    		String s = strings[i];
    		guess[getRow(i)][getCol(i)] = "";
    		answer[getRow(i)][getCol(i)] = s.substring(s.indexOf(" ") + 1, s.length());
    	}
    }
    
    public void getAnswers_EVENTS() {
    	guess = new String[numRows][numCols];
    	answer = new String[numRows][numCols];
    	Collections.shuffle(Arrays.asList(strings));
    	
    	for (int i=0; i<numItems; i++) {
    		String s = strings[i];
    		guess[getRow(i)][getCol(i)] = "";
    		answer[getRow(i)][getCol(i)] = s.substring(0, s.indexOf(" "));
    	}
    }      
            
    public int getCol(int pos) {
    	return (pos % numCols);
    }
    public int getRow(int pos) {
    	return (pos / numCols);
    }




	private class ListAdapter extends BaseAdapter {

        public ListAdapter(Context context) {}
        public int getCount() {           			 return numItems;        }
        public Object getItem(int position) {        return null;        }
        public long getItemId(int position) {        return 0;        }
        
        public EditText setEditTextFiltersWords(EditText datebox, EditText wordbox) {
        	datebox.setVisibility(View.GONE);        	
        	int maxLength = 15;
        	InputFilter[] FilterArray = new InputFilter[1];
        	FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        	wordbox.setFilters(FilterArray);
        	wordbox.setText("");   
        	wordbox.setSingleLine();
        	wordbox.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI); 
        	return wordbox;
        }
        public EditText setEditTextFiltersEvents(EditText datebox, EditText wordbox) {
        	wordbox.setVisibility(View.GONE);
        	int maxLength = 4;
        	InputFilter[] FilterArray = new InputFilter[1];
        	FilterArray[0] = new InputFilter.LengthFilter(maxLength);
        	datebox.setFilters(FilterArray);
        	datebox.setInputType(InputType.TYPE_CLASS_NUMBER);
        	datebox.setText("");
        	datebox.setSingleLine();
        	datebox.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI); 
        	return datebox;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            
        	TextView numtext;
            TextView wordtext;
            
        	if (convertView == null  || convertView.getTag() == null)  {
        		convertView = getLayoutInflater().inflate(R.layout.list, null);
        		holder = new ViewHolder();
        		holder.numtext    = (TextView) convertView.findViewById(R.id.numtext);  
        		holder.wordtext   = (TextView) convertView.findViewById(R.id.wordtext);  
        		holder.datebox = (EditText) convertView.findViewById(R.id.datebox);
        		holder.wordbox = (EditText) convertView.findViewById(R.id.wordbox);
        		
                numtext  = (TextView) convertView.findViewById(R.id.numtext);
            	wordtext = (TextView) convertView.findViewById(R.id.wordtext);
                if (gameType == LISTS_EVENTS) {
                	TableRow row = (TableRow) convertView.findViewById(R.id.tableRow1);
                	row.setBackgroundResource(R.drawable.outline_whiteonblackextralong);
                	numtext.setTextSize(20);
                	wordtext.setTextSize(20); 
                }
            
        		TextWatcher t = new TextWatcher() {	                    
                	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                    public void afterTextChanged(Editable s) {
                    	guess[getRow(holder.ref)][getCol(holder.ref)] = s.toString();
                 }};
                 
        		if (gameType == LISTS_WORDS) {
        			holder.wordbox = setEditTextFiltersWords(holder.datebox, holder.wordbox);
                	holder.wordbox.addTextChangedListener(t);
                	holder.wordtext.setVisibility(View.GONE);
                }
                else {
                	holder.datebox = setEditTextFiltersEvents(holder.datebox, holder.wordbox);
                	holder.datebox.addTextChangedListener(t);
                	holder.numtext.setVisibility(View.GONE);
                }                  
        		convertView.setTag(holder); 
        	}
        	else  {
        		holder = (ViewHolder) convertView.getTag();
                numtext  = (TextView) convertView.findViewById(R.id.numtext);
            	wordtext = (TextView) convertView.findViewById(R.id.wordtext);
        	}
        	
        	position = position + (numItems * currentPage);
        	holder.ref = position;
        	String s = strings[position];
        	
        	if (gameType == LISTS_WORDS) {
        		holder.wordbox.setText(guess[getRow(position)][getCol(position)]);
        		String num =  s.substring(0, s.indexOf(" "));
            	holder.numtext.setText(num);
        	}
        	else {
        		holder.datebox.setText(guess[getRow(position)][getCol(position)]);
        		String rest =  s.substring(s.indexOf(" ") + 1, s.length());
            	holder.wordtext.setText(rest);
        	}
        	
            return convertView;                  
        }
    }
    
    static class ViewHolder {
    	TextView numtext;
    	TextView wordtext;
    	EditText datebox;
    	EditText wordbox;
    	int ref;
    }
    
    public void getExtras() {
    	Intent intent = getIntent();
        numRows    = intent.getIntExtra("numRows", -1);
        numCols    = intent.getIntExtra("numCols", -1);
        recallTime = intent.getIntExtra("recallTime",  -1);
        gameType   = intent.getIntExtra("gameType", -1);
        mode   = intent.getIntExtra("mode", -1);
        strings    = intent.getStringArrayExtra("strings");
        
        if (gameType == LISTS_WORDS) {
        	if (numCols <= 5)
        		numPages = 1;
        	else {
        		numPages = numCols / 5;
        		numCols = 5;
        	}
        }
        numItems  = numRows * numCols;
    }
    
    public void launchScoreActivity() {
    	timer.cancel();
    	Intent i = getIntent();
         	
    	for (int row=0; row<numRows * numPages; row++) 
			i.putExtra("guess" + row, guess[row]);	
		for (int row=0; row<numRows; row++) 
			i.putExtra("answer" + row, answer[row]);
    	
    	
    	if (gameType == LISTS_WORDS) {	    	
    		int[] analysis = Scoring.getScore(guess, answer, numRows);
    		
    		int percent;
            if (analysis[1] == 0)
            	percent = 0;
            else
            	percent = (int) (100 * ((double) analysis[0] / analysis[1]));
            int misspelled = analysis[2];
	        int score = analysis[3];	        
	        
	        String[] scores = new String[4];
	        scores[0] = "Recall Accuracy:" +  percent + "% (" +  analysis[0] + " / " +  analysis[1] + ")";
	        scores[1] = "Memorization Time:" + Utils.formatIntoHHMMSStruncated(i.getIntExtra("memTimeUsed", -1));
	        scores[2] = "Misspelled:" + misspelled;
	        scores[3] = "Score:" +  score;
	        i.putExtra("scores", scores);
    	}
    	else {
    		int[] analysis = Scoring.getScore(gameType, guess, answer);
    		int percent;
            if (analysis[1] == 0)
            	percent = 0;
            else
            	percent = (int) (100 * ((double) analysis[0] / analysis[1]));
	        int score = analysis[2];
	        
	        String[] scores = new String[4];
	        scores[0] = "Recall Accuracy:" +  percent + "% (" +  analysis[0] + " / " +  analysis[1] + ")";
	        scores[1] = "Memorization Time:" + Utils.formatIntoHHMMSStruncated(i.getIntExtra("memTimeUsed", -1));
	        scores[2] = "hide";
	        scores[3] = "Score:" +  score;
	        i.putExtra("scores", scores);
    	}
		
        i.setClass(this, ScoreActivity.class);
		this.startActivity(i);
		finish();
    }
    
	@Override
	public void onClick(View v) {
		if (v == button1)
			launchExitDialog();
		else if (v == button2 && !timeExpired && !userQuit)
			launchScoreActivity();
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
	
	
	
	
	public void launchExitDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
    	builder.setMessage("Stop current game?")
    	       .setCancelable(false)
    	       .setPositiveButton("Stop Game", (dialog, id) -> {
                   userQuit = true;
                   timer.cancel();

                   Intent i = getIntent();
                   i.setClass(Lists_Recall.this, TestDetailsActivity.class);
                   i.putExtra("gameType", gameType);
                   i.putExtra("mode", mode);
                   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(i);
               })
    	       .setNegativeButton("Continue Game", (dialog, id) -> dialog.cancel());
    	AlertDialog alert = builder.create();
    	alert.show();
    }
	
	
	
	
	
	private class Timer extends CountDownTimerPausable {
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
			
			showPageSelectors(View.INVISIBLE);
			
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