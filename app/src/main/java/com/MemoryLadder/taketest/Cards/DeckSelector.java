package com.memoryladder.taketest.cards;

public interface DeckSelector {

    interface Presenter {
        void onPrevDeck();
        void onNextDeck();
    }

    interface View {
        void setDeckSelectionListener(DeckSelector.Presenter presenter);
        void displayDeckNumber(int deckNum, int numDecks);
    }
}
