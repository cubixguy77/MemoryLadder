package com.memoryladder.taketest.namesandfaces.ui.adapters;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mastersofmemory.memoryladder.R;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.namesandfaces.models.NameAndFace;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Renders the Names and Faces
 */
public class FaceAdapter extends RecyclerView.Adapter<FaceAdapter.FaceViewHolder> {

    private final TestSheet testSheet;
    private GamePhase phase;

    public FaceAdapter(TestSheet testSheet) {
        this.testSheet = testSheet;
        setGamePhase(GamePhase.PRE_MEMORIZATION);
    }

    @NonNull
    @Override
    public FaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FaceViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_names_and_faces, parent, false),
                new MyCustomEditTextListener(),
                new MyCustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull FaceViewHolder holder, int position) {
        holder.bindTo(testSheet.getNameAndFace(position), position);
    }

    @Override
    public int getItemCount() {
        return testSheet.getFaceCount();
    }

    public void setGamePhase(GamePhase phase) {
        this.phase = phase;
        notifyDataSetChanged();
    }

    class FaceViewHolder extends RecyclerView.ViewHolder {
        private final MyCustomEditTextListener firstNameWatcher;
        private final MyCustomEditTextListener lastNameWatcher;

        @BindView(R.id.image_face) ImageView face;

        @BindView(R.id.name_first) TextView firstName;

        @BindView(R.id.names_faces_cell_recall_first) EditText firstNameInput;
        @BindView(R.id.names_faces_cell_recall_last) EditText lastNameInput;

        @BindView(R.id.names_faces_cell_review_container_first) LinearLayout reviewContainerFirst;
        @BindView(R.id.names_faces_cell_review_container_last) LinearLayout reviewContainerLast;

        @BindView(R.id.names_faces_cell_review_memory_first) TextView reviewFirstMemory;
        @BindView(R.id.names_faces_cell_review_recall_first) TextView reviewFirstRecall;
        @BindView(R.id.names_faces_cell_review_memory_last) TextView reviewLastMemory;
        @BindView(R.id.names_faces_cell_review_recall_last) TextView reviewLastRecall;

        FaceViewHolder(View itemView, MyCustomEditTextListener firstNameWatcher, MyCustomEditTextListener lastNameWatcher) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.firstNameWatcher = firstNameWatcher;
            this.lastNameWatcher = lastNameWatcher;

            firstNameInput.addTextChangedListener(firstNameWatcher);
            lastNameInput.addTextChangedListener(lastNameWatcher);
        }

        void bindTo(NameAndFace nameAndFace, int position) {
            if (firstNameWatcher != null) {
                firstNameWatcher.updatePosition(position, true);
                lastNameWatcher.updatePosition(position, false);
            }

            if (phase == GamePhase.PRE_MEMORIZATION) {
                face.setImageResource(R.drawable.face_placeholder);

                firstName.setText("");
                firstName.setVisibility(View.VISIBLE);

                firstNameInput.setVisibility(View.GONE);
                lastNameInput.setVisibility(View.GONE);

                reviewContainerFirst.setVisibility(View.GONE);
                reviewContainerLast.setVisibility(View.GONE);
            }
            else if (phase == GamePhase.MEMORIZATION) {
                face.setImageResource(nameAndFace.getImageId());

                firstName.setText(nameAndFace.getFirst() + " " + nameAndFace.getLast());
                firstName.setVisibility(View.VISIBLE);

                firstNameInput.setVisibility(View.GONE);
                lastNameInput.setVisibility(View.GONE);

                reviewContainerFirst.setVisibility(View.GONE);
                reviewContainerLast.setVisibility(View.GONE);

            }
            else if (phase == GamePhase.RECALL) {
                face.setImageResource(nameAndFace.getImageId());

                firstName.setText("");
                firstName.setVisibility(View.GONE);

                firstNameInput.setText(nameAndFace.getFirstRecall());
                lastNameInput.setText(nameAndFace.getLastRecall());

                firstNameInput.setVisibility(View.VISIBLE);
                lastNameInput.setVisibility(View.VISIBLE);

                reviewContainerFirst.setVisibility(View.GONE);
                reviewContainerLast.setVisibility(View.GONE);
            }
            else if (phase == GamePhase.REVIEW) {
                face.setImageResource(nameAndFace.getImageId());

                firstName.setText("");
                firstName.setVisibility(View.GONE);

                firstNameInput.setVisibility(View.GONE);
                lastNameInput.setVisibility(View.GONE);

                reviewContainerFirst.setVisibility(View.VISIBLE);
                reviewContainerLast.setVisibility(View.VISIBLE);

                reviewFirstMemory.setText(nameAndFace.getFirst());
                reviewFirstRecall.setText(nameAndFace.getFirstRecall());

                reviewLastMemory.setText(nameAndFace.getLast());
                reviewLastRecall.setText(nameAndFace.getLastRecall());

                ReviewCellOutcome firstResult = nameAndFace.getFirstResult();
                if (firstResult == ReviewCellOutcome.CORRECT) {
                    reviewFirstRecall.setBackgroundColor(Color.parseColor("#9FDC88"));
                }
                else if (firstResult == ReviewCellOutcome.BLANK) {
                    reviewFirstRecall.setBackgroundColor(Color.parseColor("#FFDFDD"));
                }
                else if (firstResult == ReviewCellOutcome.WRONG) {
                    reviewFirstRecall.setBackgroundColor(Color.parseColor("#FF5656"));
                }

                ReviewCellOutcome lastResult = nameAndFace.getLastResult();
                if (lastResult == ReviewCellOutcome.CORRECT) {
                    reviewLastRecall.setBackgroundColor(Color.parseColor("#9FDC88"));
                }
                else if (lastResult == ReviewCellOutcome.BLANK) {
                    reviewLastRecall.setBackgroundColor(Color.parseColor("#FFDFDD"));
                }
                else if (lastResult == ReviewCellOutcome.WRONG) {
                    reviewLastRecall.setBackgroundColor(Color.parseColor("#FF5656"));
                }
            }
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;
        private boolean isFirstName;

        void updatePosition(int position, boolean isFirstName) {
            this.position = position;
            this.isFirstName = isFirstName;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (isFirstName)
                testSheet.getNameAndFace(position).setFirstRecall(charSequence.toString());
            else
                testSheet.getNameAndFace(position).setLastRecall(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }
}