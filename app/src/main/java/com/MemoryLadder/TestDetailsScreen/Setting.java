package com.MemoryLadder.TestDetailsScreen;

import android.os.Parcel;
import android.os.Parcelable;

public class Setting implements Parcelable {
    String key;
    String label;
    float value;
    String displayValue;
    boolean display;

    Setting(String key, String label, float value, String displayValue) {
        this(key, label, value, displayValue, true);
    }

    Setting(String key, String label, float value, String displayValue, boolean display) {
        this.key = key;
        this.label = label;
        this.value = value;
        this.displayValue = displayValue;
        this.display = display;
    }

    protected Setting(Parcel in) {
        key = in.readString();
        label = in.readString();
        value = in.readFloat();
        displayValue = in.readString();
        display = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(label);
        dest.writeFloat(value);
        dest.writeString(displayValue);
        dest.writeByte((byte) (display ? 1 : 0));
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