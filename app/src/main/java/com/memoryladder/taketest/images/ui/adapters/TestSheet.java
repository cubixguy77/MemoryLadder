package com.memoryladder.taketest.images.ui.adapters;

import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.images.models.Image;
import com.memoryladder.taketest.scorepanel.Score;

import java.util.ArrayList;
import java.util.Collections;

public class TestSheet {

    private ArrayList<Image>[] memorySheet;
    private Score score;

    public TestSheet(ArrayList<Image>[] memorySheet) {
        this.memorySheet = memorySheet;
    }

    Image getImage(int row, int column, GamePhase phase) {
        return memorySheet[row].get(column);
    }

    public void sortImagesForRecall() {
        for (ArrayList<Image> aMemorySheet : memorySheet) {
            Collections.sort(aMemorySheet, ((i1, i2) -> i1 == null ? -1 : i2 == null ? 1 : i1.getRecallIndex() - i2.getRecallIndex()));
        }
    }

    public Score getScore() {
        if (this.score == null) {
            int score = 7;
            int numAttempted = 10;
            int numCorrect = 5;

            int accuracy = numCorrect == 0 ? 0 : (int) ((double) numCorrect * 100 / numAttempted);
            this.score = new Score(accuracy, score);
        }

        return this.score;
    }

    int getImageCount() { return memorySheet.length * 6; }
}
