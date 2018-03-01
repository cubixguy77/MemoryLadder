package com.MemoryLadder.TakeTest.Cards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import com.mastersofmemory.memoryladder.R;

public class CardSelectionView extends GridLayout {

    private Context context;
    private SmallCardView[] cardViews;
    private CardSelectionListener cardSelectionListener;

    public CardSelectionView(Context context) {        super(context); this.context = context;   }
    public CardSelectionView(Context context, AttributeSet attrs) {        super(context, attrs); this.context = context;   }
    public CardSelectionView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr); this.context = context;   }

    public void setCardSelectionListener(CardSelectionListener listener) {
        this.cardSelectionListener = listener;
    }

    public void renderCards(Deck deck) {
        /* First pass through, views need to be inflated */
        if (cardViews == null) {
            setColumnCount(7);
            initViews(deck);
            setupListeners();
        }
        else {
            displayDeck(deck);
        }
    }

    public void setupListeners() {
        for (int i=0; i<getChildCount(); i++) {
            if (getChildAt(i).getVisibility() == View.VISIBLE) {
                getChildAt(i).setOnClickListener(v -> cardSelectionListener.onCardSelected(((SmallCardView) v).getCard()));
            }
        }
    }

    void addCard(PlayingCard card) {
        SmallCardView cCard = (SmallCardView) getChildAt(card.rank - 1);
        cCard.setCard(card);
        cCard.setVisibility(View.VISIBLE);
    }

    void removeCard(PlayingCard card) {
        SmallCardView cCard = (SmallCardView) getChildAt(card.rank - 1);
        cCard.setVisibility(View.INVISIBLE);
    }

    private void initViews(Deck deck) {
        cardViews = new SmallCardView[deck.size()];

        for (int i = 0; i < deck.size(); i++) {
            SmallCardView cardView = (SmallCardView) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.partial_card_small, null, false);
            FrameLayout.LayoutParams cardLayoutParameters = new FrameLayout.LayoutParams(getWidth() / getColumnCount(), (int) getResources().getDimension(R.dimen.small_card_height));
            cardView.setLayoutParams(cardLayoutParameters);
            cardView.setCard(deck.getCard(i));
            addView(cardView, i);
            cardViews[i] = cardView;
        }
    }

    private void displayDeck(Deck deck) {
        for (SmallCardView cardView : cardViews) {
            cardView.setVisibility(View.INVISIBLE);
        }

        for (int i = 0; i < deck.size(); i++) {
            cardViews[deck.getCard(i).rank - 1].setCard(deck.getCard(i));
            cardViews[deck.getCard(i).rank - 1].setVisibility(View.VISIBLE);
        }
    }
}
