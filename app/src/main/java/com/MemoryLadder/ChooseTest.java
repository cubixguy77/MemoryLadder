package com.MemoryLadder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

import com.google.firebase.analytics.FirebaseAnalytics;

import static com.MemoryLadder.Constants.getGameName;

public class ChooseTest extends Activity implements OnClickListener {
	
	private Boolean[] permits;

	private ImageView SpeedNumbersImage;
	private ImageView LongNumbersImage;
	private ImageView BinaryNumbersImage;
	private ImageView SpokenNumbersImage;
	private ImageView RandomWordsImage;
	private ImageView HistoricDatesImage;
	private ImageView NamesFacesImage;
	private ImageView AbstractImagesImage;
	private ImageView SpeedCardsImage;
	private ImageView SpeedCardsImageAlternate;
	private ImageView LongCardsImage;
	
	private Button UnlockButton;
	
	final private static int NUMBERS_SPEED   = Constants.NUMBERS_SPEED;
	final private static int NUMBERS_LONG    = Constants.NUMBERS_LONG;
	final private static int NUMBERS_BINARY  = Constants.NUMBERS_BINARY;
	final private static int NUMBERS_SPOKEN  = Constants.NUMBERS_SPOKEN;
	private static final int LISTS_WORDS     = Constants.LISTS_WORDS;
	private static final int LISTS_EVENTS    = Constants.LISTS_EVENTS;	
	final private static int SHAPES_FACES    = Constants.SHAPES_FACES;
	final private static int SHAPES_ABSTRACT = Constants.SHAPES_ABSTRACT;
	final private static int CARDS_SPEED     = Constants.CARDS_SPEED;
    final private static int CARDS_LONG      = Constants.CARDS_LONG;
	
	private int mode;
	private static final int STEPS  = Constants.STEPS;
	private static final int WMC    = Constants.WMC;
	private static final int CUSTOM = Constants.CUSTOM;

	private FirebaseAnalytics mFirebaseAnalytics;
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.choosetest);

		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        getExtras();            
        initGraphics();
        initButtons();
        initAnimation();
    }
    
    public void initAnimation() {
    	LinearLayout Numbers = (LinearLayout) findViewById(R.id.Numbers);
		LinearLayout Lists = (LinearLayout) findViewById(R.id.Lists);
		LinearLayout Cards = (LinearLayout) findViewById(R.id.Cards);
		
		LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.fly_in_layout);
				
		Numbers.setLayoutAnimation(controller);
		Lists.setLayoutAnimation(controller);
		Cards.setLayoutAnimation(controller);
    }
              
    public void initButtons() {
    	//UnlockButton = (Button) findViewById(R.id.UnlockButton);
    	//UnlockButton.setOnClickListener(this);
    }
    
    public void setImageLocks() {
    	Permissions p = new Permissions(this);
    	permits = p.getPermissions(mode);
    	
    	if (!permits[NUMBERS_SPEED])
    		;//SpeedNumbersImage.setImageResource(R.drawable.icon_numbers_locked);
    	if (!permits[NUMBERS_LONG])
    		;//SpeedNumbersImage.setImageResource(R.drawable.icon_numbers_locked);
    	if (!permits[NUMBERS_BINARY])
    		;//SpeedNumbersImage.setImageResource(R.drawable.icon_numbers_locked);
    	if (!permits[NUMBERS_SPOKEN])
    		;//SpeedNumbersImage.setImageResource(R.drawable.icon_numbers_spoken_locked);
    	if (!permits[LISTS_WORDS])
    		//RandomWordsImage.setImageResource(R.drawable.icon_randomwords_locked);
    	if (!permits[LISTS_EVENTS])
    		//HistoricDatesImage.setImageResource(R.drawable.icon_historicdates_locked);
    	if (!permits[SHAPES_FACES])
    		//NamesFacesImage.setImageResource(R.drawable.icon_namesandfaces_locked);
    	if (!permits[SHAPES_ABSTRACT])
    		//AbstractImagesImage.setImageResource(R.drawable.icon_abstract_images_locked);
    	if (!permits[CARDS_SPEED])
    		//SpeedCardsImage.setImageResource(R.drawable.icon_cards_locked);
    	if (!permits[CARDS_LONG]) {
    		//SpeedCardsImageAlternate.setImageResource(R.drawable.icon_cards_locked);
    		//LongCardsImage.setImageResource(R.drawable.icon_cards_locked);
    	}    		
    }
    
    public void initGraphics() {
    	
    	SpeedNumbersImage   = (ImageView) findViewById(R.id.SpeedNumbersImage);
    	LongNumbersImage    = (ImageView) findViewById(R.id.LongNumbersImage);
    	BinaryNumbersImage  = (ImageView) findViewById(R.id.BinaryNumbersImage);
    	SpokenNumbersImage  = (ImageView) findViewById(R.id.SpokenNumbersImage);
    	RandomWordsImage    = (ImageView) findViewById(R.id.RandomWordsImage);
    	HistoricDatesImage  = (ImageView) findViewById(R.id.HistoricDatesImage);
    	NamesFacesImage     = (ImageView) findViewById(R.id.NamesFacesImage);
    	AbstractImagesImage = (ImageView) findViewById(R.id.AbstractImagesImage);
    	SpeedCardsImage     = (ImageView) findViewById(R.id.SpeedCardsImage);
    	SpeedCardsImageAlternate     = (ImageView) findViewById(R.id.SpeedCardsImageAlternate);
    	LongCardsImage      = (ImageView) findViewById(R.id.LongCardsImage);
    	
    	setImageLocks();
    	
    	SpeedNumbersImage.setOnClickListener(this);
    	LongNumbersImage.setOnClickListener(this);
    	BinaryNumbersImage.setOnClickListener(this);
    	SpokenNumbersImage.setOnClickListener(this);
    	RandomWordsImage.setOnClickListener(this);
    	HistoricDatesImage.setOnClickListener(this);
    	NamesFacesImage.setOnClickListener(this);
    	AbstractImagesImage.setOnClickListener(this);
    	SpeedCardsImage.setOnClickListener(this);
    	SpeedCardsImageAlternate.setOnClickListener(this);
    	LongCardsImage.setOnClickListener(this);
    	
    	if (mode == STEPS || mode == CUSTOM) {
    		LongNumbersImage.setVisibility(View.GONE);
    		TextView LongNumbers = (TextView) findViewById(R.id.LongNumbers);
    		LongNumbers.setVisibility(View.GONE);
    		
    		LongCardsImage.setVisibility(View.GONE);
    		TextView LongCards = (TextView) findViewById(R.id.LongCards);
    		LongCards.setVisibility(View.GONE);    	
    		
    		SpeedCardsImage.setVisibility(View.GONE);
    		TextView SpeedCards = (TextView) findViewById(R.id.SpeedCards);
    		SpeedCards.setVisibility(View.GONE);
    	}    	
    	else {
    		SpeedCardsImageAlternate.setVisibility(View.GONE);
    		TextView SpeedCardsAlternate = (TextView) findViewById(R.id.SpeedCardsAlternate);
    		SpeedCardsAlternate.setVisibility(View.GONE);
    	}
    }
    
            
    public void onChooseTestFinished(int gameType) {

		logChooseTestEvent(gameType);

    	Intent i = getIntent();
		i.putExtra("gameType", gameType);		
		
		if (mode == CUSTOM)
			i.setClass(this, Utils.getSettingsClass(gameType));
		else
			i.setClass(this, Utils.getPreClass(gameType));
		
		//i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		this.startActivity(i);
    }

    private void logChooseTestEvent(int gameType) {
		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, getGameName(gameType));
		bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, mode == STEPS ? "Steps" : mode == WMC ? "WMC" : "Custom");
		mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
	}
    
    
    public void getExtras() {
    	Intent i = getIntent();
        mode = i.getIntExtra("mode",    -1);
    }

    
	@Override
	public void onClick(View view) {
		if (view == SpeedNumbersImage) {
			if (permits[NUMBERS_SPEED])
				onChooseTestFinished(Constants.NUMBERS_SPEED);
		}
		else if (view == LongNumbersImage) {
			if (permits[NUMBERS_LONG])
				onChooseTestFinished(Constants.NUMBERS_LONG);
		}
		else if (view == BinaryNumbersImage) {
			if (permits[NUMBERS_BINARY])
				onChooseTestFinished(Constants.NUMBERS_BINARY);
		}
		else if (view == SpokenNumbersImage) {
			if (permits[NUMBERS_SPOKEN])
				onChooseTestFinished(Constants.NUMBERS_SPOKEN);
		}	
		else if (view == RandomWordsImage) {
			if (permits[LISTS_WORDS])
				onChooseTestFinished(Constants.LISTS_WORDS);
		}	
		else if (view == HistoricDatesImage) {
			if (permits[LISTS_EVENTS])
				onChooseTestFinished(Constants.LISTS_EVENTS);
		}	
		else if (view == NamesFacesImage) {
			if (permits[SHAPES_FACES])
				onChooseTestFinished(Constants.SHAPES_FACES);
		}	
		else if (view == AbstractImagesImage) {
			if (permits[SHAPES_ABSTRACT])
				onChooseTestFinished(Constants.SHAPES_ABSTRACT);
		}	
		else if (view == SpeedCardsImage) {
			if (permits[CARDS_SPEED])
				onChooseTestFinished(Constants.CARDS_SPEED);
		}	
		else if (view == LongCardsImage || view == SpeedCardsImageAlternate) {
			if (permits[CARDS_LONG])
				onChooseTestFinished(Constants.CARDS_LONG);
		}
		
		else if (view == UnlockButton) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
        	intent.setData(Uri.parse("market://details?id=com.Tester&feature=search_result"));
        	startActivity(intent);
		}
		
	}
}