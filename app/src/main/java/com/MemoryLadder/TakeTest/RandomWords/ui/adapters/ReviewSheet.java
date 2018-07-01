package com.MemoryLadder.TakeTest.RandomWords.ui.adapters;

import java.util.List;

public class ReviewSheet {

    private List<String> memorySheet;
    private List<String> recallSheet;

    public ReviewSheet(List<String> memorySheet, List<String> recallSheet) {
        this.memorySheet = memorySheet;
        this.recallSheet = recallSheet;
    }

    public String getMemoryWord(int position) {
        return memorySheet.get(position);
    }

    public String getRecallWord(int position) {
        return recallSheet.get(position);
    }

    public ReviewCellOutcome getOutcome(int position) {
        String memoryWord = getMemoryWord(position);
        String recallWord = getRecallWord(position);

        if (recallWord == null || recallWord.length() == 0)
            return ReviewCellOutcome.BLANK;

        if (memoryWord.equalsIgnoreCase(recallWord))
            return ReviewCellOutcome.CORRECT;

        return ReviewCellOutcome.WRONG;
    }
}
