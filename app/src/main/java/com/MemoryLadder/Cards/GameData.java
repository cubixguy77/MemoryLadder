package com.MemoryLadder.Cards;

import android.content.Context;

import com.MemoryLadder.Cards.ScorePanel.Score;
import com.MemoryLadder.Cards.ScorePanel.ScoreCalculation;
import com.MemoryLadder.FileOps;
import com.jjoe64.graphview.series.DataPoint;

import java.util.List;

class GameData {

    /* Settings */
    private final int mode;
    private final int gameType;
    private final int step;
    private int numDecks;
    private int deckSize;
    private int numCardsPerGroup;
    private boolean shuffle;

    /* Dynamic State Variables */
    private int deckNum;
    private int highlightPosition;
    private GamePhase gamePhase;
    private float secondsElapsedMem;
    private float secondsElapsedRecall;

    /* Data Structures */
    private Deck[] memoryDecks;
    private Deck[] recalledDecks;
    private Deck[][] recallEntryDecks;

    private Score score;
    private DataPoint[] pastScores;

    GameData(CardSettings settings) {
        this.mode = settings.getMode();
        this.gameType = settings.getGameType();
        this.step = settings.getStep();

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

    public int getDeckSize() {
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
        if (this.score == null) {
            this.score = ScoreCalculation.getScore(recalledDecks, memoryDecks);
        }

        return this.score;
    }

    Score saveScore(Score score, Context context) {
        new FileOps(context).updatePastScores(mode, gameType, Integer.toString(score.score));
        return this.score;
    }

    void setSecondsElapsedMem(float secondsElapsedMem) {
        this.secondsElapsedMem = secondsElapsedMem;
    }

    float getSecondsElapsedMem() {
        return secondsElapsedMem;
    }

    DataPoint[] getPastScores(Context context) {
        if (pastScores == null) {
            pastScores = new FileOps(context).readPastScoresToDataPoints(mode, gameType);
        }

        return pastScores;
    }

    public int getStep() {
        return step;
    }

    public int getGameType() {
        return gameType;
    }

    public int getMode() {
        return mode;
    }

    public void setSecondsElapsedRecall(float secondsElapsedRecall) {
        this.secondsElapsedRecall = secondsElapsedRecall;
    }
}
