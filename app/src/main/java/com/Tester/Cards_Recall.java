package com.Tester;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
//import com.MemoryLadderFull.R;

public class Cards_Recall extends Activity implements OnClickListener,OnItemSelectedListener  {
	private String[][] guess;
	private String[][] answer;
	private CardAdapter adapter;
	private Button button1;
	private Button button2;
	private Button backButton;
	private Button forwardButton;
	private TextView deckNumText;
	private Timer timer;
	private TextView timerText;
//	private final int CARDS_SPEED  = Constants.CARDS_SPEED;
//	private final int CARDS_LONG   = Constants.CARDS_LONG;
	
	private int curSelectedVal  = 0;
	private int curSelectedSuit = 0;
    
    /* Card Value Selection Text */
	private TextView value1;
	private TextView value2;
	private TextView value3;
	private TextView value4;
	private TextView value5;
	private TextView value6;
	private TextView value7;
	private TextView value8;
	private TextView value9;
	private TextView value10;
	private TextView value11;
	private TextView value12;
	private TextView value13;
    
    /* Suit Selection Images */
	private ImageView spadeIcon;
	private ImageView heartIcon;
	private ImageView clubIcon;
	private ImageView diamondIcon;
    
	private int numDecks;
	private int deckSize;
    private int gameType;
    private int recallTime;
    private int curDeckNum = 0;
    private Boolean timeExpired = false;
    private Boolean userQuit = false;
    private CustomGallery gallery;
    private TextView CardText;
    
    private int cardHeight = 0; 
	private int cardWidth = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        getExtras();
        initButtons();
        initCardSelectors();
        initText();
        
        gallery = (CustomGallery) findViewById(R.id.gallery);
        adapter = new CardAdapter(this);
        gallery.setAdapter(adapter);
        gallery.setOnItemSelectedListener(this);        
        
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	initTimer();
        timer.start();    	

        adapter.notifyDataSetChanged();
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

    public void initButtons() {
    	button1 = (Button) findViewById(R.id.button1);
    	button1.setOnClickListener(this); button1.setText("Stop");    	
    	button2 = (Button) findViewById(R.id.button2);
    	button2.setOnClickListener(this); button2.setText("Done");
    	
    	backButton = (Button) findViewById(R.id.backButton);
    	backButton.setOnClickListener(this);
        
    	forwardButton = (Button) findViewById(R.id.forwardButton);
    	forwardButton.setOnClickListener(this);
    }
    public void initTimer() {
    	timerText = (TextView) findViewById(R.id.timerText);
    	timer = new Timer(recallTime * 1000, 1000, timerText);
    }
    public void initText() {
    	TextView TitleText = (TextView) findViewById(R.id.TitleText);
    	TitleText.setText("Recall");
    	
    	CardText = (TextView) findViewById(R.id.CardText);   
    	deckNumText = (TextView) findViewById(R.id.deckNumText);    	
    	updateText();
    	
        if (numDecks == 1) {
        	LinearLayout DeckSelector = (LinearLayout) findViewById(R.id.DeckSelector);
        	DeckSelector.setVisibility(View.GONE);
        }
    }
    public void updateText() {
    	deckNumText.setText("Deck " + (curDeckNum+1) + " of " + numDecks);
    }
    
    public void initCardSelectors() {
    	value1  = (TextView) findViewById(R.id.value1);  value1.setOnClickListener(this);
    	value2  = (TextView) findViewById(R.id.value2);  value2.setOnClickListener(this);
    	value3  = (TextView) findViewById(R.id.value3);  value3.setOnClickListener(this);
    	value4  = (TextView) findViewById(R.id.value4);  value4.setOnClickListener(this);
    	value5  = (TextView) findViewById(R.id.value5);  value5.setOnClickListener(this);
    	value6  = (TextView) findViewById(R.id.value6);  value6.setOnClickListener(this);
    	value7  = (TextView) findViewById(R.id.value7);  value7.setOnClickListener(this);
    	value8  = (TextView) findViewById(R.id.value8);  value8.setOnClickListener(this);
    	value9  = (TextView) findViewById(R.id.value9);  value9.setOnClickListener(this);
    	value10 = (TextView) findViewById(R.id.value10); value10.setOnClickListener(this);
    	value11 = (TextView) findViewById(R.id.value11); value11.setOnClickListener(this);
    	value12 = (TextView) findViewById(R.id.value12); value12.setOnClickListener(this);
    	value13 = (TextView) findViewById(R.id.value13); value13.setOnClickListener(this);
    	
    	spadeIcon   = (ImageView) findViewById(R.id.spadeIcon);   spadeIcon.setOnClickListener(this);
    	heartIcon   = (ImageView) findViewById(R.id.heartIcon);   heartIcon.setOnClickListener(this);
    	clubIcon    = (ImageView) findViewById(R.id.clubIcon);    clubIcon.setOnClickListener(this);
    	diamondIcon = (ImageView) findViewById(R.id.diamondIcon); diamondIcon.setOnClickListener(this);
    }
    
    
    public class CardAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;
        
        public CardAdapter(Context c) {
            mContext = c;
        }
        public int getCount()               {     return deckSize;      }
        public Object getItem(int position) {     return position;         }
        public long getItemId(int position) {     return position;         }
        
        public View getView(int position, View convertView, ViewGroup parent) { 
        	String cardstring = guess[curDeckNum][position];
        	ImageView image = new ImageView(mContext);
        	
        	if (cardstring.equals("card_xx"))
        	  cardstring = "card_xx";
        	else if (cardstring.contains("x"))
        	  cardstring = "card_xx";
        	
        	if (cardHeight == 0) {
        		cardHeight = gallery.getHeight();
            	cardWidth = (int) ( (double) cardHeight / 1.3 );
        	}
        	
            image.setImageResource(getResources().getIdentifier(cardstring, "drawable", getPackageName()));
            image.setLayoutParams(new Gallery.LayoutParams(cardWidth, cardHeight));
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setAdjustViewBounds(true);
            image.setBackgroundColor(Color.TRANSPARENT);
            image.setId(position);

            return image;     
        }
    }
    
    
    
	@Override
	public void onClick(View view) {
		if (view == button1)
			launchExitDialog();
		else if (view == button2 && !timeExpired && !userQuit)
			launchScoreActivity();
		
		else if (view == backButton) {
			if (curDeckNum > 0) {
				curDeckNum--;
				adapter.notifyDataSetChanged();
				gallery.setSelection(0);
				updateText();
			}
		}
		else if (view == forwardButton) {
			if (curDeckNum < (numDecks - 1)) {
				curDeckNum++;
				adapter.notifyDataSetChanged();
				gallery.setSelection(0);
				updateText();
			}
		}		
		else if (view == value1)   updateGuess(1, "1");       
		else if (view == value2)   updateGuess(1, "2");       
		else if (view == value3)   updateGuess(1, "3");       
		else if (view == value4)   updateGuess(1, "4");       
		else if (view == value5)   updateGuess(1, "5");       
		else if (view == value6)   updateGuess(1, "6");       
		else if (view == value7)   updateGuess(1, "7");       
		else if (view == value8)   updateGuess(1, "8");       
		else if (view == value9)   updateGuess(1, "9");       
		else if (view == value10)  updateGuess(1, "t");       
		else if (view == value11)  updateGuess(1, "j");       
		else if (view == value12)  updateGuess(1, "q");       
		else if (view == value13)  updateGuess(1, "k");       
		  
		else if (view == spadeIcon)   updateGuess(0, "s");   
		else if (view == heartIcon)   updateGuess(0, "h");   
		else if (view == diamondIcon) updateGuess(0, "d");   
		else if (view == clubIcon)    updateGuess(0, "c");   
		
		updateCardText(gallery.getSelectedItemPosition());
	}	
	
	public Boolean isValidCard(String card) {
		return !(card.charAt(5) == 'x' || card.charAt(6) == 'x');
	}
	
	public void updateGuess(int position, String newchar) {
		int curCardNum = gallery.getSelectedItemPosition();
		String curGuess = guess[curDeckNum][curCardNum];
		
		if (position == 0) // update suit
		  curGuess = newchar + curGuess.charAt(6);
		else               // update value
		  curGuess = curGuess.charAt(5) + newchar;
		 
		Boolean advanceToNextCard = false;
		if (!isValidCard(guess[curDeckNum][curCardNum]) && isValidCard("card_" + curGuess)) {
			advanceToNextCard = true;
		}
		
		guess[curDeckNum][curCardNum] = "card_" + curGuess;
			
		if (advanceToNextCard && curCardNum < deckSize - 1) // only advance if card was completed, and not on the final card in the deck
			gallery.setSelection(curCardNum + 1);
		else if (!isValidCard(guess[curDeckNum][curCardNum])) {
			setSelectorBackground(newchar, true);
		}
		
		adapter.notifyDataSetChanged();	
	}
	
	
	public void clearSelectorBackgrounds() {
		Drawable back = getResources().getDrawable(R.drawable.selector_orange);
		
		switch (curSelectedVal) {
		
			case 0: break;
			case 1: value1.setBackgroundDrawable(back); break;
			case 2: value2.setBackgroundDrawable(back); break;
			case 3: value3.setBackgroundDrawable(back); break;
			case 4: value4.setBackgroundDrawable(back); break;
			case 5: value5.setBackgroundDrawable(back); break;
			case 6: value6.setBackgroundDrawable(back); break;
			case 7: value7.setBackgroundDrawable(back); break;
			case 8: value8.setBackgroundDrawable(back); break;
			case 9: value9.setBackgroundDrawable(back); break;
			case 10: value10.setBackgroundDrawable(back); break;
			case 11: value11.setBackgroundDrawable(back); break;
			case 12: value12.setBackgroundDrawable(back); break;
			case 13: value13.setBackgroundDrawable(back); break;
		
		}
		
		switch (curSelectedSuit) {
		
			case 0: break;
			case 1: spadeIcon.setBackgroundDrawable(back); break;
			case 2: heartIcon.setBackgroundDrawable(back); break;
			case 3: diamondIcon.setBackgroundDrawable(back); break;
			case 4: clubIcon.setBackgroundDrawable(back); break;

		}
		
		curSelectedVal = 0;
		curSelectedSuit = 0;
		
	}
	
	public void setSelectorBackground(String value, Boolean isSelected) {
		
		clearSelectorBackgrounds();
		
		int back = getResources().getColor(R.color.orange);
		
		if      (value.equals("1")) { value1.setBackgroundColor(back); curSelectedVal = 1; }
		else if (value.equals("2")) { value2.setBackgroundColor(back); curSelectedVal = 2; }
		else if (value.equals("3")) { value3.setBackgroundColor(back); curSelectedVal = 3; }
		else if (value.equals("4")) { value4.setBackgroundColor(back); curSelectedVal = 4; }
		else if (value.equals("5")) { value5.setBackgroundColor(back); curSelectedVal = 5; }
		else if (value.equals("6")) { value6.setBackgroundColor(back); curSelectedVal = 6; }
		else if (value.equals("7")) { value7.setBackgroundColor(back); curSelectedVal = 7; }
		else if (value.equals("8")) { value8.setBackgroundColor(back); curSelectedVal = 8; }
		else if (value.equals("9")) { value9.setBackgroundColor(back); curSelectedVal = 9; }
		else if (value.equals("t")) { value10.setBackgroundColor(back); curSelectedVal = 10; }
		else if (value.equals("j")) { value11.setBackgroundColor(back); curSelectedVal = 11; }
		else if (value.equals("q")) { value12.setBackgroundColor(back); curSelectedVal = 12; }
		else if (value.equals("k")) { value13.setBackgroundColor(back); curSelectedVal = 13; }
		
		else if (value.equals("s")) { spadeIcon  .setBackgroundColor(back); curSelectedSuit = 1; }
		else if (value.equals("h")) { heartIcon  .setBackgroundColor(back); curSelectedSuit = 2; }
		else if (value.equals("d")) { diamondIcon.setBackgroundColor(back); curSelectedSuit = 3; }
		else if (value.equals("c")) { clubIcon   .setBackgroundColor(back); curSelectedSuit = 4; }
	}
	
	
	public void updateCardText(int position) {
		CardText.setText(Card.getCardText(guess[curDeckNum][position]));
	}
	
	public void launchScoreActivity() {
		timer.cancel();
		Intent i = getIntent();
		
		getAnswerArray(i);
		i = putGuessArray(i);
				
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
		
		i.setClass(this, ScoreActivity.class);
		this.startActivity(i);
	}
	
	/*************************Begin EXTRAS********************************************/
	
	public void getGuessArray() {
		guess = new String[numDecks][deckSize];
		for (int deck=0; deck<numDecks; deck++) {
			for (int card=0; card<deckSize; card++)
				guess[deck][card] = "card_xx";
		}
	}
	public void getAnswerArray(Intent i) {
		answer = new String[numDecks][deckSize];
		for (int deck=0; deck<numDecks; deck++) {
			answer[deck] = i.getStringArrayExtra("answer" + deck);
		}
	}
	public Intent putGuessArray(Intent i) {
		for (int deck=0; deck<numDecks; deck++) {
			i.putExtra("guess" + deck, guess[deck]);
		}
		return i;
	}
	public Intent putAnswerArray(Intent i) {
		for (int deck=0; deck<numDecks; deck++) {
			i.putExtra("answer" + deck, answer[deck]);
		}
		return i;
	}
	
	public void getExtras() {
    	Intent i = getIntent();
        gameType        = i.getIntExtra("gameType",   -1);
        recallTime      = i.getIntExtra("recallTime", -1);
        deckSize        = i.getIntExtra("deckSize",   -1);
        numDecks        = i.getIntExtra("numDecks",   -1);
        getGuessArray();
	}
	/*************************End EXTRAS********************************************/

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position,long id) {	
		updateCardText(position);
		clearSelectorBackgrounds();
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	
	
	
	public void launchExitDialog() {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setMessage("Stop current game?")
    	       .setCancelable(false)
    	       .setPositiveButton("Stop Game", new DialogInterface.OnClickListener() {
    	           public void onClick(DialogInterface dialog, int id) {
    	        	   Cards_Recall.this.startActivity(new Intent(Cards_Recall.this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

	public void hideBottomBar() {
		ImageView bottomBarImage = (ImageView) findViewById(R.id.bottomBarImage);
		HorizontalScrollView ValuePicker = (HorizontalScrollView) findViewById(R.id.ValuePicker);
		LinearLayout SuitPicker = (LinearLayout) findViewById(R.id.SuitLayout);
		
		bottomBarImage.setVisibility(View.GONE);
		ValuePicker.setVisibility(View.GONE);
		SuitPicker.setVisibility(View.GONE);
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
			gallery.setVisibility(View.INVISIBLE);
			CardText.setVisibility(View.INVISIBLE);
			
			LinearLayout DeckSelector = (LinearLayout) findViewById(R.id.DeckSelector);
        	DeckSelector.setVisibility(View.GONE);
        	
        	hideBottomBar();
			
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