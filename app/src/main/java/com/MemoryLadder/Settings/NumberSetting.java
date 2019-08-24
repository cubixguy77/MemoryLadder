package com.memoryladder.settings;

import android.os.Parcel;

public class NumberSetting extends Setting {

    private int minValue;
    private int maxValue;

    /* Static Setting - Steps Mode */
    NumberSetting(String key, String label, int value) {
        this(key, "", label, value, 0, 0, true);
    }

    /* Editable Setting - Custom Mode */
    NumberSetting(String key, String settingName, String label, int value, int minValue, int maxValue, boolean display) {
        super(key, settingName, label, value, display);
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public String getDisplayValue() {
        return Integer.toString((int) value);
    }

    private NumberSetting(Parcel in) {
        super(in);
        minValue = in.readInt();
        maxValue = in.readInt();
    }

    public static final Creator<NumberSetting> CREATOR = new Creator<NumberSetting>() {
        @Override
        public NumberSetting createFromParcel(Parcel in) {
            return new NumberSetting(in);
        }

        @Override
        public NumberSetting[] newArray(int size) {
            return new NumberSetting[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(minValue);
        dest.writeInt(maxValue);
    }

    public int describeContents() {
        return 0;
    }
}
