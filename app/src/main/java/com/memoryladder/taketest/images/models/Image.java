package com.memoryladder.taketest.images.models;

import com.memoryladder.taketest.images.ui.adapters.ReviewCellOutcome;

public class Image {

    private int memoryIndex;
    private int recallIndex;

    private ReviewCellOutcome result;

    private final int imageId;

    public Image(int memoryIndex, int imageId) {
        this.memoryIndex = memoryIndex;
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

    public void setRecallIndex(int recallIndex) {
        this.recallIndex = recallIndex;
    }

    public ReviewCellOutcome getResult() {
        if (result == null) {
            result = memoryIndex == recallIndex ? ReviewCellOutcome.CORRECT : ReviewCellOutcome.WRONG;
        }

        return result;
    }
}
