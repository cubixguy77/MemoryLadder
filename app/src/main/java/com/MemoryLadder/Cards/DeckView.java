package com.MemoryLadder.Cards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import com.mastersofmemory.memoryladder.R;

public class DeckView extends GridLayout {

    private Context context;
    private SmallCardView[] cardViews;
    private CardClickListener cardClickListener;

    public DeckView(Context context) {        super(context); this.context = context;     }
    public DeckView(Context context, AttributeSet attrs) {        super(context, attrs); this.context = context;    }
    public DeckView(Context context, AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr); this.context = context;    }

    public void renderCards(Deck deck) {
        if (getColumnCount() <= 0) {
            computeColumnCount(deck.size());
        }

        /* First pass through, views need to be inflated */
        if (cardViews == null) {
            initViews(deck);
            setupListeners();
        }
        else {
            displayDeck(deck);
        }
    }

    /* Renders the Review stage - Memory Data on top, Recall on bottom */
    public void renderCards(Deck deckMem, Deck deckRecall) {
        removeAllViews();

        cardViews = new SmallCardView[deckMem.size() + deckRecall.size()];

        for (int i = 0; i < cardViews.length; i++) {
            boolean isMemoryCell = (i / getColumnCount() % 2 == 0);

            SmallCardView cardView = (SmallCardView) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.partial_card_small, null, false);
            FrameLayout.LayoutParams cardLayoutParameters = new FrameLayout.LayoutParams(getWidth() / getColumnCount(), (int) getResources().getDimension(R.dimen.small_card_height));
            cardLayoutParameters.setMargins(0, 0, 0, isMemoryCell ? 0 : (int) getResources().getDimension(R.dimen.small_card_review_margin));
            cardView.setLayoutParams(cardLayoutParameters);

            if (isMemoryCell) {
                cardView.setCard(deckMem.getCard(mapIndex(i)));
            }
            else {
                int recCardIndex =  mapIndex(i);

                if (deckMem.getCard(recCardIndex).equals(deckRecall.getCard(recCardIndex))) {
                    cardView.setCard(PlayingCard.CORRECT);
                }
                else {
                    cardView.setCard(deckRecall.getCard(recCardIndex));
                }
            }

            addView(cardView, i);
            cardViews[i] = cardView;
        }
    }

    void clear() {
        if (cardViews != null) {
            removeAllViews();
            cardViews = null;
        }
    }

    private int mapIndex(int i) {
        int rowNum = i / getColumnCount();
        if (rowNum % 2 == 0) {
            return i - ((rowNum / 2) * getColumnCount());
        }
        else {
            return i - (((rowNum+1) / 2) * getColumnCount());
        }
    }

    protected void setupListeners() {
        for (SmallCardView cardView : cardViews) {
            cardView.setOnClickListener(v -> {
                PlayingCard selectedCard = ((SmallCardView) v).getCard();
                    cardClickListener.onCardClick(indexOfChild(v), selectedCard);
            });
        }
    }

    void setListener(CardClickListener listener) {
        this.cardClickListener = listener;
    }

    void highlightCards(int startIndex, int endIndex) {
        clearHighlight();
        for (int i=startIndex; i<endIndex; i++) {
            getCardAt(i).highlightCard();
        }
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
        for (int i=0; i<deck.size(); i++) {
            getCardAt(i).setCard(deck.getCard(i));
            getCardAt(i).setVisibility(View.VISIBLE);
        }
    }

    private void clearHighlight() {
        for (int i=0; i<getChildCount(); i++) {
            getCardAt(i).removeHighlight();
        }
    }

    private SmallCardView getCardAt(int index) {
        return (SmallCardView) getChildAt(index);
    }

    void computeColumnCount(int deckSize) {
        int columnCount = 13;

        while (deckSize % columnCount != 0) {
            columnCount--;
        }

        /* Deck Size is a prime number */
        if (columnCount == 1) {
            columnCount = 13;
        }

        setColumnCount(columnCount);
        setRowCount(deckSize / getColumnCount());
    }
}