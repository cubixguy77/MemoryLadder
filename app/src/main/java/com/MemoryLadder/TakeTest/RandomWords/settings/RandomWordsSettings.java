package com.memoryladder.taketest.randomwords.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class RandomWordsSettings implements Parcelable {

    private final int columnCount;
    private final int wordsPerColumn;

    public RandomWordsSettings(int columnCount, int wordsPerColumn) {
        this.columnCount = columnCount;
        this.wordsPerColumn = wordsPerColumn;
    }

    public int getWordCount() { return this.columnCount * this.wordsPerColumn; }
    public int getWordsPerColumn() { return wordsPerColumn; }
    public int getColumnCount() { return columnCount; }


    private RandomWordsSettings(Parcel in) {
        columnCount = in.readInt();
        wordsPerColumn = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(columnCount);
        dest.writeInt(wordsPerColumn);
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