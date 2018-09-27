package com.memoryladder.taketest.randomwords.ui.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.randomwords.score.ScoreProvider;
import com.memoryladder.taketest.randomwords.settings.RandomWordsSettings;
import com.memoryladder.taketest.randomwords.ui.adapters.ReviewSheet;
import com.memoryladder.taketest.ScorePanel.Score;

import java.util.List;

/**
 * The ViewModel for the Random Words game
 * This class maintains the observable fields which completely describe the UI of the game in progress
 */
public class RandomWordsViewModel extends ViewModel {

    /* Test Sheets */
    private MutableLiveData<List<String>> memorySheet = new MutableLiveData<>();
    private MutableLiveData<List<String>> recallSheet = new MutableLiveData<>();

    /* Settings */
    private RandomWordsSettings settings;
    private MutableLiveData<Integer> wordCount = new MutableLiveData<>();
    private MutableLiveData<Integer> columnCount = new MutableLiveData<>();
    private MutableLiveData<Boolean> nightMode = new MutableLiveData<>();

    /* Dynamic State Variables */
    private MutableLiveData<GamePhase> gamePhase = new MutableLiveData<>();
    private MutableLiveData<Integer> columnNum = new MutableLiveData<>();

    /* Score Provider */
    private final ScoreProvider scoreProvider;

    RandomWordsViewModel(List<String> memorySheet, List<String> recallSheet, RandomWordsSettings settings, ScoreProvider scoreProvider) {
        /* Test Sheets */
        this.memorySheet.setValue(memorySheet);
        this.recallSheet.setValue(recallSheet);

        /* Settings */
        this.settings = settings;
        this.wordCount.setValue(settings.getWordCount());
        this.columnCount.setValue(settings.getColumnCount());
        this.nightMode.setValue(settings.isNightMode());

        /* Dynamic State Variables */
        setGamePhase(GamePhase.PRE_MEMORIZATION);
        this.columnNum.setValue(0);

        /* Score Provider */
        this.scoreProvider = scoreProvider;
    }

    /* Test Sheets */
    public LiveData<List<String>> getVisibleMemorySheet() {
        return Transformations.map(columnNum, column -> this.memorySheet.getValue().subList(column * settings.getWordsPerColumn(), (column+1) * settings.getWordsPerColumn()));
    }

    public LiveData<List<String>> getVisibleRecallSheet() {
        return Transformations.map(columnNum, column -> this.recallSheet.getValue().subList(column * settings.getWordsPerColumn(), (column+1) * settings.getWordsPerColumn()));
    }

    public LiveData<ReviewSheet> getVisibleReviewSheet() {
        return Transformations.map(columnNum, column -> new ReviewSheet(this.memorySheet.getValue().subList(column * settings.getWordsPerColumn(), (column+1) * settings.getWordsPerColumn()), this.recallSheet.getValue().subList(column * settings.getWordsPerColumn(), (column+1) * settings.getWordsPerColumn())));
    }

    public String getRecallForCurrentColumnAt(int position) {
        return recallSheet == null || recallSheet.getValue() == null ? "" :
                recallSheet.getValue().get(getIndex(position, getColumnNumValue()));
    }

    public void updateRecallSheet(String text, int position) {
        if (recallSheet == null || recallSheet.getValue() == null) return;
        recallSheet.getValue().set(getIndex(position, getColumnNumValue()), text.trim());
    }




    /* Settings */
    public LiveData<Integer> getWordCount() {
        return wordCount;
    }
    public LiveData<Integer> getColumnCount() {
        return columnCount;
    }

    public int getWordsPerColumn() {
        return settings.getWordsPerColumn();
    }

    public LiveData<Boolean> getNightMode() {
        return nightMode;
    }

    public void toggleNightMode(View view) {
        nightMode.setValue(!unboxBool(nightMode, false));
    }



    public void reset(List<String> memorySheet, List<String> recallSheet) {
        this.memorySheet.setValue(memorySheet);
        this.recallSheet.setValue(recallSheet);
    }








    public Score getScore() {
        return scoreProvider.getScore(memorySheet.getValue(), recallSheet.getValue(), getWordsPerColumn());
    }







    public LiveData<GamePhase> getGamePhase() { return gamePhase; }
    public GamePhase getGamePhaseValue() { return gamePhase == null ? GamePhase.PRE_MEMORIZATION : gamePhase.getValue(); }
    public void setGamePhase(GamePhase gamePhase) { this.gamePhase.setValue(gamePhase); }

    public LiveData<Boolean> isBackEnabled() {
        return Transformations.map(columnNum, column -> column == 0);
    }

    public LiveData<Boolean> isNextEnabled() {
        return Transformations.map(columnNum, column -> column >= settings.getColumnCount());
    }

    public LiveData<Integer> getColumnNum() { return this.columnNum; }
    private int getColumnNumValue() { return unboxInt(columnNum, 0); }
    public void setColumnNum(int newColumnNum) { columnNum.setValue(newColumnNum); }

    public void prevColumn(View v) { setColumnNum(getColumnNumValue() - 1); }
    public void nextColumn(View v) { setColumnNum(getColumnNumValue() + 1); }

    private Boolean unboxBool(LiveData<Boolean> liveData, Boolean defaultValue) {
        if (liveData == null)
            return defaultValue;
        return liveData.getValue();
    }

    private Integer unboxInt(LiveData<Integer> liveData, Integer defaultValue) {
        if (liveData == null)
            return defaultValue;
        return liveData.getValue();
    }

    /* The array is indexed as follows:
     * 0 3
     * 1 4
     * 2 5
     */
    private int getIndex(int row, int col) {
        return (col * settings.getWordsPerColumn()) + row;
    }


}