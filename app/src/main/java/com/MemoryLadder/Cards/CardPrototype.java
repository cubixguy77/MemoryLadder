package com.MemoryLadder.Cards;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.MemoryLadder.Constants;
import com.mastersofmemory.memoryladder.R;

public class CardPrototype extends AppCompatActivity {

    private GameManager gameManager;
    private MenuItem finishMem;
    private MenuItem finishRecall;
    private MenuItem playAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_card_prototype);

        Toolbar toolbar = findViewById(R.id.cardsToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> launchExitDialog());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.deck_view).post(() -> {
            gameManager = new GameManager(this, getSupportActionBar(), getCardSettings());
            gameManager.setMenuItems(finishMem, finishRecall, playAgain);
            gameManager.setGamePhase(GamePhase.PRE_MEMORIZATION);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (gameManager != null) {
            gameManager.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (gameManager != null) {
            gameManager.pause();
        }
    }



    private CardSettings getCardSettings() {
        Intent i = getIntent();

        int mode              = i.getIntExtra("mode",     -1);
        int gameType          = i.getIntExtra("gameType", -1);
        int step              = i.getIntExtra("step",     -1);
        int memTime           = i.getIntExtra("memTime",  -1);
        int recallTime        = i.getIntExtra("recallTime",-1);
        int deckSize          = i.getIntExtra("deckSize", -1);
        int numDecks          = i.getIntExtra("numDecks", -1);
        int numCardsPerGroup  = i.getIntExtra("numCardsPerGroup", 2);
        boolean mnemonicsEnabled  = i.getBooleanExtra("mnemo_enabled", false);

        return new CardSettings(mode, gameType, step, numDecks, deckSize, numCardsPerGroup, true, memTime, recallTime, mnemonicsEnabled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cards_menu, menu);
        finishMem = menu.getItem(0);
        finishRecall = menu.getItem(1);
        playAgain = menu.getItem(2);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_finished_mem:
                gameManager.setGamePhase(GamePhase.RECALL);
                return true;
            case R.id.menu_finished_recall:
                gameManager.setGamePhase(GamePhase.REVIEW);
                return true;
            case R.id.menu_play_again:
                gameManager.setGamePhase(GamePhase.PRE_MEMORIZATION);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        launchExitDialog();
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
