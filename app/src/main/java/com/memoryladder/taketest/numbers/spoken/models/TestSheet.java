package com.memoryladder.taketest.numbers.spoken.models;

import com.memoryladder.taketest.dates.ui.adapters.ReviewCellOutcome;
import com.memoryladder.taketest.scorepanel.Score;

import java.util.Arrays;

public class TestSheet {

    private char[] memoryData;
    private char[] recallData;

    public static final char EMPTY_CHAR = '‒';
    private static String[] digits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "‒"};

    public TestSheet(char[] memoryData)
    {
        this.memoryData = memoryData;
        recallData = new char[memoryData.length];
        Arrays.fill(recallData, EMPTY_CHAR);
    }

    public String getMemoryText(int pos) {
        return getDigitString(memoryData[pos]);
    }

    public String getRecallText(int pos) {
        return getDigitString(recallData[pos]);
    }

    private String getDigitString(char digit) {
        switch (digit) {
            case '0': return digits[0];
            case '1': return digits[1];
            case '2': return digits[2];
            case '3': return digits[3];
            case '4': return digits[4];
            case '5': return digits[5];
            case '6': return digits[6];
            case '7': return digits[7];
            case '8': return digits[8];
            case '9': return digits[9];
            default: return digits[10];
        }
    }

    public void registerRecall(char digit, int pos) {
        this.recallData[pos] = digit;
    }

    public ReviewCellOutcome checkAnswerAt(int pos) {
        return memoryData[pos] == recallData[pos] ? ReviewCellOutcome.CORRECT : (recallData[pos] == EMPTY_CHAR ? ReviewCellOutcome.BLANK : ReviewCellOutcome.WRONG);
    }

    public int getDigitCount() {
        return this.memoryData.length;
    }

    public Score getScore() {
        int totalScore = 0;
        boolean validSoFar = true;

        int totalRecallCorrect = 0;
        int totalRecallAttempt = 0;

        for (int i=0; i<memoryData.length; i++) {
            ReviewCellOutcome result = checkAnswerAt(i);
            if (result == ReviewCellOutcome.CORRECT) {
                totalRecallAttempt++;
                totalRecallCorrect++;
                if (validSoFar) {
                    totalScore++;
                }
            }
            else if (result == ReviewCellOutcome.WRONG) {
                totalRecallAttempt++;
                validSoFar = false;
            }
        }

        return new Score((int) ((float) totalRecallCorrect * 100 / totalRecallAttempt), totalScore);
    }
}
