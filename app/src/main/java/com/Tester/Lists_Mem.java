package com.Tester;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
//import com.MemoryLadderFull.R;

public class Lists_Mem extends Activity implements OnClickListener, android.content.DialogInterface.OnDismissListener {
	
	private GridView grid;
	private Button button1;
	private Button button2;
	private Button Previous;
	private Button Next;
	private Button StartButton;
    private Button InstructionsButton;
	private int numRows;
	private int numCols;
	private int numItems;
	private int gameType;
	private int memTime;
	private int numPages = 1;
	private int currentPage = 0;
	private String[] strings;
	private Timer timer;
	private TextView timerText;
	private TextView PageNumber;
	private ListAdapter adapter;
	private Boolean timeExpired = false;
	private Boolean userQuit = false;
	private static final int LISTS_WORDS  = Constants.LISTS_WORDS;
	private static final int LISTS_EVENTS = Constants.LISTS_EVENTS;	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();                
        setScreenOrientation();
        
        setContentView(R.layout.game);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);        
        loadStrings();
        initButtons();
        initTimer();
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	if (timer != null)
    		timer.cancel();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void startMem() {
    	StartButton.setVisibility(View.INVISIBLE);
    	InstructionsButton.setVisibility(View.INVISIBLE);
    	showPageSelectors(View.VISIBLE);
    	    	
    	CountDownDialog count = new CountDownDialog(this);
    	count.setOnDismissListener(this);
    	count.show();
    }
    
    public void setListener() {
        button2.setOnClickListener(this);
        if (numPages > 1) {
        	Previous.setOnClickListener(this);
        	Next.setOnClickListener(this);
        }
    }
    
    public void initGrid() {
    	grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(numCols);
        adapter = new ListAdapter(this);
        grid.setAdapter(adapter);
    }
    
    public void setScreenOrientation() {
    	int screenSize = getResources().getConfiguration().screenLayout;
    	if ((screenSize&Configuration.SCREENLAYOUT_SIZE_MASK) == 4)
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
                    
    public void loadStrings() {
    	if (gameType == LISTS_WORDS)
    		loadWordStrings();
    	else
    		loadDateStrings();
    }
    
    public void initTimer() {
    	timerText = (TextView) findViewById(R.id.timerText);
    	timer = new Timer(memTime * 1000, 1000, timerText);
    }
    public void startTimer() {
    	timer.start();
    }
    
    public void loadWordStrings() {    	
    	strings = getRandomWordsArray(numItems * numPages);
    }
    
    public int getTranspose(int i) {
    	return (numRows * getCol(i)) + getRow(i) + 1 + (numItems * (i / numItems));
    }
    
    public void loadDateStrings() {   	   	
    	strings = getHistoricalDatesArray(numItems);
    }	    
    
    public String getRandomDate() {
    	Random generator = new Random();
    	Integer date = 1000 + generator.nextInt(1100);
    	return date.toString();    	
    }
    
    public void initButtons() {
    	button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button1.setText("Stop");
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setText("Recall");  
        
        StartButton = (Button) findViewById(R.id.StartButton);
        StartButton.setOnClickListener(this);
        StartButton.setVisibility(View.VISIBLE);
        
        InstructionsButton = (Button) findViewById(R.id.InstructionsButton);
        InstructionsButton.setOnClickListener(this);
        InstructionsButton.setVisibility(View.VISIBLE);        
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
    
    public int getCol(int pos) {  	return pos % numCols;   }
    public int getRow(int pos) {   	return (pos / numCols) % numRows;   }
    
    private class ListAdapter extends BaseAdapter {
                
        public ListAdapter(Context context) {  	   }
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
                if (gameType == LISTS_EVENTS) {
                	TableRow row = (TableRow) MyView.findViewById(R.id.tableRow1);
                	row.setBackgroundResource(R.drawable.outline_whiteonblackextralong);
                	numtext.setTextSize(20);
                	wordtext.setTextSize(20); 
                }
            }
            else {
            	numtext  = (TextView) MyView.findViewById(R.id.numtext);
            	wordtext = (TextView) MyView.findViewById(R.id.wordtext);  
            }
            position = position + ( numItems * currentPage );
            
            String s = strings[position];
            String num =  s.substring(0, s.indexOf(" "));
            String rest = s.substring(s.indexOf(" ") + 1, s.length());
            
            numtext.setText(num);
            wordtext.setText(rest);
                        
            return MyView;        
        }
    }
    
    public void launchRecallActivity() {
    	
    	timer.cancel();
    	
    	Intent i = getIntent();
		i.putExtra("strings", strings);
		i.putExtra("memTimeUsed", timer.getSecondsElapsed());

		i.setClass(this, Lists_Recall.class);
		this.startActivity(i);
    }

    public void getExtras() {
    	Intent i = getIntent();
        gameType        = i.getIntExtra("gameType",    -1);
        numRows         = i.getIntExtra("numRows",     -1);
        numCols         = i.getIntExtra("numCols",     -1);
        memTime         = i.getIntExtra("memTime",     -1);
        
        if (gameType == LISTS_WORDS) {
        	if (numCols <= 5)
        		numPages = 1;
        	else {
        		numPages = numCols / 5;
        		numCols = 5;
        	}
        }
        numItems = numRows * numCols;
    }
    
	@Override
	public void onClick(View v) {
		if (v == button1)
			launchExitDialog();
		else if (v == button2 && !timeExpired && !userQuit) {
			launchRecallActivity();
		}
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
		else if (v == StartButton)
			startMem();
		else if (v == InstructionsButton) {
			InstructionsDialog dialog = new InstructionsDialog(this, gameType);
			dialog.show();
		}
	}
	
	
	
	
	
	
	
	public String[] getRandomWordsArray(int numstrings) {
		String[] output = new String[numstrings];
		
		int elementsperarray = (numstrings / 5) + 1;
		int index = 0;
				
		String[] temp = getResources().getStringArray(R.array.randomwords1);
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<elementsperarray; i++) {
			if (index >= numstrings) return output;
			else output[index] = getTranspose(index) + " " + temp[i];
			index++;
		}
		temp = getResources().getStringArray(R.array.randomwords2);
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<elementsperarray; i++) {
			if (index >= numstrings) return output;
			else output[index] = getTranspose(index) + " " + temp[i];
			index++;
		}
		temp = getResources().getStringArray(R.array.randomwords3);
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<elementsperarray; i++) {
			if (index >= numstrings) return output;
			else output[index] = getTranspose(index) + " " + temp[i];
			index++;
		}
		temp = getResources().getStringArray(R.array.randomwords4);
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<elementsperarray; i++) {
			if (index >= numstrings) return output;
			else output[index] = getTranspose(index) + " " + temp[i];
			index++;
		}
		temp = getResources().getStringArray(R.array.randomwords5);
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<elementsperarray; i++) {
			if (index >= numstrings) return output;
			else output[index] = getTranspose(index) + " " + temp[i];
			index++;
		}
		return output;
	}
	
	
	
	public String[] getHistoricalDatesArray(int numstrings) {
		String[] output = new String[numstrings];
		
		int elementsperarray = (numstrings / 5) + 1;
		int index = 0;
				
		String[] temp = getResources().getStringArray(R.array.historicaldates1);
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<elementsperarray; i++) {
			if (index >= numstrings) return output;
			else output[index] = getRandomDate() + " " + temp[i];
			index++;
		}
		temp = getResources().getStringArray(R.array.historicaldates2);
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<elementsperarray; i++) {
			if (index >= numstrings) return output;
			else output[index] = getRandomDate() + " " + temp[i];
			index++;
		}
		temp = getResources().getStringArray(R.array.historicaldates3);
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<elementsperarray; i++) {
			if (index >= numstrings) return output;
			else output[index] = getRandomDate() + " " + temp[i];
			index++;
		}
		temp = getResources().getStringArray(R.array.historicaldates4);
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<elementsperarray; i++) {
			if (index >= numstrings) return output;
			else output[index] = getRandomDate() + " " + temp[i];
			index++;
		}
		temp = getResources().getStringArray(R.array.historicaldates5);
		Collections.shuffle(Arrays.asList(temp));
		for (int i=0; i<elementsperarray; i++) {
			if (index >= numstrings) return output;
			else output[index] = getRandomDate() + " " + temp[i];
			index++;
		}
		return output;
	}
	
	
	
	
	
	
	
	
	
	public void launchExitDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Stop current game?")
    	       .setCancelable(false)
    	       .setPositiveButton("Stop Game", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   Lists_Mem.this.startActivity(new Intent(Lists_Mem.this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
			
			showPageSelectors(View.INVISIBLE);
			
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
     	initGrid();   	         
        startTimer();  
    	setListener();
	}
	
}