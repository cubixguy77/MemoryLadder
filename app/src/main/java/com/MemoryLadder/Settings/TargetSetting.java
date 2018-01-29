package com.MemoryLadder.Settings;

import android.os.Parcel;

public class TargetSetting extends Setting {

    TargetSetting(String key, String label, int value) {
        this(key, "", label, value, true);
    }

    TargetSetting(String key, String settingName, String label, int value, boolean display) {
        super(key, settingName, label, value, display);
    }

    @Override
    public String getDisplayValue() {
        return Integer.toString(value);
    }

    private TargetSetting(Parcel in) {
        super(in);
    }

    public static final Creator<TargetSetting> CREATOR = new Creator<TargetSetting>() {
        @Override
        public TargetSetting createFromParcel(Parcel in) {
            return new TargetSetting(in);
        }

        @Override
        public TargetSetting[] newArray(int size) {
            return new TargetSetting[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public int describeContents() {
        return 0;
    }
}
