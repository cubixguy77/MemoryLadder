package com.memoryladder.taketest.cards;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mastersofmemory.memoryladder.R;

public class SuitSelectorView extends LinearLayout {

    private SuitSelectionListener suitSelectionListener;

    private AppCompatImageButton heart;
    private AppCompatImageButton diamond;
    private AppCompatImageButton club;
    private AppCompatImageButton spade;

    private int selectedSuit;

    public SuitSelectorView(Context context) {        super(context);    }
    public SuitSelectorView(Context context, @Nullable AttributeSet attrs) {        super(context, attrs);    }
    public SuitSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    void setSuitSelectionListener(SuitSelectionListener listener) {
        this.suitSelectionListener = listener;
    }

    private void initViews() {
        heart = findViewById(R.id.image_suit_selector_heart);
        diamond = findViewById(R.id.image_suit_selector_diamond);
        club = findViewById(R.id.image_suit_selector_club);
        spade = findViewById(R.id.image_suit_selector_spade);

        heart.setOnClickListener(v -> suitSelectionListener.onSuitSelected(PlayingCard.HEART));
        diamond.setOnClickListener(v -> suitSelectionListener.onSuitSelected(PlayingCard.DIAMOND));
        club.setOnClickListener(v -> suitSelectionListener.onSuitSelected(PlayingCard.CLUB));
        spade.setOnClickListener(v -> suitSelectionListener.onSuitSelected(PlayingCard.SPADE));
    }

    void renderSuits(int selectedSuit) {
        this.selectedSuit = selectedSuit;

        heart.setBackgroundResource(selectedSuit == PlayingCard.HEART ? R.drawable.border_highlight_yellow : R.drawable.border_black_on_white);
        diamond.setBackgroundResource(selectedSuit == PlayingCard.DIAMOND ? R.drawable.border_highlight_yellow : R.drawable.border_black_on_white);
        club.setBackgroundResource(selectedSuit == PlayingCard.CLUB ? R.drawable.border_highlight_yellow : R.drawable.border_black_on_white);
        spade.setBackgroundResource(selectedSuit == PlayingCard.SPADE ? R.drawable.border_highlight_yellow : R.drawable.border_black_on_white);
    }

    public int getSelectedSuit() {
        return selectedSuit;
    }
}
