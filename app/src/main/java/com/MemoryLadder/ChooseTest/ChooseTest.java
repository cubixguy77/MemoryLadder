package com.memoryladder.choosetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.memoryladder.Constants;
import com.memoryladder.testdetailsscreen.TestDetailsActivity;
import com.memoryladder.WhatsNewDialog;
import com.mastersofmemory.memoryladder.R;

public class ChooseTest extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_test);

        initToolbar();
        initGraphics();
        initChangeLog();
        initAnimation();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.test_details_toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(R.drawable.icon);
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

    private void initChangeLog() {
        findViewById(R.id.textWhatsNew).setOnClickListener(v -> {
            WhatsNewDialog dialog = new WhatsNewDialog(ChooseTest.this);
            dialog.show();
        });
    }

	private void onChooseTestFinished(int gameType) {
		Intent i = new Intent(this, TestDetailsActivity.class);
		i.putExtra("gameType", gameType);
		this.startActivity(i);
        overridePendingTransition(R.anim.slide_in_activity_right, R.anim.slide_out_activity_left);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choose_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_send_feedback:
                getUserFeedback();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUserFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "mastersofmemorycontact@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_EmailSubjectText));
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(getString(R.string.feedback_EmailMessageBodyText)));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "mastersofmemorycontact@gmail.com" });
            Email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_EmailSubjectText));
            Email.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(getString(R.string.feedback_EmailMessageBodyText)));
            startActivity(Intent.createChooser(Email, getString(R.string.send_feedback)));
        }
    }

    @Override
    public void onBackPressed() {
        launchExitDialog();
    }

    public void launchExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setMessage("Are you sure you want to close Memory Ladder?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}