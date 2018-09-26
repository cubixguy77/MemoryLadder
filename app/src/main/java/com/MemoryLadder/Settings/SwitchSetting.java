package com.memoryladder.settings;

import android.os.Parcel;

public class SwitchSetting extends Setting {

    SwitchSetting(String key, String label, int value) {
        this(key, "", label, value, true);
    }

    SwitchSetting(String key, String settingName, String label, int value, boolean display) {
        super(key, settingName, label, value, display);
    }

    @Override
    public String getDisplayValue() {
        return value == 1 ? "On": "Off";
    }

    private SwitchSetting(Parcel in) {
        super(in);
    }

    public static final Creator<SwitchSetting> CREATOR = new Creator<SwitchSetting>() {
        @Override
        public SwitchSetting createFromParcel(Parcel in) {
            return new SwitchSetting(in);
        }

        @Override
        public SwitchSetting[] newArray(int size) {
            return new SwitchSetting[size];
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