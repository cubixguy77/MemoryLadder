package com.memoryladder.taketest.randomwords.score;

import com.memoryladder.taketest.scorepanel.Score;

import java.util.List;

public class RandomWordsScoreProvider implements ScoreProvider {
    private final static char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    public Score getScore(List<String> memorySheet, List<String> recallSheet, int wordsPerColumn) {

        int numRows = wordsPerColumn;
        int numCols = memorySheet.size() / wordsPerColumn;

        String[][] guess = new String[numRows][numCols];
        String[][] answer = new String[numRows][numCols];

        for (int col = 0; col < numCols; col++) {
            for (int row = 0; row < numRows; row++) {
                guess[row][col] = recallSheet.get(numRows * col + row);
                answer[row][col] = memorySheet.get(numRows * col + row);
            }
        }

        int lastAttemptedColumn = lastAttemptedColumn(guess);

        double TotalScore = 0;
        int totalCorrect = 0;
        int totalAttempt = 0;

        for (int col = 0; col < numCols; col++) {
            int columnCorrect = 0;
            int columnWrong = 0;
            int columnMispelled = 0;
            int columnBlank = 0;
            double columnScore;

            for (int row = 0; row < numRows; row++) {
                String myGuess = guess[row][col];
                String theAnswer = answer[row][col];

                if (myGuess.length() > 0)
                    totalAttempt++;

                if (myGuess.equalsIgnoreCase(theAnswer)) {                        // right
                    columnCorrect++;
                    totalCorrect++;
                } else if (oneCharAway(guess[row][col], answer[row][col]))               // right, but mispelled
                    columnMispelled++;
                else if (myGuess.length() == 0) {                                  // blank
                    columnBlank++;
                    columnWrong++;
                } else {                                                                  // wrong
                    columnWrong++;
                }
            }

            if (col != lastAttemptedColumn) { // not the last column attempted
                if (columnWrong == 0)
                    columnScore = columnCorrect;
                else if (columnWrong == 1)
                    columnScore = columnCorrect / 2.0f;
                else
                    columnScore = 0;
            } else {                                       // indeed the last column attempted
                if ((columnWrong - columnBlank) == 0)
                    columnScore = columnCorrect;
                else if ((columnWrong - columnBlank) == 1)
                    columnScore = columnCorrect / 2.0f;
                else
                    columnScore = 0;
            }

            TotalScore += columnScore;
        }

        return new Score((int) (totalCorrect*100f / totalAttempt), (int) roundUpToZero(TotalScore));
    }

    private static int getNumRows(Object[][] array) {
        return array.length;
    }

    private static int getNumCols(Object[][] array) {
        return array[0].length;
    }

    private static Boolean oneCharAway(String guess, String answer) {
        int guessLength = guess.length();
        int answerLength = answer.length();

        int diff = guessLength - answerLength;
        if (diff < 0)
            diff = -1 * diff;

        if (diff > 1)
            return false;

        else if (diff == 1) {
            String small;
            String large;
            if (guessLength < answerLength) {
                small = guess;
                large = answer;
            } else {
                small = answer;
                large = guess;
            }
            for (int pos = 0; pos <= small.length(); pos++) {
                for (char b : alphabet) {
                    String a = small.substring(0, pos);
                    String c = small.substring(pos, small.length());
                    String newGuess = a + b + c;

                    if (newGuess.equals(large))
                        return true;
                }
            }
        } else {   // diff == 0
            for (int pos = 0; pos < guess.length(); pos++) {
                for (char b : alphabet) {
                    String a = guess.substring(0, pos);
                    String c = guess.substring(pos + 1, guess.length());
                    String newGuess = a + b + c;

                    if (newGuess.equals(answer))
                        return true;
                }
            }
        }
        return false;
    }

    private static int lastAttemptedColumn(String[][] recallSheet) {
        for (int col = getNumCols(recallSheet) - 1; col >= 0; col--) {
            if (attemptedColumn(recallSheet, col))
                return col;
        }

        return 0;
    }

    private static Boolean attemptedColumn(String[][] array, int col) {
        for (int row = 0; row < getNumRows(array); row++) {
            if (array[row][col] != null && array[row][col].length() > 0)
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
