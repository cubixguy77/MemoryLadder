package com.memoryladder.taketest.namesandfaces.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

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