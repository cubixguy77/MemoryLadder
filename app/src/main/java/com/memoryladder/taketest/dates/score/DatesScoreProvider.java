package com.memoryladder.taketest.dates.score;

import com.memoryladder.taketest.dates.models.TestSheet;
import com.memoryladder.taketest.scorepanel.Score;

public class DatesScoreProvider implements ScoreProvider {

    /* One point is awarded for correct answers
     * Half a point is deducted for incorrect answers
     * No points are awarded/deducted for blanks
     */
    public Score getScore(TestSheet testSheet) {
        int totalCorrect = 3;
        int totalAttempt = 5;
        int totalScore = 3;

        return new Score((int) (totalCorrect*100f / totalAttempt), (int) roundUpToZero(totalScore));
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
