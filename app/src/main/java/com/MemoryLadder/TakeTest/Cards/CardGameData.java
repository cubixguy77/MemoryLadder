package com.MemoryLadder.TakeTest.Cards;

import android.os.Parcel;
import android.os.Parcelable;

import com.MemoryLadder.TakeTest.ScorePanel.Score;
import com.MemoryLadder.TakeTest.GamePhase;

import java.util.List;

class CardGameData implements Parcelable {

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
    private boolean keepHighlightPos;
    private boolean keepDeckNum;

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

    public void setKeepHighlightPos(boolean keepHighlightPos) {        this.keepHighlightPos = keepHighlightPos;    }
    public boolean isKeepHighlightPos() {        return keepHighlightPos;    }

    public void setKeepDeckNum(boolean keepDeckNum) {        this.keepDeckNum = keepDeckNum;    }
    public boolean isKeepDeckNum() {        return keepDeckNum;    }

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

    private CardGameData(Parcel in) {
        numDecks = in.readInt();
        deckSize = in.readInt();
        numCardsPerGroup = in.readInt();
        shuffle = in.readByte() != 0x00;
        deckNum = in.readInt();
        highlightPosition = in.readInt();
        gamePhase = (GamePhase) in.readValue(GamePhase.class.getClassLoader());

        memoryDecks = in.createTypedArray(Deck.CREATOR);
        recalledDecks = in.createTypedArray(Deck.CREATOR);

        recallEntryDecks = new Deck[numDecks][4];
        for (int deckNum=0; deckNum<numDecks; deckNum++) {
            recallEntryDecks[deckNum] = in.createTypedArray(Deck.CREATOR);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numDecks);
        dest.writeInt(deckSize);
        dest.writeInt(numCardsPerGroup);
        dest.writeByte((byte) (shuffle ? 0x01 : 0x00));
        dest.writeInt(deckNum);
        dest.writeInt(highlightPosition);
        dest.writeValue(gamePhase);

        dest.writeTypedArray(memoryDecks, 0);
        dest.writeTypedArray(recalledDecks, 0);

        for (int deckNum=0; deckNum<numDecks; deckNum++) {
            dest.writeTypedArray(recallEntryDecks[deckNum], 0);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CardGameData> CREATOR = new Parcelable.Creator<CardGameData>() {
        @Override
        public CardGameData createFromParcel(Parcel in) {
            return new CardGameData(in);
        }

        @Override
        public CardGameData[] newArray(int size) {
            return new CardGameData[size];
        }
    };
}