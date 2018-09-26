package com.memoryladder.taketest.randomwords.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.memoryladder.taketest.randomwords.score.ScoreProvider;
import com.memoryladder.taketest.randomwords.settings.RandomWordsSettings;

import java.util.List;

/**
 * Factory for RandomWordsViewModel
 */
public class RandomWordsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final List<String> memorySheet;
    private final List<String> recallSheet;
    private final RandomWordsSettings settings;
    private final ScoreProvider scoreProvider;

    public RandomWordsViewModelFactory(List<String> memorySheet, List<String> recallSheet, RandomWordsSettings settings, ScoreProvider scoreProvider) {
        this.memorySheet = memorySheet;
        this.recallSheet = recallSheet;
        this.settings = settings;
        this.scoreProvider = scoreProvider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new RandomWordsViewModel(memorySheet, recallSheet, settings, scoreProvider);
    }
}