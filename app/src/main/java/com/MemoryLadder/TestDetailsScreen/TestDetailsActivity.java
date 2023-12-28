package com.MemoryLadder.TestDetailsScreen;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.MemoryLadder.ChoosePegsScreens.ChoosePegs_Cards;
import com.MemoryLadder.ChoosePegsScreens.ChoosePegs_Numbers;
import com.MemoryLadder.Constants;
import com.MemoryLadder.InstructionsDialog;
import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.ActivityTestDetailsBinding;

public class TestDetailsActivity extends AppCompatActivity {

    private int gameType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActivityTestDetailsBinding binding = ActivityTestDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameType = getIntent().getIntExtra("gameType", Constants.NUMBERS_SPOKEN);
        int mode = getIntent().getIntExtra("mode", Constants.STEPS);

        /* Toolbar stuff */
        Toolbar toolbar = findViewById(R.id.test_details_toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(Constants.getGameIcon(gameType));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(Constants.getGameDisplayName(gameType));
        }

        /* Tabs & Viewpager stuff */
        ViewPager viewPager = findViewById(R.id.test_details_view_pager);
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(TestDetailsFragmentLoader.getTestDetailsFragmentLevels(this, gameType), "LEVELS");
        adapter.addFragment(TestDetailsFragmentLoader.getTestDetailsFragmentCustom(this, gameType), "CUSTOM");
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.test_details_tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(mode == Constants.CUSTOM ? 1 : 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.test_details_menu, menu);
        if (gameType == Constants.NUMBERS_SPEED) {
            menu.getItem(1).setVisible(false);
        }
        else if (gameType == Constants.CARDS_LONG) {
            menu.getItem(0).setVisible(false);
        }
        else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menu_mnemo_numbers) {
            Intent intentNum = new Intent();
            intentNum.setClass(this, ChoosePegs_Numbers.class);
            startActivity(intentNum);
            return true;
        } else if (itemId == R.id.menu_mnemo_cards) {
            Intent intentCard = new Intent();
            intentCard.setClass(this, ChoosePegs_Cards.class);
            startActivity(intentCard);
            return true;
        } else if (itemId == R.id.menu_help) {
            InstructionsDialog dialog = new InstructionsDialog(this, gameType);
            dialog.show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_activity_left, R.anim.slide_out_activity_right);
    }
}