package com.memoryladder.taketest.images.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.images.models.Image;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Renders the Images
 */
public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TestSheet testSheet;
    private GamePhase phase;

    private final int ROW_NUM_VIEW_TYPE = 0;
    private final int IMAGE_VIEW_TYPE = 1;

    public ImagesAdapter(TestSheet testSheet) {
        this.testSheet = testSheet;
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
            ((ImageViewHolder) holder).bindTo(testSheet.getImage(getRow(position), getCol(position), phase), getRow(position), getCol(position));
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

    class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_image) ImageView image;
        @BindView(R.id.image_index) TextView index;

        ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindTo(Image imageModel, int row, int column) {
            if (phase == GamePhase.PRE_MEMORIZATION) {
                this.image.setImageResource(R.drawable.face_placeholder);
                this.index.setText(Integer.toString(imageModel.getMemoryIndex()));
            } else if (phase == GamePhase.MEMORIZATION) {
                this.image.setImageResource(imageModel.getImageId());
                this.index.setText(Integer.toString(imageModel.getMemoryIndex()) + " " + imageModel.getRecallIndex());
            } else if (phase == GamePhase.RECALL) {
                this.image.setImageResource(imageModel.getImageId());
                this.index.setText(Integer.toString(imageModel.getMemoryIndex()) + " " + imageModel.getRecallIndex());
            } else if (phase == GamePhase.REVIEW) {
                this.image.setImageResource(imageModel.getImageId());
                this.index.setText(Integer.toString(imageModel.getMemoryIndex()) + " " + imageModel.getRecallIndex());
            }
        }
    }

    class RowNumViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_row_num) TextView rowNum;

        RowNumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindTo(int row) {
            rowNum.setText(Integer.toString(row));
        }
    }
}