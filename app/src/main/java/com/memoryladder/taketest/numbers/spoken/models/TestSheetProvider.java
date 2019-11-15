package com.memoryladder.taketest.numbers.spoken.models;

import com.mastersofmemory.memoryladder.BuildConfig;
import com.memoryladder.taketest.numbers.MemoryDataSetFactory;

public class TestSheetProvider {
    public static TestSheet getTestSheet(int numDigits, boolean noRepeats) {
        return new TestSheet(MemoryDataSetFactory.getNumberDataSet(numDigits, 10, false, noRepeats));
    }
}
