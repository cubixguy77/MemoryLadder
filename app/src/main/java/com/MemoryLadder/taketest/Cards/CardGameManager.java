package com.memoryladder.taketest.cards;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.memoryladder.taketest.GameManager;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.scorepanel.Score;
import com.memoryladder.taketest.timer.TimerView;
import com.memoryladder.Utils;
import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.ViewgroupCardArenaBinding;

public class CardGameManager extends Fragment implements GameManager, DeckSelector.Presenter, SuitSelectionListener, CardSelectionListener, CardClickListener {

    private CardGameData data;
    private CardSettings settings;

    private TimerView timerView;
    private DeckView deckView;
    private SelectedCardsView selectedCardsView;
    private SuitSelectorView suitSelectorView;
    private CardSelectionView cardSelectorView;
    private DeckSelectorView deckSelectorView;

    private FrameLayout navigatorButtons;
    private AppCompatImageButton prevGroupButton;
    private AppCompatImageButton nextGroupButton;

    public static CardGameManager newInstance(CardSettings settings) {
        CardGameManager cardGameManager = new CardGameManager();

        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        cardGameManager.setArguments(args);

        return cardGameManager;
    }

    public CardGameManager() {}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        this.data.setKeepHighlightPos(true);
        this.data.setKeepDeckNum(true);
        outState.putParcelable("gameData", this.data);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.mastersofmemory.memoryladder.databinding.ViewgroupCardArenaBinding binding = ViewgroupCardArenaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (getArguments() != null) {
            settings = getArguments().getParcelable("settings");
        }

        timerView = binding.timerContainer.findViewById(R.id.text_timer);
        deckView = binding.deckView;
        selectedCardsView = binding.selectedCardsView;
        suitSelectorView = binding.layoutSuitSelector;
        cardSelectorView = binding.cardSelectorView;
        deckSelectorView = binding.layoutDeckSelector;
        navigatorButtons = binding.layoutBottomNavigatorButtons.getRoot();
        prevGroupButton = binding.buttonPrevGroupAlt;
        nextGroupButton = binding.buttonNextGroupAlt;

        deckView.setListener(this);
        deckSelectorView.setDeckSelectionListener(this);
        suitSelectorView.setSuitSelectionListener(this);
        cardSelectorView.setCardSelectionListener(this);
        navigatorButtons.findViewById(R.id.prevButton).setOnClickListener(v -> prevGroup());
        prevGroupButton.setOnClickListener(v -> prevGroup());
        navigatorButtons.findViewById(R.id.nextButton).setOnClickListener(v -> nextGroup());
        nextGroupButton.setOnClickListener(v -> nextGroup());

        selectedCardsView.setMnemonicsEnabled(settings.isMnemonicsEnabled());

        return view;
    }

/*
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated()");

        if (savedInstanceState != null) {
            this.data = savedInstanceState.getParcelable("gameData");
        }
    }
*/

    @Override
    public void setGamePhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            data = new CardGameData(settings);
        }
        else if (phase == GamePhase.RECALL) {
            data.setNumCardsPerGroup(1);
        }

        data.setGamePhase(phase);
        render(phase);

        /* Restore previously selected deck following config change (e.g. rotation) */
        displayDeck(data.isKeepDeckNum() ? data.getDeckNum() : 0, data.isKeepHighlightPos() ? data.getHighlightPosition() : 0);
        data.setKeepDeckNum(false);
        data.setKeepHighlightPos(false);
    }

    @Override
    public void displayTime(int secondsRemaining) {
        timerView.displayTime(secondsRemaining);
    }

    @Override
    public void render(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            suitSelectorView.setVisibility(View.GONE);
            cardSelectorView.setVisibility(View.GONE);

            if (getResources().getBoolean(R.bool.cards_bottom_navigation_buttons_display)) {
                navigatorButtons.setVisibility(View.VISIBLE);
                prevGroupButton.setVisibility(View.GONE);
                nextGroupButton.setVisibility(View.GONE);
            } else {
                navigatorButtons.setVisibility(View.GONE);
                prevGroupButton.setVisibility(View.VISIBLE);
                nextGroupButton.setVisibility(View.VISIBLE);
            }

            timerView.show();
        }
        else if (phase == GamePhase.MEMORIZATION) {
            suitSelectorView.setVisibility(View.GONE);
            cardSelectorView.setVisibility(View.GONE);

            if (getResources().getBoolean(R.bool.cards_bottom_navigation_buttons_display)) {
                navigatorButtons.setVisibility(View.VISIBLE);
                prevGroupButton.setVisibility(View.GONE);
                nextGroupButton.setVisibility(View.GONE);
            } else {
                navigatorButtons.setVisibility(View.GONE);
                prevGroupButton.setVisibility(View.VISIBLE);
                nextGroupButton.setVisibility(View.VISIBLE);
            }
        }
        else if (phase == GamePhase.RECALL) {
            navigatorButtons.setVisibility(View.GONE);
            prevGroupButton.setVisibility(View.VISIBLE);
            nextGroupButton.setVisibility(View.VISIBLE);
            suitSelectorView.setVisibility(View.VISIBLE);
            cardSelectorView.setVisibility(View.VISIBLE);

            selectedCardsView.setVisibility(getResources().getBoolean(R.bool.cards_selected_view_display_in_recall) ? View.VISIBLE : View.GONE);
        }
        else if (phase == GamePhase.REVIEW) {
            suitSelectorView.setVisibility(View.GONE);
            cardSelectorView.setVisibility(View.GONE);
            navigatorButtons.setVisibility(View.GONE);
            selectedCardsView.setVisibility(View.GONE);
            timerView.hide();
        }

        deckSelectorView.setVisibility(data.getNumDecks() <= 1 ? View.GONE : View.VISIBLE);
    }

    @Override
    public Score getScore() {
        return data.getScore();
    }

    /* Highlight position adjustment */
    void prevGroup() { setFocusAt(data.getHighlightPosition() - data.getNumCardsPerGroup()); }
    void nextGroup() { setFocusAt(data.getHighlightPosition() + data.getNumCardsPerGroup()); }

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
        displayDeck(newDeckNum, 0);
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

    private void displayDeck(int deckNum, int highlightPos) {
        data.setDeckNum(deckNum);
        deckSelectorView.displayDeckNumber(deckNum + 1, data.getNumDecks());

        if (data.getGamePhase() == GamePhase.PRE_MEMORIZATION) {
            deckView.clear();
            deckView.post(() -> {
                deckView.renderCards(data.getRecallDeck(0));
                selectedCardsView.setCardDisplayCount(settings.getNumCardsPerGroup());
                selectedCardsView.setVisibility(View.VISIBLE);
                setFocusAt(highlightPos);
            });
        }
        if (data.getGamePhase() == GamePhase.MEMORIZATION) {
            deckView.renderCards(data.getMemoryDeck(deckNum));
            setFocusAt(highlightPos);
        }
        else if (data.getGamePhase() == GamePhase.RECALL) {
            deckView.renderCards(data.getRecallDeck(deckNum));
            suitSelectorView.renderSuits(PlayingCard.HEART);
            cardSelectorView.post(() -> cardSelectorView.renderCards(data.getRecallEntryDeck(PlayingCard.HEART, deckNum)));

            setFocusAt(highlightPos);
        }
        else if (data.getGamePhase() == GamePhase.REVIEW) {
            deckView.renderCards(data.getMemoryDeck(deckNum), data.getRecallDeck(deckNum));
            selectedCardsView.presentBlankCards(); // Resolves display issue where returning to Pre_Mem briefly shows the cards previously contained in the view
        }
    }

    private void setFocusAt(int index) {
        if (index < 0 || index >= data.getDeckSize())
            index = data.getHighlightPosition();

        data.setHighlightPosition(index);
        int endIndex = Utils.lesserOf(index + data.getNumCardsPerGroup(), data.getDeckSize());

        deckView.highlightCards(index, endIndex);

        if (data.getGamePhase() == GamePhase.PRE_MEMORIZATION) {
            selectedCardsView.presentBlankCards();
        }
        if (data.getGamePhase() == GamePhase.MEMORIZATION) {
            selectedCardsView.renderCards(data.getMemoryDeckSubset(index, endIndex));
        }
        else if (data.getGamePhase() == GamePhase.RECALL) {
            selectedCardsView.renderCards(data.getRecallDeckSubset(index, endIndex));
        }
    }
}