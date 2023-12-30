package com.memoryladder.choosetest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.mastersofmemory.memoryladder.databinding.ActivityChooseTestBinding;
import com.mastersofmemory.memoryladder.databinding.ToolbarBinding;

public class ChooseTest extends AppCompatActivity {

    private ActivityChooseTestBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityChooseTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initToolbar();
        initGraphics();
        initChangeLog();
        initAnimation();
    }

    private void initToolbar() {
        ToolbarBinding toolbar = binding.testDetailsToolbar;
        setSupportActionBar(toolbar.getRoot());
        //toolbar.setNavigationIcon(R.drawable.icon);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Choose a Test");
        }
    }

    private void initAnimation() {
    	LinearLayout Numbers = binding.Numbers;
		LinearLayout Lists = binding.Lists;
		LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(this, R.anim.fly_in_layout);
		Numbers.setLayoutAnimation(controller);
		Lists.setLayoutAnimation(controller);
    }
    
    private void initGraphics() {
        binding.SpeedNumbersImage.setOnClickListener(v -> onChooseTestFinished(Constants.NUMBERS_SPEED));
        binding.SpeedNumbersImage.setOnClickListener(v -> onChooseTestFinished(Constants.NUMBERS_SPEED));
        binding.BinaryNumbersImage.setOnClickListener(v -> onChooseTestFinished(Constants.NUMBERS_BINARY));
        binding.SpokenNumbersImage.setOnClickListener(v -> onChooseTestFinished(Constants.NUMBERS_SPOKEN));
        binding.RandomWordsImage.setOnClickListener(v -> onChooseTestFinished(Constants.LISTS_WORDS));
        binding.HistoricDatesImage.setOnClickListener(v -> onChooseTestFinished(Constants.LISTS_EVENTS));
        binding.NamesFacesImage.setOnClickListener(v -> onChooseTestFinished(Constants.SHAPES_FACES));
        binding.AbstractImagesImage.setOnClickListener(v -> onChooseTestFinished(Constants.SHAPES_ABSTRACT));
        binding.SpeedCardsImage.setOnClickListener(v -> onChooseTestFinished(Constants.CARDS_LONG));
    }

    private void initChangeLog() {
        binding.textWhatsNew.setOnClickListener(v -> {
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
        if (item.getItemId() == R.id.action_send_feedback) {
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