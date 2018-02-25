package com.MemoryLadder.Cards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.MemoryLadder.Cards.ScorePanel.Score;
import com.MemoryLadder.Cards.ScorePanel.ScorePanel;
import com.MemoryLadder.TestDetailsScreen.TestDetailsActivity;
import com.MemoryLadder.Timer.ITimer;
import com.MemoryLadder.Timer.SimpleTimer;
import com.MemoryLadder.Timer.TimerView;
import com.MemoryLadder.Constants;
import com.MemoryLadder.CountDownDialog;
import com.MemoryLadder.Utils;
import com.mastersofmemory.memoryladder.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameManager implements DeckSelector.Presenter, SuitSelectionListener, CardSelectionListener, CardClickListener {

    private Context context;
    private CardSettings settings;

    @BindView(R.id.deck_view) DeckView deckView;
    @BindView(R.id.selected_cards_view) SelectedCardsView selectedCardsView;
    @BindView(R.id.layout_suit_selector) SuitSelectorView suitSelectorView;
    @BindView(R.id.card_selector_view) CardSelectionView cardSelectorView;
    @BindView(R.id.layout_deck_selector) DeckSelectorView deckSelectorView;
    @BindView(R.id.text_timer) TimerView timerView;
    @BindView(R.id.layout_bottom_navigator_buttons) FrameLayout navigatorButtons;
    @BindView(R.id.button_prev_group_alt) AppCompatImageButton prevGroupButton;
    @BindView(R.id.button_next_group_alt) AppCompatImageButton nextGroupButton;
    @BindView(R.id.layout_button_start) FrameLayout startButtonLayout;
    @BindView(R.id.cards_score_panel) ScorePanel scorePanel;

    private GameData data;
    private SimpleTimer timer;

    private ActionBar toolbar;
    private MenuItem finishMem;
    private MenuItem finishRecall;
    private MenuItem playAgain;

    GameManager(Activity activity, ActionBar toolbar, CardSettings settings) {
        this.toolbar = toolbar;
        this.context = activity;
        this.settings = settings;

        ButterKnife.bind(this, activity);

        deckView.setListener(this);
        deckSelectorView.setDeckSelectionListener(this);
        suitSelectorView.setSuitSelectionListener(this);
        cardSelectorView.setCardSelectionListener(this);

        selectedCardsView.setMnemonicsEnabled(settings.isMnemonicsEnabled());
    }

    GamePhase getGamePhase() {
        return data.getGamePhase();
    }

    void setGamePhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            data = new GameData(settings);
            timer = new SimpleTimer(settings.getTimeLimitInSeconds(), new ITimer.TimerUpdateListener() {
                @Override
                public void onTimeUpdate(float secondsRemaining, float secondsElapsed) {
                    data.setSecondsElapsedMem(secondsElapsed);
                    timerView.displayTime((int) secondsRemaining);
                }
                @Override
                public void onTimeCountdownComplete() {
                    setGamePhase(GamePhase.RECALL);
                }
            });
        }
        if (phase == GamePhase.MEMORIZATION) {
            timer.start();
        }
        else if (phase == GamePhase.RECALL) {
            timer.cancel();
            timer = new SimpleTimer(settings.getTimeLimitInSecondsForRecall(), new ITimer.TimerUpdateListener() {
                @Override
                public void onTimeUpdate(float secondsRemaining, float secondsElapsed) {
                    data.setSecondsElapsedRecall(secondsElapsed);
                    timerView.displayTime((int) secondsRemaining);
                }
                @Override
                public void onTimeCountdownComplete() {
                    setGamePhase(GamePhase.REVIEW);
                }
            });

            data.setNumCardsPerGroup(1);
            timer.start();
        }
        else if (phase == GamePhase.REVIEW) {
            timer.cancel();
            float memTime = data.getSecondsElapsedMem();
            Score score = data.getScore();
            data.saveScore(score, context);
            scorePanel.show(score, memTime, data.getPastScores(context));

            if (data.getMode() == Constants.STEPS && isLevelUp()) {
                doLevelUp();
                showLevelUpDialog();
            }
        }

        data.setGamePhase(phase);
        refreshVisibleComponentsForPhase(phase);
        displayDeck(0);
    }

    private boolean isLevelUp() {
        int score = data.getScore().score;
        double target = Utils.getTargetScore(data.getGameType(), data.getStep());
        return score >= target && data.getStep() <= 4;
    }

    private void doLevelUp() {
        SharedPreferences settings = context.getSharedPreferences("Steps", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(Constants.getGameName(Constants.CARDS_LONG), data.getStep() + 1);
        editor.apply();
    }

    private void showLevelUpDialog() {
        int step = data.getStep();

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        builder.setTitle("You passed step " + step + "!");
        builder.setMessage("Would you like to continue to step " + (step + 1) + "?");

        String positiveText = "Step " + (step + 1);
        builder.setPositiveButton(positiveText,
                (dialog, which) -> {
                    Intent i = new Intent();
                    i.putExtra("gameType", data.getGameType());
                    i.putExtra("mode", data.getMode());
                    i.setClass(context, TestDetailsActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                });

        String negativeText = "Cancel";
        builder.setNegativeButton(negativeText, (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /* Start Game */
    @OnClick(R.id.button_start) void startGame() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            CountDownDialog count = new CountDownDialog(context);
            count.setOnDismissListener(dialog -> setGamePhase(GamePhase.MEMORIZATION));
            count.show();
        }, 500);

        refreshVisibleComponentsForPhase(GamePhase.MEMORIZATION);
    }

    /* Highlight position adjustment */
    @OnClick({R.id.button_prev_group, R.id.button_prev_group_alt}) void prevGroup() { setFocusAt(data.getHighlightPosition() - data.getNumCardsPerGroup()); }
    @OnClick({R.id.button_next_group, R.id.button_next_group_alt}) void nextGroup() { setFocusAt(data.getHighlightPosition() + data.getNumCardsPerGroup()); }

    @Override
    public void onCardClick(int index, PlayingCard card) {
        if (data.getGamePhase() == GamePhase.MEMORIZATION) {
            setFocusAt(index);
        }
        else if (data.getGamePhase() == GamePhase.RECALL) {
            if (card == null) {
                setFocusAt(index);
            }
            else {
                removeCardFromRecall(index, card);
            }
        }
    }

    /* Selection of new deck */
    @Override public void onPrevDeck() {
        onDeckSelected(data.getDeckNum() - 1);
    }
    @Override public void onNextDeck() { onDeckSelected(data.getDeckNum() + 1); }
    private void onDeckSelected(int newDeckNum) {
        if (newDeckNum < 0 || newDeckNum >= data.getNumDecks()) return;

        data.setDeckNum(newDeckNum);
        displayDeck(newDeckNum);
    }

    /* Selection of Suit */
    @Override public void onSuitSelected(int suit) {
        suitSelectorView.renderSuits(suit);
        cardSelectorView.renderCards(data.getRecallEntryDeck(suit, data.getDeckNum()));
    }

    @Override
    public void onCardSelected(PlayingCard card) {
        Deck recallDeck = data.getRecallDeck(data.getDeckNum()); // "Top" Deck
        Deck cardSelectionDeck = data.getRecallEntryDeck(card.suit, data.getDeckNum()); // "Bottom" Deck

        /* Remove selected card from bottom */
        cardSelectionDeck.removeCard(card);
        cardSelectorView.removeCard(card);

        /* Remove existing card from the top in the case of an overwrite */
        PlayingCard replacedCard = recallDeck.getCard(data.getHighlightPosition());
        if (replacedCard != null) {
            data.getRecallEntryDeck(replacedCard.suit, data.getDeckNum()).addCard(replacedCard); // Transfer card from top to bottom
            if (replacedCard.suit == card.suit) {
                cardSelectorView.addCard(replacedCard); // If the card arriving in the bottom will be visible, animate its entry
            }
        }

        /* Add selected card to top */
        recallDeck.setCard(data.getHighlightPosition(), card);
        deckView.renderCards(recallDeck);

        nextGroup();
    }

    /* Remove card from top, transfer to bottom */
    private void removeCardFromRecall(int index, PlayingCard card) {
        Deck recallDeck = data.getRecallDeck(data.getDeckNum());
        Deck cardSelectionDeck = data.getRecallEntryDeck(card.suit, data.getDeckNum());

        recallDeck.setCard(index, null);
        deckView.renderCards(recallDeck);
        setFocusAt(index);

        cardSelectionDeck.addCard(card);
        if (card.suit == suitSelectorView.getSelectedSuit()) {
            cardSelectorView.addCard(card);
        }
    }

    private void displayDeck(int deckNum) {
        data.setDeckNum(deckNum);

        if (data.getNumDecks() == 1) {
            deckSelectorView.setVisibility(View.GONE);
        }
        else {
            deckSelectorView.displayDeckNumber(deckNum + 1, data.getNumDecks());
        }

        if (data.getGamePhase() == GamePhase.PRE_MEMORIZATION) {
            deckView.clear();
            deckView.renderCards(data.getRecallDeck(0));
        }
        if (data.getGamePhase() == GamePhase.MEMORIZATION) {
            deckView.renderCards(data.getMemoryDeck(deckNum));
            setFocusAt(0);
        }
        else if (data.getGamePhase() == GamePhase.RECALL) {
            deckView.renderCards(data.getRecallDeck(deckNum));
            suitSelectorView.renderSuits(PlayingCard.HEART);
            cardSelectorView.renderCards(data.getRecallEntryDeck(PlayingCard.HEART, deckNum));
            setFocusAt(0);
        }
        else if (data.getGamePhase() == GamePhase.REVIEW) {
            deckView.renderCards(data.getMemoryDeck(deckNum), data.getRecallDeck(deckNum));
        }
    }

    private void setFocusAt(int index) {
        if (index < 0 || index >= data.getDeckSize())
            index = data.getHighlightPosition();

        data.setHighlightPosition(index);
        int endIndex = lesserOf(index + data.getNumCardsPerGroup(), data.getDeckSize());

        deckView.highlightCards(index, endIndex);

        if (data.getGamePhase() == GamePhase.MEMORIZATION) {
            selectedCardsView.renderCards(data.getMemoryDeckSubset(index, endIndex));
        }
        else if (data.getGamePhase() == GamePhase.RECALL) {
            selectedCardsView.renderCards(data.getRecallDeckSubset(index, endIndex));
        }
    }

    private int lesserOf(int a, int b) {
        return a < b ? a : b;
    }

    private void refreshVisibleComponentsForPhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            suitSelectorView.setVisibility(View.GONE);
            cardSelectorView.setVisibility(View.GONE);
            deckView.renderCards(data.getRecallDeck(0));
            selectedCardsView.setVisibility(View.GONE);
            selectedCardsView.setCardDisplayCount(0);
            navigatorButtons.setVisibility(View.GONE);
            startButtonLayout.setVisibility(View.VISIBLE);
            scorePanel.hide();
            timerView.show();
        }
        else if (phase == GamePhase.MEMORIZATION) {
            prevGroupButton.setVisibility(View.GONE);
            nextGroupButton.setVisibility(View.GONE);
            suitSelectorView.setVisibility(View.GONE);
            cardSelectorView.setVisibility(View.GONE);
            selectedCardsView.setVisibility(View.VISIBLE);
            navigatorButtons.setVisibility(View.VISIBLE);
            startButtonLayout.setVisibility(View.GONE);
            scorePanel.hide();
        }
        else if (phase == GamePhase.RECALL) {
            navigatorButtons.setVisibility(View.GONE);
            prevGroupButton.setVisibility(View.VISIBLE);
            nextGroupButton.setVisibility(View.VISIBLE);
            suitSelectorView.setVisibility(View.VISIBLE);
            cardSelectorView.setVisibility(View.VISIBLE);
            scorePanel.hide();
        }
        else if (phase == GamePhase.REVIEW) {
            suitSelectorView.setVisibility(View.GONE);
            cardSelectorView.setVisibility(View.GONE);
            navigatorButtons.setVisibility(View.GONE);
            selectedCardsView.setVisibility(View.GONE);
            timerView.hide();
        }

        displayMenuItemsForPhase(phase);
    }

    void setMenuItems(MenuItem finishMem, MenuItem finishRecall, MenuItem playAgain) {
        this.finishMem = finishMem;
        this.finishRecall = finishRecall;
        this.playAgain = playAgain;
    }

    private void displayMenuItemsForPhase(GamePhase phase) {
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

    void resume() {
        if (timer != null && data.getGamePhase() == GamePhase.MEMORIZATION || data.getGamePhase() == GamePhase.MEMORIZATION) {
            timer.start();
        }
    }

    void pause() {
        if (timer != null && data.getGamePhase() == GamePhase.MEMORIZATION || data.getGamePhase() == GamePhase.MEMORIZATION) {
            timer.pause();
        }
    }
}
