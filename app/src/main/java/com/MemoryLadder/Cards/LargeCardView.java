package com.MemoryLadder.Cards;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mastersofmemory.memoryladder.BuildConfig;

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
        cardView.setImageResource(getImageResourceId(card));
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

    private int getImageResourceId(PlayingCard card) {
        return getResources().getIdentifier(getImageFileNameFor(card), "drawable", BuildConfig.APPLICATION_ID);
    }

    private String getImageFileNameFor(PlayingCard card) {
        if (card == null)
            return "card_xx";
        else
            return "card_" + getSuitChar(card.suit) + getRankChar(card.rank);
    }

    private char getSuitChar(int suit) {
        switch (suit) {
            case PlayingCard.DIAMOND: return 'd';
            case PlayingCard.HEART: return 'h';
            case PlayingCard.CLUB: return 'c';
            case PlayingCard.SPADE: return 's';
            default: return 'x';
        }
    }

    private char getRankChar(int rank) {
        switch (rank) {
            case 10: return 't';
            case 11: return 'j';
            case 12: return 'q';
            case 13: return 'k';
            default: return (char)(rank + '0');
        }
    }
}
