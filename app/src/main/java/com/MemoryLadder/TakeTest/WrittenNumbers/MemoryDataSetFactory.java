package com.memoryladder.taketest.WrittenNumbers;

import java.util.Random;

class MemoryDataSetFactory {

    static char[] getNumberDataSet(int numDigits, int base, boolean ordered) {
        if (base == 2 && ordered) {
            return getOrderedBinaryNumberSet(numDigits);
        } else if (base == 2) {
            return getRandomizedBinaryNumberSet(numDigits);
        } else if (base == 10 && ordered) {
            return getOrderedDecimalNumberSet(numDigits);
        } else if (base == 10) {
            return getRandomizedDecimalNumberSet(numDigits);
        }

        return null;
    }

    private static char[] getRandomizedDecimalNumberSet(int numDigits) {
        return getRandomizedNumberData(numDigits, 10);
    }

    private static char[] getOrderedDecimalNumberSet(int numDigits) {
        return getOrderedNumberData(numDigits, 10);
    }

    private static char[] getRandomizedBinaryNumberSet(int numDigits) {
        return getRandomizedNumberData(numDigits, 2);
    }

    private static char[] getOrderedBinaryNumberSet(int numDigits) {
        return getOrderedNumberData(numDigits, 2);
    }

    private static char[] getRandomizedNumberData(int numDigits, int base) {
        char[] data = new char[numDigits];
        Random rand = new Random();

        for (int i = 0; i < numDigits; i++) {
            data[i] = Character.forDigit(rand.nextInt(base), 10);
        }

        return data;
    }

    private static char[] getOrderedNumberData(int numDigits, int base) {
        char[] data = new char[numDigits];

        for (int i = 0; i < numDigits; i++) {
            data[i] = (char) ('0' + (i % base));
        }

        return data;
    }
}
