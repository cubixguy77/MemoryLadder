package com.MemoryLadder.Settings;

import android.os.Parcel;

import com.MemoryLadder.Utils;

public class TimeSetting extends Setting {

    TimeSetting(String key, String label, int value) {
        this(key, "", label, value);
    }

    TimeSetting(String key, String settingName, String label, int value) {
        super(key, settingName, label, value, true);
    }

    @Override
    public String getDisplayValue() {
        return Utils.formatIntoHHMMSStruncated(value);
    }

    private TimeSetting(Parcel in) {
        super(in);
    }

    public static final Creator<TimeSetting> CREATOR = new Creator<TimeSetting>() {
        @Override
        public TimeSetting createFromParcel(Parcel in) {
            return new TimeSetting(in);
        }

        @Override
        public TimeSetting[] newArray(int size) {
            return new TimeSetting[size];
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
