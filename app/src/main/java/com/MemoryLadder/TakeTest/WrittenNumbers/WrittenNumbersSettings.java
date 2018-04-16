package com.MemoryLadder.TakeTest.WrittenNumbers;

import android.os.Parcel;
import android.os.Parcelable;

public class WrittenNumbersSettings implements Parcelable {

    private int numRows;
    private int numCols;
    private int digitsPerGroup;
    private boolean mnemonicsEnabled;
    private boolean nightMode;
    private boolean drawGridLines;

    public WrittenNumbersSettings(int numRows, int numCols, int digitsPerGroup, boolean isMnemonicsEnabled, boolean nightMode, boolean drawGridLines) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.digitsPerGroup = digitsPerGroup;
        this.mnemonicsEnabled = isMnemonicsEnabled;
        this.nightMode = nightMode;
        this.drawGridLines = drawGridLines;
    }

    int getNumRows() {
        return numRows;
    }

    int getNumCols() {
        return numCols;
    }

    int getNumDigits() { return getNumRows() * getNumCols(); }

    int getDigitsPerGroup() {
        return digitsPerGroup;
    }

    boolean isMnemonicsEnabled() {
        return mnemonicsEnabled;
    }

    public void setNightMode(boolean nightMode) {        this.nightMode = nightMode;    }
    boolean isNightMode() {
        return this.nightMode;
    }

    public void setDrawGridLines(boolean drawGridLines) {        this.drawGridLines = drawGridLines;    }
    boolean isDrawGridLines() {
        return this.drawGridLines;
    }

    private WrittenNumbersSettings(Parcel in) {
        numRows = in.readInt();
        numCols = in.readInt();
        digitsPerGroup = in.readInt();
        mnemonicsEnabled = in.readByte() != 0x00;
        nightMode = in.readByte() != 0x00;
        drawGridLines = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numRows);
        dest.writeInt(numCols);
        dest.writeInt(digitsPerGroup);
        dest.writeByte((byte) (mnemonicsEnabled ? 0x01 : 0x00));
        dest.writeByte((byte) (nightMode ? 0x01 : 0x00));
        dest.writeByte((byte) (drawGridLines ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<WrittenNumbersSettings> CREATOR = new Creator<WrittenNumbersSettings>() {
        @Override
        public WrittenNumbersSettings createFromParcel(Parcel in) {
            return new WrittenNumbersSettings(in);
        }

        @Override
        public WrittenNumbersSettings[] newArray(int size) {
            return new WrittenNumbersSettings[size];
        }
    };
}