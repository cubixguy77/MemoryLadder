package com.memoryladder.taketest.randomwords.ui.adapters;

import android.annotation.SuppressLint;
import android.os.Handler;
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
    private boolean animate = false;

    public ReviewWordsAdapter(int numWords) {
        this.numWords = numWords;
    }

    public void setReviewSheet(ReviewSheet reviewSheet) {
        this.reviewSheet = reviewSheet;
        animate = true;
        notifyDataSetChanged();
        new Handler().postDelayed(() -> animate = false, 500);
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
            memoryText.setTextColor(getTextColor());
            recallText.setTextColor(getTextColor());
        }

        @SuppressLint("SetTextI18n")
        void bindTo(int position, String memorySheetWord, String recallSheetWord, ReviewCellOutcome outcome) {
            super.bindTo(position);

            if (animate) {
                memoryText.setAlpha(0f);
                recallText.setAlpha(0f);
                icon.setAlpha(0f);
                memoryText.setText("aggression");
                recallText.setText("aggression");
                memoryText.animate().alpha(1f).setDuration(200).setStartDelay(position * 20);
                recallText.animate().alpha(1f).setDuration(200).setStartDelay(position * 20);
                icon.animate().alpha(1f).setDuration(200).setStartDelay(position * 20);
            }
            else {
                memoryText.setText("aggression");
                recallText.setText("aggression");
            }

            switch (outcome) {
                case CORRECT:
                    recallText.setVisibility(View.GONE);
                    icon.setImageResource(R.drawable.icon_review_correct);
                    break;
                case WRONG:
                    recallText.setVisibility(View.VISIBLE);
                    icon.setImageResource(R.drawable.icon_review_wrong);
                    break;

                case BLANK:
                    recallText.setVisibility(View.GONE);
                    icon.setImageResource(R.drawable.icon_review_blank);
                    break;
            }
        }
    }
}