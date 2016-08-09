package com.Tester;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
//import com.MemoryLadderFull.R;

public class Main extends Activity implements OnClickListener {
	
	Button StepsButton;
	Button WMCButton;
	Button CustomButton;
	Button ScoresButton;
	Button InfoButton;
	Button MnemoButton;
	
	final private static int STEPS  = Constants.STEPS;
	final private static int WMC    = Constants.WMC;
	final private static int CUSTOM = Constants.CUSTOM;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initButtons();
        initImages();
        initAnimation();        
	}
	
	public void initAnimation() {
		final Animation from_left = AnimationUtils.loadAnimation(this, R.anim.from_left);
		from_left.reset();
		
		final Animation from_right = AnimationUtils.loadAnimation(this, R.anim.from_right);
		from_right.reset();
        
		StepsButton.startAnimation (from_left);
		WMCButton.startAnimation   (from_right);
		CustomButton.startAnimation(from_left);		
	}
	
	public void initButtons() {
		StepsButton  = (Button)  findViewById(R.id.StepsButton);
		WMCButton    = (Button)  findViewById(R.id.WMCButton);
		CustomButton = (Button)  findViewById(R.id.CustomButton);
		ScoresButton = (Button)  findViewById(R.id.ScoresButton);
		InfoButton   = (Button)  findViewById(R.id.InfoButton);
		MnemoButton   = (Button)  findViewById(R.id.MnemoButton);
		
		StepsButton .setOnClickListener(this);
		WMCButton   .setOnClickListener(this);
		CustomButton.setOnClickListener(this);
		ScoresButton.setOnClickListener(this);
		InfoButton.setOnClickListener(this);
		MnemoButton.setOnClickListener(this);
	}
	
	public void initImages() {}
	
	public void launchScoresButton() {
		Intent i = new Intent(this, ScoreActivity.class);	
		this.startActivity(i);
	}
	
	public void launchHelp() {
		Intent i = new Intent(this, HelpActivity.class);	
		this.startActivity(i);
	}
	
	public void launchMnemonics() {
		Intent i = new Intent(this, ChoosePegs_Numbers.class);	
		this.startActivity(i);
	}
	
	public void onMainFinished(int mode) {
		Intent i = new Intent(this, ChooseTest.class);
		i.putExtra("mode", mode);	
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		this.startActivity(i); 
	}
	
	@Override
	public void onClick(View v) {
		if      (v == StepsButton)   onMainFinished(STEPS);
		else if (v == WMCButton)     onMainFinished(WMC);
		else if (v == CustomButton)  onMainFinished(CUSTOM);	
		else if (v == ScoresButton)  launchScoresButton();
		else if (v == InfoButton)    launchHelp();
		else if (v == MnemoButton)   launchMnemonics();
	}	
}