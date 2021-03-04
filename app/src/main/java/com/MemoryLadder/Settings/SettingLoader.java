package com.memoryladder.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.memoryladder.Constants;

import java.util.ArrayList;
import java.util.Locale;

public class SettingLoader {

    public static int getCurrentLevel(Context context, int gameType) {
        SharedPreferences settings = context.getSharedPreferences("Steps", 0);
        return settings.getInt(Constants.getGameName(gameType), 1);
    }

    public static ArrayList<Setting> getSettings(Context context, int game, int gameMode) {
        ArrayList<Setting> settings = new ArrayList<>();

        if (game == Constants.CARDS_LONG) {
            int numDecks;
            int deckSize;
            int cardsPerGroup;
            int memTime;
            int recallTime;
            int mnemo_enabled;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt(Constants.getGameName(game), 1);
                int[] specs = Constants.getSpecs_STEPS_Cards(step);

                deckSize     = specs[0];
                numDecks     = specs[1];
                memTime      = specs[2];
                recallTime	 = specs[3];
                target       = specs[4];
                cardsPerGroup = 2;

                settings.add(new NumberSetting("deckSize",  "Number of cards:", deckSize));
                settings.add(new NumberSetting("numDecks", "numDecks", "Number of decks:", numDecks, 1, 1, false));
                settings.add(new NumberSetting("numCardsPerGroup", "numCardsPerGroup", "Cards per group:", cardsPerGroup, 2, 2, false));
                settings.add(new SwitchSetting("mnemo_enabled", "Mnemo Hint:", 1));
                settings.add(new TimeSetting("memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "Recall Time:", recallTime));
                settings.add(new TargetSetting("target", "Target:", target));
            }

            else {
                SharedPreferences prefs = context.getSharedPreferences("Card_Preferences", 0);
                deckSize         = prefs.getInt("deckSize",   Constants.default_cards_deckSize);
                numDecks         = prefs.getInt("numDecks",   Constants.default_cards_numDecks);
                cardsPerGroup    = prefs.getInt("numCardsPerGroup", Constants.default_cards_cardsPerGroup);
                memTime          = prefs.getInt("memTime",    Constants.default_cards_memTime);
                recallTime       = prefs.getInt("recallTime", Constants.default_cards_recallTime);
                try { mnemo_enabled = prefs.getInt("mnemo_enabled", 0); }
                catch (ClassCastException e) { mnemo_enabled = prefs.getBoolean("mnemo_enabled", false) ? 1 : 0; }

                settings.add(new NumberSetting("deckSize",  "deckSize", "Cards per deck:", deckSize, 1, 52, true));
                settings.add(new NumberSetting("numDecks", "numDecks", "Number of decks:", numDecks, 1, 50, true));
                settings.add(new NumberSetting("numCardsPerGroup", "numCardsPerGroup", "Cards per group:", cardsPerGroup, 1, 3, true));
                settings.add(new SwitchSetting("mnemo_enabled", "mnemo_enabled", "Mnemo Hint:", mnemo_enabled, true));
                settings.add(new TimeSetting("memTime", "memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "recallTime", "Recall Time:", recallTime));
            }
        }






        else if (game == Constants.NUMBERS_SPEED) {
            int numRows;
            int numCols;
            int digitsPerGroup;
            int memTime;
            int recallTime;
            int mnemo_enabled;
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

                settings.add(new NumberSetting("numRows", "Number of Rows:", numRows));
                settings.add(new NumberSetting("numCols", "Digits per Row:", numCols));
                settings.add(new NumberSetting("base", "WRITTEN_base", "Base:", 10, 10, 10, false));
                settings.add(new TimeSetting("memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "Recall Time:", recallTime));
                settings.add(new TargetSetting("target", "Target:", target));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("Number_Preferences", 0);
                numRows         = prefs.getInt("WRITTEN_numRows", Constants.default_written_numRows);
                numCols         = prefs.getInt("WRITTEN_numCols", Constants.default_written_numCols);
                digitsPerGroup  = prefs.getInt("WRITTEN_digitsPerGroup", Constants.default_written_digitsPerGroup);
                memTime         = prefs.getInt("WRITTEN_memTime", Constants.default_written_memTime);
                recallTime      = prefs.getInt("WRITTEN_recallTime", Constants.default_written_recallTime);
                try { mnemo_enabled = prefs.getInt("WRITTEN_mnemo", 0); }
                catch (ClassCastException e) { mnemo_enabled = prefs.getBoolean("WRITTEN_mnemo", false) ? 1 : 0; }

                settings.add(new NumberSetting("numRows", "WRITTEN_numRows", "Number of Rows:", numRows, 1, 200, true));
                settings.add(new NumberSetting("numCols", "WRITTEN_numCols", "Digits per Row:", numCols, 1, 40, true));
                settings.add(new NumberSetting("digitsPerGroup", "WRITTEN_digitsPerGroup", "Digits per Group:", digitsPerGroup, 1, 6, true));
                settings.add(new NumberSetting("base", "WRITTEN_base", "Base:", 10, 10, 10, false));
                settings.add(new SwitchSetting("mnemo_enabled", "WRITTEN_mnemo", "Mnemo Hint:", mnemo_enabled, true));
                settings.add(new TimeSetting("memTime", "WRITTEN_memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "WRITTEN_recallTime", "Recall Time:", recallTime));
            }
        }



        else if (game == Constants.NUMBERS_BINARY) {
            int numRows;
            int numCols;
            int digitsPerGroup;
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

                settings.add(new NumberSetting("numRows", "Number of Rows:", numRows));
                settings.add(new NumberSetting("numCols", "Digits per Row:", numCols));
                settings.add(new NumberSetting("base", "BINARY_base", "Base:", 2, 2, 2, false));
                settings.add(new TimeSetting("memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "Recall Time:", recallTime));
                settings.add(new TargetSetting("target", "Target:", target));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("Number_Preferences", 0);
                numRows         = prefs.getInt("BINARY_numRows", Constants.default_binary_numRows);
                numCols         = prefs.getInt("BINARY_numCols", Constants.default_binary_numCols);
                digitsPerGroup  = prefs.getInt("BINARY_digitsPerGroup", Constants.default_binary_digitsPerGroup);
                memTime         = prefs.getInt("BINARY_memTime", Constants.default_binary_memTime);
                recallTime      = prefs.getInt("BINARY_recallTime", Constants.default_binary_recallTime);

                settings.add(new NumberSetting("numRows", "BINARY_numRows", "Number of Rows:", numRows, 1, 200, true));
                settings.add(new NumberSetting("numCols", "BINARY_numCols", "Digits per Row:", numCols, 1, 40, true));
                settings.add(new NumberSetting("digitsPerGroup", "BINARY_digitsPerGroup", "Digits per Group:", digitsPerGroup, 1, 6, true));
                settings.add(new NumberSetting("base", "BINARY_base", "Base:", 2, 2, 2, false));
                settings.add(new TimeSetting("memTime", "BINARY_memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "BINARY_recallTime", "Recall Time:", recallTime));
            }
        }




        else if (game == Constants.NUMBERS_SPOKEN) {
            int numRows;
            int numCols;
            int digitSpeed;
            int recallTime;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt("NUMBERS_SPOKEN", 1);
                int[] specs = Constants.getSpecs_STEPS_Spoken(16);

                numRows         = specs[0];
                numCols         = specs[1];
                recallTime      = specs[2];
                digitSpeed      = specs[3];
                target          = specs[4];

                settings.add(new NumberSetting("numRows", "", "Number of Rows (Hidden):", numRows, 1, 1, false));
                settings.add(new NumberSetting("numCols", "", "Number of Columns (Hidden):", numCols, 1, 1, false));
                settings.add(new NumberSetting("numDigits", "Number of Digits:", numRows * numCols));
                settings.add(new DigitSpeedSetting("digitSpeed", "Digit Speed:", digitSpeed));
                settings.add(new LanguageSetting("language", "Language:", Locale.UK));
                settings.add(new TimeSetting("recallTime", "Recall Time:", recallTime));
                settings.add(new TargetSetting("target", "Target:", target));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("Number_Preferences", 0);
                numRows         = prefs.getInt("SPOKEN_numRows", Constants.default_spoken_numRows);
                numCols         = prefs.getInt("SPOKEN_numCols", Constants.default_spoken_numCols);
                recallTime      = prefs.getInt("SPOKEN_recallTime", Constants.default_spoken_recallTime);
                digitSpeed      = prefs.getInt("SPOKEN_digitSpeed", Constants.default_spoken_digitSpeed);
	            String language = prefs.getString("SPOKEN_language", "en");
	            String country  = prefs.getString("SPOKEN_country", "uk");
	            String variant  = prefs.getString("SPOKEN_variant", "");

                settings.add(new NumberSetting("numRows", "SPOKEN_numRows", "Number of Rows:", numRows, 1, 200, true));
                settings.add(new NumberSetting("numCols", "SPOKEN_numCols", "Digits per Row:", numCols, 1, 40, true));
                settings.add(new DigitSpeedSetting("digitSpeed", "SPOKEN_digitSpeed", "Digit Speed:", digitSpeed, true));
                settings.add(new LanguageSetting("language", "SPOKEN_", "Language:", new Locale(language, country, variant), true));
                settings.add(new TimeSetting("recallTime", "SPOKEN_recallTime", "Recall Time:", recallTime));
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

                settings.add(new NumberSetting("numRows", "Words per Column:", numRows));
                settings.add(new NumberSetting("numCols", "Number of Columns:", numCols));
                settings.add(new SwitchSetting("fullWordList", "Full Word List", 0));
                settings.add(new TimeSetting("memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "Recall Time:", recallTime));
                settings.add(new TargetSetting("target", "Target:", target));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("List_Preferences", 0);
                numRows         = prefs.getInt("WORDS_numRows", Constants.default_words_numRows);
                numCols         = prefs.getInt("WORDS_numCols", Constants.default_words_numCols);
                memTime         = prefs.getInt("WORDS_memTime", Constants.default_words_memTime);
                recallTime      = prefs.getInt("WORDS_recallTime", Constants.default_words_recallTime);

                settings.add(new NumberSetting("numRows", "WORDS_numRows", "Words per Column:", numRows, 1, 20, true));
                settings.add(new NumberSetting("numCols", "WORDS_numCols", "Number of Columns:", numCols, 1, 40, true));
                settings.add(new TimeSetting("memTime", "WORDS_memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "WORDS_recallTime", "Recall Time:", recallTime));
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

                settings.add(new NumberSetting("numRows", "Number of dates:", numRows));
                settings.add(new NumberSetting("numCols", "", "Number of Columns:", numCols, 1, 1, false));
                settings.add(new SwitchSetting("fullDateList", "Full Event List", 0));
                settings.add(new TimeSetting("memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "Recall Time:", recallTime));
                settings.add(new TargetSetting("target", "Target:", target));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("List_Preferences", 0);
                numRows         = prefs.getInt("DATES_numRows", Constants.default_dates_numRows);
                numCols         = prefs.getInt("DATES_numCols", Constants.default_dates_numCols);
                memTime         = prefs.getInt("DATES_memTime", Constants.default_dates_memTime);
                recallTime      = prefs.getInt("DATES_recallTime", Constants.default_dates_recallTime);

                settings.add(new NumberSetting("numRows", "DATES_numRows", "Number of dates:", numRows, 1, 200, true));
                settings.add(new NumberSetting("numCols", "DATES_numCols", "Number of Columns:", numCols, 1, 1, false));
                settings.add(new TimeSetting("memTime", "DATES_memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "DATES_recallTime", "Recall Time:", recallTime));
            }
        }




        else if (game == Constants.SHAPES_FACES) {
            int numFaces;
            int numCols;
            int memTime;
            int recallTime;
            int target;

            if (gameMode == Constants.STEPS) {
                SharedPreferences prefs = context.getSharedPreferences("Steps", 0);
                int step     = prefs.getInt(Constants.getGameName(game), 1);
                int[] specs = Constants.getSpecs_STEPS_NameAndFaces(step);

                numFaces        = specs[0];
                memTime         = specs[1];
                recallTime      = specs[2];
                target          = specs[3];

                settings.add(new NumberSetting("numFaces", "Number of faces:", numFaces));
                settings.add(new SwitchSetting("fullFaceDataSet", "Full HD Face Library", 0));
                settings.add(new TimeSetting("memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "Recall Time:", recallTime));
                settings.add(new TargetSetting("target", "Target:", target));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("Shape_Preferences", 0);
                numFaces        = prefs.getInt("FACES_numFaces", Constants.default_faces_numRows);
                memTime         = prefs.getInt("FACES_memTime", Constants.default_faces_memTime);
                recallTime      = prefs.getInt("FACES_recallTime", Constants.default_faces_recallTime);

                settings.add(new NumberSetting("numFaces", "FACES_numRows", "Number of faces:", numFaces, 1, 40, true));
                settings.add(new TimeSetting("memTime", "FACES_memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "FACES_recallTime", "Recall Time:", recallTime));
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

                settings.add(new NumberSetting("numRows", "Number of rows:", numRows));
                settings.add(new NumberSetting("numCols", "", "Number of Columns:", numCols, 5, 5, false));
                settings.add(new SwitchSetting("fullImageDataSet", "Full HD Image Library", 0));
                settings.add(new TimeSetting("memTime", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "Recall Time:", recallTime));
                settings.add(new TargetSetting("target", "Target:", target));
            }
            else {
                SharedPreferences prefs = context.getSharedPreferences("Shape_Preferences", 0);
                numRows         = prefs.getInt("ABSTRACT_numRows", Constants.default_abstract_numRows);
                numCols         = prefs.getInt("ABSTRACT_numCols", Constants.default_abstract_numCols);
                memTime         = prefs.getInt("ABSTRACT_memTime", Constants.default_abstract_memTime);
                recallTime      = prefs.getInt("ABSTRACT_recallTime", Constants.default_abstract_recallTime);

                settings.add(new NumberSetting("numRows", "", "Number of rows:", numRows, 1, 100, true));
                settings.add(new NumberSetting("numCols", "", "Number of Columns:", numCols, 5, 5, false));
                settings.add(new TimeSetting("memTime", "", "Memorization Time:", memTime));
                settings.add(new TimeSetting("recallTime", "", "Recall Time:", recallTime));
            }
        }

        return settings;
    }
}
