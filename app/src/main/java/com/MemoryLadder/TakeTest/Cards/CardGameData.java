package com.MemoryLadder.TakeTest.Cards;

import com.MemoryLadder.TakeTest.ScorePanel.Score;
import com.MemoryLadder.TakeTest.GamePhase;

import java.util.List;

class CardGameData {

    /* Settings */
    private int numDecks;
    private int deckSize;
    private int numCardsPerGroup;
    private boolean shuffle;

    /* Dynamic State Variables */
    private int deckNum;
    private int highlightPosition;
    private GamePhase gamePhase;

    /* Data Structures */
    private Deck[] memoryDecks;
    private Deck[] recalledDecks;
    private Deck[][] recallEntryDecks;

    CardGameData(CardSettings settings) {
        this.numDecks = settings.getNumDecks();
        this.deckSize = settings.getDeckSize();
        this.numCardsPerGroup = settings.getNumCardsPerGroup();
        this.shuffle = settings.isShuffle();

        this.deckNum = 0;
        this.highlightPosition = 0;

        initDataModels();

        setGamePhase(GamePhase.PRE_MEMORIZATION);
    }

    int getDeckNum() {
        return deckNum;
    }

    void setDeckNum(int deckNum) {
        this.deckNum = deckNum;
    }

    int getNumDecks() {
        return numDecks;
    }

    GamePhase getGamePhase() {
        return gamePhase;
    }

    Deck getMemoryDeck(int deckNum) {
        return memoryDecks[deckNum];
    }

    Deck getRecallDeck(int deckNum) {
        return recalledDecks[deckNum];
    }

    Deck getRecallEntryDeck(int suit, int deckNum) {
        return recallEntryDecks[deckNum][suit];
    }

    int getDeckSize() {
        return deckSize;
    }

    void setHighlightPosition(int highlightPosition) {
        this.highlightPosition = highlightPosition;
    }

    int getNumCardsPerGroup() {
        return numCardsPerGroup;
    }

    void setNumCardsPerGroup(int numCardsPerGroup) { this.numCardsPerGroup = numCardsPerGroup; }

    List<PlayingCard> getMemoryDeckSubset(int index, int endIndex) {
        return memoryDecks[deckNum].subList(index, endIndex);
    }

    List<PlayingCard> getRecallDeckSubset(int index, int endIndex) {
        return recalledDecks[deckNum].subList(index, endIndex);
    }

    int getHighlightPosition() {
        return highlightPosition;
    }

    private void initDataModels() {
        memoryDecks = new Deck[numDecks];
        for (int deckNum = 0; deckNum < memoryDecks.length; deckNum++) {
            memoryDecks[deckNum] = new Deck(deckSize, shuffle);
        }

        recallEntryDecks = new Deck[numDecks][4];
        recalledDecks = new Deck[numDecks];

        for (int deckNum = 0; deckNum < numDecks; deckNum++) {
            recallEntryDecks[deckNum][PlayingCard.HEART] = new Deck(PlayingCard.HEART);
            recallEntryDecks[deckNum][PlayingCard.DIAMOND] = new Deck(PlayingCard.DIAMOND);
            recallEntryDecks[deckNum][PlayingCard.CLUB] = new Deck(PlayingCard.CLUB);
            recallEntryDecks[deckNum][PlayingCard.SPADE] = new Deck(PlayingCard.SPADE);

            recalledDecks[deckNum] = new Deck(PlayingCardFactory.generateEmptyDeck(deckSize));
        }
    }

    void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public Score getScore() {
        return ScoreCalculation.getScore(recalledDecks, memoryDecks);
    }
}
