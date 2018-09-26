package com.memoryladder.testdetailsscreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.memoryladder.settings.DigitSpeedSetting;
import com.memoryladder.settings.DigitSpeedSettingDialog;
import com.memoryladder.settings.NumberSetting;
import com.memoryladder.settings.NumberSettingDialog;
import com.memoryladder.settings.Setting;
import com.memoryladder.settings.SwitchSetting;
import com.memoryladder.settings.TargetSetting;
import com.memoryladder.settings.TimeSetting;
import com.memoryladder.settings.TimeSettingDialog;
import com.mastersofmemory.memoryladder.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestDetailsCard extends android.support.v7.widget.CardView {

    private final Context context;

    @BindView(R.id.testDetailsCardTitle) Button titleText;
    @BindView(R.id.testDetailsPlayButton) AppCompatButton playButton;
    @BindView(R.id.testDetailsUnlockButton) AppCompatButton unlockButton;

    @BindView(R.id.testDetailsLabelLayout) LinearLayout testDetailsLabelLayout;
    @BindView(R.id.testDetailsValueLayout) LinearLayout testDetailsValueLayout;

    public TestDetailsCard(Context context) {        super(context); this.context = context;    }
    public TestDetailsCard(Context context, AttributeSet attrs) {        super(context, attrs); this.context = context;   }
    public TestDetailsCard(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr); this.context = context;   }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
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
        }
    }

    private void updateSetting(TextView editableField, Setting setting, int newValue, String prefsName) {
        setting.value = newValue;
        editableField.setText(setting.getDisplayValue());
        SharedPreferences.Editor editor = context.getSharedPreferences(prefsName, 0).edit();
        editor.putInt(setting.settingName, newValue);
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
