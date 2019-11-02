package com.memoryladder.taketest.randomwords.ui;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.memoryladder.taketest.GameManager;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.randomwords.ui.adapters.MemoryWordsAdapter;
import com.memoryladder.taketest.randomwords.ui.adapters.RecallWordsAdapter;
import com.memoryladder.taketest.randomwords.ui.adapters.ReviewWordsAdapter;
import com.memoryladder.taketest.randomwords.ui.viewmodel.RandomWordsViewModel;
import com.memoryladder.taketest.randomwords.ui.viewmodel.RandomWordsViewModelFactory;
import com.memoryladder.taketest.randomwords.memorysheetproviders.MemorySheetProvider;
import com.memoryladder.taketest.randomwords.score.RandomWordsScoreProvider;
import com.memoryladder.taketest.randomwords.settings.RandomWordsSettings;
import com.memoryladder.taketest.scorepanel.Score;
import com.memoryladder.taketest.timer.TimerView;
import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.RandomWordsFragmentBinding;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomWordsGameManager extends Fragment implements GameManager {

    @BindView(R.id.word_list) RecyclerView wordList;
    @BindView(R.id.text_timer) TimerView timerView;

    private RandomWordsViewModel viewModel;

    private MemoryWordsAdapter memoryAdapter;
    private RecallWordsAdapter recallAdapter;
    private ReviewWordsAdapter reviewAdapter;

    RandomWordsSettings settings;

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
            settings = getArguments().getParcelable("settings");

            RandomWordsViewModelFactory factory = new RandomWordsViewModelFactory(
                    MemorySheetProvider.getMemorySheet(getWordList(settings.isFullWordList()), settings.getWordCount()),
                    MemorySheetProvider.getRecallSheet(settings.getWordCount()),
                    settings,
                    new RandomWordsScoreProvider());
            viewModel = ViewModelProviders.of(this, factory).get(RandomWordsViewModel.class);
            binding.setViewModel(viewModel);
            binding.setLifecycleOwner(this);

            wordList.setLayoutManager(new LinearLayoutManager(getContext()));
            wordList.addItemDecoration(new DividerItemDecoration(wordList.getContext(), DividerItemDecoration.VERTICAL));

            viewModel.getGamePhase().observe(this, this::render);

            viewModel.getColumnNum().observe(this, columnNum -> wordList.smoothScrollToPosition(0));

            viewModel.getTimerVisible().observe(this, visible -> timerView.setVisibility(visible != null && visible ? View.VISIBLE : View.INVISIBLE));

            viewModel.getVisibleMemorySheet().observe(this, memorySheet -> {
                if (viewModel.getGamePhaseValue() == GamePhase.MEMORIZATION)
                    memoryAdapter.setMemorySheet(memorySheet);
            });

            viewModel.getVisibleRecallSheet().observe(this, recallSheet -> {
                if (viewModel.getGamePhaseValue() == GamePhase.RECALL)
                    recallAdapter.setRecallSheet(recallSheet);
            });

            viewModel.getVisibleReviewSheet().observe(this, reviewSheet -> {
                if (viewModel.getGamePhaseValue() == GamePhase.REVIEW)
                    reviewAdapter.setReviewSheet(reviewSheet);
            });
        }

        return root;
    }

    private List<String> getWordList(boolean useFullWordList) {
        return Arrays.asList(useFullWordList ? getResources().getStringArray(R.array.randomwords_english) : getResources().getStringArray(R.array.randomwords_english_truncated));
    }

    @Override
    public void setGamePhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            viewModel.resetTestSheets(MemorySheetProvider.getMemorySheet(getWordList(settings.isFullWordList()), settings.getWordCount()), MemorySheetProvider.getRecallSheet(settings.getWordCount()));
        }

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
                memoryAdapter = new MemoryWordsAdapter(settings.getWordsPerColumn());
                wordList.setAdapter(memoryAdapter);
                break;
            case MEMORIZATION:
                if (memoryAdapter == null) {
                    memoryAdapter = new MemoryWordsAdapter(settings.getWordsPerColumn());
                    wordList.setAdapter(memoryAdapter);
                }
                break;
            case RECALL:
                recallAdapter = new RecallWordsAdapter(settings.getWordsPerColumn(), viewModel);
                wordList.setAdapter(recallAdapter);
                break;
            case REVIEW:
                reviewAdapter = new ReviewWordsAdapter(settings.getWordsPerColumn());
                wordList.setAdapter(reviewAdapter);
                break;
        }
    }

    @Override
    public Score getScore() {
        return viewModel.getScore();
    }
}
