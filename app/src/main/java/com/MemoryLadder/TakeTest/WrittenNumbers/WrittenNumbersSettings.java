package com.MemoryLadder.TakeTest.WrittenNumbers;

import android.os.Parcel;
import android.os.Parcelable;

public class WrittenNumbersSettings implements Parcelable {

    private int numRows;
    private int numCols;
    private int digitsPerGroup;
    private boolean mnemonicsEnabled;

    public WrittenNumbersSettings(int numRows, int numCols, int digitsPerGroup, boolean isMnemonicsEnabled) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.digitsPerGroup = digitsPerGroup < 0 ? 2 : digitsPerGroup;
        this.mnemonicsEnabled = isMnemonicsEnabled;
    }

    int getNumRows() {
        return numRows;
    }

    int getNumCols() {
        return numCols;
    }

    int getDigitsPerGroup() {
        return digitsPerGroup;
    }

    boolean isMnemonicsEnabled() {
        return mnemonicsEnabled;
    }

    private WrittenNumbersSettings(Parcel in) {
        numRows = in.readInt();
        numCols = in.readInt();
        digitsPerGroup = in.readInt();
        mnemonicsEnabled = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numRows);
        dest.writeInt(numCols);
        dest.writeInt(digitsPerGroup);
        dest.writeByte((byte) (mnemonicsEnabled ? 0x01 : 0x00));
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