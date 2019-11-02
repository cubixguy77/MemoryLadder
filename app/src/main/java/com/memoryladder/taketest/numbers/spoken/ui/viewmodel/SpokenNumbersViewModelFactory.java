package com.memoryladder.taketest.numbers.spoken.ui.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.memoryladder.taketest.numbers.spoken.settings.SpokenNumbersSettings;

/**
 * Factory for SpokenNumbersViewModelFactory
 */
public class SpokenNumbersViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final SpokenNumbersSettings settings;

    public SpokenNumbersViewModelFactory(SpokenNumbersSettings settings) {
        this.settings = settings;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new SpokenNumbersViewModel(settings);
    }
}