package com.MemoryLadder.Cards;

class GeneralSettings {

    private final int mode;
    private final int gameType;
    private int step;

    GeneralSettings(int mode, int gameType, int step) {
        this.mode = mode;
        this.gameType = gameType;
        this.step = step;
    }

    public int getMode() {
        return mode;
    }

    public int getGameType() {
        return gameType;
    }

    public int getStep() {
        return step;
    }
}
