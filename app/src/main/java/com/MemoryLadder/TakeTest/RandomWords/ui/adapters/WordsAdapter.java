package com.memoryladder.taketest.randomwords.ui.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Base from which phase specific adapters extend
 */
abstract class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.RandomWordsViewHolder> {

    private static final int fadedBlack = Color.parseColor("#77000000");
    //private static final int fadedWhite = Color.parseColor("#ddFFFFFF");

    class RandomWordsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.words_list_cell_index) TextView number;

        RandomWordsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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