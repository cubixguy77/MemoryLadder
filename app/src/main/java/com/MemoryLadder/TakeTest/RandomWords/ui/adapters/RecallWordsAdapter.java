package com.memoryladder.taketest.randomwords.ui.adapters;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.memoryladder.taketest.randomwords.ui.viewmodel.RandomWordsViewModel;
import com.mastersofmemory.memoryladder.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Renders the word list in Recall phase
 */
public class RecallWordsAdapter extends WordsAdapter {

    private final int numWords;
    private RandomWordsViewModel viewModel;

    public RecallWordsAdapter(int numWords, RandomWordsViewModel viewModel) {
        this.numWords = numWords;
        this.viewModel = viewModel;
    }

    public void setRecallSheet(List<String> recallSheet) {
        //this.recallSheet = recallSheet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RandomWordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecallViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_random_words_recall, parent, false),
                new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull RandomWordsViewHolder holder, int position) {
        ((RecallViewHolder) holder).bindTo(position, viewModel.getRecallForCurrentColumnAt(position));
    }

    @Override
    public int getItemCount() {
        return numWords;
    }

    class RecallViewHolder extends RandomWordsViewHolder {
        private final MyCustomEditTextListener myCustomEditTextListener;
        @BindView(R.id.words_list_cell_recall_input) EditText input;

        RecallViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.myCustomEditTextListener = myCustomEditTextListener;
            this.input.addTextChangedListener(myCustomEditTextListener);
            this.input.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        }

        void bindTo(int position, String text) {
            super.bindTo(position);

            myCustomEditTextListener.updatePosition(position);
            input.setTextColor(getTextColor());
            input.setText(text);
        }
    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            viewModel.updateRecallSheet(charSequence.toString(), position);
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}