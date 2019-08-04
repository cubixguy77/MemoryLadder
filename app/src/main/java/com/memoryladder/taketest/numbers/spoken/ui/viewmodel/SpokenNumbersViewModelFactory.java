package com.memoryladder.taketest.numbers.spoken.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

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