package com.memoryladder.taketest.namesandfaces.ui.adapters;

import com.memoryladder.taketest.namesandfaces.models.NameAndFace;
import com.memoryladder.taketest.scorepanel.Score;

import java.util.Collections;
import java.util.List;

public class TestSheet {

    private List<NameAndFace> memorySheet;
    private Score score;

    public TestSheet(List<NameAndFace> memorySheet) {
        this.memorySheet = memorySheet;
    }

    NameAndFace getNameAndFace(int position) {
        return memorySheet.get(position);
    }

    public void shuffle() {
        Collections.shuffle(memorySheet);
    }

    public Score getScore() {
        if (this.score == null) {
            int score = 0;
            int numAttempted = 0;
            int numCorrect = 0;

            for (NameAndFace face : memorySheet) {
                ReviewCellOutcome firstResult = face.getFirstResult();
                if (firstResult != ReviewCellOutcome.BLANK) {
                    numAttempted++;
                }
                if (firstResult == ReviewCellOutcome.CORRECT) {
                    score++;
                    numCorrect++;
                }

                ReviewCellOutcome lastResult = face.getLastResult();
                if (firstResult != ReviewCellOutcome.BLANK) {
                    numAttempted++;
                }
                if (lastResult == ReviewCellOutcome.CORRECT) {
                    score++;
                    numCorrect++;
                }
            }

            int accuracy = numCorrect == 0 ? 0 : (int) ((double) numCorrect * 100 / numAttempted);
            this.score = new Score(accuracy, score);
        }

        return this.score;
    }

    int getFaceCount() { return memorySheet.size(); }
}
