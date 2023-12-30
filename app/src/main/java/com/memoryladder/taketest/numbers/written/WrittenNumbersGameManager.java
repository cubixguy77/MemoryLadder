package com.memoryladder.taketest.numbers.written;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.memoryladder.taketest.GameManager;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.numbers.CellSelectListener;
import com.memoryladder.taketest.numbers.MemoryDataSetFactory;
import com.memoryladder.taketest.scorepanel.Score;
import com.memoryladder.taketest.timer.TimerView;
import com.memoryladder.taketest.numbers.written.keyboard.KeyListener;
import com.memoryladder.taketest.numbers.written.keyboard.NumericKeyboardView;
import com.memoryladder.taketest.numbers.written.numbercarousel.CustomNumberCarousel;
import com.memoryladder.taketest.numbers.written.numbergrid.NumberGridAdapter;
import com.memoryladder.Utils;
import com.mastersofmemory.memoryladder.BuildConfig;
import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.ViewgroupWrittenNumbersArenaBinding;

import java.util.Objects;public class WrittenNumbersGameManager extends Fragment implements GameManager, KeyListener, CellSelectListener {
    private WrittenNumberData data;
    private WrittenNumbersSettings settings;
    private int rowHeight = 0;
    private static final String TAG = "WrittenNumbersFrag";
    private NumberGridAdapter adapter;
    private LinearLayout root;
    private TimerView timerView;
    private FrameLayout timerContainer;
    private NumericKeyboardView keyboardView;
    private CustomNumberCarousel textCarousel;
    private RecyclerView numberGrid;
    private FrameLayout navigatorLayout;
    private ImageView nightModeIcon;
    private ImageView gridLinesIcon;

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
        Log.d(TAG, "onCreateView()");

        com.mastersofmemory.memoryladder.databinding.ViewgroupWrittenNumbersArenaBinding binding = ViewgroupWrittenNumbersArenaBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        root = binding.numbersFragmentContainer;
        timerView = root.findViewById(R.id.text_timer);
        timerContainer = binding.timerContainer;
        keyboardView = binding.keyboard.getRoot();
        textCarousel = binding.carousel.getRoot();
        numberGrid = binding.numberGrid;
        navigatorLayout = binding.navigatorLayout.getRoot();
        AppCompatImageButton nextButton = root.findViewById(R.id.nextButton);
        nightModeIcon = binding.buttonToggleNightMode;
        gridLinesIcon = binding.buttonToggleGridLines;

        root.findViewById(R.id.closeButton).setOnClickListener(v -> toggleCarousel());
        root.findViewById(R.id.prevButton).setOnClickListener(v -> onPrevClick());
        nextButton.setOnClickListener(v -> onNextClick());
        root.findViewById(R.id.buttonToggleNightMode).setOnClickListener(v -> onToggleNightMode());
        root.findViewById(R.id.buttonToggleGridLines).setOnClickListener(v -> onToggleDrawGridLines());

        if (getArguments() != null) {
            settings = getArguments().getParcelable("settings");
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");

        if (savedInstanceState != null) {
            this.data = savedInstanceState.getParcelable("gameData");

            if (this.data != null) {
                this.data.setKeepHighlightPos(true);
            }

            resetGrid();
        }
    }

    private void resetGrid() {
        adapter = new NumberGridAdapter(getActivity(), this, data, settings.isNightMode(), settings.isDrawGridLines());
        numberGrid.setLayoutManager(new GridLayoutManager(getContext(), settings.getNumCols()));
        numberGrid.setAdapter(adapter);
    }

    private void generateDataModel() {
        data = new WrittenNumberData(settings, MemoryDataSetFactory.getNumberDataSet(settings.getNumDigits(), settings.getBase(), BuildConfig.DEBUG, false));
    }

    @Override
    public void setGamePhase(GamePhase phase) {
        Log.d(TAG, "setGamePhase()");

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

        restoreScrollPosition(data.getHighlightPos());
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
            keyboardView.show(settings.getBase());

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
        SharedPreferences prefs = requireContext().getSharedPreferences("Peg_Numbers", 0);
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
    void toggleCarousel() {
        if (textCarousel.isExpanded()) {
            textCarousel.collapse();
        }
        else {
            textCarousel.expand();
            textCarousel.setHeight(getLargeCarouselHeight());
        }
    }

    void onPrevClick() {
        onCellHighlighted(data.getHighlightPos() - data.getDigitsPerGroup());
    }

    void onNextClick() {
        onCellHighlighted(data.getHighlightPos() + data.getDigitsPerGroup());
    }

    private void scroll(boolean down) {
        numberGrid.smoothScrollBy(0, down ? getRowHeight() : -getRowHeight());
    }

    private void restoreScrollPosition(int pos) {
        numberGrid.scrollToPosition(pos);
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
        nightModeIcon.setImageResource(adapter.isNightMode() ? R.drawable.icon_night_mode_on : R.drawable.icon_night_mode_off);
    }

    private void refreshGridLinesIcon() {
        gridLinesIcon.setImageResource(adapter.isDrawGridLines() ? R.drawable.icon_gridlines_on : R.drawable.icon_gridlines_off);
    }

    void onToggleNightMode() {
        adapter.toggleNightMode();
        settings.setNightMode(adapter.isNightMode());

        SharedPreferences.Editor editor = requireContext().getSharedPreferences("Number_Preferences", 0).edit();
        editor.putBoolean("WRITTEN_nightMode", adapter.isNightMode());
        editor.apply();

        refreshNightModeIcon();
    }

    void onToggleDrawGridLines() {
        adapter.toggleDrawGridLines();
        settings.setDrawGridLines(adapter.isDrawGridLines());

        SharedPreferences.Editor editor = requireContext().getSharedPreferences("Number_Preferences", 0).edit();
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

    @Override
    public void onCellHighlighted(int index) {
        if (index < 0 || index > data.getNumDigits()-1)
            return;

        if (textCarousel.animationsInProgress())
            return;

        int curRow = data.getRow(data.getHighlightPosEnd());
        adapter.notifyItemRangeChanged(data.getHighlightPos(), data.getDigitsPerGroup());

        data.highlightCell(index);

        int nextRow = data.getRow(data.getHighlightPos());
        adapter.notifyItemRangeChanged(data.getHighlightPos(), data.getDigitsPerGroup());

        textCarousel.transitionForward (data.getPreviousGroupText(), data.getCurrentGroupText(), data.getNextGroupText());
        textCarousel.setRowNum(data.getHighlightRowNumBegin(), data.getHighlightRowNumEnd(), settings.getNumRows());

        if (shouldShowMnemonics(settings.isMnemonicsEnabled(), data.getGamePhase(), settings.getDigitsPerGroup())) {
            textCarousel.setMnemoText(getMnemo(data.getCurrentGroupText()));
        }

        if (curRow != nextRow && shouldScroll(nextRow, nextRow > curRow)) {
            scroll(nextRow > curRow);
        }
    }
}
