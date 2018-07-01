package com.MemoryLadder.TakeTest.RandomWords.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class RandomWordsSettings implements Parcelable {

    private final int columnCount;
    private final int wordsPerColumn;
    private boolean nightMode;

    public RandomWordsSettings(int columnCount, int wordsPerColumn, boolean nightMode) {
        this.columnCount = columnCount;
        this.wordsPerColumn = wordsPerColumn;
        this.nightMode = nightMode;
    }

    public int getWordCount() { return this.columnCount * this.wordsPerColumn; }
    public int getWordsPerColumn() { return wordsPerColumn; }
    public int getColumnCount() { return columnCount; }

    public boolean isNightMode() {
        return this.nightMode;
    }

    private RandomWordsSettings(Parcel in) {
        columnCount = in.readInt();
        wordsPerColumn = in.readInt();
        nightMode = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(columnCount);
        dest.writeInt(wordsPerColumn);
        dest.writeByte((byte) (nightMode ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RandomWordsSettings> CREATOR = new Parcelable.Creator<RandomWordsSettings>() {
        @Override
        public RandomWordsSettings createFromParcel(Parcel in) {
            return new RandomWordsSettings(in);
        }

        @Override
        public RandomWordsSettings[] newArray(int size) {
            return new RandomWordsSettings[size];
        }
    };
}