package com.MemoryLadder.Cards;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.MemoryLadder.Cards.ScorePanel.Score;
import com.MemoryLadder.Cards.ScorePanel.ScorePanel;
import com.MemoryLadder.Constants;
import com.MemoryLadder.CountDownDialog;
import com.MemoryLadder.FileOps;
import com.MemoryLadder.TestDetailsScreen.TestDetailsActivity;
import com.MemoryLadder.Timer.ITimer;
import com.MemoryLadder.Timer.SimpleTimer;
import com.MemoryLadder.Utils;
import com.jjoe64.graphview.series.DataPoint;
import com.mastersofmemory.memoryladder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardPrototype extends AppCompatActivity implements GameManagerActivity {

    @BindView(R.id.cards_score_panel) ScorePanel scorePanel;
    @BindView(R.id.cardsToolbar) Toolbar toolbar;

    GeneralSettings settings;

    private GameManager gameManager;

    private SimpleTimer timer;
    private float secondsElapsedMem;

    private MenuItem finishMem;
    private MenuItem finishRecall;
    private MenuItem playAgain;

    private GamePhase gamePhase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_card_prototype);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        settings = SettingsProvider.getGeneralSettings(getIntent());
        gameManager = GameManagerProvider.getGameManager(settings.getGameType(), getIntent());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.gameContainer, (Fragment) gameManager);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (timer == null) {
            setGamePhase(GamePhase.PRE_MEMORIZATION);
        }
        else {
            if (this.gamePhase == GamePhase.MEMORIZATION || this.gamePhase == GamePhase.RECALL) {
                timer.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (timer != null && !timer.isPaused()) {
            timer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }








    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        renderToolbarFor(gamePhase);

        if (gamePhase == GamePhase.PRE_MEMORIZATION) {
            gameManager.setGamePhase(GamePhase.PRE_MEMORIZATION);
            secondsElapsedMem = 0;
            scorePanel.setVisibility(View.GONE);

            timer = new SimpleTimer(settings.getTimeLimitInSeconds(), new ITimer.TimerUpdateListener() {
                @Override
                public void onTimeUpdate(float secondsRemaining, float secondsElapsed) {
                    secondsElapsedMem = secondsElapsed;
                    gameManager.displayTime((int) secondsRemaining);
                }
                @Override
                public void onTimeCountdownComplete() {
                    gameManager.setGamePhase(GamePhase.RECALL);
                }
            });
        }
        if (gamePhase == GamePhase.MEMORIZATION) {
            gameManager.setGamePhase(GamePhase.MEMORIZATION);
            timer.start();
        }
        if (gamePhase == GamePhase.RECALL) {
            timer.cancel();
            gameManager.setGamePhase(gamePhase);

            timer = new SimpleTimer(settings.getTimeLimitInSecondsForRecall(), new ITimer.TimerUpdateListener() {
                @Override
                public void onTimeUpdate(float secondsRemaining, float secondsElapsed) { gameManager.displayTime((int) secondsRemaining); }
                @Override
                public void onTimeCountdownComplete() {
                    setGamePhase(GamePhase.REVIEW);
                }
            });

            timer.start();
        }
        else if (gamePhase == GamePhase.REVIEW) {
            timer.cancel();
            gameManager.setGamePhase(GamePhase.REVIEW);

            Score score = gameManager.getScore();
            saveScore(score);
            scorePanel.show(score, secondsElapsedMem, getPastScores());

            if (settings.getMode() == Constants.STEPS && isLevelUp(score.score)) {
                doLevelUp();
                showLevelUpDialog();
            }
        }
    }


    void saveScore(Score score) {
        new FileOps(this).updatePastScores(settings.getMode(), settings.getGameType(), Integer.toString(score.score));
    }

    DataPoint[] getPastScores() {
        return new FileOps(this).readPastScoresToDataPoints(settings.getMode(), settings.getGameType());
    }

    private boolean isLevelUp(int scoreValue) {
        double target = Utils.getTargetScore(settings.getGameType(), settings.getStep());
        return scoreValue >= target && settings.getStep() < 5;
    }

    private void doLevelUp() {
        SharedPreferences prefs = getSharedPreferences("Steps", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(Constants.getGameName(settings.getGameType()), settings.getStep() + 1);
        editor.apply();
    }

    private void showLevelUpDialog() {
        int step = settings.getStep();

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle("You passed step " + step + "!");
        builder.setMessage("Would you like to continue to step " + (step + 1) + "?");

        String positiveText = "Step " + (step + 1);
        builder.setPositiveButton(positiveText,
                (dialog, which) -> {
                    Intent i = new Intent();
                    i.putExtra("gameType", settings.getGameType());
                    i.putExtra("mode", settings.getMode());
                    i.setClass(this, TestDetailsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                });

        String negativeText = "Cancel";
        builder.setNegativeButton(negativeText, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }












    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cards_menu, menu);
        finishMem = menu.getItem(0);
        finishRecall = menu.getItem(1);
        playAgain = menu.getItem(2);

        renderToolbarFor(this.gamePhase);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_finished_mem:
                setGamePhase(GamePhase.RECALL);
                return true;
            case R.id.menu_finished_recall:
                setGamePhase(GamePhase.REVIEW);
                return true;
            case R.id.menu_play_again:
                setGamePhase(GamePhase.PRE_MEMORIZATION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void renderToolbarFor(GamePhase phase) {

        if (toolbar == null || finishMem == null)
            return;

        if (phase == GamePhase.PRE_MEMORIZATION) {
            toolbar.setTitle("Click Start to Begin");
            finishMem.setVisible(false);
            finishRecall.setVisible(false);
            playAgain.setVisible(false);
        }
        else if (phase == GamePhase.MEMORIZATION) {
            toolbar.setTitle("Memorization");
            finishMem.setVisible(true);
            finishRecall.setVisible(false);
            playAgain.setVisible(false);
        }
        else if (phase == GamePhase.RECALL) {
            toolbar.setTitle("Recall");
            finishMem.setVisible(false);
            finishRecall.setVisible(true);
            playAgain.setVisible(false);
        }
        else if (phase == GamePhase.REVIEW) {
            toolbar.setTitle("Review");
            finishMem.setVisible(false);
            finishRecall.setVisible(false);
            playAgain.setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (this.gamePhase == GamePhase.MEMORIZATION || this.gamePhase == GamePhase.RECALL) {
            launchExitDialog();
        }
        else {
            finish();
        }
    }

    public void launchExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setMessage("Stop current game?")
               .setCancelable(false)
               .setPositiveButton("Stop Game", (dialog, id) -> finish())
               .setNegativeButton("Continue Game", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onStartClicked() {
        gameManager.refreshVisibleComponentsForPhase(GamePhase.MEMORIZATION);
        renderToolbarFor(GamePhase.MEMORIZATION);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            CountDownDialog count = new CountDownDialog(this);
            count.setOnDismissListener(dialog -> setGamePhase(GamePhase.MEMORIZATION));
            count.show();
        }, 500);
    }
}
