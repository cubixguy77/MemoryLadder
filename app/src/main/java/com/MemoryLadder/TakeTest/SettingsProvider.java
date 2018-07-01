package com.MemoryLadder.TakeTest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.MemoryLadder.Constants;
import com.MemoryLadder.TakeTest.Cards.CardSettings;
import com.MemoryLadder.TakeTest.RandomWords.settings.RandomWordsSettings;
import com.MemoryLadder.TakeTest.WrittenNumbers.WrittenNumbersSettings;

import static com.MemoryLadder.Constants.LISTS_WORDS;
import static com.MemoryLadder.Constants.NUMBERS_SPEED;

class SettingsProvider {

    static GameSettings getGeneralSettings(Intent i) {
        int mode              = i.getIntExtra("mode",     -1);
        int gameType          = i.getIntExtra("gameType", -1);
        int step              = i.getIntExtra("step",     -1);
        int memTime           = i.getIntExtra("memTime",  -1);
        int recallTime        = i.getIntExtra("recallTime",-1);

        return new GameSettings(mode, gameType, step, memTime, recallTime);
    }

    static CardSettings getCardSettings(Intent i) {
        int deckSize              = i.getIntExtra("deckSize", -1);
        int numDecks              = i.getIntExtra("numDecks", -1);
        int numCardsPerGroup      = i.getIntExtra("numCardsPerGroup", 2);
        boolean mnemonicsEnabled  = i.getIntExtra("mnemo_enabled", 0) == 1;

        return new CardSettings(numDecks, deckSize, numCardsPerGroup, true, mnemonicsEnabled);
    }

    static WrittenNumbersSettings getWrittenNumbersSettings(Intent i, Context context) {
        int numRows               = i.getIntExtra("numRows",     -1);
        int numCols               = i.getIntExtra("numCols",     -1);
        int digitsPerGroup        = i.getIntExtra("digitsPerGroup",     2);
        boolean mnemonicsEnabled  = i.getIntExtra("mnemo_enabled", 0) == 1;
        int base                  = i.getIntExtra("base",     10);

        SharedPreferences prefs = context.getSharedPreferences(Constants.getPrefsName(NUMBERS_SPEED), 0);
        boolean nightMode = prefs.getBoolean("WRITTEN_nightMode", false);
        boolean drawGridLines = prefs.getBoolean("WRITTEN_drawGridLines", true);

        return new WrittenNumbersSettings(numRows, numCols, digitsPerGroup, mnemonicsEnabled, base, nightMode, drawGridLines);
    }

    static RandomWordsSettings getRandomWordsSettings(Intent i, Context context) {
        int numCols               = i.getIntExtra("numCols",     -1);
        int wordsPerCol           = i.getIntExtra("numRows",     -1);

        SharedPreferences prefs = context.getSharedPreferences(Constants.getPrefsName(LISTS_WORDS), 0);
        boolean nightMode = prefs.getBoolean("WORDS_nightMode", false);

        return new RandomWordsSettings(numCols, wordsPerCol, nightMode);
    }
}
