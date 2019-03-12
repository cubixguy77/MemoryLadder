package com.memoryladder.taketest.images.models;

import com.memoryladder.taketest.images.ui.adapters.ReviewCellOutcome;
import com.memoryladder.taketest.scorepanel.Score;

import java.util.Collections;
import java.util.List;

public class TestSheet {

    private List<Image>[] memorySheet;
    private Score score;

    public TestSheet(List<Image>[] memorySheet) {
        this.memorySheet = memorySheet;
    }

    public Image getMemoryImage(int row, int column) {
        return memorySheet[row].get(column);
    }

    public Image getRecallImage(int row, int column) {
        return memorySheet[row].get(column);
    }

    public void swapImages(int rowNumber, int fromColumn, int toColumn) {
        Image image1 = getRecallImage(rowNumber, fromColumn);
        Image image2 = getRecallImage(rowNumber, toColumn);

        Collections.swap(memorySheet[rowNumber], fromColumn, toColumn);
        image1.setRecallIndex(toColumn);
        image2.setRecallIndex(fromColumn);

        for (Image image : memorySheet[rowNumber]) {
            if (image != null) {
                image.setAttempted(true);
            }
        }
    }

    public void sortImagesForRecall() {
        for (List<Image> row : memorySheet) {
            Collections.sort(row, ((i1, i2) -> i1 == null ? -1 : i2 == null ? 1 : i1.getRecallIndex() - i2.getRecallIndex()));
        }
    }

    public void sortUnattemptedRowsForReview() {
        for (List<Image> row : memorySheet) {
            boolean rowAttempted = row.get(1).isAttempted();
            if (!rowAttempted) {
                Collections.sort(row, ((i1, i2) -> i1 == null ? -1 : i2 == null ? 1 : i1.getMemoryIndex() - i2.getMemoryIndex()));
            }
        }
    }

    public Score getScore() {
        if (this.score == null) {
            int score = 0;
            int numAttempted = 0;
            int numCorrect = 0;

            for (List<Image> row : memorySheet) {
                int numCorrectInRow = 0;
                int numAttemptedInRow = 0;

                for (Image image : row) {
                    if (image == null)
                        continue;

                    if (image.getResult() == ReviewCellOutcome.CORRECT) {
                        numCorrectInRow++;
                    }

                    if (image.getResult() != ReviewCellOutcome.BLANK) {
                        numAttemptedInRow++;
                    }
                }

                numCorrect += numCorrectInRow;
                numAttempted += numAttemptedInRow;

                if (numCorrectInRow == 5) {
                    score += 5;
                }
                else if (numAttemptedInRow > 0) {
                    score--;
                }
            }

            if (score < 0) {
                score = 0;
            }

            int accuracy = numCorrect == 0 ? 0 : (int) ((double) numCorrect * 100 / numAttempted);
            this.score = new Score(accuracy, score);
        }

        return this.score;
    }

    public int getImageCount() { return memorySheet.length * 6; }
}
