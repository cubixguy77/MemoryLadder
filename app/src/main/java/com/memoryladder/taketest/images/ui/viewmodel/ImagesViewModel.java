package com.memoryladder.taketest.images.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.images.settings.ImagesSettings;
import com.memoryladder.taketest.images.models.TestSheet;
import com.memoryladder.taketest.scorepanel.Score;

/**
 * The ViewModel for the Images event
 * This class maintains the observable fields which completely describe the UI of the game in progress
 */
public class ImagesViewModel extends ViewModel {

    /* Test Sheet */
    private MutableLiveData<TestSheet> testSheet = new MutableLiveData<>();

    /* Settings */
    private MutableLiveData<Integer> rowCount = new MutableLiveData<>();

    /* Dynamic State Variables */
    private MutableLiveData<GamePhase> gamePhase = new MutableLiveData<>();

    ImagesViewModel(ImagesSettings settings) {
        /* Settings */
        this.rowCount.setValue(settings.getRowCount());

        /* Dynamic State Variables */
        setGamePhase(GamePhase.PRE_MEMORIZATION);
    }

    /* Test Sheet */
    public LiveData<TestSheet> getTestSheet() { return this.testSheet; }
    public void resetTestSheets(TestSheet testSheet) {
        this.testSheet.setValue(testSheet);
    }

    /* Settings */
    public LiveData<Integer> getRowCount() {
        return rowCount;
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

    public void sortImagesForRecall() {
        if (this.testSheet.getValue() != null) {
            this.testSheet.getValue().sortImagesForRecall();
        }
    }

    public void sortUnattemptedRowsForReview() {
        if (this.testSheet.getValue() != null) {
            this.testSheet.getValue().sortUnattemptedRowsForReview();
        }
    }
}