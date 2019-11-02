package com.memoryladder.taketest.images.ui.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.memoryladder.taketest.images.settings.ImagesSettings;

/**
 * Factory for SpokenNumbersViewModelFactory
 */
public class ImagesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final ImagesSettings settings;

    public ImagesViewModelFactory(ImagesSettings settings) {
        this.settings = settings;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ImagesViewModel(settings);
    }
}