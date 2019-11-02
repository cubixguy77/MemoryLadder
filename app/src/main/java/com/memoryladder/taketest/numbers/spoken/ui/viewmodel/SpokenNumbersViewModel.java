package com.memoryladder.taketest.numbers.spoken.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.numbers.spoken.models.TestSheet;
import com.memoryladder.taketest.numbers.spoken.settings.SpokenNumbersSettings;
import com.memoryladder.taketest.scorepanel.Score;

/**
 * The ViewModel for the Images event
 * This class maintains the observable fields which completely describe the UI of the game in progress
 */
public class SpokenNumbersViewModel extends ViewModel {

    /* Test Sheet */
    private MutableLiveData<TestSheet> testSheet = new MutableLiveData<>();

    /* Settings */
    private SpokenNumbersSettings settings;

    /* Dynamic State Variables */
    private MutableLiveData<GamePhase> gamePhase = new MutableLiveData<>();
    private MutableLiveData<Integer> highlight = new MutableLiveData<>();

    SpokenNumbersViewModel(SpokenNumbersSettings settings) {
        /* Settings */
        this.settings = settings;

        /* Dynamic State Variables */
        setGamePhase(GamePhase.PRE_MEMORIZATION);
        setHighlight(0);
    }

    /* Test Sheet */
    public LiveData<TestSheet> getTestSheet() { return this.testSheet; }
    public void resetTestSheets(TestSheet testSheet) {
        this.testSheet.setValue(testSheet);
    }

    /* Settings */
    public int getDigitCount() {
        return this.settings.getDigitCount();
    }

    /* Dynamic State Variables */
    public LiveData<GamePhase> getGamePhase() { return gamePhase; }
    public void setGamePhase(GamePhase gamePhase) { this.gamePhase.setValue(gamePhase); }

    public LiveData<Integer> getHighlight() { return this.highlight; }
    public void setHighlight(Integer highlight) { this.highlight.setValue(highlight); }

    public LiveData<Boolean> getTimerVisible() {
        return Transformations.map(gamePhase, phase -> phase == GamePhase.RECALL);
    }

    /* Scoring */
    public Score getScore() {
        if (testSheet.getValue() == null) {
            return new Score(0, 0);
        }

        return testSheet.getValue().getScore();
    }
}