package com.memoryladder.taketest.writtennumbers.NumberGrid;

import android.content.Context;
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

import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.writtennumbers.CellSelectListener;
import com.memoryladder.taketest.writtennumbers.Review.SimpleSpanBuilder;
import com.memoryladder.taketest.writtennumbers.WrittenNumberData;
import com.mastersofmemory.memoryladder.R;

public class NumberGridAdapter extends RecyclerView.Adapter<NumberGridAdapter.ViewHolder> {

    private final CellSelectListener cellSelectListener;
    private WrittenNumberData mData;
    private LayoutInflater mInflater;
    private boolean nightMode;
    private boolean drawGridLines;

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

    // data is passed into the constructor
    public NumberGridAdapter(Context context, CellSelectListener cellSelectListener, WrittenNumberData data, boolean nightMode, boolean drawGridLines) {
        this.mInflater = LayoutInflater.from(context);
        this.cellSelectListener = cellSelectListener;
        this.mData = data;
        this.nightMode = nightMode;
        this.drawGridLines = drawGridLines;
    }

    public boolean isNightMode() {
        return this.nightMode;
    }

    public boolean isDrawGridLines() {
        return this.drawGridLines;
    }

    public void toggleNightMode() {
        this.nightMode = !nightMode;
        notifyDataSetChanged();
    }

    public void toggleDrawGridLines() {
        this.drawGridLines = !drawGridLines;
        notifyDataSetChanged();
    }

    // inflates the cell layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.viewgroup_numbers_grid_cell, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (mData.getGamePhase() == GamePhase.MEMORIZATION || mData.getGamePhase() == GamePhase.RECALL) {
            holder.myTextView.setOnClickListener(view -> cellSelectListener.onCellHighlighted(position));
        } else {
            holder.myTextView.setOnClickListener(null);
        }

        if (mData.getGamePhase() == GamePhase.REVIEW) {
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

            int answerResult = mData.checkAnswerAt(position);

            String memoryText = mData.getMemoryText(position);
            String recallText = mData.getRecallText(position);

            SimpleSpanBuilder ssb = new SimpleSpanBuilder();
            switch (answerResult) {
                case -1: ssb.append(dash,   redColorSpan); break;
                case  0: ssb.append(recallText, redColorSpan, strikeThroughSpan); break;
                case  1: ssb.append(recallText, greenColorSpan); break;
            }

            ssb.append(lineSeparator);

            ForegroundColorSpan standardTextColor = nightMode ? whiteColorSpan : blackColorSpan;
            switch (answerResult) {
                case -1: ssb.append(memoryText, standardTextColor); break;
                case  0: ssb.append(memoryText, standardTextColor); break;
                case  1: ssb.append(memoryText, standardTextColor); break;
            }

            holder.myTextView.setText(ssb.build());
        }
        else if (mData.getGamePhase() == GamePhase.RECALL && mData.isTextHighlighted(position)) {
            holder.myTextView.setBackgroundResource(R.drawable.border_highlight_green_red_outline);
            holder.myTextView.setTextColor(Color.WHITE);
            holder.myTextView.setText(mData.getText(position));
        }
        else if ((mData.getGamePhase() == GamePhase.MEMORIZATION || mData.getGamePhase() == GamePhase.RECALL) && mData.isHighlighted(position)) {
            holder.myTextView.setBackgroundResource(R.drawable.border_highlight_green);
            holder.myTextView.setTextColor(Color.WHITE);
            holder.myTextView.setText(mData.getText(position));
        }
        else {
            if (this.drawGridLines) {
                holder.myTextView.setBackgroundResource(nightMode ? R.drawable.border_white_on_black : R.drawable.border_black_on_white);
            } else {
                holder.myTextView.setBackgroundColor(nightMode ? Color.BLACK : Color.WHITE);
            }

            int standardTextColor = nightMode ? fadedWhite : fadedBlack;
            holder.myTextView.setTextColor(standardTextColor);
            holder.myTextView.setText(mData.getText(position));
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.getNumDigits();
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