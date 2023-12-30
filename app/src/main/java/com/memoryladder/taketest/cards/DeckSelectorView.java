package com.memoryladder.taketest.cards;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

public class DeckSelectorView extends LinearLayout implements DeckSelector.View {

    private final Context context;
    private DeckSelector.Presenter presenter;

    private TextView deckNumText;

    public DeckSelectorView(Context context) {        super(context); this.context = context;    }
    public DeckSelectorView(Context context, @Nullable AttributeSet attrs) {        super(context, attrs); this.context = context;    }
    public DeckSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {        super(context, attrs, defStyleAttr); this.context = context;    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        deckNumText = findViewById(R.id.text_deck_number);
        findViewById(R.id.button_prev_deck).setOnClickListener(this::prevDeck);
        findViewById(R.id.button_next_deck).setOnClickListener(this::nextDeck);
    }

    @Override
    public void setDeckSelectionListener(DeckSelector.Presenter presenter) {
        this.presenter = presenter;
    }

    public void prevDeck(View view) {
        presenter.onPrevDeck();
    }
    public void nextDeck(View view) {
        presenter.onNextDeck();
    }

    @Override
    public void displayDeckNumber(int deckNum, int numDecks) {
        deckNumText.setText(String.format(context.getString(R.string.deckNum), deckNum, numDecks));
    }
}
