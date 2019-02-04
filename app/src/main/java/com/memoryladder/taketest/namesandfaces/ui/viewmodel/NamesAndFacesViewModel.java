package com.memoryladder.taketest.namesandfaces.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.namesandfaces.settings.NamesAndFacesSettings;
import com.memoryladder.taketest.namesandfaces.ui.adapters.TestSheet;
import com.memoryladder.taketest.scorepanel.Score;

import java.util.Objects;

/**
 * The ViewModel for the Names & Faces event
 * This class maintains the observable fields which completely describe the UI of the game in progress
 */
public class NamesAndFacesViewModel extends ViewModel {

    /* Test Sheet */
    private MutableLiveData<TestSheet> testSheet = new MutableLiveData<>();

    /* Settings */
    private MutableLiveData<Integer> faceCount = new MutableLiveData<>();

    /* Dynamic State Variables */
    private MutableLiveData<GamePhase> gamePhase = new MutableLiveData<>();
    private MutableLiveData<Integer> viewPortHeight = new MutableLiveData<>();

    NamesAndFacesViewModel(NamesAndFacesSettings settings) {
        /* Settings */
        this.faceCount.setValue(settings.getFaceCount());

        /* Dynamic State Variables */
        setGamePhase(GamePhase.PRE_MEMORIZATION);
    }

    /* Test Sheet */
    public LiveData<TestSheet> getTestSheet() { return this.testSheet; }
    public void resetTestSheets(TestSheet testSheet) {
        this.testSheet.setValue(testSheet);
    }
    public void shuffleTestSheet() {
        if (this.testSheet.getValue() != null) {
            this.testSheet.getValue().shuffle();
        }
    }

    /* Settings */
    public LiveData<Integer> getFaceCount() {
        return faceCount;
    }

    /* Dynamic State Variables */
    public LiveData<GamePhase> getGamePhase() { return gamePhase; }
    public void setGamePhase(GamePhase gamePhase) { this.gamePhase.setValue(gamePhase); }

    public LiveData<Integer> getViewPortHeight() { return viewPortHeight; }
    public void setViewPortHeight(Integer newViewPortHeight) {
        this.viewPortHeight.setValue(newViewPortHeight);
    }

    public LiveData<Boolean> getTimerVisible() {
        return Transformations.map(gamePhase, phase -> phase != GamePhase.REVIEW);
    }

    /* Scoring */
    public Score getScore() {
        return Objects.requireNonNull(testSheet.getValue()).getScore();
    }
}