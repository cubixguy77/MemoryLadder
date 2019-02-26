package com.memoryladder.taketest.images.ui.adapters;

import com.memoryladder.taketest.images.models.Image;
import com.memoryladder.taketest.scorepanel.Score;

import java.util.Collections;
import java.util.List;

public class TestSheet {

    private List<Image> memorySheet;
    private Score score;

    public TestSheet(List<Image> memorySheet) {
        this.memorySheet = memorySheet;
    }

    Image getImage(int position) {
        return memorySheet.get(position);
    }

    public void shuffle() {
        Collections.shuffle(memorySheet);
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

    int getRowCount() { return memorySheet.size() / 5; }
}
