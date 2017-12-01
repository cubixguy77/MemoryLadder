package com.MemoryLadder.Cards.Timer;

public class TimeFormat {

    public static String formatIntoHHMMSStruncated(long secsIn) {
        int hours = (int) (secsIn / 3600),
                remainder = (int) (secsIn % 3600),
                minutes = remainder / 60,
                seconds = remainder % 60;
        if (hours > 0)
            return  (hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds );
        else
            return (minutes < 10 ? minutes : minutes) + ":" + (seconds < 10 ? "0" : "") + seconds ;
    }

}
