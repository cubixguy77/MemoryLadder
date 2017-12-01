package com.MemoryLadder.Cards;

class CardSettings extends GeneralSettings {

    private int numDecks;
    private int deckSize;
    private int numCardsPerGroup;
    private boolean shuffle;
    private int timeLimitInSeconds;
    private int timeLimitInSecondsForRecall;
    private boolean mnemonicsEnabled;

    CardSettings(int mode, int gameType, int step, int numDecks, int deckSize, int numCardsPerGroup, boolean shuffle, int timeLimitInSeconds, int timeLimitInSecondsForRecall, boolean isMnemonicsEnabled) {
        super(mode, gameType, step);
        this.numDecks = numDecks;
        this.deckSize = deckSize;
        this.numCardsPerGroup = numCardsPerGroup;
        this.shuffle = shuffle;
        this.timeLimitInSeconds = timeLimitInSeconds;
        this.timeLimitInSecondsForRecall = timeLimitInSecondsForRecall;
        this.mnemonicsEnabled = isMnemonicsEnabled;
    }

    int getNumDecks() {
        return numDecks;
    }

    int getDeckSize() {
        return deckSize;
    }

    int getNumCardsPerGroup() {
        return numCardsPerGroup;
    }

    boolean isShuffle() {
        return shuffle;
    }

    int getTimeLimitInSeconds() {
        return timeLimitInSeconds;
    }

    int getTimeLimitInSecondsForRecall() {
        return timeLimitInSecondsForRecall;
    }

    boolean isMnemonicsEnabled() {
        return mnemonicsEnabled;
    }
}
