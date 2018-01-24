package com.MemoryLadder.TestDetailsScreen;

import android.os.Parcel;
import android.os.Parcelable;

public class Setting implements Parcelable {
    String key;
    String label;
    int value;
    String displayValue;

    Setting(String key, String label, int value, String displayValue) {
        this.key = key;
        this.label = label;
        this.value = value;
        this.displayValue = displayValue;
    }

    protected Setting(Parcel in) {
        key = in.readString();
        label = in.readString();
        value = in.readInt();
        displayValue = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(label);
        dest.writeInt(value);
        dest.writeString(displayValue);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Setting> CREATOR = new Parcelable.Creator<Setting>() {
        @Override
        public Setting createFromParcel(Parcel in) {
            return new Setting(in);
        }

        @Override
        public Setting[] newArray(int size) {
            return new Setting[size];
        }
    };
}