package com.memoryladder.taketest.randomwords.ui.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Renders the word list in Pre Mem phase
 * All cells will be empty
 */
public class PreMemWordsAdapter extends WordsAdapter {

    private final int numWords;

    public PreMemWordsAdapter(int numWords) {
        this.numWords = numWords;
    }

    @NonNull
    @Override
    public RandomWordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PreMemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_random_words_memory, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomWordsViewHolder holder, int position) {
        ((PreMemViewHolder) holder).bindTo(position);
    }

    @Override
    public int getItemCount() {
        return numWords;
    }

    class PreMemViewHolder extends RandomWordsViewHolder {
        @BindView(R.id.words_list_cell_memory_text) TextView text;

        PreMemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindTo(int position) {
            super.bindTo(position);
            text.setText("");
        }
    }
}