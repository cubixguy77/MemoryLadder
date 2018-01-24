package com.MemoryLadder.TestDetailsScreen;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestDetailsCard extends android.support.v7.widget.CardView {

    @BindView(R.id.testDetailsCardTitle) TextView titleText;
    @BindView(R.id.testDetailsEditSettings) TextView editButton;
    @BindView(R.id.testDetailsPlayButton) Button playButton;
    @BindView(R.id.testDetailsUnlockButton) Button unlockButton;
    @BindView(R.id.testDetailsLabelLayout) LinearLayout testDetailsLabelLayout;
    @BindView(R.id.testDetailsValueLayout) LinearLayout testDetailsValueLayout;

    public TestDetailsCard(Context context) {        super(context);    }
    public TestDetailsCard(Context context, AttributeSet attrs) {        super(context, attrs);    }
    public TestDetailsCard(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    void setTitle(String title) {
        titleText.setText(title);
    }

    public void setEditableSettings(boolean editableSettings) {
        editButton.setVisibility(editableSettings ? View.VISIBLE : View.GONE);
    }

    public void setLocked(boolean locked) {
        playButton.setVisibility(locked ? View.GONE : View.VISIBLE);
        unlockButton.setVisibility(locked ? View.VISIBLE : View.GONE);
        editButton.setEnabled(!locked);
    }

    void setSettings(List<Setting> settings) {
        boolean targetScoreProvided = false;

        for (int i=0; i<settings.size(); i++) {
            Setting setting = settings.get(i);
            if (setting.key.equals("target")) {
                targetScoreProvided = true;
                ((TextView) findViewById(R.id.TargetTextLabel)).setText(setting.label);
                ((TextView) findViewById(R.id.TargetTextValue)).setText(setting.displayValue);
                getTextLabelAt(i).setVisibility(View.GONE);
                getTextValueAt(i).setVisibility(View.GONE);
            }
            else {
                getTextLabelAt(i).setText(settings.get(i).label);
                getTextValueAt(i).setText(settings.get(i).displayValue);
            }
        }

        for (int i=settings.size(); i<6; i++) {
            getTextLabelAt(i).setVisibility(View.GONE);
            getTextValueAt(i).setVisibility(View.GONE);
        }

        if (!targetScoreProvided) {
            findViewById(R.id.TargetTextLabel).setVisibility(View.GONE);
            findViewById(R.id.TargetTextValue).setVisibility(View.GONE);
        }
    }

    private TextView getTextLabelAt(int pos) {
        return (TextView) testDetailsLabelLayout.getChildAt(pos);
    }

    private TextView getTextValueAt(int pos) {
        return (TextView) testDetailsValueLayout.getChildAt(pos);
    }
}
