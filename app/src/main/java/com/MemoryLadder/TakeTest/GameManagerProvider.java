package com.memoryladder.taketest;

import android.content.Context;
import android.content.Intent;

import com.memoryladder.taketest.cards.CardGameManager;
import com.memoryladder.taketest.images.ui.ImagesGameManager;
import com.memoryladder.taketest.namesandfaces.ui.NamesAndFacesGameManager;
import com.memoryladder.taketest.randomwords.ui.RandomWordsGameManager;
import com.memoryladder.taketest.writtennumbers.WrittenNumbersGameManager;

import static com.memoryladder.Constants.CARDS_LONG;
import static com.memoryladder.Constants.LISTS_EVENTS;
import static com.memoryladder.Constants.LISTS_WORDS;
import static com.memoryladder.Constants.NUMBERS_BINARY;
import static com.memoryladder.Constants.NUMBERS_SPEED;
import static com.memoryladder.Constants.NUMBERS_SPOKEN;
import static com.memoryladder.Constants.SHAPES_ABSTRACT;
import static com.memoryladder.Constants.SHAPES_FACES;

class GameManagerProvider {

    static GameManager getGameManager(int gameType, Intent intent, Context context) {
        switch (gameType) {
            case NUMBERS_SPEED: return WrittenNumbersGameManager.newInstance(SettingsProvider.getWrittenNumbersSettings(intent, context));
            case NUMBERS_BINARY: return WrittenNumbersGameManager.newInstance(SettingsProvider.getWrittenNumbersSettings(intent, context));
            case NUMBERS_SPOKEN: return null;
            case LISTS_WORDS: return RandomWordsGameManager.newInstance(SettingsProvider.getRandomWordsSettings(intent));
            case LISTS_EVENTS: return null;
            case SHAPES_FACES: return NamesAndFacesGameManager.newInstance(SettingsProvider.getNamesAndFacesSettings(intent));
            case SHAPES_ABSTRACT: return ImagesGameManager.newInstance(SettingsProvider.getImagesSettings(intent));
            case CARDS_LONG: return CardGameManager.newInstance(SettingsProvider.getCardSettings(intent));
            default: return null;
        }
    }
}
