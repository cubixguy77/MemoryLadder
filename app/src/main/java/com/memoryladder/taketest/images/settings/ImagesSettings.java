package com.memoryladder.taketest.images.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagesSettings implements Parcelable {

    private final int numRows;
    private final boolean fullImageDataSet;

    public ImagesSettings(int numRows, boolean fullImageDataSet) {
        this.numRows = numRows;
        this.fullImageDataSet = fullImageDataSet;
    }

    public int getRowCount() { return this.numRows; }

    public boolean isFullImageDataSet() {
        return fullImageDataSet;
    }

    private ImagesSettings(Parcel in) {
        numRows = in.readInt();
        fullImageDataSet = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numRows);
        dest.writeByte((byte) (fullImageDataSet ? 0x01 : 0x00));
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