package com.MemoryLadder.TakeTest.WrittenNumbers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.MemoryLadder.TakeTest.GameManager;
import com.MemoryLadder.TakeTest.GamePhase;
import com.MemoryLadder.TakeTest.ScorePanel.Score;
import com.MemoryLadder.TakeTest.Timer.TimerView;
import com.MemoryLadder.TakeTest.WrittenNumbers.Keyboard.KeyListener;
import com.MemoryLadder.TakeTest.WrittenNumbers.Keyboard.NumericKeyboardView;
import com.MemoryLadder.TakeTest.WrittenNumbers.NumberCarousel.CustomNumberCarousel;
import com.MemoryLadder.TakeTest.WrittenNumbers.NumberGrid.MaxHeightScrollView;
import com.MemoryLadder.TakeTest.WrittenNumbers.NumberGrid.NumberGridAdapter;
import com.MemoryLadder.Utils;
import com.mastersofmemory.memoryladder.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WrittenNumbersGameManager extends Fragment implements GameManager, KeyListener {

    private WrittenNumberData data;
    private WrittenNumbersSettings settings;

    private NumberGridAdapter adapter;

    @BindView(R.id.numbersFragmentContainer) LinearLayout root;
    @BindView(R.id.text_timer) TimerView timerView;
    @BindView(R.id.timerContainer) FrameLayout timerContainer;
    @BindView(R.id.keyboard) NumericKeyboardView keyboardView;
    @BindView(R.id.carousel) CustomNumberCarousel textCarousel;
    @BindView(R.id.numberGridContainer) MaxHeightScrollView numberGridContainer;
    @BindView(R.id.numberGrid) RecyclerView numberGrid;
    @BindView(R.id.navigatorLayout) FrameLayout navigatorLayout;
    @BindView(R.id.nextButton) AppCompatImageButton nextButton;
    @BindView(R.id.buttonToggleNightMode) Button nightModeIcon;
    @BindView(R.id.buttonToggleGridLines) Button gridLinesIcon;

    public static WrittenNumbersGameManager newInstance(WrittenNumbersSettings settings) {
        WrittenNumbersGameManager cardGameManager = new WrittenNumbersGameManager();

        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        cardGameManager.setArguments(args);

        return cardGameManager;
    }

    public WrittenNumbersGameManager() {}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("gameData", data);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewgroup_written_numbers_arena, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            settings = getArguments().getParcelable("settings");
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated()");

        if (savedInstanceState != null) {
            this.data = savedInstanceState.getParcelable("gameData");
            this.data.setKeepHighlightPos(true);
            resetGrid();
        }
    }

    private void resetGrid() {
        adapter = new NumberGridAdapter(getActivity(), data);
        numberGrid.setLayoutManager(new GridLayoutManager(getContext(), settings.getNumCols()));
        numberGrid.setAdapter(adapter);
    }

    @Override
    public void setGamePhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            data = new WrittenNumberData(settings);
            resetGrid();
            refreshCarousel();
        } else {
            data.setGamePhase(phase);

            if (!data.isKeepHighlightPos()) {
                data.resetHighlight();
            } else {
                data.setKeepHighlightPos(false);
            }

            adapter.notifyDataSetChanged();
        }

        render(phase);

        if (phase == GamePhase.MEMORIZATION) {
            refreshCarousel();
        }
        else if (phase == GamePhase.RECALL) {
            refreshCarousel();
            keyboardView.setKeyListener(this);
        }
        else if (phase == GamePhase.REVIEW) {
            keyboardView.setKeyListener(null);
        }
    }

    @Override
    public void render(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            textCarousel.hide();
            keyboardView.hide();
            navigatorLayout.setVisibility(View.GONE);
            timerView.show();
            nightModeIcon.setVisibility(View.VISIBLE);
            gridLinesIcon.setVisibility(View.VISIBLE);
            setGridHeightSmall();
        }
        else if (phase == GamePhase.MEMORIZATION) {
            textCarousel.show();

            if (textCarousel.isExpanded()) {
                setGridHeightSmall();
            } else {
                setGridHeightLarge();
            }

            navigatorLayout.setVisibility(View.VISIBLE);
        }
        else if (phase == GamePhase.RECALL) {
            numberGrid.smoothScrollToPosition(0);
            navigatorLayout.setVisibility(View.GONE);
            keyboardView.show();
            //textCarousel.hide(); // TODO: only hide in landscape phones

            if (textCarousel.isExpanded()) {
                setGridHeightSmall();
            } else {
                setGridHeightLarge();
            }
        }
        else if (phase == GamePhase.REVIEW) {
            numberGrid.smoothScrollToPosition(0);
            textCarousel.hide();
            keyboardView.hide();
            timerView.hide();
            nightModeIcon.setVisibility(View.INVISIBLE);
            gridLinesIcon.setVisibility(View.INVISIBLE);
            navigatorLayout.setVisibility(View.GONE);
            setGridHeightLarge();
        }
    }

    private void setGridHeightLarge() {
        int largeHeight = root.getHeight() -
                (data.getGamePhase() == GamePhase.REVIEW ? (getResources().getDimensionPixelSize(R.dimen.score_panel_height)) : 0) -
                (timerContainer.getHeight()) -
                (textCarousel.getVisibleHeight()) -
                (keyboardView.getVisibility() == View.VISIBLE ? (getResources().getDimensionPixelSize(R.dimen.numeric_keyboard_layout_height)) : 0) -
                (navigatorLayout.getVisibility() == View.VISIBLE ? navigatorLayout.getHeight() : 0);
        numberGridContainer.setMaxHeight(largeHeight);
        adapter.notifyDataSetChanged();
    }

    private void setGridHeightSmall() {
        int maxLines = getResources().getInteger(R.integer.numbers_grid_maxLines);
        int rowHeight = getResources().getDimensionPixelSize(R.dimen.numbers_grid_row_height);
        int smallHeight = Utils.lesserOf(settings.getNumRows(), maxLines) * rowHeight;
        numberGridContainer.setMaxHeight(smallHeight);
        adapter.notifyDataSetChanged();
    }



    private void refreshCarousel() {
        String currentText = data.getGroupText(data.getHighlightPos());
        textCarousel.display(data.getPreviousGroupText(), currentText, data.getNextGroupText());
        textCarousel.setRowNum(data.getHighlightRowNumBegin(), data.getHighlightRowNumEnd(), settings.getNumRows());

        if (settings.isMnemonicsEnabled() && data.getGamePhase() == GamePhase.MEMORIZATION && data.getDigitsPerGroup() == 2) {
            textCarousel.setMnemo(getMnemo(currentText));
        } else {
            textCarousel.hideMnemo();
        }
    }

    private String getMnemo(String text) {
        SharedPreferences prefs = getContext().getSharedPreferences("Peg_Numbers", 0);
        int sanitizedNumber = Integer.parseInt(text);
        return prefs.getString("peg_numbers_" + sanitizedNumber, Utils.getNumberSuggestions(sanitizedNumber)[0]);
    }

    @Override
    public void displayTime(int secondsRemaining) {
        timerView.displayTime(secondsRemaining);
    }

    @Override
    public Score getScore() {
        return data.getScore();
    }

    /* Expands or collapses the carousel */
    @OnClick(R.id.closeButton) void toggleCarousel() {
        if (textCarousel.isExpanded()) {
            textCarousel.toggle();
            setGridHeightLarge();
        }
        else {
            setGridHeightSmall();
            textCarousel.toggle();
        }
    }

    @OnClick(R.id.prevButton) void onPrevClick() {
        if (!textCarousel.animationsInProgress() && data.allowPrev()) {
            data.highlightPrevGroup();
            adapter.notifyItemRangeChanged(data.getHighlightPos(), data.getDigitsPerGroup() * 2);
            refreshCarousel();
            //textCarousel.transitionBackward(data.getNextGroupText());
        }
    }

    @OnClick(R.id.nextButton) void onNextClick() {
        if (!textCarousel.animationsInProgress() && data.allowNext()) {
            data.highlightNextGroup();
            adapter.notifyItemRangeChanged(data.getHighlightPos() - data.getDigitsPerGroup(), data.getDigitsPerGroup() * 2);
            refreshCarousel();
            numberGrid.smoothScrollToPosition(Utils.lesserOf(data.getHighlightPos() + settings.getNumCols(), adapter.getItemCount()-1));
            //numberGrid.smoothScrollToPosition(data.getHighlightPosEnd());
            //numberGrid.smoothScrollToPosition(adapter.getItemCount() - 1);
            //numberGrid.smoothScrollBy(0, getResources().getDimensionPixelSize(R.dimen.numbers_grid_row_height));
            //textCarousel.transitionForward(data.getNextGroupText());
        }
    }

    private void refreshNightModeIcon() {
        nightModeIcon.setBackgroundResource(adapter.isNightMode() ? R.drawable.icon_night_mode_on : R.drawable.icon_night_mode_off);
    }

    private void refreshGridLinesIcon() {
        gridLinesIcon.setBackgroundResource(adapter.isDrawGridLines() ? R.drawable.icon_gridlines_on : R.drawable.icon_gridlines_off);
    }

    @OnClick(R.id.buttonToggleNightMode) void onToggleNightMode() {
        adapter.toggleNightMode();
        refreshNightModeIcon();
    }

    @OnClick(R.id.buttonToggleGridLines) void onToggleDrawGridLines() {
        adapter.toggleDrawGridLines();
        refreshGridLinesIcon();
    }


    @Override
    public void onDigit(char key) {
        data.registerRecall(key);
        onForward();
        refreshCarousel();
    }

    @Override
    public void onBack() {
        retreatTextEntryPos();
    }

    @Override
    public void onForward() {
        advanceTextEntryPos();
    }

    @Override
    public void onBackspace() {
        data.registerRecall(WrittenNumberData.EMPTY_CHAR);
        onBack();
        refreshCarousel();
    }




    private void retreatTextEntryPos() {
        boolean decrementGroup = data.highlightPrevCell();
        int textEntryPos = data.getTextEntryPos();

        if (decrementGroup) {
            onPrevClick();
            data.setTextEntryPos(textEntryPos);
            adapter.notifyItemRangeChanged(data.getTextEntryPos(), 1);
        }
        else {
            adapter.notifyItemRangeChanged(data.getTextEntryPos(), 2);
        }
    }

    private void advanceTextEntryPos() {
        boolean advanceGroup = data.highlightNextCell();
        adapter.notifyItemRangeChanged(data.getTextEntryPos()-1, 2);

        if (advanceGroup) {
            onNextClick();
        }
    }


}
