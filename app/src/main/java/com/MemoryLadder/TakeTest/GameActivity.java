package com.MemoryLadder.TakeTest;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;

import com.MemoryLadder.InstructionsDialog;
import com.MemoryLadder.TakeTest.ScorePanel.Score;
import com.MemoryLadder.TakeTest.ScorePanel.ScorePanel;
import com.MemoryLadder.Constants;
import com.MemoryLadder.CountDownDialog;
import com.MemoryLadder.FileOps;
import com.MemoryLadder.TestDetailsScreen.TestDetailsActivity;
import com.MemoryLadder.TakeTest.Timer.ITimer;
import com.MemoryLadder.TakeTest.Timer.SimpleTimer;
import com.MemoryLadder.Utils;
import com.jjoe64.graphview.series.DataPoint;
import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.ActivityTestArenaBinding;

import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    private ScorePanel scorePanel;
    private Toolbar toolbar;
    private Button startButton;

    private GameSettings settings;
    private GameManager gameManager;
    private GamePhase gamePhase;

    private SimpleTimer timer;
    private float secondsElapsedMem;
    private float secondsElapsedRecall;

    private boolean saveScore;

    private MenuItem finishMem;
    private MenuItem finishRecall;
    private MenuItem playAgain;
    private MenuItem help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        com.mastersofmemory.memoryladder.databinding.ActivityTestArenaBinding binding = ActivityTestArenaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        scorePanel = binding.scorePanel.getRoot();
        toolbar = binding.generalToolbar.getRoot();
        startButton = binding.buttonStart;
        startButton.setOnClickListener(v -> startGame());

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        settings = SettingsProvider.getGeneralSettings(getIntent());

        if (savedInstanceState != null) {
            gameManager = (GameManager) getSupportFragmentManager().getFragment(savedInstanceState, "gameManager");
            this.gamePhase = (GamePhase) savedInstanceState.getSerializable("gamePhase");
            this.secondsElapsedMem = savedInstanceState.getFloat("secondsElapsedMem");
            this.secondsElapsedRecall = savedInstanceState.getFloat("secondsElapsedRecall");
            if (this.gamePhase == GamePhase.REVIEW) {
                saveScore = false;
            }

        } else {
            this.gamePhase = GamePhase.PRE_MEMORIZATION;
            gameManager = GameManagerProvider.getGameManager(settings.getGameType(), getIntent(), this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.gameContainer, (Fragment) gameManager);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (this.gamePhase == GamePhase.PRE_MEMORIZATION) {
            setGamePhase(GamePhase.PRE_MEMORIZATION);
        }

        /* From this point on, we know we're restoring previous state */
        else if (this.gamePhase == GamePhase.MEMORIZATION) {
            setGamePhase(GamePhase.MEMORIZATION);
        }
        else if (this.gamePhase == GamePhase.RECALL) {
            setGamePhase(GamePhase.RECALL);
        }
        else if (this.gamePhase == GamePhase.REVIEW) {
            setGamePhase(GamePhase.REVIEW);
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "gameManager", (Fragment) gameManager);
        outState.putSerializable("gamePhase", this.gamePhase);
        outState.putFloat("secondsElapsedMem", this.secondsElapsedMem);
        outState.putFloat("secondsElapsedRecall", this.secondsElapsedRecall);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private SimpleTimer getMemTimer() {
        if (timer == null) {
            timer = new SimpleTimer(settings.getTimeLimitInSeconds(), this.secondsElapsedMem, new ITimer.TimerUpdateListener() {
                @Override
                public void onTimeUpdate(float secondsRemaining, float secondsElapsed) {
                    secondsElapsedMem = secondsElapsed;
                    gameManager.displayTime((int) secondsRemaining);
                }
                @Override
                public void onTimeCountdownComplete() {
                    setGamePhase(GamePhase.RECALL);
                }
            });
        }

        return timer;
    }

    private SimpleTimer getRecallTimer() {
        if (timer == null) {
            timer = new SimpleTimer(settings.getTimeLimitInSecondsForRecall(), this.secondsElapsedRecall, new ITimer.TimerUpdateListener() {
                @Override
                public void onTimeUpdate(float secondsRemaining, float secondsElapsed) {
                    secondsElapsedRecall = secondsElapsed;
                    gameManager.displayTime((int) secondsRemaining);
                }
                @Override
                public void onTimeCountdownComplete() {
                    setGamePhase(GamePhase.REVIEW);
                }
            });
        }

        return timer;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        renderToolbarFor(gamePhase);

        if (gamePhase == GamePhase.PRE_MEMORIZATION) {
            new Handler().postDelayed(() -> startButton.setVisibility(View.VISIBLE), 400);
        } else {
            startButton.setVisibility(View.GONE);
        }

        View gameRootView = ((Fragment) gameManager).getView();
        ViewTreeObserver viewTreeObserver = Objects.requireNonNull(gameRootView).getViewTreeObserver();
        if (viewTreeObserver == null)
            return;

        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                gameRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                if (gamePhase == GamePhase.PRE_MEMORIZATION) {
                    gameManager.setGamePhase(GamePhase.PRE_MEMORIZATION);
                    scorePanel.setVisibility(View.GONE);
                    secondsElapsedMem = 0;
                    secondsElapsedRecall = 0;
                    saveScore = true;
                    getMemTimer();
                }
                if (gamePhase == GamePhase.MEMORIZATION) {
                    gameManager.setGamePhase(GamePhase.MEMORIZATION);
                    getMemTimer().start();
                }
                if (gamePhase == GamePhase.RECALL) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }

                    gameManager.setGamePhase(gamePhase);
                    getRecallTimer().start();
                }
                else if (gamePhase == GamePhase.REVIEW) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }

                    gameManager.setGamePhase(GamePhase.REVIEW);

                    Score score = gameManager.getScore();

                    /* This prevents re-submitting the same score when rotating device in Review mode */
                    if (saveScore) {
                        saveScore(score);
                    }

                    scorePanel.show(score, secondsElapsedMem, getPastScores());

                    if (saveScore && settings.getMode() == Constants.STEPS && isLevelUp(score.score)) {
                        doLevelUp();
                        showLevelUpDialog();
                    }
                }
            }
        });
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
        getMenuInflater().inflate(R.menu.game_menu, menu);
        finishMem = menu.getItem(0);
        finishRecall = menu.getItem(1);
        playAgain = menu.getItem(2);
        help = menu.getItem(3);

        renderToolbarFor(this.gamePhase);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_finished_mem) {
            setGamePhase(GamePhase.RECALL);
            return true;
        } else if (itemId == R.id.menu_finished_recall) {
            setGamePhase(GamePhase.REVIEW);
            return true;
        } else if (itemId == R.id.menu_play_again) {
            setGamePhase(GamePhase.PRE_MEMORIZATION);
            return true;
        } else if (itemId == R.id.menu_help) {
            InstructionsDialog dialog = new InstructionsDialog(this, settings.getGameType());
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void renderToolbarFor(GamePhase phase) {

        if (toolbar == null || finishMem == null)
            return;

        if (phase == GamePhase.PRE_MEMORIZATION) {
            toolbar.setTitle("Click Start to Begin");
            finishMem.setVisible(false);
            finishRecall.setVisible(false);
            playAgain.setVisible(false);
            help.setVisible(true);
        }
        else if (phase == GamePhase.MEMORIZATION) {
            toolbar.setTitle("Memorization");
            finishMem.setVisible(true);
            finishRecall.setVisible(false);
            playAgain.setVisible(false);
            help.setVisible(false);
        }
        else if (phase == GamePhase.RECALL) {
            toolbar.setTitle("Recall");
            finishMem.setVisible(false);
            finishRecall.setVisible(true);
            playAgain.setVisible(false);
            help.setVisible(false);
        }
        else if (phase == GamePhase.REVIEW) {
            toolbar.setTitle("Review");
            finishMem.setVisible(false);
            finishRecall.setVisible(false);
            playAgain.setVisible(true);
            help.setVisible(false);
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
               .setCancelable(true)
               .setPositiveButton("Stop Game", (dialog, id) -> finish())
               .setNegativeButton("Continue Game", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    void startGame() {
        startButton.setVisibility(View.GONE);

        gameManager.render(GamePhase.MEMORIZATION);
        renderToolbarFor(GamePhase.MEMORIZATION);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            CountDownDialog count = new CountDownDialog(this);
            count.setOnDismissListener(dialog -> setGamePhase(GamePhase.MEMORIZATION));
            count.show();
        }, 500);
    }
}
