package com.MemoryLadder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.mastersofmemory.memoryladder.R;
//import com.MemoryLadderFull.R;

public class Cards_Review extends Activity implements OnClickListener,OnItemSelectedListener  {
	private String[][] guess;
	private String[][] answer;
	private CardAdapter adapter;
	private CustomGallery gallery;
	private Button button1;
	private Button button2;
	private Button backButton;
	private Button forwardButton;
	private TextView CardText;
	private TextView deckNumText;
	private int numDecks;
	private int deckSize;
	private int gameType;
	private int mode;
	private int score;
	private int curDeckNum = 0;
//	private final int CARDS_SPEED  = Constants.CARDS_SPEED;
//	private final int CARDS_LONG   = Constants.CARDS_LONG;
    
	private int cardHeight = 0; 
	private int cardWidth = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card);
        
        getExtras();
        initText(); 
        initButtons();
        hideCardSelectors();
       
        
        gallery = (CustomGallery) findViewById(R.id.gallery);
        adapter = new CardAdapter(this);
        gallery.setAdapter(adapter); 
        gallery.setOnItemSelectedListener(this);
    }
    
    @Override
    public void onResume() {
    	super.onResume();    	        
        adapter.notifyDataSetChanged();
    }
    
    public void initButtons() {
    	button1 = (Button) findViewById(R.id.button1);
    	button1.setOnClickListener(this); button1.setText("Back");  
        
    	button2 = (Button) findViewById(R.id.button2);
    	button2.setOnClickListener(this); button2.setText("Play Again");  
    	
    	backButton = (Button) findViewById(R.id.backButton);
    	backButton.setOnClickListener(this);
        
    	forwardButton = (Button) findViewById(R.id.forwardButton);
    	forwardButton.setOnClickListener(this);
    }
    
    public void hideCardSelectors() {
    	LinearLayout ValueLayout = (LinearLayout) findViewById(R.id.ValueLayout);
        LinearLayout SuitLayout = (LinearLayout) findViewById(R.id.SuitLayout);
        ImageView bottomBarImage = (ImageView) findViewById(R.id.bottomBarImage);
        ValueLayout.setVisibility(View.GONE);
        SuitLayout.setVisibility(View.GONE);
        bottomBarImage.setVisibility(View.GONE);
        
        if (numDecks == 1) {
        	LinearLayout DeckSelector = (LinearLayout) findViewById(R.id.DeckSelector);
        	DeckSelector.setVisibility(View.GONE);
        }
    }
    public void initText() {
    
    	TextView TitleText = (TextView) findViewById(R.id.TitleText);
    	TitleText.setText("Review");
    	TextView timerText = (TextView) findViewById(R.id.timerText);
    	timerText.setVisibility(View.INVISIBLE);
        
    	CardText = (TextView) findViewById(R.id.CardText);
    	deckNumText = (TextView) findViewById(R.id.deckNumText);    	
    	updateText();
    	
    	TextView ScoreText = (TextView) findViewById(R.id.finalScore);
    	ScoreText.setVisibility(View.VISIBLE);
    	ScoreText.setText("Score: " + Integer.toString(score));
    }
    public void updateText() {
    	deckNumText.setText("Deck " + (curDeckNum+1) + " of " + numDecks);
    }
        
    public class CardAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;
        
        public CardAdapter(Context c) {
            mContext = c;
        }
        public int getCount()               {     return answer[curDeckNum].length;      }
        public Object getItem(int position) {     return position;         }
        public long getItemId(int position) {     return position;         }
        
        public View getView(int position, View convertView, ViewGroup parent) {   
        	if (cardHeight == 0) {
        		cardHeight = gallery.getHeight();
            	cardWidth = (int) ( (double) cardHeight / 1.3 );
        	}
        	
        	ImageView image = new ImageView(mContext);
            image.setImageResource(getResources().getIdentifier(answer[curDeckNum][position], "drawable", getPackageName()));
            image.setLayoutParams(new Gallery.LayoutParams(cardWidth, cardHeight));
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setAdjustViewBounds(true);
            image.setBackgroundColor(Color.TRANSPARENT);
            return image;     
        }
    }

	@Override
	public void onClick(View v) {
		if (v == button1)
			launchScoreActivity();
		else if (v == button2)
			launchTest();
		else if (v == backButton) {
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
	
	public void getGuessArray(Intent i) {
		guess = new String[numDecks][deckSize];
		for (int deck=0; deck<numDecks; deck++) {
			guess[deck] = i.getStringArrayExtra("guess" + deck);
		}
	}
	public void getAnswerArray(Intent i) {
		answer = new String[numDecks][deckSize];
		for (int deck=0; deck<numDecks; deck++) {
			answer[deck] = i.getStringArrayExtra("answer" + deck);
		}
	}
	
	public void getExtras() {
    	Intent i = getIntent();
        deckSize        = i.getIntExtra("deckSize",  -1);
        numDecks        = i.getIntExtra("numDecks",  -1);
        gameType        = i.getIntExtra("gameType", -1);
        mode            = i.getIntExtra("mode", -1);
        score           = i.getIntExtra("score", -1);
        
        getGuessArray(i);
        getAnswerArray(i);
    }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int position,long id) {	updateCardText(position);	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	public void updateCardText(int position) {
		String answer_str = answer[curDeckNum][position];
		String guess_str = guess[curDeckNum][position];
		CardText.setText(Card.getCardText(answer_str) + "\n" + Card.getCardText(guess_str));
		if (answer_str.equals(guess_str)) {
			CardText.setBackgroundColor(Color.GREEN);
			CardText.setTextColor(Color.BLACK);
		}
		else if (!guess_str.equals("card_xx")) {
			CardText.setBackgroundColor(Color.RED);
			CardText.setTextColor(Color.BLACK);
		}
		else {
			CardText.setBackgroundColor(Color.BLACK);
			CardText.setTextColor(Color.WHITE);
		}
	}

    
    
}