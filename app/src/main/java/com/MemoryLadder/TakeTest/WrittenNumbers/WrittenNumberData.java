package com.MemoryLadder.TakeTest.WrittenNumbers;

import com.MemoryLadder.TakeTest.GamePhase;
import com.MemoryLadder.TakeTest.ScorePanel.Score;
import com.MemoryLadder.TakeTest.WrittenNumbers.NumberCarousel.CustomNumberCarousel;
import com.MemoryLadder.TakeTest.WrittenNumbers.Scoring.ScoreCalculation;

import java.util.Arrays;

public class WrittenNumberData {

    private char[] memoryData;
    private char[] recallData;

    private int highlightPos;
    private int textEntryPos;

    private int numDigits;
    private int numRows;
    private int numCols;
    private final int digitsPerGroup;
    private GamePhase gamePhase;

    public static final char EMPTY_CHAR = '-';

    WrittenNumberData(WrittenNumbersSettings settings) {
        this.digitsPerGroup = settings.getDigitsPerGroup();
        this.numDigits = settings.getNumRows() * settings.getNumCols();
        this.numRows = settings.getNumRows();
        this.numCols = settings.getNumCols();
        resetHighlight();

        memoryData = new char[numDigits];
        recallData = new char[numDigits];
        Arrays.fill(recallData, '-');

        /* Fill memory data sequentially */
        for (int i=0; i<numDigits; i++) {
            memoryData[i] = (char) ('0' + (i % 10));
        }
    }

    void resetHighlight() {
        this.highlightPos = 0;
        this.textEntryPos = 0;
    }

    void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    void setRecallDigit(char digit) {
        recallData[textEntryPos] = digit;
    }

    void highlightNextGroup() {
        this.highlightPos += digitsPerGroup;
        this.textEntryPos = this.highlightPos;
    }

    /* Returns true when the current group has been filled */
    boolean highlightNextCell() {
        this.textEntryPos++;
        return this.textEntryPos % this.digitsPerGroup == 0;
    }

    public String getMemoryText(int pos) {
        return Character.toString(memoryData[pos]);
    }

    public String getRecallText(int pos) {
        return Character.toString(recallData[pos]);
    }

    private char getCharAt(int pos) {
        return this.gamePhase == GamePhase.MEMORIZATION ? memoryData[pos] : recallData[pos];
    }

    public String getText(int pos) {
        return Character.toString(getCharAt(pos));
    }

    String getGroupText(int pos) {
        if (pos < 0 || pos >= numDigits)
            return CustomNumberCarousel.EMPTY_STRING;

        int stringLen = lesserOf(digitsPerGroup, numDigits - pos);

        switch (stringLen) {
            case 1: return getText(pos);
            case 2: return new String(new char[] { getCharAt(pos), getCharAt(pos+1) });
            case 3: return new String(new char[] { getCharAt(pos), getCharAt(pos+1), getCharAt(pos+2) });
            default: return "x";
        }
    }

    String getPreviousGroupText() {
        return this.getGroupText(getHighlightPos() - this.digitsPerGroup);
    }

    String getNextGroupText() {
        return this.getGroupText(getHighlightPos() + this.digitsPerGroup);
    }

    public int getNumDigits() {
        return this.numDigits;
    }

    /*
    public int getNumGroups() {
        return (this.numDigits / this.digitsPerGroup) + (this.numDigits % this.digitsPerGroup == 0 ? 0 : 1);
    }
    */

    int getHighlightPos() {
        return this.highlightPos;
    }

    int getDigitsPerGroup() {
        return digitsPerGroup;
    }

    public boolean isHighlighted(int pos) {
        return this.highlightPos == pos ||
                (digitsPerGroup >= 2 && this.highlightPos + 1 == pos) ||
                (digitsPerGroup >= 3 && this.highlightPos + 2 == pos);
    }

    /*
     * -1: Empty
     *  0: Wrong
     *  1: Right
     */
    public int checkAnswerAt(int pos) {
        return memoryData[pos] == recallData[pos] ? 1 : (recallData[pos] == EMPTY_CHAR ? -1 : 0);
    }

    public Score getScore() {
        char[][] scoreFormattedMemoryArray = new char[numRows][numCols];
        char[][] scoreFormattedRecallArray = new char[numRows][numCols];
        for (int row=0; row<numRows; row++) {
            for (int col=0; col<numCols; col++) {
                scoreFormattedMemoryArray[row][col] = memoryData[(row * numCols) + col];
                scoreFormattedRecallArray[row][col] = recallData[(row * numCols) + col];
            }
        }

        return ScoreCalculation.getScore(scoreFormattedRecallArray, scoreFormattedMemoryArray);
    }

    public GamePhase getGamePhase() {
        return this.gamePhase;
    }

    private int lesserOf(int a, int b) {
        return a < b ? a : b;
    }
}
