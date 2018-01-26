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
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;

import com.MemoryLadder.TestDetailsScreen.TestDetailsActivity;
import com.mastersofmemory.memoryladder.R;

public class ScoreActivity extends Activity implements OnClickListener {
	
	private Button button1;
	private Button button2;
	private Button TakeTestButton;
	private Button NextStepButton;
	private Button StepsButton;
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
	private int source = JUST_PLAYED;
	private Boolean reviewPossible = false;
	private int origGameType;
	private int origMode;
	private Animation a;

	final private static int STEPS  = Constants.STEPS;
	final private static int CUSTOM = Constants.CUSTOM;
	
	final private static int JUST_PLAYED = 0;
	final private static int MAIN        = 1;
	final private static int NAVIGATION  = 2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.scores);
        
        getExtras();
        initButtons();  
        initText();
        if (source == JUST_PLAYED) {
        	updatePastScoreSummary();
        	updatePastScores(getValue(scoreSummary[scoreSummary.length - 1]));
        }
        getPastScores();
        
        a = AnimationUtils.loadAnimation(this, R.anim.fade_in_fade_out);
        refresh();
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
        	CustomButton.setBackgroundResource(unselected); CustomButton.setTextColor(Color.WHITE);
    	}
    	else if (mode == CUSTOM) {
        	StepsButton.setBackgroundResource(unselected); StepsButton.setTextColor(Color.WHITE);
        	CustomButton.setBackgroundResource(selected);  CustomButton.setTextColor(Color.BLACK);
    	}    	
    	
    	GameTypeText.setText(Utils.getTestString(gameType));
    }
    
    public void refreshGraphLayout() {
    	
    	if (pastScores.length == 0) {
    		FrameLayout GraphLayout = findViewById(R.id.GraphLayout);
			GraphLayout.setVisibility(View.GONE);    		
    	}
    	else if (mode == CUSTOM) {
			FrameLayout GraphLayout = findViewById(R.id.GraphLayout);
			GraphLayout.setVisibility(View.VISIBLE);
			drawGraph();
    	}
    	else if (mode == STEPS) {
			FrameLayout GraphLayout = findViewById(R.id.GraphLayout);
			GraphLayout.setVisibility(View.GONE);
    	}
    }
    
    public void refreshStepsLayout() {
    	
    	if (pastScores.length == 0) {
    		FrameLayout StepsLayout = findViewById(R.id.StepsLayout);
    		StepsLayout.setVisibility(View.VISIBLE);
    		
			TextView SuccessText = findViewById(R.id.SuccessText);
			ImageView SuccessGraphic = findViewById(R.id.SuccessGraphic);
			SuccessGraphic.setVisibility(View.INVISIBLE);
			SuccessText.setText("You have not taken this test.");
			if (mode == STEPS)
				NextStepButton.setText("Step " + step);
			else
				NextStepButton.setText("Take Test");
    	}
    	else if (mode == CUSTOM) {
    		FrameLayout StepsLayout = findViewById(R.id.StepsLayout);
    		StepsLayout.setVisibility(View.GONE);
    	}
    	else if (mode == STEPS) {	
    		FrameLayout StepsLayout = findViewById(R.id.StepsLayout);
    		StepsLayout.setVisibility(View.VISIBLE);
    		NextStepButton.setText("Step " + step);
			TextView SuccessText = findViewById(R.id.SuccessText);
			ImageView SuccessGraphic = findViewById(R.id.SuccessGraphic);
			
			if (source == JUST_PLAYED) {
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
        editor.putInt(Constants.getGameName(gameType), step);
        editor.commit();        
    }
    
    public int getStep(int gameType) {
    	SharedPreferences settings = getSharedPreferences("Steps", 0);
    	return settings.getInt(Constants.getGameName(gameType), 1);
    }
    
    public void refreshScoreSummaryLayout() {
    	if (scoreSummary.length == 0) {
    		FrameLayout ScoreSummaryLayout = findViewById(R.id.layout);
    		ScoreSummaryLayout.setVisibility(View.GONE);
    	}
    	else {    	
	    	TextView[] labels = new TextView[3];
	    	labels[0] = findViewById(R.id.Label0);
	    	labels[1] = findViewById(R.id.Label1);
	    	labels[2] = findViewById(R.id.Label2);
	    	
	    	TextView[] values = new TextView[3];
	    	values[0] = findViewById(R.id.Value0);
	    	values[1] = findViewById(R.id.Value1);
	    	values[2] = findViewById(R.id.Value2);
	    	TextView ScoreValue = findViewById(R.id.ScoreValue);
	    	
	    	FrameLayout ScoreSummaryLayout = findViewById(R.id.layout);
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

    public String getValue(String string) {
    	return string.substring(string.indexOf(':') + 1, string.length());
    }
    
    public void drawGraph() {
    	ImageView image = findViewById(R.id.graph);
    	Display display = getWindowManager().getDefaultDisplay();
		int width = display.getWidth() - 10;
		int height = width * 4 / 5;
    	Graphing graph = new Graphing(width, height, pastScores);
    	image.setImageBitmap(graph.getGraph());
    }
    
    public void initButtons() {
    	button1 = findViewById(R.id.button1);
    	if (source == JUST_PLAYED) {
    		button1.setOnClickListener(this);
        	button1.setText("Review");
    	}
    	else
    		button1.setVisibility(View.INVISIBLE);
        
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button2.setText("Home");
        
        TakeTestButton = findViewById(R.id.TakeTestButton);
        TakeTestButton.setOnClickListener(this);
        AnimatedArrow = findViewById(R.id.AnimatedArrow);
        AnimatedArrow.setOnClickListener(this);
        
        NextStepButton = findViewById(R.id.NextStepButton);
        NextStepButton.setOnClickListener(this);

        StepsButton = findViewById(R.id.StepsButton);
        StepsButton.setOnClickListener(this);
        CustomButton = findViewById(R.id.CustomButton);
        CustomButton.setOnClickListener(this);
        
        NextGameButton = findViewById(R.id.NextGameButton);
        NextGameButton.setOnClickListener(this);
        LastGameButton = findViewById(R.id.LastGameButton);
        LastGameButton.setOnClickListener(this);
    }
    
    public void initText() {
    	GameTypeText = findViewById(R.id.GameTypeText);
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
    	}
		else if (button == CustomButton) {
			mode = CUSTOM;
		}
    	
    	button1.setVisibility(View.INVISIBLE);
    	getLocalVariables(NAVIGATION);
    	refresh();
    }


    private void onNextGameClick() {
    	gameType = getNextGameType(gameType, true);
    	getLocalVariables(NAVIGATION);
    	animateTextNextGame(gameType);
    	refreshExcludeSelector();
    }

	private void onLastGameClick() {
    	gameType = getNextGameType(gameType, false);
    	getLocalVariables(NAVIGATION);
    	animateTextLastGame(gameType);
    	refreshExcludeSelector();
    }

	private int getNextGameType(int gameType, boolean forward) {
		switch (gameType) {
			case Constants.NUMBERS_SPEED:
				return forward ? Constants.NUMBERS_BINARY : Constants.SHAPES_ABSTRACT;
			case Constants.NUMBERS_BINARY:
				return forward ? Constants.NUMBERS_SPOKEN : Constants.NUMBERS_SPEED;
			case Constants.NUMBERS_SPOKEN:
				return forward ? Constants.CARDS_LONG : Constants.NUMBERS_BINARY;
			case Constants.CARDS_LONG:
				return forward ? Constants.LISTS_WORDS : Constants.NUMBERS_SPOKEN;
			case Constants.LISTS_WORDS:
				return forward ? Constants.LISTS_EVENTS : Constants.CARDS_LONG;
			case Constants.LISTS_EVENTS:
				return forward ? Constants.SHAPES_FACES : Constants.LISTS_WORDS;
			case Constants.SHAPES_FACES:
				return forward ? Constants.SHAPES_ABSTRACT : Constants.LISTS_EVENTS;
			case Constants.SHAPES_ABSTRACT:
				return forward ? Constants.NUMBERS_SPEED : Constants.SHAPES_FACES;
			default:
				return Constants.NUMBERS_SPEED;
		}
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
    	if (fromsource == JUST_PLAYED) {
    		getPastScores();
    		source = JUST_PLAYED;
    	}
    	else if (fromsource == NAVIGATION) {
    		getPastScores();
    		getPastScoreSummary();
    		if (mode == STEPS)
    			step = getStep(gameType);
    		source = NAVIGATION;
    	}
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
        	source = JUST_PLAYED;
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
			startActivity(new Intent(this, ChooseTest.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
			finish();
		}
		else if (v == StepsButton && mode != STEPS)
			onTabClick(StepsButton);
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
		Intent i = new Intent(this, TestDetailsActivity.class);
		i.putExtra("mode", mode);
		i.putExtra("gameType", gameType);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(i);
		finish();
	}
   
}