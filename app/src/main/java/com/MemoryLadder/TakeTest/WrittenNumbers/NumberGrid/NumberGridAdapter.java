package com.MemoryLadder.TakeTest.WrittenNumbers.NumberGrid;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.MemoryLadder.TakeTest.GamePhase;
import com.MemoryLadder.TakeTest.WrittenNumbers.Review.SimpleSpanBuilder;
import com.MemoryLadder.TakeTest.WrittenNumbers.WrittenNumberData;
import com.mastersofmemory.memoryladder.R;

public class NumberGridAdapter extends RecyclerView.Adapter<NumberGridAdapter.ViewHolder> {

    private WrittenNumberData mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public NumberGridAdapter(Context context, WrittenNumberData data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.viewgroup_numbers_grid_cell, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mData.getGamePhase() == GamePhase.REVIEW) {
            holder.myTextView.setBackgroundResource(R.drawable.border);
            holder.myTextView.setLines(2);

            /* Double height of cells for review phase */
            GridLayoutManager.LayoutParams params = (GridLayoutManager.LayoutParams) holder.myTextView.getLayoutParams();
            //Resources.getSystem().getDimensionPixelSize(R.dimen.numbers_grid_row_height_large);
            params.height = 180;
            holder.myTextView.setLayoutParams(params);

            int answerResult = mData.checkAnswerAt(position);

            String memoryText = mData.getMemoryText(position);
            String recallText = mData.getRecallText(position);

            SimpleSpanBuilder ssb = new SimpleSpanBuilder();
            switch (answerResult) {
                case -1: ssb.append("—",  new ForegroundColorSpan(Color.RED)); break;
                case  0: ssb.append(recallText,new ForegroundColorSpan(Color.RED), new StrikethroughSpan()); break;
                case  1: ssb.append(recallText,new ForegroundColorSpan(Color.GREEN)); break;
            }

            ssb.append(System.getProperty("line.separator"));

            switch (answerResult) {
                case -1: ssb.append(memoryText, new ForegroundColorSpan(Color.BLACK)); break;
                case  0: ssb.append(memoryText, new ForegroundColorSpan(Color.BLACK)); break;
                case  1: ssb.append(memoryText, new ForegroundColorSpan(Color.BLACK)); break;
            }

            holder.myTextView.setText(ssb.build());
        }
        else if ((mData.getGamePhase() == GamePhase.MEMORIZATION || mData.getGamePhase() == GamePhase.RECALL) && mData.isHighlighted(position)) {
            holder.myTextView.setBackgroundResource(R.drawable.border_highlight_green);
            holder.myTextView.setTextColor(Color.parseColor("#FFFFFF"));
            holder.myTextView.setText(mData.getText(position));
        }
        else {
            holder.myTextView.setBackgroundResource(R.drawable.border);
            holder.myTextView.setTextColor(Color.parseColor("#000000"));
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