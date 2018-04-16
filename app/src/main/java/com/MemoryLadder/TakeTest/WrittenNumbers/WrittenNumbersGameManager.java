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
import android.widget.Space;

import com.MemoryLadder.TakeTest.GameManager;
import com.MemoryLadder.TakeTest.GamePhase;
import com.MemoryLadder.TakeTest.ScorePanel.Score;
import com.MemoryLadder.TakeTest.Timer.TimerView;
import com.MemoryLadder.TakeTest.WrittenNumbers.Keyboard.KeyListener;
import com.MemoryLadder.TakeTest.WrittenNumbers.Keyboard.NumericKeyboardView;
import com.MemoryLadder.TakeTest.WrittenNumbers.NumberCarousel.CustomNumberCarousel;
import com.MemoryLadder.TakeTest.WrittenNumbers.NumberGrid.NumberGridAdapter;
import com.MemoryLadder.Utils;
import com.mastersofmemory.memoryladder.BuildConfig;
import com.mastersofmemory.memoryladder.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WrittenNumbersGameManager extends Fragment implements GameManager, KeyListener {

    private WrittenNumberData data;
    private WrittenNumbersSettings settings;
    private int rowHeight = 0;

    private NumberGridAdapter adapter;

    @BindView(R.id.numbersFragmentContainer) LinearLayout root;
    @BindView(R.id.text_timer) TimerView timerView;
    @BindView(R.id.timerContainer) FrameLayout timerContainer;
    @BindView(R.id.keyboard) NumericKeyboardView keyboardView;
    @BindView(R.id.carousel) CustomNumberCarousel textCarousel;
    @BindView(R.id.numberGrid) RecyclerView numberGrid;
    @BindView(R.id.navigatorLayout) FrameLayout navigatorLayout;
    @BindView(R.id.nextButton) AppCompatImageButton nextButton;
    @BindView(R.id.buttonToggleNightMode) Button nightModeIcon;
    @BindView(R.id.buttonToggleGridLines) Button gridLinesIcon;
    @BindView(R.id.bottomSpacer) Space bottomSpacer;

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
        System.out.println("onCreateView()");
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

            if (this.data != null) {
                this.data.setKeepHighlightPos(true);
            }

            resetGrid();
        }
    }

    private void resetGrid() {
        adapter = new NumberGridAdapter(getActivity(), data, settings.isNightMode(), settings.isDrawGridLines());
        numberGrid.setLayoutManager(new GridLayoutManager(getContext(), settings.getNumCols()));
        numberGrid.setAdapter(adapter);
    }

    private void generateDataModel() {
        data = new WrittenNumberData(
                settings,
                BuildConfig.DEBUG ?
                        MemoryDataSetFactory.getOrderedDecimalNumberSet(settings.getNumDigits()) :
                        MemoryDataSetFactory.getRandomizedDecimalNumberSet(settings.getNumDigits()));
    }

    @Override
    public void setGamePhase(GamePhase phase) {
        System.out.println("setGamePhase(" + phase.toString() + ")");

        if (phase == GamePhase.PRE_MEMORIZATION) {
            generateDataModel();
            resetGrid();
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
        refreshCarousel();

        if (phase == GamePhase.MEMORIZATION) {
            refreshCarousel();
        }
        else if (phase == GamePhase.RECALL) {
            keyboardView.setKeyListener(this);
        }
        else if (phase == GamePhase.REVIEW) {
            keyboardView.setKeyListener(null);
        }

        scrollToRow(data.getRow(data.getHighlightPosEnd()) - 1); // Restores scroll to highlighted digits on rotation, and scrolls back to top after phase changes
    }

    @Override
    public void render(GamePhase phase) {
        refreshNightModeIcon();
        refreshGridLinesIcon();

        if (phase == GamePhase.PRE_MEMORIZATION) {
            textCarousel.show();
            keyboardView.hide();
            navigatorLayout.setVisibility(View.VISIBLE);
            timerView.show();
            nightModeIcon.setVisibility(View.VISIBLE);
            gridLinesIcon.setVisibility(View.VISIBLE);
            bottomSpacer.setVisibility(View.GONE);
            timerContainer.setVisibility(View.VISIBLE);
        }
        else if (phase == GamePhase.MEMORIZATION) {
            textCarousel.show();
            navigatorLayout.setVisibility(View.VISIBLE);

            if (shouldShowMnemonics(settings.isMnemonicsEnabled(), GamePhase.MEMORIZATION, settings.getDigitsPerGroup())) {
                textCarousel.showMnemo();
            } else {
                textCarousel.hideMnemo();
            }
        }
        else if (phase == GamePhase.RECALL) {
            numberGrid.smoothScrollToPosition(0);
            navigatorLayout.setVisibility(View.GONE);
            keyboardView.show();

            if (!getResources().getBoolean(R.bool.numbers_carousel_recall_display)) {
                textCarousel.hide();
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
            bottomSpacer.setVisibility(View.VISIBLE);
            timerContainer.setVisibility(View.GONE);
        }
    }

    private static boolean shouldShowMnemonics(boolean isMnemonicsEnabled, GamePhase phase, int digitsPerGroup) {
        return isMnemonicsEnabled && phase == GamePhase.MEMORIZATION && digitsPerGroup == 2;
    }

    private void refreshCarousel() {
        String currentText = data.getGroupText(data.getHighlightPos());
        textCarousel.display(data.getPreviousGroupText(), currentText, data.getNextGroupText());
        textCarousel.setRowNum(data.getHighlightRowNumBegin(), data.getHighlightRowNumEnd(), settings.getNumRows());

        if (shouldShowMnemonics(settings.isMnemonicsEnabled(), data.getGamePhase(), settings.getDigitsPerGroup())) {
            textCarousel.setMnemoText(getMnemo(currentText));
        } else {
            textCarousel.hideMnemo();
        }

        if (textCarousel.isExpanded()) {
            textCarousel.setHeight(getLargeCarouselHeight());
        }
    }

    private String getMnemo(String text) {
        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("Peg_Numbers", 0);
        int sanitizedNumber = Integer.parseInt(text);
        return prefs.getString("peg_numbers_" + sanitizedNumber, Objects.requireNonNull(Utils.getNumberSuggestions(sanitizedNumber))[0]);
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
            textCarousel.collapse();
        }
        else {
            textCarousel.expand();
            textCarousel.setHeight(getLargeCarouselHeight());
        }
    }

    @OnClick(R.id.prevButton) void onPrevClick() {
        if (!textCarousel.animationsInProgress() && data.allowPrev()) {
            int curRow = data.getRow(data.getHighlightPosEnd());
            data.highlightPrevGroup();
            int nextRow = data.getRow(data.getHighlightPos());

            adapter.notifyItemRangeChanged(data.getHighlightPos(), data.getDigitsPerGroup() * 2);
            refreshCarousel();

            if (curRow != nextRow && shouldScroll(data.getRow(data.getHighlightPos()), false)) {
                scroll(false);
            }

            //textCarousel.transitionBackward(data.getNextGroupText());
        }
    }

    @OnClick(R.id.nextButton) void onNextClick() {
        if (!textCarousel.animationsInProgress() && data.allowNext()) {

            int curRow = data.getRow(data.getHighlightPosEnd());
            data.highlightNextGroup();
            int nextRow = data.getRow(data.getHighlightPosEnd());

            adapter.notifyItemRangeChanged(data.getHighlightPos() - data.getDigitsPerGroup(), data.getDigitsPerGroup() * 2);
            refreshCarousel();

            if (curRow != nextRow && shouldScroll(data.getRow(data.getHighlightPosEnd()), true)) {
                scroll(true);
            }
        }
    }

    private void scroll(boolean down) {
        int rowHeight = getRowHeight();

        /* Resolves scrolling bug, see https://stackoverflow.com/questions/46156882/nestedscrollviews-fullscrollview-focus-up-not-working-properly */
        numberGrid.fling(0, 0);
        numberGrid.smoothScrollBy(0, down ? rowHeight : -rowHeight);
    }

    private void scrollToRow(int row) {
        numberGrid.post(() -> {
            numberGrid.fling(0, 0);
            numberGrid.scrollTo(0, getRowHeight() * row);
        });
    }

    private int getNumRowsVisibleCondensedGrid() {
        return getResources().getInteger(R.integer.numbers_grid_maxLines);
    }

    private int getLargeCarouselHeight() {
        return root.getMeasuredHeight() -
                (timerContainer.getHeight()) -
                (Utils.lesserOf(settings.getNumRows(), getNumRowsVisibleCondensedGrid()) * getRowHeight()) -
                (keyboardView.getVisibility() == View.VISIBLE ? (getResources().getDimensionPixelSize(R.dimen.numeric_keyboard_layout_height)) : 0) -
                (navigatorLayout.getVisibility() == View.VISIBLE ? navigatorLayout.getHeight() : 0);
    }

    private int getRowHeight() {
        if (rowHeight > 0)
            return rowHeight;

        return rowHeight = getResources().getDimensionPixelSize(R.dimen.numbers_grid_row_height) + getResources().getDimensionPixelSize(R.dimen.numbers_grid_row_spacing);
    }

    private int getFirstVisibleRowNum() {
        return numberGrid.computeVerticalScrollOffset() / getRowHeight();
    }

    private int getLastVisibleRowNum() {
        return getFirstVisibleRowNum() + getNumVisibleRows() - 1;
    }

    private int getNumVisibleRows() {
        return numberGrid.getHeight() / getRowHeight();
    }

    private boolean shouldScroll(int toRow, boolean scrollDown) {
        int numVisibleRows = getNumVisibleRows();
        boolean scrollToRevealNextRow = numVisibleRows > 2;

        if (scrollDown) {
            return toRow + (scrollToRevealNextRow ? 1 : 0) > (getLastVisibleRowNum());
        } else {
            return toRow + (scrollToRevealNextRow ? -1 : 0) < (getFirstVisibleRowNum());
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
        settings.setNightMode(adapter.isNightMode());

        SharedPreferences.Editor editor = Objects.requireNonNull(getContext()).getSharedPreferences("Number_Preferences", 0).edit();
        editor.putBoolean("WRITTEN_nightMode", adapter.isNightMode());
        editor.apply();

        refreshNightModeIcon();
    }

    @OnClick(R.id.buttonToggleGridLines) void onToggleDrawGridLines() {
        adapter.toggleDrawGridLines();
        settings.setDrawGridLines(adapter.isDrawGridLines());

        SharedPreferences.Editor editor = Objects.requireNonNull(getContext()).getSharedPreferences("Number_Preferences", 0).edit();
        editor.putBoolean("WRITTEN_drawGridLines", adapter.isDrawGridLines());
        editor.apply();

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
