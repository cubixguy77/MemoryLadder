package com.memoryladder.taketest.images.memorysheetproviders;

import com.memoryladder.Utils;
import com.memoryladder.taketest.images.models.Image;
import com.memoryladder.taketest.images.ui.adapters.TestSheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemorySheetProvider {

    public static TestSheet getTestSheet(int numRows, List<Integer> images) {
        int IMAGES_PER_ROW = 5;
        int NUM_IMAGES_TO_FETCH = numRows * IMAGES_PER_ROW;

        Utils.pickNRandomElements(images, NUM_IMAGES_TO_FETCH);

        ArrayList<Image>[] memorySheet = new ArrayList[numRows];
        List<Integer> permutation = Arrays.asList(1, 2, 3, 4, 5);
        int imageFetchCount = 0;

        for (int row = 0; row < numRows; row++) {
            memorySheet[row] = new ArrayList<>(IMAGES_PER_ROW + 1);
            memorySheet[row].add(null);

            Collections.shuffle(permutation);
            memorySheet[row].add(new Image(1, permutation.get(0), images.get(imageFetchCount++)));
            memorySheet[row].add(new Image(2, permutation.get(1), images.get(imageFetchCount++)));
            memorySheet[row].add(new Image(3, permutation.get(2), images.get(imageFetchCount++)));
            memorySheet[row].add(new Image(4, permutation.get(3), images.get(imageFetchCount++)));
            memorySheet[row].add(new Image(5, permutation.get(4), images.get(imageFetchCount++)));
        }

        return new TestSheet(memorySheet);
    }
}
