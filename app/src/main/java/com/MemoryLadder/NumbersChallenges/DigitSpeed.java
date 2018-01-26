package com.MemoryLadder.NumbersChallenges;

public class DigitSpeed {

    public static String getDisplayName(float digitSpeed) {
        if (digitSpeed == 3) return "Slow";
        if (digitSpeed == 2) return "Medium";
        if (digitSpeed == 1) return "Fast";
        if (digitSpeed == 0.5f) return "Super Fast";
        return "Error";
    }

    public static float getDigitSpeed(String displayName) {
        switch (displayName) {
            case "Slow": return 3;
            case "Medium": return 2;
            case "Fast": return 1;
            case "Super Fast": return 0.5f;
            default: return 1;
        }
    }
}
