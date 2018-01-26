package com.MemoryLadder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.MemoryLadder.TestDetailsScreen.TestDetailsActivity;
import com.mastersofmemory.memoryladder.R;

public class ChooseTest extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.choosetest);

        initToolbar();
        initGraphics();
        initAnimation();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.test_details_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.icon);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Choose a Test");
        }
    }

    private void initAnimation() {
    	LinearLayout Numbers = findViewById(R.id.Numbers);
		LinearLayout Lists = findViewById(R.id.Lists);
		LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.fly_in_layout);
		Numbers.setLayoutAnimation(controller);
		Lists.setLayoutAnimation(controller);
    }
    
    private void initGraphics() {
        findViewById(R.id.SpeedNumbersImage).setOnClickListener(v -> onChooseTestFinished(Constants.NUMBERS_SPEED));
        findViewById(R.id.BinaryNumbersImage).setOnClickListener(v -> onChooseTestFinished(Constants.NUMBERS_BINARY));
        findViewById(R.id.SpokenNumbersImage).setOnClickListener(v -> onChooseTestFinished(Constants.NUMBERS_SPOKEN));
        findViewById(R.id.RandomWordsImage).setOnClickListener(v -> onChooseTestFinished(Constants.LISTS_WORDS));
        findViewById(R.id.HistoricDatesImage).setOnClickListener(v -> onChooseTestFinished(Constants.LISTS_EVENTS));
        findViewById(R.id.NamesFacesImage).setOnClickListener(v -> onChooseTestFinished(Constants.SHAPES_FACES));
        findViewById(R.id.AbstractImagesImage).setOnClickListener(v -> onChooseTestFinished(Constants.SHAPES_ABSTRACT));
        findViewById(R.id.SpeedCardsImage).setOnClickListener(v -> onChooseTestFinished(Constants.CARDS_LONG));
    }

	private void onChooseTestFinished(int gameType) {
		Intent i = getIntent();
		i.setClass(this, TestDetailsActivity.class);
		i.putExtra("gameType", gameType);
		this.startActivity(i);
	}
}