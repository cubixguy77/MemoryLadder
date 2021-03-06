package com.MemoryLadder.TakeTest.Cards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;

import com.MemoryLadder.Utils;
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
        if (getColumnCount() <= 0) {
            computeColumnCount(deckMem.size()); // Column count is wiped out if user rotates device during review
        }

        removeAllViews();

        int numRows = 2 * getRowCount(deckMem.size());
        for (int row=0; row<numRows; row++) {
            boolean isMemoryCell = row % 2 == 0;

            int startIndex = getColumnCount() * (isMemoryCell ? row / 2 : (row - 1) / 2);
            int endIndex = Utils.lesserOf(startIndex + getColumnCount(), deckMem.size());
            for (int i = startIndex; i < endIndex; i++) {
                SmallCardView cardView;

                if (isMemoryCell) {
                    cardView = generateCardView(false, deckMem.getCard(i));
                }
                else {
                    if (deckMem.getCard(i).equals(deckRecall.getCard(i)))
                        cardView = generateCardView(true, PlayingCard.CORRECT);
                    else
                        cardView = generateCardView(true, deckRecall.getCard(i));
                }

                addView(cardView);
            }

            /* Fills out the unfilled row with invisible views, creating a carriage return */
            if (row + 2 == numRows) {
                for (int i = 0; i < getColumnCount() - (endIndex - startIndex); i++) {
                    SmallCardView cardView = generateCardView(false, null);
                    cardView.setVisibility(View.INVISIBLE);
                    addView(cardView);
                }
            }
        }
    }

    void clear() {
        removeAllViews();
        cardViews = null;
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
            SmallCardView cardView = generateCardView(false, deck.getCard(i));
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

    private SmallCardView generateCardView(boolean withBottomMargin, PlayingCard card) {
        SmallCardView cardView = (SmallCardView) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.partial_card_small, null, false);
        FrameLayout.LayoutParams cardLayoutParameters = new FrameLayout.LayoutParams(getWidth() / getColumnCount(), (int) getResources().getDimension(R.dimen.small_card_height));
        cardLayoutParameters.setMargins(0, 0, 0, withBottomMargin ? (int) getResources().getDimension(R.dimen.small_card_review_margin) : 0);
        cardView.setLayoutParams(cardLayoutParameters);
        cardView.setCard(card);
        return cardView;
    }

    private int getRowCount(int deckSize) {
        return (deckSize / getColumnCount()) + (deckSize % getColumnCount() == 0 ? 0 : 1);
    }

    private void computeColumnCount(int deckSize) {
        int maxColumnCount = getResources().getInteger(R.integer.cards_deckView_maxCardsPerRow);
        int columnCount = maxColumnCount;

        while (deckSize % columnCount != 0) {
            columnCount--;
        }

        /* Deck Size is a prime number */
        if (columnCount == 1) {
            columnCount = maxColumnCount;
        }

        /* Deck size is a multiple of two or three times a prime number */
        if ((columnCount == 2 || columnCount == 3) && deckSize > 3) {
            columnCount = maxColumnCount;
        }

        setColumnCount(columnCount);
        setRowCount(deckSize / getColumnCount());
    }
}
