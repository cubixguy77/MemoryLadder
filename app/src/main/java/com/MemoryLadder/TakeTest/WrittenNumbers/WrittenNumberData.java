package com.MemoryLadder.TakeTest.WrittenNumbers;

import android.os.Parcel;
import android.os.Parcelable;

import com.MemoryLadder.TakeTest.GamePhase;
import com.MemoryLadder.TakeTest.ScorePanel.Score;
import com.MemoryLadder.TakeTest.WrittenNumbers.Scoring.ScoreCalculation;
import com.MemoryLadder.Utils;

import java.util.Arrays;

public class WrittenNumberData implements Parcelable {

    private char[] memoryData;
    private char[] recallData;

    private int highlightPos;
    private int textEntryPos;
    private boolean keepHighlightPos = false;

    private int numDigits;
    private int numRows;
    private int numCols;
    private final int digitsPerGroup;
    private GamePhase gamePhase;

    public static final char EMPTY_CHAR = '‒';
    private static final String EMPTY_STRING = "";

    WrittenNumberData(WrittenNumbersSettings settings) {
        this.digitsPerGroup = settings.getDigitsPerGroup();
        this.numDigits = settings.getNumRows() * settings.getNumCols();
        this.numRows = settings.getNumRows();
        this.numCols = settings.getNumCols();
        resetHighlight();

        memoryData = new char[numDigits];
        recallData = new char[numDigits];
        Arrays.fill(recallData, EMPTY_CHAR);

        /* Fill memory data sequentially */
        for (int i=0; i<numDigits; i++) {
            memoryData[i] = (char) ('0' + (i % 10));
        }
    }

    public int getNumRows() {
        return this.numRows;
    }

    public int getNumCols() {
        return this.numCols;
    }

    void resetHighlight() {
        this.setHighlightPos(0);
    }

    void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    void registerRecall(char digit) {
        recallData[textEntryPos] = digit;
    }

    /* Returns true when the current group has been filled */
    boolean highlightNextCell() {
        if (textEntryPos + 1 < this.numDigits) {
            this.textEntryPos++;
        }

        return this.allowNext() && this.textEntryPos % this.digitsPerGroup == 0;
    }

    /* Returns true when the current group has been filled */
    boolean highlightPrevCell() {
        if (textEntryPos - 1 >= 0) {
            this.textEntryPos--;
        }

        return this.textEntryPos < this.highlightPos;
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
            return EMPTY_STRING;

        int stringLen = Utils.lesserOf(digitsPerGroup, numDigits - pos);

        switch (stringLen) {
            case 1: return new String(new char[] { getCharAt(pos)});//, EMPTY_SPACE,          EMPTY_SPACE });
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

    public int getRow(int pos) {
        return pos / numCols;
    }

    int getHighlightRowNumBegin() {
        return getRow(this.highlightPos);
    }

    int getHighlightRowNumEnd() {
        return getRow(Utils.lesserOf(this.highlightPos + this.digitsPerGroup - 1, this.numDigits - 1));
    }

    int getHighlightPos() {
        return this.highlightPos;
    }

    int getHighlightPosEnd() {
        return this.highlightPos + Utils.lesserOf(this.digitsPerGroup, this.numDigits - this.highlightPos) - 1;
    }

    int getTextEntryPos() {
        return this.textEntryPos;
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

    boolean allowNext() {
        return this.highlightPos + this.digitsPerGroup < this.numDigits;
    }

    boolean allowPrev() {
        return this.highlightPos > 0;
    }

    private void setHighlightPos(int pos) {
        this.highlightPos = pos;
        this.textEntryPos = pos;
    }

    void highlightNextGroup() {
        setHighlightPos(this.highlightPos + digitsPerGroup);
    }

    void highlightPrevGroup() {
        setHighlightPos(this.highlightPos - digitsPerGroup);
    }

    public boolean isTextHighlighted(int position) {
        return position == this.textEntryPos;
    }

    void setTextEntryPos(int textEntryPos) {
        this.textEntryPos = textEntryPos;
    }

    private WrittenNumberData(Parcel in) {
        highlightPos = in.readInt();
        textEntryPos = in.readInt();
        numDigits = in.readInt();
        numRows = in.readInt();
        numCols = in.readInt();
        memoryData = new char[numDigits];
        recallData = new char[numDigits];
        in.readCharArray(memoryData);
        in.readCharArray(recallData);
        digitsPerGroup = in.readInt();
        gamePhase = (GamePhase) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(highlightPos);
        dest.writeInt(textEntryPos);
        dest.writeInt(numDigits);
        dest.writeInt(numRows);
        dest.writeInt(numCols);
        dest.writeCharArray(memoryData);
        dest.writeCharArray(recallData);
        dest.writeInt(digitsPerGroup);
        dest.writeSerializable(gamePhase);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WrittenNumberData> CREATOR = new Parcelable.Creator<WrittenNumberData>() {
        @Override
        public WrittenNumberData createFromParcel(Parcel in) {
            return new WrittenNumberData(in);
        }

        @Override
        public WrittenNumberData[] newArray(int size) {
            return new WrittenNumberData[size];
        }
    };

    public void setKeepHighlightPos(boolean keepHighlightPos) {
        this.keepHighlightPos = keepHighlightPos;
    }

    public boolean isKeepHighlightPos() {
        return keepHighlightPos;
    }
}
