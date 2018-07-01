package com.MemoryLadder.TakeTest.RandomWords.score;

import com.MemoryLadder.TakeTest.ScorePanel.Score;

import java.util.List;

public class RandomWordsScoreProvider implements ScoreProvider {
    /* Note to self: I have not tested the case of having some blank entries in the last row for either or the behemoths
     * double check rounding properly
     */
    public Score getScore(List<String> memorySheet, List<String> recallSheet, int wordsPerColumn) {

        return new Score(75, 13);

        /*
        double TotalScore = 0;

        int wordCount = memorySheet.size();
        int numRows = wordsPerColumn;
        int numCols = wordCount / numRows;

        int numPages = numRows / wordsPerColumn;
        int lastAttemptedColumn = lastAttemptedColumn(recallSheet, "", wordsPerColumn);

        System.out.println("Last attempted column: " + lastAttemptedColumn);

        int recallCorrect = 0;
        int recallAttempt = 0;
        int totalMispelled = 0;


        for (int page = 0; page < numPages; page++) {
            int rowstart = (wordsPerColumn * page);
            int rowend = rowstart + wordsPerColumn;

            for (int col = 0; col < numCols; col++) {

                int numCorrect = 0;
                int numWrong = 0;
                int numMispelled = 0;
                int numBlank = 0;
                double columnScore = 0;

                if (attemptedColumn(recallSheet, col % numCols, rowstart, rowend, ""))
                    recallAttempt += wordsPerColumn;

                for (int row = rowstart; row < rowend; row++) {

                    if (recallSheet[row][col].equals(answer[row][col])) {                        // right
                        numCorrect++;
                        recallCorrect++;
                    } else if (checkSpelling && oneCharAway(recallSheet[row][col], answer[row][col]))               // right, but mispelled
                        numMispelled++;
                    else if (recallSheet[row][col].equals("")) {                                  // blank
                        numBlank++;
                        numWrong++;
                    } else {                                                                  // wrong
                        numWrong++;
                    }
                }

                if (!((col + (numCols * page)) == lastAttemptedColumn)) { // not the last column attempted
                    if (numWrong == 0)
                        columnScore = numCorrect + numMispelled;
                    else if (numWrong == 1)
                        columnScore = (((double) (numCorrect + numMispelled) / 2) + .5);
                    else
                        columnScore = 0;

                    columnScore -= numMispelled;
                } else {                                       // indeed the last column attempted
                    if ((numWrong - numBlank) == 0)
                        columnScore = numCorrect + numMispelled;
                    else if ((numWrong - numBlank) == 1)
                        columnScore = (((double) (numCorrect + numMispelled) / 2) + .5);
                    else
                        columnScore = 0;
                    columnScore -= numMispelled;
                }

                TotalScore += columnScore;
                totalMispelled += numMispelled;
                System.out.println("Col: " + col + " colscore: " + columnScore);
            }

        }


        int[] scores = new int[4];
        scores[0] = recallCorrect;
        scores[1] = recallAttempt;
        scores[2] = totalMispelled;
        scores[3] = (int) roundUpToZero(TotalScore);
        return scores;
        */
    }

    private static int lastAttemptedColumn(String[][] recallSheet, String blank, int wordsPerColumn) {
        int numRows = recallSheet.length;
        int numCols = recallSheet[0].length;
        int numPages = numRows / wordsPerColumn;

        for (int col = (numCols * numPages) - 1; col >= 0; col--) {
            int modcol = col % numCols;
            int page = col / numCols;
            int rowstart = wordsPerColumn * page;
            int rowend = rowstart + wordsPerColumn;

            System.out.println(modcol + " " + page + " " + rowstart + " " + rowend);

            if (attemptedColumn(recallSheet, modcol, rowstart, rowend, blank))
                return col;
        }
        return 0;
    }

    private static Boolean attemptedColumn(String[][] array, int col, int rowstart, int rowend, String blank) {
        for (int row = rowstart; row < rowend; row++) {
            if (!array[row][col].equals(blank))
                return true;
        }
        return false;
    }

    private static double roundUpToZero(double d) {
        if (d < 0)
            return 0;
        return roundUp(d);
    }

    private static double roundUp(double d) {
        return (double) ((int) (d + 0.5));
    }
}
