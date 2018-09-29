package com.memoryladder.taketest.writtennumbers.Scoring;

import com.memoryladder.taketest.scorepanel.Score;

import static com.memoryladder.taketest.writtennumbers.WrittenNumberData.EMPTY_CHAR;

public class ScoreCalculation {

    /* The scoring works as follows
     * For each row (of length n), you receive n points for 0 errors in the row, ceiling(n/2) points for 1 error, and zero points for 2+ errors.
     * For the last attempted row only, if n digits are recalled, you receive n points for 0 errors, ceiling(n/2) points for 1 error, 0 points for 2+ errors.
     */
    public static Score getScore(char[][] guess, char[][] answer) {

        double totalScore = 0;
        int numCols = guess[0].length;

        int totalRecallCorrect = 0;
        int totalRecallAttempt = 0;

        int lastAttemptedRow = lastAttemptedRow(guess);

        for (int row = 0; row <= lastAttemptedRow; row++) {   // one row at a time, up to the last attempted row

            int numCorrect = 0;
            int numWrong = 0;
            int numBlank = 0;
            double rowScore;

            for (int col = 0; col < numCols; col++) {
                if (guess[row][col] == EMPTY_CHAR)
                    numBlank++;
                else
                    totalRecallAttempt++;

                if (guess[row][col] == (answer[row][col])) {   // right
                    numCorrect++;
                    totalRecallCorrect++;
                }
                else {                                         // wrong
                    numWrong++;
                }
            }

            if (row != lastAttemptedRow) {
                if (numWrong == 0)
                    rowScore = numCorrect;
                else if (numWrong == 1)
                    rowScore = roundUp((double) numCols / 2);
                else
                    rowScore = 0;
            } else {
                if ((numWrong - numBlank) == 0)
                    rowScore = numCorrect;
                else if ((numWrong - numBlank) == 1)
                    rowScore = roundUp((double) (numCorrect + numWrong - numBlank) / 2);
                else
                    rowScore = 0;
            }

            totalScore += rowScore;
        }

        return new Score((int) ((float) totalRecallCorrect * 100 / totalRecallAttempt), (int) roundUpToZero(totalScore));
    }

    private static int lastAttemptedRow(char[][] guess) {
        for (int row = guess.length - 1; row >= 0; row--) {
            if (attemptedRow(guess[row]))
                return row;
        }
        return 0;
    }

    private static Boolean attemptedRow(char[] row) {
        for (char aRow : row) {
            if (!(aRow == EMPTY_CHAR))
                return true;
        }
        return false;
    }

    private static double roundUp(double d) {
        return (double) ((int) (d + 0.5));
    }

    private static double roundUpToZero(double d) {
        if (d < 0)
            return 0;
        return roundUp(d);
    }
}
