package com.MemoryLadder.Cards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class SmallCardView extends FrameLayout {

    private android.support.v7.widget.AppCompatImageView suitIcon;
    private TextView rankIcon;
    private PlayingCard card;

    public SmallCardView(@NonNull Context context) {      super(context);   }
    public SmallCardView(@NonNull Context context, @Nullable AttributeSet attrs) {       super(context, attrs);   }
    public SmallCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {       super(context, attrs, defStyleAttr);   }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        initViews();
    }

    public void initViews() {
        suitIcon = findViewById(R.id.card_suit);
        rankIcon = findViewById(R.id.card_rank);
    }

    public void setCard(PlayingCard card) {
        this.card = card;

        if (card == null) {
            suitIcon.setImageResource(0);
            rankIcon.setText("_");
            return;
        }
        else if (card == PlayingCard.CORRECT) {
            suitIcon.setVisibility(View.GONE);
            rankIcon.setText("✔");
            rankIcon.setTextColor(getResources().getColor(R.color.green));
            ((FrameLayout.LayoutParams) rankIcon.getLayoutParams()).gravity = Gravity.CENTER;
            return;
        }

        switch (card.suit) {
            case PlayingCard.HEART: suitIcon.setImageResource(R.drawable.icon_suit_heart); break;
            case PlayingCard.DIAMOND: suitIcon.setImageResource(R.drawable.icon_suit_diamond); break;
            case PlayingCard.SPADE: suitIcon.setImageResource(R.drawable.icon_suit_spade); break;
            case PlayingCard.CLUB: suitIcon.setImageResource(R.drawable.icon_suit_club); break;
        }

        rankIcon.setText(getRankString(card.rank));
    }

    public void highlightCard() {
        setBackgroundResource(R.drawable.border_highlight);
    }

    public void removeHighlight() {
        setBackgroundResource(R.drawable.border);
    }

    public PlayingCard getCard() {
        return card;
    }

    private String getRankString(int rank) {
        if (rank == 1)
            return "A";
        if (rank == 11)
            return "J";
        if (rank == 12)
            return "Q";
        if (rank == 13)
            return "K";

        return Integer.toString(rank);
    }
}
