package com.memoryladder.taketest.randomwords.ui.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Renders the word list in Review phase
 */
public class ReviewWordsAdapter extends WordsAdapter {

    private final int numWords;
    private ReviewSheet reviewSheet;

    public ReviewWordsAdapter(int numWords) {
        this.numWords = numWords;
    }

    public void setReviewSheet(ReviewSheet reviewSheet) {
        this.reviewSheet = reviewSheet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RandomWordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_random_words_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RandomWordsViewHolder holder, int position) {
        ((ReviewViewHolder) holder).bindTo(position, reviewSheet.getMemoryWord(position), reviewSheet.getRecallWord(position), reviewSheet.getOutcome(position));
    }

    @Override
    public int getItemCount() {
        return numWords;
    }

    class ReviewViewHolder extends RandomWordsViewHolder {
        @BindView(R.id.words_list_cell_memory_text) TextView memoryText;
        @BindView(R.id.words_list_cell_recall_text) TextView recallText;
        @BindView(R.id.words_list_cell_review_icon) ImageView icon;

        ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        void bindTo(int position, String memorySheetWord, String recallSheetWord, ReviewCellOutcome outcome) {
            super.bindTo(position);

            switch (outcome) {
                case CORRECT:
                    memoryText.setTextColor(getTextColor());
                    memoryText.setText(memorySheetWord);

                    recallText.setVisibility(View.GONE);

                    icon.setImageResource(R.drawable.icon_review_correct);
                    break;
                case WRONG:
                    memoryText.setTextColor(getTextColor());
                    memoryText.setText(memorySheetWord);

                    recallText.setVisibility(View.VISIBLE);
                    recallText.setTextColor(getTextColor());
                    recallText.setText(recallSheetWord);

                    icon.setImageResource(R.drawable.icon_review_wrong);
                    break;

                case BLANK:
                    memoryText.setTextColor(getTextColor());
                    memoryText.setText(memorySheetWord);

                    recallText.setVisibility(View.GONE);

                    icon.setImageResource(R.drawable.icon_review_blank);
                    break;
            }
        }
    }
}