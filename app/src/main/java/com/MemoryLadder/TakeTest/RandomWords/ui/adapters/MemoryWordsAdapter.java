package com.memoryladder.taketest.randomwords.ui.adapters;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Renders the word list in Mem phase
 */
public class MemoryWordsAdapter extends WordsAdapter {

    private final int numWords;
    private List<String> memorySheet;
    private boolean animate = false;

    public MemoryWordsAdapter(int numWords) {
        this.numWords = numWords;
    }

    public void setMemorySheet(List<String> memorySheet) {
        this.memorySheet = memorySheet;
        animate = true;
        notifyDataSetChanged();
        new Handler().postDelayed(() -> animate = false, 500);
    }

    @NonNull
    @Override
    public RandomWordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_random_words_memory, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomWordsViewHolder holder, int position) {
        ((MemoryViewHolder) holder).bindTo(position, memorySheet == null ? ""  : memorySheet.get(position));
    }

    @Override
    public int getItemCount() {
        return numWords;
    }

    class MemoryViewHolder extends RandomWordsViewHolder {
        @BindView(R.id.words_list_cell_memory_text) TextView text;

        MemoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindTo(int position, String word) {
            super.bindTo(position);
            text.setTextColor(getTextColor());

            if (animate) {
                text.setAlpha(0f);
                text.setText(word);
                text.animate().alpha(1f).setDuration(200).setStartDelay(position * 30);
            }
            else {
                text.setText(word);
            }
        }
    }
}