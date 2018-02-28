package com.MemoryLadder.Cards;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.MemoryLadder.Cards.ScorePanel.Score;
import com.MemoryLadder.Timer.TimerView;
import com.mastersofmemory.memoryladder.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameManager extends Fragment implements DeckSelector.Presenter, SuitSelectionListener, CardSelectionListener, CardClickListener {

    private GameData data;
    private CardSettings settings;
    private GameManagerActivity activity;

    @BindView(R.id.text_timer) TimerView timerView;
    @BindView(R.id.deck_view) DeckView deckView;
    @BindView(R.id.selected_cards_view) SelectedCardsView selectedCardsView;
    @BindView(R.id.layout_suit_selector) SuitSelectorView suitSelectorView;
    @BindView(R.id.card_selector_view) CardSelectionView cardSelectorView;
    @BindView(R.id.layout_deck_selector) DeckSelectorView deckSelectorView;

    @BindView(R.id.layout_bottom_navigator_buttons) FrameLayout navigatorButtons;
    @BindView(R.id.button_prev_group_alt) AppCompatImageButton prevGroupButton;
    @BindView(R.id.button_next_group_alt) AppCompatImageButton nextGroupButton;
    @BindView(R.id.layout_button_start) FrameLayout startButtonLayout;

    public static GameManager newInstance(CardSettings settings) {
        GameManager gameManager = new GameManager();

        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        gameManager.setArguments(args);

        return gameManager;
    }

    public GameManager() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.viewgroup_card_arena, container, false);
        ButterKnife.bind(this, view);

        settings = getArguments().getParcelable("settings");

        deckView.setListener(this);
        deckSelectorView.setDeckSelectionListener(this);
        suitSelectorView.setSuitSelectionListener(this);
        cardSelectorView.setCardSelectionListener(this);

        selectedCardsView.setMnemonicsEnabled(settings.isMnemonicsEnabled());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (GameManagerActivity) context;
        try {
            this.activity = (GameManagerActivity) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement GameManagerActivity");
        }
    }

    GamePhase getGamePhase() {
        return data.getGamePhase();
    }

    void setGamePhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            data = new GameData(settings);
        }
        if (phase == GamePhase.MEMORIZATION) {
        }
        else if (phase == GamePhase.RECALL) {
            data.setNumCardsPerGroup(1);
        }
        else if (phase == GamePhase.REVIEW) {
            //float memTime = data.getSecondsElapsedMem();


            Score score = data.getScore();

            //data.saveScore(score, context);

            /*
            scorePanel.show(score, memTime, data.getPastScores(context));

            if (data.getMode() == Constants.STEPS && isLevelUp()) {
                doLevelUp();
                showLevelUpDialog();
            }
            */
        }

        data.setGamePhase(phase);
        refreshVisibleComponentsForPhase(phase);
        displayDeck(0);
    }

    /*
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
    */


    /* Start Game */
    @OnClick(R.id.button_start) void startGame() {
        activity.onStartClicked();
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
            deckView.post(() -> deckView.renderCards(data.getRecallDeck(0)));
        }
        if (data.getGamePhase() == GamePhase.MEMORIZATION) {
            deckView.renderCards(data.getMemoryDeck(deckNum));
            setFocusAt(0);
        }
        else if (data.getGamePhase() == GamePhase.RECALL) {
            deckView.renderCards(data.getRecallDeck(deckNum));
            suitSelectorView.renderSuits(PlayingCard.HEART);
            cardSelectorView.post(() -> cardSelectorView.renderCards(data.getRecallEntryDeck(PlayingCard.HEART, deckNum)));

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

    public void refreshVisibleComponentsForPhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            suitSelectorView.setVisibility(View.GONE);
            cardSelectorView.setVisibility(View.GONE);
            deckView.setVisibility(View.VISIBLE);
            selectedCardsView.setVisibility(View.GONE);
            selectedCardsView.setCardDisplayCount(0);
            navigatorButtons.setVisibility(View.GONE);
            startButtonLayout.setVisibility(View.VISIBLE);
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
        }
        else if (phase == GamePhase.RECALL) {
            navigatorButtons.setVisibility(View.GONE);
            prevGroupButton.setVisibility(View.VISIBLE);
            nextGroupButton.setVisibility(View.VISIBLE);
            suitSelectorView.setVisibility(View.VISIBLE);
            cardSelectorView.setVisibility(View.VISIBLE);
        }
        else if (phase == GamePhase.REVIEW) {
            suitSelectorView.setVisibility(View.GONE);
            cardSelectorView.setVisibility(View.GONE);
            navigatorButtons.setVisibility(View.GONE);
            selectedCardsView.setVisibility(View.GONE);
            timerView.hide();
        }
    }

    public void displayTime(int secondsRemaining) {
        timerView.displayTime(secondsRemaining);
    }
}
