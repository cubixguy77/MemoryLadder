package com.memoryladder.taketest.images.ui.adapters;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.images.models.Image;
import com.memoryladder.taketest.images.models.TestSheet;
import com.memoryladder.taketest.numbers.written.Review.SimpleSpanBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Renders the Images
 */
public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {

    private TestSheet testSheet;
    private GamePhase phase;
    private final OnStartDragListener mDragStartListener;
    private final GridLayoutManager layoutManager;

    private final int ROW_NUM_VIEW_TYPE = 0;
    private final int IMAGE_VIEW_TYPE = 1;

    private static final ForegroundColorSpan redColorSpan = new ForegroundColorSpan(0xFFFF7272);
    private static final ForegroundColorSpan greenColorSpan = new ForegroundColorSpan(0xFF9FDC88);
    private static final ForegroundColorSpan greyColorSpan = new ForegroundColorSpan(0xFF7a7f91);
    private static final StrikethroughSpan strikeThroughSpan = new StrikethroughSpan();

    public ImagesAdapter(TestSheet testSheet, OnStartDragListener dragStartListener, GridLayoutManager layoutManager) {
        this.testSheet = testSheet;
        this.mDragStartListener = dragStartListener;
        this.layoutManager = layoutManager;
        setGamePhase(GamePhase.PRE_MEMORIZATION);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ROW_NUM_VIEW_TYPE) {
            return new RowNumViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_row_num, parent, false));
        }
        else {
            return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_images, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ROW_NUM_VIEW_TYPE) {
            ((RowNumViewHolder) holder).bindTo(getRow(position) + 1);
        }
        else if (holder.getItemViewType() == IMAGE_VIEW_TYPE) {
            Image image = phase == GamePhase.RECALL ?
                    testSheet.getRecallImage(getRow(position), getCol(position)) :
                    testSheet.getMemoryImage(getRow(position), getCol(position));
            ((ImageViewHolder) holder).bindTo(image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getCol(position) == 0 ? ROW_NUM_VIEW_TYPE : IMAGE_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
        return testSheet.getImageCount();
    }

    public void setGamePhase(GamePhase phase) {
        this.phase = phase;
        notifyDataSetChanged();
    }

    private int getRow(int position) {
        return position / 6;
    }

    private int getCol(int position) {
        return position % 6;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition == toPosition)
            return true;

        testSheet.swapImages(getRow(fromPosition), getCol(fromPosition), getCol(toPosition));
        notifyItemMoved(fromPosition, toPosition);

        View holder1 = layoutManager.findViewByPosition(fromPosition);
        View holder2 = layoutManager.findViewByPosition(toPosition);
        if (holder1 == null || holder2 == null) {
            notifyItemRangeChanged(fromPosition < toPosition ? fromPosition : toPosition, 2);
        }
        else {
            TextView index1  = holder1.findViewById(R.id.image_index);
            TextView index2  = holder2.findViewById(R.id.image_index);
            index1.setText(Integer.toString(getCol(toPosition)));
            index2.setText(Integer.toString(getCol(fromPosition)));
        }

        return true;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @BindView(R.id.image_image) ImageView image;
        @BindView(R.id.image_index) TextView index;

        @Override
        public void onItemSelected() {
            Resources res = itemView.getContext().getResources();
            itemView.setBackgroundColor(res.getColor(R.color.images_list_item_background_selected));
        }

        @Override
        public void onItemClear() {
            Resources res = itemView.getContext().getResources();
            itemView.setBackgroundColor(res.getColor(R.color.images_list_item_background));
        }

        ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
        void bindTo(Image imageModel) {
            image.setOnTouchListener((v, event) -> {
                if (phase == GamePhase.RECALL && event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(this);
                }
                return false;
            });

            if (phase == GamePhase.PRE_MEMORIZATION) {
                this.image.setImageResource(R.drawable.image_placeholder);
                this.index.setText(Integer.toString(imageModel.getMemoryIndex()));
            } else if (phase == GamePhase.MEMORIZATION) {
                this.image.setImageResource(imageModel.getImageId());
                this.index.setText(Integer.toString(imageModel.getMemoryIndex()));
            } else if (phase == GamePhase.RECALL) {
                if (this.image.getTag() == null || (int) this.image.getTag() != imageModel.getImageId()) {
                    this.image.setImageResource(imageModel.getImageId());
                    this.image.setTag(imageModel.getImageId());
                }
                this.index.setText(Integer.toString(imageModel.getRecallIndex()));
            } else if (phase == GamePhase.REVIEW) {
                this.image.setImageResource(imageModel.getImageId());

                if (imageModel.getResult() == ReviewCellOutcome.CORRECT) {
                    Resources res = itemView.getContext().getResources();
                    itemView.setBackgroundColor(res.getColor(R.color.gradeCorrect));

                    SimpleSpanBuilder ssb = new SimpleSpanBuilder();
                    ssb.append(Integer.toString(imageModel.getRecallIndex()));
                    ssb.append(" ");
                    ssb.append("✓", greenColorSpan);
                    this.index.setText(ssb.build());
                }
                else if (imageModel.getResult() == ReviewCellOutcome.WRONG) {
                    Resources res = itemView.getContext().getResources();
                    itemView.setBackgroundColor(res.getColor(R.color.gradeWrong));

                    SimpleSpanBuilder ssb = new SimpleSpanBuilder();
                    ssb.append(Integer.toString(imageModel.getRecallIndex()), redColorSpan, strikeThroughSpan);
                    ssb.append(" ");
                    ssb.append(Integer.toString(imageModel.getMemoryIndex()), redColorSpan);
                    this.index.setText(ssb.build());
                }
                else {
                    Resources res = itemView.getContext().getResources();
                    itemView.setBackgroundColor(res.getColor(R.color.gradeBlank));

                    SimpleSpanBuilder ssb = new SimpleSpanBuilder();
                    ssb.append(Integer.toString(imageModel.getMemoryIndex()), greyColorSpan);
                    this.index.setText(ssb.build());
                }
            }
        }
    }

    class RowNumViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_row_num) TextView rowNum;

        RowNumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("SetTextI18n")
        void bindTo(int row) {
            rowNum.setText(Integer.toString(row));
        }
    }
}