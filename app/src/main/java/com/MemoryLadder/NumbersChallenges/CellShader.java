package com.MemoryLadder.NumbersChallenges;

import android.content.Context;
import android.content.SharedPreferences;

import com.MemoryLadder.Constants;
import com.MemoryLadder.Utils;

/**
 * This class handles the shading of cells when Mnemonic Assist is On
 */
public class CellShader {

    private int numCells;
    private int numRows;
    private int numCols;
    private int gameType;
    private SharedPreferences prefs;

    private int maxIndex;
    private int[] cellPos;

    private int firstPos;
    private int lastPos;

    CellShader(int numCells, int numRows, int numCols, int gameType, Context context) {
        this.numCells = numCells;
        this.numRows = numRows;
        this.numCols = numCols;
        this.gameType = gameType;

        initializePositions();

        prefs = context.getSharedPreferences("Peg_Numbers", 0);
    }

    private void initializePositions() {
        maxIndex = (numRows * numCols) - 1;

        cellPos = new int[numCells];

        cellPos[0] = 1;
        for (int i = 1; i < numCells; i++) {
            cellPos[i] = 1;
            while (cellPos[i] <= cellPos[i - 1] || getCol(cellPos[i]) == 0)
                cellPos[i]++;
        }

        firstPos = cellPos[0];
        lastPos = cellPos[numCells - 1];

    }

    void advancePositions() {
        if (cellPos[numCells - 1] + numCells <= maxIndex) {
            for (int i = numCells - 1; i >= 0; i--) {
                cellPos[i] = advanceCell(cellPos[i]);
            }
        }

        firstPos = cellPos[0];
        lastPos = cellPos[numCells - 1];
    }

    private int advanceCell(int pos) {
        int newpos = pos;
        for (int i = 1; i <= numCells; i++) {
            newpos = getNext(newpos);
        }
        return newpos;
    }

    private int getNext(int pos) {
        if (getCol(pos + 1) == 0)
            return (pos + 2);
        return pos + 1;
    }

    Boolean shouldHighlight(int pos) {
        return pos >= firstPos && pos <= lastPos && getCol(pos) != 0;
    }

    public int getCol(int pos) {
        return pos % (numCols);
    }

    public int getRow(int pos) {
        return pos / (numCols);
    }

    String getMnemoText(char[][] answer) {
        if (gameType == Constants.NUMBERS_BINARY) {
            return getMnemoBinary(answer);
        } else
            return getMnemoDecimal(answer);
    }

    private String getMnemoDecimal(char[][] chars) {
        int num = getNumericValue(chars, 0, numCells - 1, false);
        String mnemo = prefs.getString("peg_numbers_" + num, Utils.getNumberSuggestions(num)[0]);
        return "\"" + mnemo + "\"";
    }

    private String getMnemoBinary(char[][] chars) {
        int tens = getNumericValue(chars, 0, 2, true);
        int ones = getNumericValue(chars, 3, 5, true);
        int total = (10 * tens) + ones;
        String mnemo = prefs.getString("peg_numbers_" + total, Utils.getNumberSuggestions(total)[0]);
        return "\"" + mnemo + "\"";
    }

    private int getNumericValue(char[][] chars, int start, int end, Boolean binary) {
        int power = 0;
        int base;

        if (binary) base = 2;
        else base = 10;

        int result = 0;
        for (int i = end; i >= start; i--) {
            result += getDigit(chars, cellPos[i]) * pow(base, power);
            power++;
        }
        return result;
    }

    private int pow(int base, int power) {
        if (power == 0)
            return 1;
        return base * pow(base, power - 1);
    }

    private int getDigit(char[][] chars, int pos) {
        int row = getRow(pos);
        int col = getCol(pos);
        return Character.getNumericValue(chars[row][col]);
    }

}