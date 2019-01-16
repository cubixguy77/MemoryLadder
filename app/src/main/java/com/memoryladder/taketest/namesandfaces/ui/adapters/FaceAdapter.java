package com.memoryladder.taketest.namesandfaces.ui.adapters;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Renders the word list in Mem phase
 */
public class FaceAdapter extends RecyclerView.Adapter<FaceAdapter.FaceViewHolder> {

    private TestSheet testSheet;
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

        @BindView(R.id.names_faces_cell_review_text_first) TextView reviewFirstName;
        @BindView(R.id.names_faces_cell_review_text_last) TextView reviewLastName;

        @BindView(R.id.names_faces_cell_review_icon_first) ImageView reviewFirstIcon;
        @BindView(R.id.names_faces_cell_review_icon_last) ImageView reviewLastIcon;

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
                firstName.setVisibility(View.GONE);

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

                ReviewCellOutcome firstResult = nameAndFace.getFirstResult();
                if (firstResult == ReviewCellOutcome.CORRECT) {
                    reviewFirstName.setText(nameAndFace.getFirst());
                    reviewFirstName.setTextColor(Color.WHITE);
                    reviewFirstIcon.setImageResource(R.drawable.icon_review_correct_white);
                }
                else if (firstResult == ReviewCellOutcome.BLANK) {
                    reviewFirstName.setText(nameAndFace.getFirst());
                    reviewFirstName.setTextColor(Color.LTGRAY);
                    reviewFirstIcon.setImageResource(R.drawable.icon_review_blank);
                }
                else if (firstResult == ReviewCellOutcome.WRONG) {
                    SimpleSpanBuilder ssb = new SimpleSpanBuilder();
                    ssb.append(nameAndFace.getFirstRecall(), new ForegroundColorSpan(Color.RED), new StrikethroughSpan());
                    ssb.append(" ");
                    ssb.append(nameAndFace.getFirst());
                    reviewFirstName.setText(ssb.build());
                    reviewFirstName.setTextColor(Color.WHITE);
                    reviewFirstIcon.setImageResource(R.drawable.icon_review_wrong);
                }

                ReviewCellOutcome lastResult = nameAndFace.getLastResult();
                if (lastResult == ReviewCellOutcome.CORRECT) {
                    reviewLastName.setText(nameAndFace.getLast());
                    reviewLastName.setTextColor(Color.WHITE);
                    reviewLastIcon.setImageResource(R.drawable.icon_review_correct_white);
                }
                else if (lastResult == ReviewCellOutcome.BLANK) {
                    reviewLastName.setText(nameAndFace.getLast());
                    reviewLastName.setTextColor(Color.LTGRAY);
                    reviewLastIcon.setImageResource(R.drawable.icon_review_blank);
                }
                else if (lastResult == ReviewCellOutcome.WRONG) {
                    SimpleSpanBuilder ssb = new SimpleSpanBuilder();
                    ssb.append(nameAndFace.getLastRecall(), new ForegroundColorSpan(Color.RED), new StrikethroughSpan());
                    ssb.append(" ");
                    ssb.append(nameAndFace.getLast());
                    reviewLastName.setText(ssb.build());
                    reviewLastName.setTextColor(Color.WHITE);
                    reviewLastIcon.setImageResource(R.drawable.icon_review_wrong);
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

    class SimpleSpanBuilder {
        private class SpanSection{
            private final String text;
            private final int startIndex;
            private final ParcelableSpan[] spans;

            SpanSection(String text, int startIndex, ParcelableSpan... spans){
                this.spans = spans;
                this.text = text;
                this.startIndex = startIndex;
            }

            void apply(SpannableStringBuilder spanStringBuilder){
                if (spanStringBuilder == null) return;
                for (ParcelableSpan span : spans){
                    spanStringBuilder.setSpan(span, startIndex, startIndex + text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
        }

        private List<SpanSection> spanSections;
        private StringBuilder stringBuilder;

        SimpleSpanBuilder(){
            stringBuilder = new StringBuilder();
            spanSections = new ArrayList<>();
        }

        SimpleSpanBuilder append(String text, ParcelableSpan... spans){
            if (spans != null && spans.length > 0) {
                spanSections.add(new SpanSection(text, stringBuilder.length(),spans));
            }
            stringBuilder.append(text);
            return this;
        }


        public SpannableStringBuilder build(){
            SpannableStringBuilder ssb = new SpannableStringBuilder(stringBuilder.toString());
            for (SpanSection section : spanSections){
                section.apply(ssb);
            }
            return ssb;
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }
    }
}