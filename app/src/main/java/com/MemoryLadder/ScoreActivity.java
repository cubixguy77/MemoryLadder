package com.MemoryLadder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.mastersofmemory.memoryladder.R;

public class ScoreActivity extends Activity implements OnClickListener {
	
	private Button button1;
	private Button button2;
	private Button TakeTestButton;
	private Button NextStepButton;
	private Button StepsButton;
	private Button ChampButton;
	private Button CustomButton;
	private Button NextGameButton;
	private Button LastGameButton;
	private Button AnimatedArrow;
	private TextView GameTypeText;
	private int gameType;
	private int mode;
	private String[] scoreSummary;
	private String[] pastScores;
	private int step;
	private int source = JUSTPLAYED;
	private Boolean reviewPossible = false;
	private int origGameType;
	private int origMode;
	private static int width;
	private static int height;
	private Animation a;
	
	private Boolean[] permitsWMC;
	private Boolean[] permitsCUSTOM;
	private Boolean[] permitsSTEPS;
	
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
	
	final private static int STEPS  = Constants.STEPS;
	final private static int WMC    = Constants.WMC;
	final private static int CUSTOM = Constants.CUSTOM;
	
	final private static int JUSTPLAYED  = 0;
	final private static int MAIN        = 1;
	final private static int NAVIGATION  = 2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);
        
        getExtras();                        
        initPermissions();
        initButtons();  
        initText();
        if (source == JUSTPLAYED) {
        	updatePastScoreSummary();
        	updatePastScores(getValue(scoreSummary[scoreSummary.length - 1]));
        }
        getPastScores();
        
        a = AnimationUtils.loadAnimation(this, R.anim.fade_in_fade_out);
        refresh();      
        
        
        
    }

/*  @Override
    public void onPause() {
    	super.onPause();
    	finish();
    }
*/
    
    public void initPermissions() {
    	Permissions p = new Permissions(this);
    	permitsWMC = p.getPermissions(WMC);
    	permitsSTEPS = p.getPermissions(STEPS);
    	permitsCUSTOM = p.getPermissions(CUSTOM);
    }
    
    public Boolean[] getPermissions(int mode) {
    	if (mode == WMC)
    		return permitsWMC;
    	else if (mode == STEPS)
    		return permitsSTEPS;
    	else if (mode == CUSTOM)
    		return permitsCUSTOM;
    	return null;    			
    }
    
    public int getNextGameType(int gameType, int mode) {
    	Boolean[] permissions = getPermissions(mode);
    	int result = gameType;
    	while (result == gameType || !permissions[result]) {
    		System.out.println("********Result************: " + result);
    		result++;
    		if (result >= (permissions.length))
    			result = 1;
    	}
    	return result;
    }
    
    public int getPreviousGameType(int gameType, int mode) {
    	Boolean[] permissions = getPermissions(mode);
    	int result = gameType;
    	while (result == gameType || !permissions[result]) {
    		System.out.println("********Result************: " + result);
    		result--;
    		if (result <= 0)
    			result = 10;
    	}
    	return result;
    }
    
    public Boolean legalGameType(int gameType, int mode) {
    	return getPermissions(mode)[gameType];
    }
      
    
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindDrawables(findViewById(R.id.LinearLayout));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
    
    
    public void refresh() {    	
    	refreshButtons();
    	refreshSelectorLayout();
    	refreshGraphLayout();
    	refreshStepsLayout();
    	refreshScoreSummaryLayout();
    	refreshAnimation();
    }
    
    public void refreshExcludeSelector() {    	
    	refreshButtons();
    	refreshGraphLayout();
    	refreshStepsLayout();
    	refreshScoreSummaryLayout();
    	refreshAnimation();
    }
    
    public void refreshAnimation() {
		a.reset();
		AnimatedArrow.startAnimation(a);
    }
    
    public void refreshButtons() {
    	if (reviewPossible && gameType == origGameType && mode == origMode)
        	button1.setVisibility(View.VISIBLE);
    	else
        	button1.setVisibility(View.INVISIBLE);
    }
    
    public void refreshSelectorLayout() {
    	Resources res = getResources();
    	int unselected = res.getIdentifier("score_tab_unselected","drawable", getPackageName());
    	int selected   = res.getIdentifier("score_tab_selected",  "drawable", getPackageName());
    	
    	if (mode == STEPS) {
        	StepsButton.setBackgroundResource(selected);    StepsButton.setTextColor(Color.BLACK);
        	ChampButton.setBackgroundResource(unselected);  ChampButton.setTextColor(Color.WHITE);
        	CustomButton.setBackgroundResource(unselected); CustomButton.setTextColor(Color.WHITE);
    	}
    	else if (mode == WMC) {
        	StepsButton.setBackgroundResource(unselected);  StepsButton.setTextColor(Color.WHITE);
        	ChampButton.setBackgroundResource(selected);    ChampButton.setTextColor(Color.BLACK);
        	CustomButton.setBackgroundResource(unselected); CustomButton.setTextColor(Color.WHITE);
    	}
    	else if (mode == CUSTOM) {
        	StepsButton.setBackgroundResource(unselected); StepsButton.setTextColor(Color.WHITE);
        	ChampButton.setBackgroundResource(unselected); ChampButton.setTextColor(Color.WHITE);
        	CustomButton.setBackgroundResource(selected);  CustomButton.setTextColor(Color.BLACK);
    	}    	
    	
    	GameTypeText.setText(Utils.getTestString(gameType));
    }
    
    public void refreshGraphLayout() {
    	
    	if (pastScores.length == 0) {
    		FrameLayout GraphLayout = (FrameLayout) findViewById(R.id.GraphLayout);
			GraphLayout.setVisibility(View.GONE);    		
    	}
    	else if (mode == WMC || mode == CUSTOM) {
			FrameLayout GraphLayout = (FrameLayout) findViewById(R.id.GraphLayout);
			GraphLayout.setVisibility(View.VISIBLE);
			drawGraph();
		//	graphVisible = true;
    	}
    	else if (mode == STEPS) {
			FrameLayout GraphLayout = (FrameLayout) findViewById(R.id.GraphLayout);
			GraphLayout.setVisibility(View.GONE);
		//	graphVisible = false;
    	}
    }
    
    public void refreshStepsLayout() {
    	
    	if (pastScores.length == 0) {
    		FrameLayout StepsLayout = (FrameLayout) findViewById(R.id.StepsLayout);
    		StepsLayout.setVisibility(View.VISIBLE);
    		
			TextView SuccessText = (TextView) findViewById(R.id.SuccessText);
			ImageView SuccessGraphic = (ImageView) findViewById(R.id.SuccessGraphic);
			SuccessGraphic.setVisibility(View.INVISIBLE);
			SuccessText.setText("You have not taken this test.");
			if (mode == STEPS)
				NextStepButton.setText("Step " + step);
			else
				NextStepButton.setText("Take Test");
    	}
    	else if (mode == WMC || mode == CUSTOM) {
    		FrameLayout StepsLayout = (FrameLayout) findViewById(R.id.StepsLayout);
    		StepsLayout.setVisibility(View.GONE);
    	}
    	else if (mode == STEPS) {	
    		FrameLayout StepsLayout = (FrameLayout) findViewById(R.id.StepsLayout);
    		StepsLayout.setVisibility(View.VISIBLE);
    		NextStepButton.setText("Step " + step);
			TextView SuccessText = (TextView) findViewById(R.id.SuccessText);
			ImageView SuccessGraphic = (ImageView) findViewById(R.id.SuccessGraphic);
			
			if (source == JUSTPLAYED) {
				double score = Double.parseDouble(getValue(scoreSummary[scoreSummary.length - 1]));
				double target = Utils.getTargetScore(gameType, step);
				if (score >= target) {
					SuccessText.setText("You passed Step " + step + "!");
					SuccessGraphic.setImageResource(R.drawable.graphic_mark_check);
					if (step < 5)
						LevelUp();
				}
				else {
					SuccessText.setText("You did not pass step " + step);
					SuccessGraphic.setImageResource(R.drawable.graphic_mark_x);
				}
				NextStepButton.setText("Step " + step);
			}
			else {
				SuccessText.setText("Go for the next step!");
				SuccessGraphic.setVisibility(View.INVISIBLE);
			}
    	}
    }
    
    public void LevelUp() {
    	step++;
    	SharedPreferences settings = getSharedPreferences("Steps", 0);
        SharedPreferences.Editor editor = settings.edit();
        switch (gameType) {
			case NUMBERS_SPEED:   editor.putInt("NUMBERS_SPEED",   step); break;
			case NUMBERS_BINARY:  editor.putInt("NUMBERS_BINARY",  step); break;
			case NUMBERS_SPOKEN:  editor.putInt("NUMBERS_SPOKEN",  step); break;
			case LISTS_WORDS:     editor.putInt("LISTS_WORDS",     step); break;
			case LISTS_EVENTS:    editor.putInt("LISTS_EVENTS",    step); break;
			case SHAPES_FACES:    editor.putInt("SHAPES_FACES",    step); break;
			case SHAPES_ABSTRACT: editor.putInt("SHAPES_ABSTRACT", step); break;
			case CARDS_SPEED:     editor.putInt("CARDS_SPEED",     step); break;
			case CARDS_LONG:      editor.putInt("CARDS_LONG",      step); break;
        }
        editor.commit();        
    }
    
    public int getStep(int gameType) {
    	SharedPreferences settings = getSharedPreferences("Steps", 0);
    	switch (gameType) {
			case NUMBERS_SPEED:   return settings.getInt("NUMBERS_SPEED", 1);
			case NUMBERS_LONG:    return settings.getInt("NUMBERS_LONG", 1);
			case NUMBERS_BINARY:  return settings.getInt("NUMBERS_BINARY", 1);
			case NUMBERS_SPOKEN:  return settings.getInt("NUMBERS_SPOKEN", 1);
			case LISTS_WORDS:     return settings.getInt("LISTS_WORDS", 1);
			case LISTS_EVENTS:    return settings.getInt("LISTS_EVENTS", 1);
			case SHAPES_FACES:    return settings.getInt("SHAPES_FACES", 1);
			case SHAPES_ABSTRACT: return settings.getInt("SHAPES_ABSTRACT", 1);
			case CARDS_SPEED:     return settings.getInt("CARDS_SPEED", 1);
			case CARDS_LONG:      return settings.getInt("CARDS_LONG", 1);
			default: return -1;
    	}
    }
    
    public void refreshScoreSummaryLayout() {
    	if (scoreSummary.length == 0) {
    		FrameLayout ScoreSummaryLayout = (FrameLayout) findViewById(R.id.layout);
    		ScoreSummaryLayout.setVisibility(View.GONE);
    	}
    	else {    	
	    	TextView[] labels = new TextView[3];
	    	labels[0] = (TextView) findViewById(R.id.Label0);
	    	labels[1] = (TextView) findViewById(R.id.Label1);
	    	labels[2] = (TextView) findViewById(R.id.Label2);
	    	
	    	TextView[] values = new TextView[3];
	    	values[0] = (TextView) findViewById(R.id.Value0);
	    	values[1] = (TextView) findViewById(R.id.Value1);
	    	values[2] = (TextView) findViewById(R.id.Value2);
	    	TextView ScoreValue = (TextView) findViewById(R.id.ScoreValue);
	    	
	    	FrameLayout ScoreSummaryLayout = (FrameLayout) findViewById(R.id.layout);
    		ScoreSummaryLayout.setVisibility(View.VISIBLE);
	    	
	    	for (int i=0; i<=2; i++) {
	    		String string = scoreSummary[i];
	    		string = string.replaceAll("(\\r|\\n)", "");
	    		System.out.println("1" + string + "1");
	    		if (string.equals("hide")) {
	    			labels[i].setVisibility(View.GONE);
	    			values[i].setVisibility(View.GONE);    			
	    		}

	    		else {
	    			labels[i].setVisibility(View.VISIBLE);
	    			values[i].setVisibility(View.VISIBLE);
	    			labels[i].setText(string.substring(0, (string.indexOf(':') + 1) ));
	    			values[i].setText(string.substring(string.indexOf(':') + 1, string.length()));
	    		}
	    	}
	    	ScoreValue.setText(getValue(scoreSummary[scoreSummary.length - 1]));
    	}
    }
    
    public String getLabel(String string) {
    	return string.substring(0, (string.indexOf(':') + 1) );
    }
    
    public String getValue(String string) {
    	return string.substring(string.indexOf(':') + 1, string.length());
    }
    
    public void drawGraph() {
    	ImageView image = (ImageView) findViewById(R.id.graph);
    	Display display = getWindowManager().getDefaultDisplay(); 
    	width = display.getWidth() - 10;
    	height = width * 4 / 5;
    	Graphing graph = new Graphing(width, height, pastScores);
    	image.setImageBitmap(graph.getGraph());
    }
    
    public void initButtons() {
    	button1 = (Button) findViewById(R.id.button1);
    	if (source == JUSTPLAYED) {
    		button1.setOnClickListener(this);
        	button1.setText("Review");
    	}
    	else
    		button1.setVisibility(View.INVISIBLE);
        
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button2.setText("Home");
        
        TakeTestButton = (Button) findViewById(R.id.TakeTestButton);
        TakeTestButton.setOnClickListener(this);
        AnimatedArrow = (Button) findViewById(R.id.AnimatedArrow);
        AnimatedArrow.setOnClickListener(this);
        
        NextStepButton = (Button) findViewById(R.id.NextStepButton);
        NextStepButton.setOnClickListener(this);
        
     //   HighScoresButton = (Button) findViewById(R.id.HighScoresButton);
     //   HighScoresButton.setOnClickListener(this);
        
        StepsButton = (Button) findViewById(R.id.StepsButton);
        StepsButton.setOnClickListener(this);
        ChampButton = (Button) findViewById(R.id.ChampButton);
        ChampButton.setOnClickListener(this);
        CustomButton = (Button) findViewById(R.id.CustomButton);
        CustomButton.setOnClickListener(this);
        
        NextGameButton = (Button) findViewById(R.id.NextGameButton);
        NextGameButton.setOnClickListener(this);
        LastGameButton = (Button) findViewById(R.id.LastGameButton);
        LastGameButton.setOnClickListener(this);
    }
    
    public void initText() {
    	GameTypeText = (TextView) findViewById(R.id.GameTypeText);
    }
                                  
    public void launchReviewActivity() {
    	Intent i = getIntent();
    	i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    	i.setClass(this, Utils.getReviewClass(gameType));    	
    	i.putExtra("score", (int) Double.parseDouble(getValue(scoreSummary[scoreSummary.length - 1])));
		this.startActivity(i);
    }    
    
    public void onTabClick(Button button) {
    	if (button == StepsButton) {
			mode = STEPS;
		/*	if (gameType == NUMBERS_LONG)
				gameType = NUMBERS_SPEED;
			else if (gameType == CARDS_SPEED)
				gameType = CARDS_LONG; */
    	}
		else if (button == ChampButton)
			mode = WMC;
		else if (button == CustomButton) {
			mode = CUSTOM;
	    /*	if (gameType == NUMBERS_LONG)
				gameType = NUMBERS_SPEED;
			else if (gameType == CARDS_SPEED)
				gameType = CARDS_LONG;  */
		}
    	if (!legalGameType(gameType, mode))
    		gameType = getNextGameType(gameType, mode);
    	
    	button1.setVisibility(View.INVISIBLE);
    	getLocalVariables(NAVIGATION);
    	refresh();
    }
    
    public void onNextGameClick() {
    /*	if (mode == STEPS || mode == CUSTOM) {
    		if (gameType == NUMBERS_SPEED)
    			gameType = NUMBERS_BINARY;
    		else if (gameType == CARDS_LONG)
    			gameType = NUMBERS_SPEED;
    		else if (gameType == SHAPES_ABSTRACT)
    			gameType = CARDS_LONG;
    		else
    			gameType++;
    	}
    	else if (mode == WMC) {
    		if (gameType == CARDS_LONG)
    			gameType = NUMBERS_SPEED;
    		else
    			gameType++;
    	} */
    	
    	gameType = getNextGameType(gameType, mode);
    	
    	getLocalVariables(NAVIGATION);
    	
    	animateTextNextGame(gameType);
    	refreshExcludeSelector();
    }
    
    
    
    public void onLastGameClick() {
     /*	if (mode == STEPS || mode == CUSTOM) {
    		if (gameType == NUMBERS_BINARY)
    			gameType = NUMBERS_SPEED;
    		else if (gameType == NUMBERS_SPEED)
    			gameType = CARDS_LONG;
    		else if (gameType == CARDS_LONG)
    			gameType = SHAPES_ABSTRACT;
    		else
    			gameType--;
    	}
    	else if (mode == WMC) {
    		if (gameType == NUMBERS_SPEED)
    			gameType = CARDS_LONG;
    		else
    			gameType--;
    	}    */
    	
    	gameType = getPreviousGameType(gameType, mode);
    	
    	getLocalVariables(NAVIGATION);
    	
    	animateTextLastGame(gameType);
    	refreshExcludeSelector();
    }
    
    public void animateTextNextGame(final int gameType) {
    	final Animation to_left = AnimationUtils.loadAnimation(this, R.anim.to_left);
    	to_left.reset();
		final Animation from_right_fast = AnimationUtils.loadAnimation(this, R.anim.from_right_fast);
		from_right_fast.reset();

		to_left.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				GameTypeText.setText(Utils.getTestString(gameType));
				GameTypeText.startAnimation(from_right_fast);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
		});

		GameTypeText.startAnimation(to_left);
    }
    
    public void animateTextLastGame(final int gameType) {
    	final Animation to_right = AnimationUtils.loadAnimation(this, R.anim.to_right);
    	to_right.reset();
		final Animation from_left_fast = AnimationUtils.loadAnimation(this, R.anim.from_left_fast);
		from_left_fast.reset();

		to_right.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				GameTypeText.setText(Utils.getTestString(gameType));
				GameTypeText.startAnimation(from_left_fast);
			}
			@Override
			public void onAnimationRepeat(Animation animation) {}
		});

		GameTypeText.startAnimation(to_right);
	}
    
    
    
    /* Extracts Score Summary info of most recent test from local storage 
     * Mode and GameType are only needed if launched from Main
     * Step is always needed if mode == STEPS
     * ScoreSummary is only needed if navigating or launched from Main
     * */
    public void getLocalVariables(int fromsource) {
    	
    	if (fromsource == JUSTPLAYED) {
    		getPastScores();
    		source = JUSTPLAYED;
    	}
    	else if (fromsource == MAIN) {
    		SharedPreferences prefs = getSharedPreferences("LastGameMode", 0);    		
    		mode = prefs.getInt("mode", WMC);
    		gameType = prefs.getInt("gameType", NUMBERS_SPEED);
    		getPastScores();
    		getPastScoreSummary();
    		if (mode == STEPS)
    			step = getStep(gameType);
    		source = MAIN;
    	}
    	else if (fromsource == NAVIGATION) {
    		getPastScores();
    		getPastScoreSummary();
    		if (mode == STEPS)
    			step = getStep(gameType);
    		source = NAVIGATION;
    	}
    	System.out.println(mode + " " + gameType + " " + step);
    }
    
    
    public void updatePastScoreSummary() {
    	new FileOps(this).updateScoreSummary(mode, gameType, scoreSummary);
    }
    
    public void updatePastScores(String newscore) {
    	new FileOps(this).updatePastScores(mode, gameType, newscore);
    }
    
    public void getPastScores() {
    	pastScores = new FileOps(this).readPastScores(mode, gameType);
    }
    
    public void getPastScoreSummary() {
    	scoreSummary = new FileOps(this).readScoreSummary(mode, gameType);
    }
    
    public void getExtras() {
    	Intent i = getIntent();
        gameType = i.getIntExtra("gameType", -1);
        if (gameType == -1) 
        	getLocalVariables(MAIN);
        else {
        	source = JUSTPLAYED;
        	reviewPossible = true;
        	        	
            mode = i.getIntExtra("mode", -1);
        	step = i.getIntExtra("step", -1);
        	
        	origGameType = gameType;
        	origMode = mode;
        	
        	commitGameModePrefs();
        	
        	scoreSummary = i.getStringArrayExtra("scores");        	
        	getPastScores();

        }  
    }
    
    public void checkLevelUp() {
    	double score = Double.parseDouble(getValue(scoreSummary[scoreSummary.length - 1]));
		double target = Utils.getTargetScore(gameType, step);

		if (score >= target && step <= 4) 
				LevelUp();
    }
    
    public void commitGameModePrefs() {
    	SharedPreferences settings = getSharedPreferences("LastGameMode", 0);
        SharedPreferences.Editor editor = settings.edit();
        
        editor.putInt("mode", mode);
        editor.putInt("gameType", gameType);
        editor.commit();
    }
    
    
	@Override
	public void onClick(View v) {
		if (v == button1)
			launchReviewActivity();
		else if (v == button2) {
			startActivity(new Intent(this, Main.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
			finish();
		}
		else if (v == StepsButton && mode != STEPS)
			onTabClick(StepsButton);
		else if (v == ChampButton && mode != WMC)
			onTabClick(ChampButton);
		else if (v == CustomButton && mode != CUSTOM)
			onTabClick(CustomButton);
		
		else if (v == NextGameButton)
			onNextGameClick();
		else if (v == LastGameButton)
			onLastGameClick();
		
		else if (v == NextStepButton)
			launchTest();
		else if (v == TakeTestButton)
			launchTest();
	}
	
	public void launchTest() {
		Intent i = new Intent(this, Utils.getPreClass(gameType));
		i.putExtra("mode", mode);
		i.putExtra("gameType", gameType);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
		finish();
	}
   
}