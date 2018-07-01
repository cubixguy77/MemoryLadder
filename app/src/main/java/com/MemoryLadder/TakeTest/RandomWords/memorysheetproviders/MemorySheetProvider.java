package com.MemoryLadder.TakeTest.RandomWords.memorysheetproviders;

import java.util.ArrayList;
import java.util.List;

public class MemorySheetProvider {

    public static List<String> getMemorySheet(int numWords) {
        List<String> words = new ArrayList<>();

        for (int i=0; i<numWords; i++) {
            words.add(Character.toString((char) ('a' + i)));
        }

        return words;
    }

    public static List<String> getRecallSheet(int numWords) {
        List<String> recallSheet = new ArrayList<>();

        for (int i=0; i<numWords; i++) {
            recallSheet.add("");
        }

        return recallSheet;
    }
}
