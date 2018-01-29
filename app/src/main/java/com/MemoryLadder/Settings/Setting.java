package com.MemoryLadder.Settings;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class Setting implements Parcelable {
    public String key;
    public String settingName;
    public String label;
    public int value;
    public boolean display;

    Setting(String key, String settingName, String label, int value, boolean display) {
        this.key = key;
        this.settingName = settingName;
        this.label = label;
        this.value = value;
        this.display = display;
    }

    public abstract String getDisplayValue();

    Setting(Parcel in) {
        key = in.readString();
        settingName = in.readString();
        label = in.readString();
        value = in.readInt();
        display = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(settingName);
        dest.writeString(label);
        dest.writeInt(value);
        dest.writeByte((byte) (display ? 1 : 0));
    }
}