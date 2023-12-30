package com.memoryladder.taketest.namesandfaces.ui.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

/**
 * Base from which phase specific adapters extend
 */
abstract class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.RandomWordsViewHolder> {

    private static final int fadedBlack = Color.parseColor("#77000000");
    //private static final int fadedWhite = Color.parseColor("#ddFFFFFF");

    static class RandomWordsViewHolder extends RecyclerView.ViewHolder {
        private final TextView number;

        RandomWordsViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.words_list_cell_index);
        }

        @SuppressLint("SetTextI18n")
        void bindTo(int position) {
            number.setText(Integer.toString(position + 1));
        }

        int getTextColor() {
            return fadedBlack;
        }
    }
}