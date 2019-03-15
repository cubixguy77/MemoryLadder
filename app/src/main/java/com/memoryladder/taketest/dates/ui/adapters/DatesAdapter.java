package com.memoryladder.taketest.dates.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.dates.models.HistoricDate;
import com.memoryladder.taketest.dates.models.TestSheet;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Renders the event list
 */
public class DatesAdapter extends RecyclerView.Adapter<DatesAdapter.MemoryViewHolder> {

    private TestSheet testSheet;
    private GamePhase phase;

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

                dateInput.setVisibility(View.GONE);

                eventDetailsText.setText("Press start to begin...");
            }
            else if (phase == GamePhase.MEMORIZATION) {
                dateText.setVisibility(View.VISIBLE);
                dateText.setText(date.getMemoryDate());

                dateInput.setVisibility(View.GONE);

                eventDetailsText.setText(date.getEventDetails());
            }
            else if (phase == GamePhase.RECALL) {
                dateText.setVisibility(View.GONE);

                dateInput.setVisibility(View.VISIBLE);
                dateInput.setText(date.getRecallDate());

                eventDetailsText.setText(date.getEventDetails());
            }
            else if (phase == GamePhase.REVIEW) {
                dateText.setVisibility(View.VISIBLE);
                dateText.setText(date.getMemoryDate());

                dateInput.setVisibility(View.VISIBLE);
                dateInput.setText(date.getRecallDate());

                eventDetailsText.setText(date.getEventDetails());
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