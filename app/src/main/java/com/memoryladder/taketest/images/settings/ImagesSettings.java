package com.memoryladder.taketest.images.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagesSettings implements Parcelable {

    private final int numRows;

    public ImagesSettings(int numRows) {
        this.numRows = numRows;
    }

    public int getRowCount() { return this.numRows; }

    private ImagesSettings(Parcel in) {
        numRows = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numRows);
    }

    @SuppressWarnings("unused")
    public static final Creator<ImagesSettings> CREATOR = new Creator<ImagesSettings>() {
        @Override
        public ImagesSettings createFromParcel(Parcel in) {
            return new ImagesSettings(in);
        }

        @Override
        public ImagesSettings[] newArray(int size) {
            return new ImagesSettings[size];
        }
    };
}