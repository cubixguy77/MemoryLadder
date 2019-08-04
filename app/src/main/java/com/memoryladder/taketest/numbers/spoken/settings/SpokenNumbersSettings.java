package com.memoryladder.taketest.numbers.spoken.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class SpokenNumbersSettings implements Parcelable {

    private int numRows;
    private int numCols;
    private int digitSpeed;
    //private int digitsPerGroup;
    //private boolean mnemonicsEnabled;
    //private int base;
    private boolean nightMode;
    private boolean drawGridLines;

    public SpokenNumbersSettings(int numRows, int numCols, int digitSpeed, boolean nightMode, boolean drawGridLines) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.digitSpeed = digitSpeed;
        //this.digitsPerGroup = digitsPerGroup;
        //this.mnemonicsEnabled = isMnemonicsEnabled;
        //this.base = base;
        this.nightMode = nightMode;
        this.drawGridLines = drawGridLines;
    }

    int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public int getDigitCount() { return getNumRows() * getNumCols(); }

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

    private SpokenNumbersSettings(Parcel in) {
        numRows = in.readInt();
        numCols = in.readInt();
        //digitsPerGroup = in.readInt();
        //mnemonicsEnabled = in.readByte() != 0x00;
        //base = in.readInt();
        nightMode = in.readByte() != 0x00;
        drawGridLines = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numRows);
        dest.writeInt(numCols);
        //dest.writeInt(digitsPerGroup);
        //dest.writeByte((byte) (mnemonicsEnabled ? 0x01 : 0x00));
        //dest.writeInt(base);
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