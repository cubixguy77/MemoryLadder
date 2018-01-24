package com.MemoryLadder.TestDetailsScreen;

import android.content.Context;
import android.content.SharedPreferences;

import com.MemoryLadder.Constants;
import com.MemoryLadder.Utils;

import java.util.ArrayList;

class SettingLoader {


    static int getCurrentLevel(Context context, int game) {
        SharedPreferences settings = context.getSharedPreferences("Steps", 0);
        return settings.getInt("CARDS_SPEED", 1);
    }

    static ArrayList<Setting> getSettings(Context context, int game, int gameMode) {
        ArrayList<Setting> settings = new ArrayList<>();

        if (game == Constants.CARDS_SPEED) {
            int numDecks;
            int deckSize;
            int cardsPerGroup;
            int memTime;
            int recallTime;
            boolean mnemo_enabled;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt("CARDS_SPEED", 1);
                int[] specs = Constants.getSpecs_STEPS_Cards(step);

                deckSize     = specs[0];
                numDecks     = specs[1];
                memTime      = specs[2];
                recallTime	 = specs[3];
                target       = specs[4];
                cardsPerGroup = 2;

                settings.add(new Setting("deckSize", "Cards per deck:", deckSize, Integer.toString(deckSize)));
                settings.add(new Setting("numDecks", "Number of decks:", numDecks, Integer.toString(numDecks)));
                settings.add(new Setting("numCardsPerGroup", "Cards per group:", cardsPerGroup, Integer.toString(cardsPerGroup)));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
                settings.add(new Setting("target", "Target:", target, Integer.toString(target)));
            }

            else {
                SharedPreferences prefs = context.getSharedPreferences("Card_Preferences", 0);
                deckSize         = prefs.getInt("deckSize",   Constants.default_cards_deckSize);
                numDecks         = prefs.getInt("numDecks",   Constants.default_cards_numDecks);
                cardsPerGroup    = prefs.getInt("numCardsPerGroup", Constants.default_cards_cardsPerGroup);
                memTime          = prefs.getInt("memTime",    Constants.default_cards_memTime);
                recallTime       = prefs.getInt("recallTime", Constants.default_cards_recallTime);
                mnemo_enabled    = prefs.getBoolean("mnemo_enabled", false);

                settings.add(new Setting("deckSize", "Cards per deck:", deckSize, Integer.toString(deckSize)));
                settings.add(new Setting("numDecks", "Number of decks:", numDecks, Integer.toString(numDecks)));
                settings.add(new Setting("numCardsPerGroup", "Cards per group:", cardsPerGroup, Integer.toString(cardsPerGroup)));
                settings.add(new Setting("mnemo_enabled", "Mnemo Enabled:", mnemo_enabled ? 1 : 0, mnemo_enabled ? "On": "Off"));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
            }
        }

        return settings;
    }
}
