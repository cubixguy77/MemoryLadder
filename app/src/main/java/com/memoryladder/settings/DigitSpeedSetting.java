package com.memoryladder.settings;

import android.os.Parcel;

import com.memoryladder.taketest.numbers.spoken.settings.DigitSpeed;

public class DigitSpeedSetting extends Setting {

    /* Static Setting - Steps Mode */
    DigitSpeedSetting(String key, String label, int value) {
        this(key, "", label, value, true);
    }

    /* Editable Setting - Custom Mode */
    DigitSpeedSetting(String key, String settingName, String label, int value, boolean display) {
        super(key, settingName, label, value, display);
    }

    @Override
    public String getDisplayValue() {
        return DigitSpeed.getDisplayName(value);
    }

    private DigitSpeedSetting(Parcel in) {
        super(in);
    }

    public static final Creator<DigitSpeedSetting> CREATOR = new Creator<DigitSpeedSetting>() {
        @Override
        public DigitSpeedSetting createFromParcel(Parcel in) {
            return new DigitSpeedSetting(in);
        }

        @Override
        public DigitSpeedSetting[] newArray(int size) {
            return new DigitSpeedSetting[size];
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