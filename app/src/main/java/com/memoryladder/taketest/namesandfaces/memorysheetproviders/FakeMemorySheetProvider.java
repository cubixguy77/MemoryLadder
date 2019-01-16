package com.memoryladder.taketest.namesandfaces.memorysheetproviders;

import java.util.ArrayList;
import java.util.List;

public class FakeMemorySheetProvider {

    public static List<String> getMemorySheet(List<String> wordBank, int numWords) {
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
