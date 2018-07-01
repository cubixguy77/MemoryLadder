package com.MemoryLadder.TakeTest.RandomWords.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MemoryLadder.TakeTest.GameManager;
import com.MemoryLadder.TakeTest.GamePhase;
import com.MemoryLadder.TakeTest.RandomWords.ui.adapters.MemoryWordsAdapter;
import com.MemoryLadder.TakeTest.RandomWords.ui.adapters.PreMemWordsAdapter;
import com.MemoryLadder.TakeTest.RandomWords.ui.adapters.RecallWordsAdapter;
import com.MemoryLadder.TakeTest.RandomWords.ui.adapters.ReviewWordsAdapter;
import com.MemoryLadder.TakeTest.RandomWords.ui.viewmodel.RandomWordsViewModel;
import com.MemoryLadder.TakeTest.RandomWords.ui.viewmodel.RandomWordsViewModelFactory;
import com.MemoryLadder.TakeTest.RandomWords.memorysheetproviders.MemorySheetProvider;
import com.MemoryLadder.TakeTest.RandomWords.score.RandomWordsScoreProvider;
import com.MemoryLadder.TakeTest.RandomWords.settings.RandomWordsSettings;
import com.MemoryLadder.TakeTest.ScorePanel.Score;
import com.MemoryLadder.TakeTest.Timer.TimerView;
import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.RandomWordsFragmentBinding;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomWordsGameManager extends Fragment implements GameManager {

    @BindView(R.id.word_list) RecyclerView wordList;
    @BindView(R.id.text_timer) TimerView timerView;

    private RandomWordsViewModel viewModel;

    private PreMemWordsAdapter preMemAdapter;
    private MemoryWordsAdapter memoryAdapter;
    private RecallWordsAdapter recallAdapter;
    private ReviewWordsAdapter reviewAdapter;

    private int wordCount;
    private int wordsPerColumn;

    public static RandomWordsGameManager newInstance(RandomWordsSettings settings) {
        RandomWordsGameManager cardGameManager = new RandomWordsGameManager();

        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        cardGameManager.setArguments(args);

        return cardGameManager;
    }

    public RandomWordsGameManager() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RandomWordsFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.viewgroup_random_words_arena, container, false);
        View root = binding.getRoot();
        ButterKnife.bind(this, root);

        if (getArguments() != null) {
            RandomWordsSettings settings = getArguments().getParcelable("settings");
            wordCount = settings == null ? 0 : settings.getWordCount();
            wordsPerColumn = settings == null ? 0 : settings.getWordsPerColumn();

            RandomWordsViewModelFactory factory = new RandomWordsViewModelFactory(
                    MemorySheetProvider.getMemorySheet(wordCount),
                    MemorySheetProvider.getRecallSheet(wordCount),
                    settings,
                    new RandomWordsScoreProvider());
            viewModel = ViewModelProviders.of(this, factory).get(RandomWordsViewModel.class);
            binding.setViewModel(viewModel);
            binding.setLifecycleOwner(this);

            wordList.setLayoutManager(new LinearLayoutManager(getContext()));

            viewModel.getGamePhase().observe(this, this::render);

            viewModel.getVisibleMemorySheet().observe(this, wordList -> {
                if (viewModel.getGamePhaseValue() == GamePhase.MEMORIZATION)
                    memoryAdapter.setMemorySheet(wordList);
            });

            viewModel.getVisibleRecallSheet().observe(this, wordList -> {
                if (viewModel.getGamePhaseValue() == GamePhase.RECALL)
                    recallAdapter.setRecallSheet(wordList);
            });

            viewModel.getVisibleReviewSheet().observe(this, reviewSheet -> {
                if (viewModel.getGamePhaseValue() == GamePhase.REVIEW)
                    reviewAdapter.setReviewSheet(reviewSheet);
            });
        }

        return root;
    }

    @Override
    public void setGamePhase(GamePhase phase) {
        viewModel.setGamePhase(phase);
        viewModel.setColumnNum(0);
    }

    @Override
    public void displayTime(int secondsRemaining) {
        timerView.displayTime(secondsRemaining);
    }

    @Override
    public void render(GamePhase phase) {
        switch (phase) {
            case PRE_MEMORIZATION:
                preMemAdapter = new PreMemWordsAdapter(wordsPerColumn);
                wordList.setAdapter(preMemAdapter);
                break;
            case MEMORIZATION:
                memoryAdapter = new MemoryWordsAdapter(wordsPerColumn);
                wordList.setAdapter(memoryAdapter);
                break;
            case RECALL:
                recallAdapter = new RecallWordsAdapter(wordsPerColumn, viewModel);
                wordList.setAdapter(recallAdapter);
                break;
            case REVIEW:
                reviewAdapter = new ReviewWordsAdapter(wordsPerColumn);
                wordList.setAdapter(reviewAdapter);
                break;
        }
    }

    @Override
    public Score getScore() {
        return viewModel.getScore();
    }
}
