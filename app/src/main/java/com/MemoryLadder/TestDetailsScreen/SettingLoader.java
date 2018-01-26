package com.MemoryLadder.TestDetailsScreen;

import android.content.Context;
import android.content.SharedPreferences;

import com.MemoryLadder.Constants;
import com.MemoryLadder.DigitSpeed;
import com.MemoryLadder.Utils;

import java.util.ArrayList;

class SettingLoader {

    static int getCurrentLevel(Context context, int gameType) {
        SharedPreferences settings = context.getSharedPreferences("Steps", 0);
        return settings.getInt(Constants.getGameName(gameType), 1);
    }

    static ArrayList<Setting> getSettings(Context context, int game, int gameMode) {
        ArrayList<Setting> settings = new ArrayList<>();

        if (game == Constants.CARDS_LONG) {
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

                settings.add(new Setting("deckSize", "Number of cards:", deckSize, Integer.toString(deckSize)));
                settings.add(new Setting("numDecks", "Number of decks:", numDecks, Integer.toString(numDecks), false));
                settings.add(new Setting("numCardsPerGroup", "Cards per group:", cardsPerGroup, Integer.toString(cardsPerGroup), false));
                settings.add(new Setting("mnemo_enabled", "Mnemo Hint:", 1, "On"));
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
                settings.add(new Setting("mnemo_enabled", "Mnemo Hint:", mnemo_enabled ? 1 : 0, mnemo_enabled ? "On": "Off"));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
            }
        }






        else if (game == Constants.NUMBERS_SPEED) {
            int numRows;
            int numCols;
            int memTime;
            int recallTime;
            boolean mnemo_enabled;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt("NUMBERS_SPEED", 1);
                int[] specs = Constants.getSpecs_STEPS_Numbers(step);

                numRows         = specs[0];
                numCols         = specs[1];
                memTime         = specs[2];
                recallTime      = specs[3];
                target          = specs[4];

                settings.add(new Setting("numRows", "Number of Rows:", numRows, Integer.toString(numRows)));
                settings.add(new Setting("numCols", "Digits per Row:", numCols, Integer.toString(numCols)));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
                settings.add(new Setting("target", "Target:", target, Integer.toString(target)));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("Number_Preferences", 0);
                numRows         = prefs.getInt("WRITTEN_numRows", Constants.default_written_numRows);
                numCols         = prefs.getInt("WRITTEN_numCols", Constants.default_written_numCols);
                memTime         = prefs.getInt("WRITTEN_memTime", Constants.default_written_memTime);
                recallTime      = prefs.getInt("WRITTEN_recallTime", Constants.default_written_recallTime);
                mnemo_enabled   = prefs.getBoolean("WRITTEN_mnemo", false);

                settings.add(new Setting("numRows", "Number of Rows:", numRows, Integer.toString(numRows)));
                settings.add(new Setting("numCols", "Digits per Row:", numCols, Integer.toString(numCols)));
                settings.add(new Setting("mnemo_enabled", "Mnemo Hint:", mnemo_enabled ? 1 : 0, mnemo_enabled ? "On": "Off"));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
            }
        }



        else if (game == Constants.NUMBERS_BINARY) {
            int numRows;
            int numCols;
            int memTime;
            int recallTime;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt("NUMBERS_BINARY", 1);
                int[] specs = Constants.getSpecs_STEPS_Binary(step);

                numRows         = specs[0];
                numCols         = specs[1];
                memTime         = specs[2];
                recallTime      = specs[3];
                target          = specs[4];

                settings.add(new Setting("numRows", "Number of Rows:", numRows, Integer.toString(numRows)));
                settings.add(new Setting("numCols", "Digits per Row:", numCols, Integer.toString(numCols)));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
                settings.add(new Setting("target", "Target:", target, Integer.toString(target)));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("Number_Preferences", 0);
                numRows         = prefs.getInt("BINARY_numRows", Constants.default_binary_numRows);
                numCols         = prefs.getInt("BINARY_numCols", Constants.default_binary_numCols);
                memTime         = prefs.getInt("BINARY_memTime", Constants.default_binary_memTime);
                recallTime      = prefs.getInt("BINARY_recallTime", Constants.default_binary_recallTime);

                settings.add(new Setting("numRows", "Number of Rows:", numRows, Integer.toString(numRows)));
                settings.add(new Setting("numCols", "Digits per Row:", numCols, Integer.toString(numCols)));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
            }
        }




        else if (game == Constants.NUMBERS_SPOKEN) {
            int numRows;
            int numCols;
            float secondsPerDigit;
            int recallTime;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt("NUMBERS_SPOKEN", 1);
                int[] specs = Constants.getSpecs_STEPS_Spoken(step);

                numRows         = specs[0];
                numCols         = specs[1];
                recallTime      = specs[2];
                secondsPerDigit = specs[3];
                target          = specs[4];

                settings.add(new Setting("numRows", "Number of Rows:", numRows, Integer.toString(numRows), false));
                settings.add(new Setting("numCols", "Number of Digits:", numCols, Integer.toString(numCols)));
                settings.add(new Setting("secondsPerDigit", "Digit Speed:", secondsPerDigit, DigitSpeed.getDisplayName(secondsPerDigit)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
                settings.add(new Setting("target", "Target:", target, Integer.toString(target)));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("Number_Preferences", 0);
                numRows         = prefs.getInt("SPOKEN_numRows", Constants.default_spoken_numRows);
                numCols         = prefs.getInt("SPOKEN_numCols", Constants.default_spoken_numCols);
                recallTime      = prefs.getInt("SPOKEN_recallTime", Constants.default_spoken_recallTime);
                secondsPerDigit = prefs.getFloat("SPOKEN_secondsPerDigit", Constants.default_spoken_secondsPerDigit);

                settings.add(new Setting("numRows", "Number of Rows:", numRows, Integer.toString(numRows), false));
                settings.add(new Setting("numCols", "Digits per Row:", numCols, Integer.toString(numCols), false));
                settings.add(new Setting("secondsPerDigit", "Digit Speed:", secondsPerDigit, DigitSpeed.getDisplayName(secondsPerDigit)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
            }
        }




        else if (game == Constants.LISTS_WORDS) {
            int numRows;
            int numCols;
            int memTime;
            int recallTime;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt(Constants.getGameName(game), 1);
                int[] specs = Constants.getSpecs_STEPS_RandomWords(step);

                numRows         = specs[0];
                numCols         = specs[1];
                memTime         = specs[2];
                recallTime      = specs[3];
                target          = specs[4];

                settings.add(new Setting("numRows", "Words per Column:", numRows, Integer.toString(numRows)));
                settings.add(new Setting("numCols", "Number of Columns:", numCols, Integer.toString(numCols)));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
                settings.add(new Setting("target", "Target:", target, Integer.toString(target)));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("List_Preferences", 0);
                numRows         = prefs.getInt("WORDS_numRows", Constants.default_words_numRows);
                numCols         = prefs.getInt("WORDS_numCols", Constants.default_words_numCols);
                memTime         = prefs.getInt("WORDS_memTime", Constants.default_words_memTime);
                recallTime      = prefs.getInt("WORDS_recallTime", Constants.default_words_recallTime);

                settings.add(new Setting("numRows", "Words per Column:", numRows, Integer.toString(numRows)));
                settings.add(new Setting("numCols", "Number of Columns:", numCols, Integer.toString(numCols)));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
            }
        }



        else if (game == Constants.LISTS_EVENTS) {
            int numRows;
            int numCols;
            int memTime;
            int recallTime;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt(Constants.getGameName(game), 1);
                int[] specs = Constants.getSpecs_STEPS_HistoricDates(step);

                numRows         = specs[0];
                numCols         = specs[1];
                memTime         = specs[2];
                recallTime      = specs[3];
                target          = specs[4];

                settings.add(new Setting("numRows", "Number of dates:", numRows, Integer.toString(numRows)));
                settings.add(new Setting("numCols", "Number of Columns:", numCols, Integer.toString(numCols), false));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
                settings.add(new Setting("target", "Target:", target, Integer.toString(target)));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("List_Preferences", 0);
                numRows         = prefs.getInt("WORDS_numRows", Constants.default_words_numRows);
                numCols         = prefs.getInt("WORDS_numCols", Constants.default_words_numCols);
                memTime         = prefs.getInt("WORDS_memTime", Constants.default_words_memTime);
                recallTime      = prefs.getInt("WORDS_recallTime", Constants.default_words_recallTime);

                settings.add(new Setting("numRows", "Number of dates:", numRows, Integer.toString(numRows)));
                settings.add(new Setting("numCols", "Number of Columns:", numCols, Integer.toString(numCols), false));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
            }
        }




        else if (game == Constants.SHAPES_FACES) {
            int numRows;
            int numCols;
            int memTime;
            int recallTime;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt(Constants.getGameName(game), 1);
                int[] specs = Constants.getSpecs_STEPS_NameAndFaces(step);

                numRows         = specs[0];
                numCols         = specs[1];
                memTime         = specs[2];
                recallTime      = specs[3];
                target          = specs[4];

                settings.add(new Setting("numRows", "Number of faces:", numRows, Integer.toString(numRows * numCols)));
                settings.add(new Setting("numCols", "Number of Columns:", numCols, Integer.toString(numCols), false));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
                settings.add(new Setting("target", "Target:", target, Integer.toString(target)));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("Shape_Preferences", 0);
                numRows         = prefs.getInt("FACES_numRows", Constants.default_faces_numRows);
                numCols         = prefs.getInt("FACES_numCols", Constants.default_faces_numCols);
                memTime         = prefs.getInt("FACES_memTime", Constants.default_faces_memTime);
                recallTime      = prefs.getInt("FACES_recallTime", Constants.default_faces_recallTime);

                settings.add(new Setting("numRows", "Number of faces:", numRows, Integer.toString(numRows * numCols)));
                settings.add(new Setting("numCols", "Number of Columns:", numCols, Integer.toString(numCols), false));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
            }
        }


        else if (game == Constants.SHAPES_ABSTRACT) {
            int numRows;
            int numCols;
            int memTime;
            int recallTime;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt(Constants.getGameName(game), 1);
                int[] specs = Constants.getSpecs_STEPS_AbstractImages(step);

                numRows         = specs[0];
                numCols         = specs[1];
                memTime         = specs[2];
                recallTime      = specs[3];
                target          = specs[4];

                settings.add(new Setting("numRows", "Number of images:", numRows, Integer.toString(numRows * numCols)));
                settings.add(new Setting("numCols", "Number of Columns:", numCols, Integer.toString(numCols), false));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
                settings.add(new Setting("target", "Target:", target, Integer.toString(target)));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("Shape_Preferences", 0);
                numRows         = prefs.getInt("ABSTRACT_numRows", Constants.default_abstract_numRows);
                numCols         = prefs.getInt("ABSTRACT_numCols", Constants.default_abstract_numCols);
                memTime         = prefs.getInt("ABSTRACT_memTime", Constants.default_abstract_memTime);
                recallTime      = prefs.getInt("ABSTRACT_recallTime", Constants.default_abstract_recallTime);

                settings.add(new Setting("numRows", "Number of images:", numRows, Integer.toString(numRows * numCols)));
                settings.add(new Setting("numCols", "Number of Columns:", numCols, Integer.toString(numCols), false));
                settings.add(new Setting("memTime", "Memorization Time:", memTime, Utils.formatIntoHHMMSStruncated(memTime)));
                settings.add(new Setting("recallTime", "Recall Time:", recallTime, Utils.formatIntoHHMMSStruncated(recallTime)));
            }
        }

        return settings;
    }
}
