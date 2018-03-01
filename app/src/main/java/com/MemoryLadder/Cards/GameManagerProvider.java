package com.MemoryLadder.Cards;

import android.content.Intent;

import static com.MemoryLadder.Constants.CARDS_LONG;
import static com.MemoryLadder.Constants.LISTS_EVENTS;
import static com.MemoryLadder.Constants.LISTS_WORDS;
import static com.MemoryLadder.Constants.NUMBERS_BINARY;
import static com.MemoryLadder.Constants.NUMBERS_SPEED;
import static com.MemoryLadder.Constants.NUMBERS_SPOKEN;
import static com.MemoryLadder.Constants.SHAPES_ABSTRACT;
import static com.MemoryLadder.Constants.SHAPES_FACES;

class GameManagerProvider {

    static GameManager getGameManager(int gameType, Intent intent) {
        switch (gameType) {
            case NUMBERS_SPEED: return null;
            case NUMBERS_BINARY: return null;
            case NUMBERS_SPOKEN: return null;
            case LISTS_WORDS: return null;
            case LISTS_EVENTS: return null;
            case SHAPES_FACES: return null;
            case SHAPES_ABSTRACT: return null;
            case CARDS_LONG: return CardGameManager.newInstance(SettingsProvider.getCardSettings(intent));
            default: return null;
        }
    }
}
