package com.Tester;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
//import com.MemoryLadderFull.R;

public class Cards_Mem extends Activity implements OnClickListener,OnItemSelectedListener, android.content.DialogInterface.OnDismissListener  {
    private int[][] imageIds;
    private Deck[] decks;
    private CardAdapter adapter;
    private CustomGallery gallery;
    private Button backButton;
    private Button forwardButton;
    private Button button1;
    private Button button2;
    private Button StartButton;
    private Button InstructionsButton;
    private TextView CardText;
    private TextView deckNumText;
    private Timer timer;
    private TextView timerText;
    private int numDecks;
    private int deckSize;
    private int gameType;
    private int memTime;
    private int curDeckNum = 0;
    private Boolean timeExpired = false;
	private Boolean userQuit = false;
	private Boolean mnemo_enabled = false;
	private SharedPreferences prefs;
//    private final int CARDS_SPEED  = Constants.CARDS_SPEED;
//    private final int CARDS_LONG   = Constants.CARDS_LONG;
	
	private int cardHeight; 
	private int cardWidth;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        getExtras();
        initButtons();
        hideCardSelectors();
        initText();   
        initTimer();
        showCardWidgits(View.INVISIBLE);
        loadCards();
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
    	
    	CountDownDialog count = new CountDownDialog(this);
    	count.setOnDismissListener(this);
    	count.show();
    }
    
    public void initGallery() {
    	gallery = (CustomGallery) findViewById(R.id.gallery);
        adapter = new CardAdapter(this);
        gallery.setAdapter(adapter);     
        gallery.setOnItemSelectedListener(this);
        gallery.setSelection(0);
        
        cardHeight = gallery.getHeight();
        cardWidth = (int) ( (double) cardHeight / 1.3 );
    }
    
    public void setListener() {
        button2.setOnClickListener(this);
    }
    
    public void initButtons() {
    	backButton = (Button) findViewById(R.id.backButton);
    	backButton.setOnClickListener(this);
        
    	forwardButton = (Button) findViewById(R.id.forwardButton);
    	forwardButton.setOnClickListener(this);
    	
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
    
    public void initTimer() {
    	timerText = (TextView) findViewById(R.id.timerText);
    	timer = new Timer(memTime * 1000, 1000, timerText);
    }
    public void startTimer() {
    	timer.start();
    }
    
    public void hideCardSelectors() {
    	LinearLayout ValueLayout = (LinearLayout) findViewById(R.id.ValueLayout);
        LinearLayout SuitLayout  = (LinearLayout) findViewById(R.id.SuitLayout);
        ImageView bottomBarImage = (ImageView) findViewById(R.id.bottomBarImage);
        ValueLayout.setVisibility(View.INVISIBLE);
        SuitLayout.setVisibility(View.INVISIBLE);
        bottomBarImage.setVisibility(View.INVISIBLE);
        
        if (numDecks == 1) {
        	LinearLayout DeckSelector = (LinearLayout) findViewById(R.id.DeckSelector);
        	DeckSelector.setVisibility(View.GONE);
        }
    }
    
    public void showCardWidgits(int vis) {
    	CardText.setVisibility(vis);
    	deckNumText.setVisibility(vis);
    	backButton.setVisibility(vis);
    	forwardButton.setVisibility(vis);
    }
    
    public void initText() {
    	CardText = (TextView) findViewById(R.id.CardText); 
    	deckNumText = (TextView) findViewById(R.id.deckNumText);    	
    	
    	
    	
    	updateText();
    }
    public void updateText() {
    	deckNumText.setText("Deck " + (curDeckNum+1) + " of " + numDecks);
    }
    
    public void loadCards() {
    	decks = new Deck[numDecks];
    	imageIds = new int[numDecks][deckSize];
    	for (int i=0; i<numDecks; i++) {
    		decks[i] = new Deck(deckSize);
    		imageIds[i] = decks[i].getImageIds();
    	}
    }
    
    public class CardAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;
        
        public CardAdapter(Context c) {
            mContext = c;
        }
        public int getCount()               {     return imageIds[curDeckNum].length;      }
        public Object getItem(int position) {     return position;         }
        public long getItemId(int position) {     return position;         }
        
        public View getView(int position, View convertView, ViewGroup parent) {   
        	ImageView image = new ImageView(mContext);
            image.setImageResource(imageIds[curDeckNum][position]); 
            image.setLayoutParams(new Gallery.LayoutParams(cardWidth, cardHeight));
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setAdjustViewBounds(true);
            image.setBackgroundColor(Color.TRANSPARENT);
            return image;     
        }
    }
    
    

	@Override
	public void onClick(View v) {
		if (v == backButton) {
			if (curDeckNum > 0) {
				curDeckNum--;
				adapter.notifyDataSetChanged();
				gallery.setSelection(0);
				updateText();
			}
		}
		else if (v == forwardButton) {
			if (v == forwardButton) {
				if (curDeckNum < (numDecks - 1)) {
					curDeckNum++;
					adapter.notifyDataSetChanged();
					gallery.setSelection(0);
					updateText();
				}
			}
		}
		else if (v == button1)
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
	
	public void launchRecallActivity() {
		timer.cancel();
		Intent i = getIntent();
		for (int deck=0; deck<numDecks; deck++) 
			i.putExtra("answer" + deck, decks[deck].getDeckStringArray());
		i.putExtra("memTimeUsed", timer.getSecondsElapsed());
		i.setClass(this, Cards_Recall.class);
		startActivity(i);
	}

	
	public void getExtras() {
    	Intent i = getIntent();
        gameType        = i.getIntExtra("gameType",  -1);
        memTime         = i.getIntExtra("memTime",     -1);
        deckSize        = i.getIntExtra("deckSize",  -1);
        numDecks        = i.getIntExtra("numDecks",  -1);
        mnemo_enabled   = i.getBooleanExtra("mnemo_enabled", false);
        if (mnemo_enabled) {
        	prefs = getSharedPreferences("Peg_Cards", 0);
        }
    }

	
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position,long id) {	updateCardText(position);	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	public void updateCardText(int position) {
		if (mnemo_enabled)
			CardText.setText(decks[curDeckNum].getCard(position).toMnemonic(prefs));
		else
			CardText.setText(decks[curDeckNum].getCard(position).toString());
	}
 
	
	
	
	
	public void launchExitDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Stop current game?")
    	       .setCancelable(false)
    	       .setPositiveButton("Stop Game", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   Cards_Mem.this.startActivity(new Intent(Cards_Mem.this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
			gallery.setVisibility(View.INVISIBLE);
			CardText.setVisibility(View.INVISIBLE);
			
			LinearLayout DeckSelector = (LinearLayout) findViewById(R.id.DeckSelector);
        	DeckSelector.setVisibility(View.GONE);
			
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
		initGallery();   	   
     	showCardWidgits(View.VISIBLE);
        startTimer();  
        setListener();
	}

}