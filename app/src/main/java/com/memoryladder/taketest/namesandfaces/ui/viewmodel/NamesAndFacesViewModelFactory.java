package com.memoryladder.taketest.namesandfaces.ui.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.memoryladder.taketest.namesandfaces.settings.NamesAndFacesSettings;

/**
 * Factory for NamesAndFacesViewModel
 */
public class NamesAndFacesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final NamesAndFacesSettings settings;

    public NamesAndFacesViewModelFactory(NamesAndFacesSettings settings) {
        this.settings = settings;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new NamesAndFacesViewModel(settings);
    }
}