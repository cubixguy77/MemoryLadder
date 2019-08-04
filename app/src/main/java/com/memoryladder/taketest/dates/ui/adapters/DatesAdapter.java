package com.memoryladder.taketest.dates.ui.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.dates.models.HistoricDate;
import com.memoryladder.taketest.dates.models.TestSheet;
import com.memoryladder.taketest.numbers.written.Review.SimpleSpanBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Renders the event list
 */
public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.MemoryViewHolder> {

    private TestSheet testSheet;
    private GamePhase phase;

    private static final ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
    private static final ForegroundColorSpan grayColorSpan = new ForegroundColorSpan(Color.GRAY);
    private static final StrikethroughSpan strikeThroughSpan = new StrikethroughSpan();
    private static final String lineSeparator = System.getProperty("line.separator");


    public DatesAdapter(TestSheet testSheet) {
        this.testSheet = testSheet;
        setGamePhase(GamePhase.PRE_MEMORIZATION);
    }

    public void setGamePhase(GamePhase phase) {
        this.phase = phase;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dates, parent, false), new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull MemoryViewHolder holder, int position) {
        holder.bindTo(testSheet.getDate(position), position);
    }

    @Override
    public int getItemCount() {
        return testSheet.getDateCount();
    }

    class MemoryViewHolder extends RecyclerView.ViewHolder {
        private final MyCustomEditTextListener myCustomEditTextListener;

        @BindView(R.id.dates_date_text) TextView dateText;
        @BindView(R.id.dates_input) EditText dateInput;
        @BindView(R.id.dates_event_details_text) TextView eventDetailsText;
        @BindView(R.id.dates_review_icon) ImageView outcomeIcon;

        MemoryViewHolder(View itemView, MyCustomEditTextListener myCustomEditTextListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.myCustomEditTextListener = myCustomEditTextListener;
            this.dateInput.addTextChangedListener(myCustomEditTextListener);
        }

        void bindTo(HistoricDate date, int position) {
            myCustomEditTextListener.updatePosition(position);

            if (phase == GamePhase.PRE_MEMORIZATION) {
                dateText.setVisibility(View.VISIBLE);
                dateText.setText("----");
                dateText.setTypeface(null, Typeface.NORMAL);

                dateInput.setVisibility(View.GONE);

                eventDetailsText.setText("Press start to begin...");
                outcomeIcon.setVisibility(View.GONE);
            }
            else if (phase == GamePhase.MEMORIZATION) {
                dateText.setVisibility(View.VISIBLE);
                dateText.setText(date.getMemoryDate());
                dateText.setTypeface(null, Typeface.BOLD);

                dateInput.setVisibility(View.GONE);

                eventDetailsText.setText(date.getEventDetails());
                outcomeIcon.setVisibility(View.GONE);
            }
            else if (phase == GamePhase.RECALL) {
                dateText.setVisibility(View.GONE);

                dateInput.setVisibility(View.VISIBLE);
                dateInput.setText(date.getRecallDate());

                eventDetailsText.setText(date.getEventDetails());
                outcomeIcon.setVisibility(View.GONE);
            }
            else if (phase == GamePhase.REVIEW) {
                ReviewCellOutcome cellResult = date.getResult();

                dateText.setVisibility(View.VISIBLE);

                dateInput.setVisibility(View.GONE);

                eventDetailsText.setText(date.getEventDetails());
                outcomeIcon.setVisibility(View.VISIBLE);

                switch (cellResult) {
                    case CORRECT:
                        dateText.setText(date.getMemoryDate());
                        dateText.setTypeface(null, Typeface.BOLD);
                        outcomeIcon.setImageResource(R.drawable.icon_review_correct_green);
                        break;
                    case WRONG:
                        SimpleSpanBuilder ssb = new SimpleSpanBuilder();
                        ssb.append(date.getRecallDate(), redColorSpan, strikeThroughSpan);
                        ssb.append(lineSeparator);
                        ssb.append(date.getMemoryDate(), grayColorSpan);
                        dateText.setTypeface(null, Typeface.NORMAL);
                        dateText.setText(ssb.build());
                        outcomeIcon.setImageResource(R.drawable.icon_review_wrong);
                        break;
                    case BLANK:
                        dateText.setText(date.getMemoryDate());
                        dateText.setTypeface(null, Typeface.NORMAL);
                        outcomeIcon.setImageResource(R.drawable.icon_review_blank);
                        break;
                }
            }
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
            testSheet.getDate(position).setRecallDate(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}