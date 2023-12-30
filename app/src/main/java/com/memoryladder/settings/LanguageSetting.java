package com.memoryladder.settings;

import android.os.Parcel;

import java.util.Locale;

public class LanguageSetting extends Setting {

	private Locale locale;

    /* Static Setting - Steps Mode */
    LanguageSetting(String key, String label, Locale locale) {
        this(key, "", label, locale, true);
    }

    /* Editable Setting - Custom Mode */
    LanguageSetting(String key, String settingName, String label, Locale locale, boolean display) {
        super(key, settingName, label, 0, display);
        this.locale = locale;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String getDisplayValue() {
        if (this.locale == null)
            this.locale = Locale.UK;
        return this.locale.getDisplayLanguage();
    }

    private LanguageSetting(Parcel in) {
        super(in);
    }

    public static final Creator<LanguageSetting> CREATOR = new Creator<LanguageSetting>() {
        @Override
        public LanguageSetting createFromParcel(Parcel in) {
            return new LanguageSetting(in);
        }

        @Override
        public LanguageSetting[] newArray(int size) {
            return new LanguageSetting[size];
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