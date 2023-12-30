package com.memoryladder.taketest.numbers.written;

import android.os.Parcel;
import android.os.Parcelable;

import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.scorepanel.Score;
import com.memoryladder.taketest.numbers.written.scoring.ScoreCalculation;
import com.memoryladder.Utils;

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
    private static final String EMPTY_STRING = "  ";
    private static String[] digits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "‒"};

    WrittenNumberData(WrittenNumbersSettings settings, char[] memoryData) {
        this.digitsPerGroup = settings.getDigitsPerGroup();
        this.numDigits = settings.getNumRows() * settings.getNumCols();
        this.numRows = settings.getNumRows();
        this.numCols = settings.getNumCols();
        resetHighlight();

        this.memoryData = memoryData;
        recallData = new char[numDigits];
        Arrays.fill(recallData, EMPTY_CHAR);
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

    void highlightCell(int index) {
        this.textEntryPos = index;
        this.highlightPos = index - (index % digitsPerGroup);
    }

    private void setHighlightPos(int pos) {
        this.highlightPos = pos;
        this.textEntryPos = pos;
    }

    void setTextEntryPos(int textEntryPos) {
        this.textEntryPos = textEntryPos;
    }



    public String getMemoryText(int pos) {
        return getDigitString(memoryData[pos]);
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

    public String getRecallText(int pos) {
        return getDigitString(recallData[pos]);
    }

    private char getCharAt(int pos) {
        return this.gamePhase == GamePhase.MEMORIZATION ? memoryData[pos] : recallData[pos];
    }

    public String getText(int pos) {
        return getDigitString(getCharAt(pos));
    }

    String getGroupText(int pos) {
        if (pos < 0 || pos >= numDigits)
            return EMPTY_STRING;

        int stringLen = Utils.lesserOf(digitsPerGroup, numDigits - pos);

        switch (stringLen) {
            case 1: return new String(new char[] { getCharAt(pos)});//, EMPTY_SPACE,          EMPTY_SPACE });
            case 2: return new String(new char[] { getCharAt(pos), getCharAt(pos+1) });
            case 3: return new String(new char[] { getCharAt(pos), getCharAt(pos+1), getCharAt(pos+2) });
            case 4: return new String(new char[] { getCharAt(pos), getCharAt(pos+1), getCharAt(pos+2), getCharAt(pos+3) });
            case 5: return new String(new char[] { getCharAt(pos), getCharAt(pos+1), getCharAt(pos+2), getCharAt(pos+3), getCharAt(pos+4) });
            case 6: return new String(new char[] { getCharAt(pos), getCharAt(pos+1), getCharAt(pos+2), getCharAt(pos+3), getCharAt(pos+4), getCharAt(pos+5) });
            default: return "x";
        }
    }

    String getPreviousGroupText() {
        return this.getGroupText(getHighlightPos() - this.digitsPerGroup);
    }

    String getCurrentGroupText() {
        return this.getGroupText(getHighlightPos());
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
                (digitsPerGroup >= 3 && this.highlightPos + 2 == pos) ||
                (digitsPerGroup >= 4 && this.highlightPos + 3 == pos) ||
                (digitsPerGroup >= 5 && this.highlightPos + 4 == pos) ||
                (digitsPerGroup >= 6 && this.highlightPos + 5 == pos);
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





    public boolean isTextHighlighted(int position) {
        return position == this.textEntryPos;
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
