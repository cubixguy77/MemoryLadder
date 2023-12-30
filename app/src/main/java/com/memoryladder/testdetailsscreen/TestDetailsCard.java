package com.memoryladder.testdetailsscreen;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.memoryladder.settings.DigitSpeedSetting;
import com.memoryladder.settings.DigitSpeedSettingDialog;
import com.memoryladder.settings.NumberSetting;
import com.memoryladder.settings.NumberSettingDialog;
import com.memoryladder.settings.Setting;
import com.memoryladder.settings.SwitchSetting;
import com.memoryladder.settings.TargetSetting;
import com.memoryladder.settings.TimeSetting;
import com.memoryladder.settings.TimeSettingDialog;
import com.memoryladder.settings.LanguageSetting;
import com.memoryladder.settings.LanguageSettingDialog;
import com.mastersofmemory.memoryladder.R;

import java.util.List;
import java.util.Locale;

public class TestDetailsCard extends CardView {

    private final Context context;

    private Button titleText;
    private AppCompatButton playButton;
    private AppCompatButton unlockButton;
    private LinearLayout testDetailsLabelLayout;
    private LinearLayout testDetailsValueLayout;

    public TestDetailsCard(Context context) {        super(context); this.context = context;    }
    public TestDetailsCard(Context context, AttributeSet attrs) {        super(context, attrs); this.context = context;   }
    public TestDetailsCard(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr); this.context = context;   }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        titleText = findViewById(R.id.testDetailsCardTitle);
        playButton = findViewById(R.id.testDetailsPlayButton);
        unlockButton = findViewById(R.id.testDetailsUnlockButton);
        testDetailsLabelLayout = findViewById(R.id.testDetailsLabelLayout);
        testDetailsValueLayout = findViewById(R.id.testDetailsValueLayout);
    }

    void setTitle(String title) {
        titleText.setText(title);
    }

    public void setLocked(boolean locked) {
        playButton.setVisibility(locked ? View.GONE : View.VISIBLE);
        unlockButton.setVisibility(locked ? View.VISIBLE : View.GONE);
    }

    void setSettings(List<Setting> settings, boolean editable, String prefsName) {
        LayoutInflater inflater = LayoutInflater.from(context); // or (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Setting setting : settings) {
            if (!setting.display)
                continue;

            if (setting instanceof TargetSetting) {
                addTextField(inflater, R.layout.test_details_card_setting_target, testDetailsLabelLayout, setting.label);
                addTextField(inflater, R.layout.test_details_card_setting_target, testDetailsValueLayout, setting.getDisplayValue());
                continue;
            }

            /* Add Label */
            addTextField(inflater, R.layout.test_details_card_setting_label, testDetailsLabelLayout, setting.label);

            /* Add Value */
            if (!editable) {
                addTextField(inflater, R.layout.test_details_card_setting_label, testDetailsValueLayout, setting.getDisplayValue());
            }
            else if (setting instanceof NumberSetting) {
                TextView editableField = addTextField(inflater, R.layout.test_details_card_setting_editable, testDetailsValueLayout, setting.getDisplayValue());
                editableField.setOnClickListener(v -> {
                    NumberSettingDialog dialog = new NumberSettingDialog(context, setting.label, setting.value, ((NumberSetting) setting).getMinValue(), ((NumberSetting) setting).getMaxValue());
                    dialog.setValueChangedListener(newValue -> updateSetting(editableField, setting, newValue, prefsName));
                    dialog.show();
                });
            }
            else if (setting instanceof TimeSetting) {
                TextView editableField = addTextField(inflater, R.layout.test_details_card_setting_editable, testDetailsValueLayout, setting.getDisplayValue());
                editableField.setOnClickListener(v -> {
                    TimeSettingDialog dialog = new TimeSettingDialog(context, setting.label, setting.value);
                    dialog.setValueChangeListener(newValue -> updateSetting(editableField, setting, newValue, prefsName));
                    dialog.show();
                });
            }
            else if (setting instanceof SwitchSetting) {
                TextView editableField = addTextField(inflater, R.layout.test_details_card_setting_editable, testDetailsValueLayout, setting.getDisplayValue());
                editableField.setOnClickListener(v -> updateSetting(editableField, setting, setting.value == 1 ? 0 : 1, prefsName));
            }
            else if (setting instanceof DigitSpeedSetting) {
                TextView editableField = addTextField(inflater, R.layout.test_details_card_setting_editable, testDetailsValueLayout, setting.getDisplayValue());
                editableField.setOnClickListener(v -> {
                    DigitSpeedSettingDialog dialog = new DigitSpeedSettingDialog(context, setting.value);
                    dialog.setValueChangedListener(newValue -> updateSetting(editableField, setting, newValue, prefsName));
                    dialog.show();
                });
            }
            else if (setting instanceof LanguageSetting) {
                TextView editableField = addTextField(inflater, R.layout.test_details_card_setting_editable, testDetailsValueLayout, setting.getDisplayValue());
                editableField.setOnClickListener(v -> {
                    LanguageSettingDialog dialog = new LanguageSettingDialog(context, ((LanguageSetting) setting).getLocale());
                    dialog.setValueChangedListener(newValue -> updateLanguageSetting(editableField, setting, newValue, prefsName));
                    dialog.show();
                });
            }
        }
    }

    private void updateSetting(TextView editableField, Setting setting, int newValue, String prefsName) {
        setting.value = newValue;
        editableField.setText(setting.getDisplayValue());
        SharedPreferences.Editor editor = context.getSharedPreferences(prefsName, 0).edit();
        editor.putInt(setting.settingName, newValue);
        editor.apply();
    }

    private void updateLanguageSetting(TextView editableField, Setting setting, Locale locale, String prefsName) {
        ((LanguageSetting) setting).setLocale(locale);
        editableField.setText(setting.getDisplayValue());
        SharedPreferences.Editor editor = context.getSharedPreferences(prefsName, 0).edit();
        editor.putString(setting.settingName + "language", locale.getLanguage());
        editor.putString(setting.settingName + "country", locale.getCountry());
        editor.putString(setting.settingName + "variant", locale.getVariant());
        editor.apply();
    }

    private TextView addTextField(LayoutInflater inflater, @LayoutRes int resource, ViewGroup root, String text) {
        LinearLayout layout = (LinearLayout) inflater.inflate(resource, root);
        TextView textView = getLastChild(layout);
        textView.setText(text);
        return textView;
    }

    private TextView getLastChild(ViewGroup layout) {
        return (TextView) layout.getChildAt(layout.getChildCount() - 1);
    }
}
