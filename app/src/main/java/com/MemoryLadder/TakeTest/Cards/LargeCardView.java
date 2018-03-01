package com.MemoryLadder.TakeTest.Cards;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class LargeCardView extends LinearLayout {

    /* Views */
    AppCompatImageView cardView;
    AppCompatTextView mnemonicTextView;

    /* Data */
    private PlayingCard card;

    public LargeCardView(Context context) {        super(context);    }
    public LargeCardView(Context context, AttributeSet attrs) {        super(context, attrs);    }
    public LargeCardView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr);    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        this.cardView = (AppCompatImageView) getChildAt(0);
        this.mnemonicTextView = (AppCompatTextView) getChildAt(1);
    }

    public void setCard(PlayingCard card) {
        this.card = card;
        cardView.setImageResource(CardImageProvider.getImageResourceId(getContext(), card));
    }

    public PlayingCard getCard() {
        return card;
    }

    public void showMnemonicText(String text) {
        mnemonicTextView.setText(text);
        mnemonicTextView.setVisibility(View.VISIBLE);
    }

    public void hideMnemonicText() {
        mnemonicTextView.setText("");
        mnemonicTextView.setVisibility(View.GONE);
    }
}
