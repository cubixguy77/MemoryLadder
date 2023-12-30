package com.memoryladder.taketest.images.models;

import com.memoryladder.taketest.images.ui.adapters.ReviewCellOutcome;

public class Image {

    private int memoryIndex;
    private int recallIndex;
    private boolean attempted;

    private ReviewCellOutcome result;

    private final int imageId;

    public Image(int memoryIndex, int recallIndex, int imageId) {
        this.memoryIndex = memoryIndex;
        this.recallIndex = recallIndex;
        this.attempted = false;
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public int getMemoryIndex() {
        return memoryIndex;
    }
    public int getRecallIndex() {
        return recallIndex;
    }
    public boolean isAttempted() { return attempted; }

    void setRecallIndex(int recallIndex) {
        this.recallIndex = recallIndex;
    }

    void setAttempted(boolean attempted) {
        this.attempted = attempted;
    }

    public ReviewCellOutcome getResult() {
        if (result == null) {
            result = !attempted ? ReviewCellOutcome.BLANK : memoryIndex == recallIndex ? ReviewCellOutcome.CORRECT : ReviewCellOutcome.WRONG;
        }

        return result;
    }
}
