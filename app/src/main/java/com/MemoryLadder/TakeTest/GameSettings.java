package com.MemoryLadder.TakeTest;

import android.os.Parcel;
import android.os.Parcelable;

class GameSettings implements Parcelable {

    private final int mode;
    private final int gameType;
    private int step;
    private int timeLimitInSeconds;
    private int timeLimitInSecondsForRecall;

    GameSettings(int mode, int gameType, int step, int timeLimitInSeconds, int timeLimitInSecondsForRecall) {
        this.mode = mode;
        this.gameType = gameType;
        this.step = step;
        this.timeLimitInSeconds = timeLimitInSeconds;
        this.timeLimitInSecondsForRecall = timeLimitInSecondsForRecall;
    }

    public int getMode() {
        return mode;
    }

    public int getGameType() {
        return gameType;
    }

    int getStep() {
        return step;
    }

    int getTimeLimitInSeconds() {
        return timeLimitInSeconds;
    }

    int getTimeLimitInSecondsForRecall() {
        return timeLimitInSecondsForRecall;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GameSettings> CREATOR = new Parcelable.Creator<GameSettings>() {
        @Override
        public GameSettings createFromParcel(Parcel in) {
            return new GameSettings(in);
        }

        @Override
        public GameSettings[] newArray(int size) {
            return new GameSettings[size];
        }
    };

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mode);
        out.writeInt(gameType);
        out.writeInt(step);
        out.writeInt(timeLimitInSeconds);
        out.writeInt(timeLimitInSecondsForRecall);
    }

    private GameSettings(Parcel in) {
        mode = in.readInt();
        gameType = in.readInt();
        step = in.readInt();
        timeLimitInSeconds = in.readInt();
        timeLimitInSecondsForRecall = in.readInt();
    }
}
