package com.MemoryLadder.Cards;

import android.os.Parcel;
import android.os.Parcelable;

class GeneralSettings implements Parcelable {

    private final int mode;
    private final int gameType;
    private int step;
    private int timeLimitInSeconds;
    private int timeLimitInSecondsForRecall;

    GeneralSettings(int mode, int gameType, int step, int timeLimitInSeconds, int timeLimitInSecondsForRecall) {
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

    public int getStep() {
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
    public static final Parcelable.Creator<GeneralSettings> CREATOR = new Parcelable.Creator<GeneralSettings>() {
        @Override
        public GeneralSettings createFromParcel(Parcel in) {
            return new GeneralSettings(in);
        }

        @Override
        public GeneralSettings[] newArray(int size) {
            return new GeneralSettings[size];
        }
    };

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mode);
        out.writeInt(gameType);
        out.writeInt(step);
        out.writeInt(timeLimitInSeconds);
        out.writeInt(timeLimitInSecondsForRecall);
    }

    private GeneralSettings(Parcel in) {
        mode = in.readInt();
        gameType = in.readInt();
        step = in.readInt();
        timeLimitInSeconds = in.readInt();
        timeLimitInSecondsForRecall = in.readInt();
    }
}
