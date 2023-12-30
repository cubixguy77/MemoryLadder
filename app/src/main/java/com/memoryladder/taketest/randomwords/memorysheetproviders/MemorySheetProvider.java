package com.memoryladder.taketest.randomwords.memorysheetproviders;

import com.memoryladder.Utils;

import java.util.ArrayList;
import java.util.List;

public class MemorySheetProvider {

    public static List<String> getMemorySheet(List<String> wordBank, int numWords) {
        return Utils.pickNRandomElements(wordBank, numWords);
    }

    public static List<String> getRecallSheet(int numWords) {
        List<String> recallSheet = new ArrayList<>();

        for (int i=0; i<numWords; i++) {
            recallSheet.add("");
        }

        return recallSheet;
    }
}
