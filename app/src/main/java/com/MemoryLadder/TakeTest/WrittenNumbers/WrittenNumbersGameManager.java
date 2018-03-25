package com.MemoryLadder.TakeTest.WrittenNumbers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.MemoryLadder.TakeTest.GameManager;
import com.MemoryLadder.TakeTest.GameManagerActivity;
import com.MemoryLadder.TakeTest.GamePhase;
import com.MemoryLadder.TakeTest.ScorePanel.Score;
import com.MemoryLadder.TakeTest.Timer.TimerView;
import com.MemoryLadder.TakeTest.WrittenNumbers.Keyboard.NumericKeyboardView;
import com.MemoryLadder.TakeTest.WrittenNumbers.NumberCarousel.CustomNumberCarousel;
import com.MemoryLadder.TakeTest.WrittenNumbers.NumberGrid.NumberGridAdapter;
import com.mastersofmemory.memoryladder.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WrittenNumbersGameManager extends Fragment implements GameManager {

    private WrittenNumberData data;
    private WrittenNumbersSettings settings;
    private GameManagerActivity activity;

    private NumberGridAdapter adapter;

    @BindView(R.id.text_timer) TimerView timerView;
    @BindView(R.id.keyboard) NumericKeyboardView keyboardView;
    @BindView(R.id.carousel) CustomNumberCarousel textCarousel;
    @BindView(R.id.numberGrid) RecyclerView recyclerView;
    @BindView(R.id.layout_button_start) FrameLayout startButton;
    @BindView(R.id.nextButton) Button nextButton;

    public static WrittenNumbersGameManager newInstance(WrittenNumbersSettings settings) {
        WrittenNumbersGameManager cardGameManager = new WrittenNumbersGameManager();

        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        cardGameManager.setArguments(args);

        return cardGameManager;
    }

    public WrittenNumbersGameManager() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewgroup_written_numbers_arena, container, false);
        ButterKnife.bind(this, view);
        settings = getArguments().getParcelable("settings");
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (GameManagerActivity) context;
        try { this.activity = (GameManagerActivity) context; }
        catch (final ClassCastException e) { throw new ClassCastException(activity.toString() + " must implement GameManagerActivity"); }
    }

    @Override
    public void setGamePhase(GamePhase phase) {
        if (data != null) {
            data.setGamePhase(phase);
            data.resetHighlight();
            adapter.notifyDataSetChanged();
        }

        if (phase == GamePhase.PRE_MEMORIZATION) {
            data = new WrittenNumberData(settings);
            adapter = new NumberGridAdapter(getContext(), data);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), settings.getNumCols()));
            recyclerView.setAdapter(adapter);
            refreshCarousel();
        }
        else if (phase == GamePhase.MEMORIZATION) {
            refreshCarousel();
        }
        else if (phase == GamePhase.RECALL) {
            refreshCarousel();
            keyboardView.setKeyListener(key -> {
                data.setRecallDigit(key);
                adapter.notifyItemRangeChanged(data.getHighlightPos(), 1);
                refreshCarousel();

                boolean advanceGroup = data.highlightNextCell();

                if (advanceGroup) {
                    onNextClick();
                }
            });
        }
        else if (phase == GamePhase.REVIEW) {
            keyboardView.setKeyListener(null);
        }

        render(phase);
    }

    private void refreshCarousel() {
        textCarousel.display(data.getPreviousGroupText(), data.getGroupText(data.getHighlightPos()), data.getNextGroupText());
    }

    @Override
    public void displayTime(int secondsRemaining) {
        timerView.displayTime(secondsRemaining);
    }

    @Override
    public void render(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            textCarousel.hide();
            keyboardView.hide();
            timerView.show();
            startButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
        }
        else if (phase == GamePhase.MEMORIZATION) {
            textCarousel.show();
            startButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        }
        else if (phase == GamePhase.RECALL) {
            keyboardView.show();
            nextButton.setVisibility(View.GONE);
        }
        else if (phase == GamePhase.REVIEW) {
            textCarousel.hide();
            keyboardView.hide();
            timerView.hide();
        }
    }

    @Override
    public Score getScore() {
        return data.getScore();
    }

    /* Start Game */
    @OnClick(R.id.button_start) void startGame() {
        activity.onStartClicked();
    }

    @OnClick(R.id.nextButton) void onNextClick() {
        if (!textCarousel.animationsInProgress()) {
            data.highlightNextGroup();
            adapter.notifyItemRangeChanged(data.getHighlightPos() - data.getDigitsPerGroup(), data.getDigitsPerGroup() * 2);
            textCarousel.transitionForward(data.getNextGroupText());
        }
    }
}
