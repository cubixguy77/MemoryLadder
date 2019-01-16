package com.memoryladder.taketest.namesandfaces.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.namesandfaces.score.ScoreProvider;
import com.memoryladder.taketest.namesandfaces.settings.NamesAndFacesSettings;
import com.memoryladder.taketest.namesandfaces.ui.adapters.TestSheet;
import com.memoryladder.taketest.scorepanel.Score;

/**
 * The ViewModel for the Random Words game
 * This class maintains the observable fields which completely describe the UI of the game in progress
 */
public class NamesAndFacesViewModel extends ViewModel {

    /* Test Sheets */
    private MutableLiveData<TestSheet> testSheet = new MutableLiveData<>();

    /* Settings */
    private NamesAndFacesSettings settings;
    private MutableLiveData<Integer> faceCount = new MutableLiveData<>();

    /* Dynamic State Variables */
    private MutableLiveData<GamePhase> gamePhase = new MutableLiveData<>();

    /* Score Provider */
    private final ScoreProvider scoreProvider;

    NamesAndFacesViewModel(NamesAndFacesSettings settings, ScoreProvider scoreProvider) {
        /* Test Sheets */
        //this.testSheet.setValue(testSheet);

        /* Settings */
        //this.settings = settings;
        this.faceCount.setValue(settings.getFaceCount());

        /* Dynamic State Variables */
        setGamePhase(GamePhase.PRE_MEMORIZATION);

        /* Score Provider */
        this.scoreProvider = scoreProvider;
    }

    /* Test Sheet */
    public LiveData<TestSheet> getTestSheet() { return this.testSheet; }
    public void resetTestSheets(TestSheet testSheet) {
        this.testSheet.setValue(testSheet);
    }
    public void shuffleTestSheet() {        if (this.testSheet.getValue() != null) {           this.testSheet.getValue().shuffle();        }    }

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
        return testSheet.getValue().getScore();
    }
}