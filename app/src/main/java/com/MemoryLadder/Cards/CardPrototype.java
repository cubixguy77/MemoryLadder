package com.MemoryLadder.Cards;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.MemoryLadder.Cards.ScorePanel.ScorePanel;
import com.MemoryLadder.CountDownDialog;
import com.MemoryLadder.Timer.ITimer;
import com.MemoryLadder.Timer.SimpleTimer;
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
    private float secondsElapsedRecall;

    private MenuItem finishMem;
    private MenuItem finishRecall;
    private MenuItem playAgain;

    private GamePhase gamePhase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        fragmentTransaction.add(R.id.gameContainer, gameManager);
        fragmentTransaction.commit();
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        displayToolbar(gamePhase);

        if (gamePhase == GamePhase.PRE_MEMORIZATION) {
            secondsElapsedMem = 0;
            secondsElapsedRecall = 0;

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

            gameManager.setGamePhase(GamePhase.PRE_MEMORIZATION);
        }
        if (gamePhase == GamePhase.MEMORIZATION) {
            gameManager.setGamePhase(GamePhase.MEMORIZATION);
        }
        if (gamePhase == GamePhase.RECALL) {
            timer.cancel();

            gameManager.setGamePhase(gamePhase);

            timer = new SimpleTimer(settings.getTimeLimitInSecondsForRecall(), new ITimer.TimerUpdateListener() {
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

            timer.start();
        }
        else if (gamePhase == GamePhase.REVIEW) {
            timer.cancel();
            gameManager.setGamePhase(GamePhase.REVIEW);
        }
    }

    @Override
    public void onStartClicked() {
        gameManager.refreshVisibleComponentsForPhase(GamePhase.MEMORIZATION);
        displayToolbar(GamePhase.MEMORIZATION);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            CountDownDialog count = new CountDownDialog(this);
            count.setOnDismissListener(dialog -> {
                setGamePhase(GamePhase.MEMORIZATION);
                timer.start();
            });
            count.show();
        }, 500);
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

        if (timer != null && this.gamePhase == GamePhase.MEMORIZATION || this.gamePhase == GamePhase.MEMORIZATION) {
            timer.pause();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cards_menu, menu);
        finishMem = menu.getItem(0);
        finishRecall = menu.getItem(1);
        playAgain = menu.getItem(2);

        displayToolbar(this.gamePhase);
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

    private void displayToolbar(GamePhase phase) {

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
}
