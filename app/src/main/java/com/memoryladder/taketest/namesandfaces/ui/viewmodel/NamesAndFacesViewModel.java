package com.memoryladder.taketest.namesandfaces.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.namesandfaces.settings.NamesAndFacesSettings;
import com.memoryladder.taketest.namesandfaces.ui.adapters.TestSheet;
import com.memoryladder.taketest.scorepanel.Score;

/**
 * The ViewModel for the Names & Faces event
 * This class maintains the observable fields which completely describe the UI of the game in progress
 */
public class NamesAndFacesViewModel extends ViewModel {

    /* Test Sheet */
    private final MutableLiveData<TestSheet> testSheet = new MutableLiveData<>();

    /* Settings */
    private final MutableLiveData<Integer> faceCount = new MutableLiveData<>();

    /* Dynamic State Variables */
    private final MutableLiveData<GamePhase> gamePhase = new MutableLiveData<>();

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

    public LiveData<Boolean> getTimerVisible() {
        return Transformations.map(gamePhase, phase -> phase != GamePhase.REVIEW);
    }

    /* Scoring */
    public Score getScore() {
        if (testSheet.getValue() == null) {
            return new Score(0, 0);
        }

        return testSheet.getValue().getScore();
    }
}