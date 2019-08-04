package com.memoryladder.taketest.numbers.spoken.ui.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.dates.ui.adapters.ReviewCellOutcome;
import com.memoryladder.taketest.numbers.CellSelectListener;
import com.memoryladder.taketest.numbers.spoken.models.TestSheet;
import com.memoryladder.taketest.numbers.written.Review.SimpleSpanBuilder;

public class SpokenNumberGridAdapter extends RecyclerView.Adapter<SpokenNumberGridAdapter.ViewHolder> {

    private final CellSelectListener cellSelectListener;
    private TestSheet testSheet;
    private boolean nightMode;
    private boolean drawGridLines;
    private GamePhase gamePhase;
    private int highlight;

    /* Cached values to boost performance */
    private static final int fadedBlack = Color.parseColor("#77000000");
    private static final int fadedWhite = Color.parseColor("#ddFFFFFF");
    private static final ForegroundColorSpan redColorSpan = new ForegroundColorSpan(Color.RED);
    private static final ForegroundColorSpan greenColorSpan = new ForegroundColorSpan(Color.GREEN);
    private static final ForegroundColorSpan whiteColorSpan = new ForegroundColorSpan(Color.WHITE);
    private static final ForegroundColorSpan blackColorSpan = new ForegroundColorSpan(Color.BLACK);
    private static final StrikethroughSpan strikeThroughSpan = new StrikethroughSpan();
    private static final String lineSeparator = System.getProperty("line.separator");
    private static final String dash = "—";
	private static final String EMPTY_CHAR = "‒";


    public SpokenNumberGridAdapter(CellSelectListener cellSelectListener, TestSheet testSheet, boolean nightMode, boolean drawGridLines) {
        this.cellSelectListener = cellSelectListener;
        this.testSheet = testSheet;
        this.nightMode = nightMode;
        this.drawGridLines = drawGridLines;
    }

    public void setNightMode(boolean nightMode) {
        this.nightMode = nightMode;
        notifyDataSetChanged();
    }

    public void setDrawGridLines(boolean drawGridLines) {
        this.drawGridLines = drawGridLines;
        notifyDataSetChanged();
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
        notifyDataSetChanged();
    }

    public void setHighlight(int position) {
        this.highlight = position;
        notifyDataSetChanged();
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewgroup_numbers_grid_cell, parent, false);
        return new ViewHolder(view);
    }

    // binds the test sheet's data to the text in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position >= testSheet.getDigitCount()) {
            holder.myTextView.setVisibility(View.GONE);
            holder.myTextView.setOnClickListener(null);
        }

        if (this.gamePhase == GamePhase.RECALL) {
            holder.myTextView.setOnClickListener(view -> cellSelectListener.onCellHighlighted(position));
        } else {
            holder.myTextView.setOnClickListener(null);
        }

        if (this.gamePhase == GamePhase.REVIEW) {
            if (this.drawGridLines) {
                holder.myTextView.setBackgroundResource(nightMode ? R.drawable.border_white_on_black : R.drawable.border_black_on_white);
            } else {
                holder.myTextView.setBackgroundColor(nightMode ? Color.BLACK : Color.WHITE);
            }

            holder.myTextView.setLines(2);

            /* Double height of cells for review phase */
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) holder.myTextView.getLayoutParams();
            params.height = 2 * holder.myTextView.getContext().getResources().getDimensionPixelSize(R.dimen.numbers_grid_row_height);
            holder.myTextView.setLayoutParams(params);

            ReviewCellOutcome answerResult = testSheet.checkAnswerAt(position);

            String memoryText = testSheet.getMemoryText(position);
            String recallText = testSheet.getRecallText(position);

            SimpleSpanBuilder ssb = new SimpleSpanBuilder();
            switch (answerResult) {
                case BLANK:   ssb.append(dash,       redColorSpan); break;
                case WRONG:   ssb.append(recallText, redColorSpan, strikeThroughSpan); break;
                case CORRECT: ssb.append(recallText, greenColorSpan); break;
            }

            ssb.append(lineSeparator);

            ForegroundColorSpan standardTextColor = nightMode ? whiteColorSpan : blackColorSpan;
            switch (answerResult) {
                case BLANK:   ssb.append(memoryText, standardTextColor); break;
                case WRONG:   ssb.append(memoryText, standardTextColor); break;
                case CORRECT: ssb.append(memoryText, standardTextColor); break;
            }

            holder.myTextView.setText(ssb.build());
        }
        else if (this.gamePhase == GamePhase.MEMORIZATION && this.highlight == position) {
            holder.myTextView.setBackgroundResource(R.drawable.border_highlight_green);
            holder.myTextView.setTextColor(Color.WHITE);
            holder.myTextView.setText(testSheet.getMemoryText(position));
        }
        else if (this.gamePhase == GamePhase.RECALL && this.highlight == position) {
            holder.myTextView.setBackgroundResource(R.drawable.border_highlight_green_red_outline);
            holder.myTextView.setTextColor(Color.WHITE);
            holder.myTextView.setText(testSheet.getRecallText(position));
        }
        else {
            if (this.drawGridLines) {
                holder.myTextView.setBackgroundResource(nightMode ? R.drawable.border_white_on_black : R.drawable.border_black_on_white);
            } else {
                holder.myTextView.setBackgroundColor(nightMode ? Color.BLACK : Color.WHITE);
            }

            int standardTextColor = nightMode ? fadedWhite : fadedBlack;
            holder.myTextView.setTextColor(standardTextColor);
            if (this.gamePhase == GamePhase.RECALL) {
                holder.myTextView.setText(testSheet.getRecallText(position));
            } else {
	            holder.myTextView.setText(EMPTY_CHAR);
            }
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return testSheet.getDigitCount();
    }

    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.numberGridCell);
        }
    }
}