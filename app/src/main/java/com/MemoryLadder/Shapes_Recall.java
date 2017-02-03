package com.MemoryLadder;

import java.util.HashMap;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;
//import com.MemoryLadderFull.R;

public class Shapes_Recall extends Activity implements OnClickListener {
	
	private GridView grid;
	private Button button1;
	private Button button2;
	private int numRows;
	private int numCols;
	private int recallTime;
	private int gameType;
	private String[][] guess;
	private String[][] answer;
	private int[] images;
	private String[] strings;
	private Boolean timeExpired = false;
	private Boolean userQuit = false;
	private Timer timer;
	private TextView timerText;
	private EditText focusedView;
	
	private Button keyA;
	private Button keyB;
	private Button keyC;
	private Button keyD;
	private Button keyE;
	private Button backspace;
	
	final private static int SHAPES_FACES    = Constants.SHAPES_FACES;
	final private static int SHAPES_ABSTRACT = Constants.SHAPES_ABSTRACT;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getExtras();
        setScreenOrientation();
        
        setContentView(R.layout.game);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                
        getAnswers();
        
        initButtons();
        initText();                
        initGrid();        
        initList();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void initGrid() {
        grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(numCols);
        grid.setBackgroundColor(Color.WHITE);
        
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) grid.getLayoutParams();
    	params.addRule(RelativeLayout.ABOVE, R.id.KeyboardLayout);
    	grid.setLayoutParams(params);

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
    
    public void initTimer() {
    	timerText = (TextView) findViewById(R.id.timerText);
    	timer = new Timer(recallTime * 1000, 1000, timerText);
    }
    
    public void getAnswers() {
    	answer = new String[numRows][numCols];
    	guess = new String[numRows][numCols];
    	
    	/* Create a map that matches image Ids with the corresponding name */
    	HashMap<Integer, String> map = new HashMap<Integer, String>();
    	for (int i=0; i<strings.length; i++) {
    		map.put(images[i], strings[i]);
    	}    		    	
    	
    	if (gameType == SHAPES_FACES)
    		images = Utils.shuffleIntArray(images);  // shuffle faces around
        else
        	images = Utils.shuffleIntArrayByRow(images, numRows, numCols);    // shuffle images within each row
    	
    	for (int i=0; i<strings.length; i++) {
    		answer[getRow(i)][getCol(i)] = map.get(images[i]);
    		guess[getRow(i)][getCol(i)] = "";
    	}
    	
    }        
    
    public void initButtons() {
    	button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button1.setText("Stop");
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button2.setText("Done");
        
        if (gameType == SHAPES_ABSTRACT) {
        	LinearLayout KeyboardLayout = (LinearLayout) findViewById(R.id.KeyboardLayout);
        	KeyboardLayout.setVisibility(View.VISIBLE);
        	        	
	        keyA = (Button) findViewById(R.id.keyA);
	        keyA.setOnClickListener(this);
	        keyA.setText("1");
	        
	        keyB = (Button) findViewById(R.id.keyB);
	        keyB.setOnClickListener(this);
	        keyB.setText("2");
	        
	        keyC = (Button) findViewById(R.id.keyC);
	        keyC.setOnClickListener(this);
	        keyC.setText("3");
	        
	        keyD = (Button) findViewById(R.id.keyD);
	        keyD.setOnClickListener(this);
	        keyD.setText("4");
	        
	        keyE = (Button) findViewById(R.id.keyE);
	        keyE.setOnClickListener(this);
	        keyE.setText("5");
	        
	        backspace = (Button) findViewById(R.id.backspace);
	        backspace.setOnClickListener(this);
        }
    }
    
    public void setScreenOrientation() {
    	int screenSize = getResources().getConfiguration().screenLayout;
    	if ((screenSize&Configuration.SCREENLAYOUT_SIZE_MASK) == 4)
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    public void initText() {
    	TextView TitleText = (TextView) findViewById(R.id.TitleText);
    	TitleText.setText("Recall");
    }
            
    public int getCol(int pos) {
    	return pos % numCols;
    }
    public int getRow(int pos) {
    	return pos / numCols;
    }
    
    
    public void initList()
    {
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.shape, strings)
        {
                    	
        	public EditText setEditTextFilters(EditText et) {
            	if (gameType == SHAPES_ABSTRACT) 
    	        	et.setInputType(InputType.TYPE_NULL);
            	else {
            		et.setImeOptions(EditorInfo.IME_ACTION_DONE);
            		et.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
            	}
            	return et;
            }
        	
        	@Override
            public View getView(final int position, View convertView, ViewGroup parent)  {
                final ViewHolder holder;
                
            	if (convertView == null  || convertView.getTag() == null)  {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.shape, null);
                   
                    holder = new ViewHolder();
                    holder.text = (TextView) convertView.findViewById(R.id.shape_text);
                    holder.image = (ImageView) convertView.findViewById(R.id.shape_image);
                    holder.editText = (EditText) convertView.findViewById(R.id.shape_edittext);
                    holder.editText = setEditTextFilters(holder.editText);
                    holder.editText.addTextChangedListener(new TextWatcher() {	                    
                    	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
	                    public void onTextChanged(CharSequence s, int start, int before, int count) {
	                    	if (gameType == SHAPES_ABSTRACT && before == 0 && count == 1) {
	                    		;
	                    	}	 
	                    }
	                    public void afterTextChanged(Editable s) {
	                    	guess[getRow(holder.ref)][getCol(holder.ref)]= s.toString(); 
	                    }
	                });
                    if (gameType == SHAPES_ABSTRACT) {
	                    holder.editText.setOnFocusChangeListener(new OnFocusChangeListener() {
	                    	@Override
	                    	public void onFocusChange(View view, boolean hasFocus) {
	                    	    focusedView = (EditText) view;
	                    	}
	                    });
                    }
                    
                    convertView.setTag(holder);                    
                }
                else {
                	holder = (ViewHolder) convertView.getTag();
                }
            	holder.ref = position;
            	holder.editText.setText(guess[getRow(position)][getCol(position)]);           	            	
                
                holder.image.setImageResource(images[position]);
                
            	if (gameType == SHAPES_ABSTRACT)
            		holder.text.setText("Seq:");
            	else
            		holder.text.setVisibility(View.GONE); 	
                
            	return convertView;
            }
        };

        grid.setAdapter(listAdapter);
    }
    
    
    static class ViewHolder {
    	TextView text;
    	ImageView image;
    	EditText editText;
    	String string;
    	int ref;
    }
    
    public void getExtras() {
    	Intent intent = getIntent();
        numRows     = intent.getIntExtra("numRows", -1);
        numCols     = intent.getIntExtra("numCols", -1);
        recallTime  = intent.getIntExtra("recallTime",  -1);
        gameType    = intent.getIntExtra("gameType", -1);
        images      = intent.getIntArrayExtra("images");
        strings     = intent.getStringArrayExtra("strings");        
    }
    
    public void launchScoreActivity() {
    	timer.cancel();
    	
    	Intent i = getIntent();      
    	
    	for (int row=0; row<numRows; row++) 
			i.putExtra("guess" + row, guess[row]);	 
		for (int row=0; row<numRows; row++) 
			i.putExtra("answer" + row, answer[row]);
        
    	
    	int[] analysis = Scoring.getScore(gameType, guess, answer);
    	
    	if (gameType == SHAPES_FACES) {	    	
    		int percentFirst;
            if (analysis[1] == 0)
            	percentFirst = 0;
            else
            	percentFirst = (int) (100 * ((double) analysis[0] / analysis[1]));
	        
            int percentLast;
            if (analysis[3] == 0)
            	percentLast = 0;
            else
            	percentLast = (int) (100 * ((double) analysis[2] / analysis[3]));
            
	        int score = analysis[4];
	        
	        String[] scores = new String[4];
	        scores[0] = "First Name Accuracy:" + percentFirst + "% (" + analysis[0] + " / " + analysis[1] + ")";
	        scores[1] = "Last Name Accuracy:"  + percentLast  + "% (" + analysis[2] + " / " + analysis[3] + ")";
	        scores[2] = "Memorization Time:" + Utils.formatIntoHHMMSStruncated(i.getIntExtra("memTimeUsed", -1));
	        scores[3] = "Score:" + score;
	        i.putExtra("scores", scores);
    	}
    	else {
    		int percent;
    		if (analysis[1] == 0)
            	percent = 0;
            else
            	percent = (int) (100 * ((double) analysis[0] / analysis[1]));
    		
	        int score = analysis[2];
	        
	        String[] scores = new String[4];
	        scores[0] = "Recall Accuracy:" + percent + "% (" +  analysis[0] + " / " +  analysis[1] + ")";
	        scores[1] = "Memorization Time:" + Utils.formatIntoHHMMSStruncated(i.getIntExtra("memTimeUsed", -1));
	        scores[2] = "hide";
	        scores[3] = "Score:" +  score;
	        i.putExtra("scores", scores);
    	}
    	    	
		i.setClass(this, ScoreActivity.class);
        this.startActivity(i);
    }
    
	@Override
	public void onClick(View v) {		
		if (v == button1)
			launchExitDialog();
		else if (v == button2 && !timeExpired && !userQuit)
			launchScoreActivity();
		else if (focusedView != null) {
			if (v == keyA) {
				focusedView.setText("1");
			}
			else if (v == keyB) {
				focusedView.setText("2");
			}
			else if (v == keyC) {
				focusedView.setText("3");
			}
			else if (v == keyD) {
				focusedView.setText("4");
			}
			else if (v == keyE) {
				focusedView.setText("5");
			}
			else if (v == backspace) {
				focusedView.setText("");
			}
			
			EditText next = (EditText) focusedView.focusSearch(View.FOCUS_RIGHT);
			if (next != null)
				next.requestFocus();
		}
	}
	
	
	
	
	
	
	public void launchExitDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Stop current game?")
    	       .setCancelable(false)
    	       .setPositiveButton("Stop Game", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   Shapes_Recall.this.startActivity(new Intent(Shapes_Recall.this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
			timesUp.setVisibility(View.VISIBLE);
			
			grid.setVisibility(View.INVISIBLE);
						
			LinearLayout KeyboardLayout = (LinearLayout) findViewById(R.id.KeyboardLayout);
        	KeyboardLayout.setVisibility(View.GONE);
			
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