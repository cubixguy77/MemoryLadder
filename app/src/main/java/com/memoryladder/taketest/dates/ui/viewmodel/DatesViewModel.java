package com.memoryladder.taketest.dates.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.dates.score.ScoreProvider;
import com.memoryladder.taketest.dates.settings.DatesSettings;
import com.memoryladder.taketest.dates.models.TestSheet;
import com.memoryladder.taketest.scorepanel.Score;

/**
 * The ViewModel for the Random Words game
 * This class maintains the observable fields which completely describe the UI of the game in progress
 */
public class DatesViewModel extends ViewModel {

    /* Test Sheets */
    private MutableLiveData<TestSheet> testSheet = new MutableLiveData<>();

    /* Settings */
    private MutableLiveData<Integer> dateCount = new MutableLiveData<>();

    /* Dynamic State Variables */
    private MutableLiveData<GamePhase> gamePhase = new MutableLiveData<>();

    /* Score Provider */
    private final ScoreProvider scoreProvider;

    DatesViewModel(TestSheet testSheet, DatesSettings settings, ScoreProvider scoreProvider) {
        /* Test Sheets */
        this.testSheet.setValue(testSheet);

        /* Settings */
        this.dateCount.setValue(settings.getDateCount());

        /* Dynamic State Variables */
        setGamePhase(GamePhase.PRE_MEMORIZATION);

        /* Score Provider */
        this.scoreProvider = scoreProvider;
    }

    /* Test Sheets */
    public LiveData<TestSheet> getTestSheet() { return this.testSheet; }
    public void resetTestSheets(TestSheet testSheet) {
        this.testSheet.setValue(testSheet);
    }

    /* Settings */
    public LiveData<Integer> getDateCount() {
        return dateCount;
    }

    /* Dynamic State Variables */
    public LiveData<GamePhase> getGamePhase() { return gamePhase; }
    public void setGamePhase(GamePhase gamePhase) { this.gamePhase.setValue(gamePhase); }

    public LiveData<Boolean> getTimerVisible() {
        return Transformations.map(gamePhase, phase -> phase != GamePhase.REVIEW);
    }

    /* Scoring */
    public Score getScore() {
        return scoreProvider.getScore(testSheet.getValue());
    }

    public void shuffleDates() {
        testSheet.getValue().shuffleDates();
    }
}