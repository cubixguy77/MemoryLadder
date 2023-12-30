package com.memoryladder.taketest.dates.ui.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.memoryladder.taketest.dates.score.ScoreProvider;
import com.memoryladder.taketest.dates.settings.DatesSettings;
import com.memoryladder.taketest.dates.models.TestSheet;

/**
 * Factory for RandomWordsViewModel
 */
public class DatesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final TestSheet testSheet;
    private final DatesSettings settings;
    private final ScoreProvider scoreProvider;

    public DatesViewModelFactory(TestSheet testSheet, DatesSettings settings, ScoreProvider scoreProvider) {
        this.testSheet = testSheet;
        this.settings = settings;
        this.scoreProvider = scoreProvider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DatesViewModel(testSheet, settings, scoreProvider);
    }
}