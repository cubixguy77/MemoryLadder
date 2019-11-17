package com.memoryladder.taketest.numbers.spoken.settings;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class SpokenNumbersSettings implements Parcelable {
    private int digitCount;
    private int numRows;
    private int numCols;
    private int digitSpeed;
    private boolean nightMode;
    private boolean drawGridLines;
    private Locale locale;

    public SpokenNumbersSettings(int numRows, int numCols, int digitSpeed, boolean nightMode, boolean drawGridLines, Locale locale) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.digitCount = numRows * numCols;
        this.digitSpeed = digitSpeed;
        this.nightMode = nightMode;
        this.drawGridLines = drawGridLines;
        this.locale = locale;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getDigitCount() { return digitCount; }

    public int getDigitSpeed() {
        return this.digitSpeed;
    }

    public void setNightMode(boolean nightMode) {        this.nightMode = nightMode;    }
    public boolean isNightMode() {
        return this.nightMode;
    }

    public void setDrawGridLines(boolean drawGridLines) {        this.drawGridLines = drawGridLines;    }
    public boolean isDrawGridLines() {
        return this.drawGridLines;
    }

    public Locale getLocale() {
        return locale;
    }

    private SpokenNumbersSettings(Parcel in) {
        digitCount = in.readInt();
        numCols = in.readInt();
        digitSpeed = in.readInt();
        nightMode = in.readByte() != 0x00;
        drawGridLines = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(digitCount);
        dest.writeInt(numCols);
        dest.writeInt(digitSpeed);
        dest.writeByte((byte) (nightMode ? 0x01 : 0x00));
        dest.writeByte((byte) (drawGridLines ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<SpokenNumbersSettings> CREATOR = new Creator<SpokenNumbersSettings>() {
        @Override
        public SpokenNumbersSettings createFromParcel(Parcel in) {
            return new SpokenNumbersSettings(in);
        }

        @Override
        public SpokenNumbersSettings[] newArray(int size) {
            return new SpokenNumbersSettings[size];
        }
    };


}