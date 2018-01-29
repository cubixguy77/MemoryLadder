package com.MemoryLadder.NumbersChallenges;

public class DigitSpeed {

    public final static int DIGIT_SPEED_SUPER_FAST = 0;
    public final static int DIGIT_SPEED_FAST = 1;
    public final static int DIGIT_SPEED_STANDARD = 2;
    public final static int DIGIT_SPEED_SLOW = 3;
    public final static int DIGIT_SPEED_SUPER_SLOW = 4;

    public static String getDisplayName(int digitSpeed) {
        switch (digitSpeed) {
            case DIGIT_SPEED_SUPER_FAST: return "Super Fast";
            case DIGIT_SPEED_FAST:       return "Fast";
            case DIGIT_SPEED_STANDARD:   return "Standard";
            case DIGIT_SPEED_SLOW:       return "Slow";
            case DIGIT_SPEED_SUPER_SLOW: return "Super Slow";
            default: return "Error";
        }
    }

    static int getMillisPerSecond(int digitSpeed) {
        switch (digitSpeed) {
            case DIGIT_SPEED_SUPER_FAST: return 500;
            case DIGIT_SPEED_FAST:       return 750;
            case DIGIT_SPEED_STANDARD:   return 1000;
            case DIGIT_SPEED_SLOW:       return 2000;
            case DIGIT_SPEED_SUPER_SLOW: return 4000;
            default: return -1;
        }
    }

    static float getSpeechRate(int digitSpeed) {
        switch (digitSpeed) {
            case DIGIT_SPEED_SUPER_FAST: return 2.5f;
            case DIGIT_SPEED_FAST:       return 2.0f;
            case DIGIT_SPEED_STANDARD:   return 1.0f;
            case DIGIT_SPEED_SLOW:       return 1.0f;
            case DIGIT_SPEED_SUPER_SLOW: return 1.0f;
            default: return -1;
        }
    }
}
