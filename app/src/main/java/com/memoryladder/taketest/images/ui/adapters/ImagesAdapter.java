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
public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImageViewHolder> {

    private TestSheet testSheet;
    private GamePhase phase;

    public ImagesAdapter(TestSheet testSheet) {
        this.testSheet = testSheet;
        setGamePhase(GamePhase.PRE_MEMORIZATION);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_images, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.bindTo(testSheet.getImage(position), position);
    }

    @Override
    public int getItemCount() {
        return testSheet.getRowCount();
    }

    public void setGamePhase(GamePhase phase) {
        this.phase = phase;
        notifyDataSetChanged();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_image) ImageView image;
        @BindView(R.id.image_index) TextView index;

        ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindTo(Image image, int position) {
            if (phase == GamePhase.PRE_MEMORIZATION) {
                this.image.setImageResource(R.drawable.face_placeholder);
                this.index.setText(position);
            }
            else if (phase == GamePhase.MEMORIZATION) {
                this.image.setImageResource(image.getImageId());
                this.index.setText(position);
            }
            else if (phase == GamePhase.RECALL) {
                this.image.setImageResource(image.getImageId());
                this.index.setText(position);
            }
            else if (phase == GamePhase.REVIEW) {
                this.image.setImageResource(image.getImageId());
                this.index.setText(position);
            }
        }
    }
}