package com.memoryladder.taketest.namesandfaces.models;

import com.memoryladder.taketest.namesandfaces.ui.adapters.ReviewCellOutcome;

public class NameAndFace {

    private final String first;
    private final String last;

    private String firstRecall;
    private String lastRecall;

    private ReviewCellOutcome firstResult;
    private ReviewCellOutcome lastResult;

    private final int imageId;

    public NameAndFace(String first, String last, int imageId) {
        this.first = first;
        this.last = last;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getFirst() {
        return first;
    }
    public String getLast() {
        return last;
    }

    public String getFirstRecall() {
        return firstRecall;
    }
    public String getLastRecall() {
        return lastRecall;
    }

    public void setFirstRecall(String firstRecall) { this.firstRecall = firstRecall; }
    public void setLastRecall(String lastRecall) { this.lastRecall = lastRecall; }

    public ReviewCellOutcome getFirstResult() {
        if (firstResult == null) {
            firstResult = (firstRecall == null || firstRecall.isEmpty()) ? ReviewCellOutcome.BLANK : first.equalsIgnoreCase(firstRecall) ? ReviewCellOutcome.CORRECT : ReviewCellOutcome.WRONG;
        }

        return firstResult;
    }
    public ReviewCellOutcome getLastResult() {
        if (lastResult == null) {
            lastResult = (lastRecall == null || lastRecall.isEmpty()) ? ReviewCellOutcome.BLANK : last.equalsIgnoreCase(lastRecall) ? ReviewCellOutcome.CORRECT : ReviewCellOutcome.WRONG;
        }

        return lastResult;
    }
}
