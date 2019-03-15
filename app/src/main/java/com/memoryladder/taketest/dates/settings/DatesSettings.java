package com.memoryladder.taketest.dates.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class DatesSettings implements Parcelable {

    private final int dateCount;
    private final boolean fullDateList;

    public DatesSettings(int dateCount, boolean fullDateList) {
        this.dateCount = dateCount;
        this.fullDateList = fullDateList;
    }

    public int getDateCount() { return dateCount; }
    public boolean isFullDateList() { return fullDateList; }

    private DatesSettings(Parcel in) {
        dateCount = in.readInt();
        fullDateList = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dateCount);
        dest.writeByte((byte) (fullDateList ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Creator<DatesSettings> CREATOR = new Creator<DatesSettings>() {
        @Override
        public DatesSettings createFromParcel(Parcel in) {
            return new DatesSettings(in);
        }

        @Override
        public DatesSettings[] newArray(int size) {
            return new DatesSettings[size];
        }
    };
}