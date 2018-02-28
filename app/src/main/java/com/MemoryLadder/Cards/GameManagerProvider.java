package com.MemoryLadder.Cards;

import android.content.Intent;
import android.support.v4.app.Fragment;

class GameManagerProvider {

    static GameManager getGameManager(int gameType, Intent intent) {
        return GameManager.newInstance(SettingsProvider.getCardSettings(intent));
    }
}
