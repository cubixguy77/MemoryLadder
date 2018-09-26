package com.memoryladder.taketest.cards;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeckSelectorView extends LinearLayout implements DeckSelector.View {

    private Context context;
    private DeckSelector.Presenter presenter;

    @BindView(R.id.text_deck_number) TextView deckNumText;

    public DeckSelectorView(Context context) {        super(context); this.context = context;    }
    public DeckSelectorView(Context context, @Nullable AttributeSet attrs) {        super(context, attrs); this.context = context;    }
    public DeckSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr); this.context = context;    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    public void setDeckSelectionListener(DeckSelector.Presenter presenter) {
        this.presenter = presenter;
    }

    @OnClick(R.id.button_prev_deck) public void prevDeck(View view) {
        presenter.onPrevDeck();
    }
    @OnClick(R.id.button_next_deck) public void nextDeck(View view) { presenter.onNextDeck(); }

    @Override
    public void displayDeckNumber(int deckNum, int numDecks) {
        deckNumText.setText(String.format(context.getString(R.string.deckNum), deckNum, numDecks));
    }
}
