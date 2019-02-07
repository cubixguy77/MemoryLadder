package com.memoryladder.shapeschallenges;

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
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.memoryladder.Constants;
import com.memoryladder.CountDownDialog;
import com.memoryladder.InstructionsDialog;
import com.memoryladder.taketest.timer.CountDownTimerPausable;
import com.memoryladder.Utils;
import com.mastersofmemory.memoryladder.R;

public class Shapes_Mem extends Activity implements OnClickListener, android.content.DialogInterface.OnDismissListener {
	
	private GridView grid;
	private Button button1;
	private Button button2;
	private Button StartButton;
    private Button InstructionsButton;
	private int numRows;
	private int numCols;
	private int numItems;
	private int gameType;
	private int memTime;
	private String[] strings;
	private int[]     images;
	private Timer timer;
	private TextView timerText;
	private Boolean timeExpired = false;
	private Boolean userQuit = false;
	
	final private static int SHAPES_FACES    = Constants.SHAPES_FACES;
//	final private static int SHAPES_ABSTRACT = Constants.SHAPES_ABSTRACT;	
	
	final int MALE   = 0; 
	final int FEMALE = 1;
	
	final int NUM_MALE_FACES = 383;
	final int NUM_FEMALE_FACES = 364;
	
	final int NUM_ABSTRACT_IMAGES = 200;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getExtras();
        setScreenOrientation();

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.game);

        initButtons();
        loadData();
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
		if (timer != null && !timer.isPaused())
			timer.pause();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void startMem() {
    	StartButton.setVisibility(View.INVISIBLE);
    	InstructionsButton.setVisibility(View.INVISIBLE);
    	
    	
    	CountDownDialog count = new CountDownDialog(this);
    	count.setOnDismissListener(this);
    	count.show();
    }
    
    public void setListener() {
        button2.setOnClickListener(this);
    }
    
    public void initGrid() {
    	grid = (GridView) findViewById(R.id.grid);
        grid.setNumColumns(5);
        grid.setBackgroundColor(Color.WHITE);
        grid.setAdapter(new ShapeAdapter(this));
    }
    
    public void setScreenOrientation() {
    	if (gameType == SHAPES_FACES) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			return;
		}

    	int screenSize = getResources().getConfiguration().screenLayout;
    	if ((screenSize&Configuration.SCREENLAYOUT_SIZE_MASK) == 4)
    		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    public void initTimer() {
    	timerText = (TextView) findViewById(R.id.timerText);
    	timer = new Timer(memTime * 1000, 1000, timerText);
    }
    public void startTimer() {
    	timer.start();
    }
    
    public void loadData() {
    	if (gameType == SHAPES_FACES)
    		loadData_FACES();
    	else
    		loadData_IMAGES(); 
    }

    /*
    public Bitmap getBitmap(int fileNum) {

        try {
            URL aURL = new URL("http://www.anddev.org/images/tiny_tutheaders/droiddraw.png");
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
            return bm;
	    } catch (IOException e) {
            System.out.println(e.toString());
        }
	    
	    return null;
    }

    */
    
    
    
    public int[] getMaleImages() {
    	int[] images = new int[NUM_MALE_FACES];
    	
    	Resources res = getResources();
    	String packageName = getPackageName();
    	
    	for (int i=0; i<images.length; i++) 
    		images[i] = res.getIdentifier("male"+i, "drawable", packageName);
    	
    	images = Utils.shuffleIntArray(images);
    	
    	return images;
    }
    
    public int[] getFemaleImages() {
    	int[] images = new int[NUM_FEMALE_FACES];
    	
    	Resources res = getResources();
    	String packageName = getPackageName();
    	
    	for (int i=0; i<images.length; i++) 
    		images[i] = res.getIdentifier("female"+i, "drawable", packageName);
    	
    	images = Utils.shuffleIntArray(images);
    	
    	return images;
    }
    
    
    public void loadData_FACES() {
    	images = new int[numItems];    	
    	strings = new String[numItems];
    	    	
    	int[] maleImages   = getMaleImages();
    	int[] femaleImages = getFemaleImages();
    	
    	String[] malenames   = getResources().getStringArray(R.array.malenames);
    	String[] femalenames = getResources().getStringArray(R.array.femalenames);
    	String[] lastnames   = getResources().getStringArray(R.array.lastnames);
    	
    	Collections.shuffle(Arrays.asList(malenames));
    	Collections.shuffle(Arrays.asList(femalenames));
    	Collections.shuffle(Arrays.asList(lastnames));
    	
    	int maleIndex   = 0;
    	int femaleIndex = 0;
    	MyHolder[] holders = new MyHolder[numItems];
    	Random rand = new Random();
    	
    	for (int i=0; i<numItems; i++) {
    		
    		if (rand.nextInt(2) == MALE) { // flip a coin
    			String firstname = malenames[maleIndex];
    			String lastname = lastnames[i];
    			strings[i] = firstname + " " + lastname;    			
    			images[i] = maleImages[maleIndex];
    			maleIndex++;
    		}
    		else {
    			String firstname = femalenames[femaleIndex];
    			String lastname = lastnames[i];
    			strings[i] = firstname + " " + lastname;    			
    			images[i] = femaleImages[femaleIndex];
    			femaleIndex++;
    		}
    		MyHolder holder = new MyHolder();
    		holder.id = images[i];
    		holder.name = strings[i];
    		holders[i] = holder;
    	}
    	Collections.shuffle(Arrays.asList(holders));
    	for (int i=0; i<numItems; i++) {
    		images[i] = holders[i].id;
    		strings[i] = holders[i].name;
    	}
    }
    
   public static class MyHolder {
	   int id;
	   String name;
   }
    
    public void loadData_IMAGES() {
        	
    	int[] imagenums = new int[NUM_ABSTRACT_IMAGES];
    	for (int i=0; i<imagenums.length; i++)
    		imagenums[i] = i;
    	
    	imagenums = Utils.shuffleIntArray(imagenums);
    	
    	images = new int[numItems];
    	strings = new String[numItems];
    	    	
    	Resources res = getResources();
    	String packageName = getPackageName();
    	for (int i=0; i<numItems; i++) {    		  
    		images[i]  = res.getIdentifier("image"+imagenums[i], "drawable", packageName);
    		strings[i] = Integer.toString(((i % numCols) + 1)); // which column am i in
    	}
    	images = Utils.shuffleIntArray(images);    	
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
    
    private class ShapeAdapter extends BaseAdapter {
                
        public ShapeAdapter(Context context) {  	   }
        public int getCount() {           			 return numItems;        }
        public Object getItem(int position) {        return null;        }
        public long getItemId(int position) {        return 0;        }
        public View getView(final int position, View convertView, ViewGroup parent) {
        	
        	View MyView = convertView;
            if (convertView == null) {
            	
            	LayoutInflater li = getLayoutInflater();
                MyView = li.inflate(R.layout.shape, null);               
                                
                EditText textbox = (EditText) MyView.findViewById(R.id.shape_edittext);
            	textbox.setVisibility(View.GONE);
            	
            }
            ImageView image = (ImageView) MyView.findViewById(R.id.shape_image);
            image.setImageResource(images[position]);
                 
            image.setOnClickListener(new OnClickListener() {
            	@Override
            	public void onClick(View v) {
            	    loadImageDialog(images, strings, position);
            	}
            });
            
            TextView text = (TextView) MyView.findViewById(R.id.shape_text);
            text.setText(strings[position]);
            
            return MyView;        
        }
    }
    
    public void loadImageDialog(int[] images, String[] strings, int position) {
    	ImageDialog dialog = new ImageDialog(this, images, strings, position);
    	dialog.show();
    }
    
    public void launchRecallActivity() {
    	timer.cancel();
    	Intent i = getIntent();
		i.putExtra("images", images);
		i.putExtra("strings", strings);
		i.putExtra("memTimeUsed", timer.getSecondsElapsed());
		i.setClass(this, Shapes_Recall.class);
		this.startActivity(i);
		finish();
    }

    public void getExtras() {
    	Intent i = getIntent();
        gameType        = i.getIntExtra("gameType",  -1);
        numRows         = i.getIntExtra("numRows",     -1);
        numCols         = i.getIntExtra("numCols",     -1);
        memTime         = i.getIntExtra("memTime",     -1);
        numItems = numRows * numCols;
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
	}
	
	
	
	public void launchExitDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
    	builder.setMessage("Stop current game?")
    	       .setCancelable(false)
    	       .setPositiveButton("Stop Game", (dialog, id) -> {
                   userQuit = true;
                   timer.cancel();
                   finish();
               })
    	       .setNegativeButton("Continue Game", (dialog, id) -> dialog.cancel());
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