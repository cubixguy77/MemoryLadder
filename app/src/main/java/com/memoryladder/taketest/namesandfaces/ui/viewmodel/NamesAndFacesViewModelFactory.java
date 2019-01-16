package com.memoryladder.taketest.namesandfaces.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.memoryladder.taketest.namesandfaces.score.ScoreProvider;
import com.memoryladder.taketest.namesandfaces.settings.NamesAndFacesSettings;
import com.memoryladder.taketest.namesandfaces.ui.adapters.TestSheet;

import java.util.List;

/**
 * Factory for NamesAndFacesViewModel
 */
public class NamesAndFacesViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    //private final TestSheet testSheet;
    private final NamesAndFacesSettings settings;
    private final ScoreProvider scoreProvider;

    public NamesAndFacesViewModelFactory(NamesAndFacesSettings settings, ScoreProvider scoreProvider) {
        //this.testSheet = testSheet;
        this.settings = settings;
        this.scoreProvider = scoreProvider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new NamesAndFacesViewModel(settings, scoreProvider);
    }
}