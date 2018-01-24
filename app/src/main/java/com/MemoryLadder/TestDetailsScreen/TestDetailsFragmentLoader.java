package com.MemoryLadder.TestDetailsScreen;

import android.content.Context;

import com.MemoryLadder.Constants;

import java.util.ArrayList;

class TestDetailsFragmentLoader {

    static TestDetailsFragment getTestDetailsFragment(Context context, int game, int gameMode, boolean locked) {
        String title = gameMode == Constants.STEPS ? "LEVEL " + SettingLoader.getCurrentLevel(context, game) : "CUSTOM";
        ArrayList<Setting> settings = SettingLoader.getSettings(context, game, gameMode);

        return TestDetailsFragment.newInstance(title, settings, game, gameMode, locked);
    }
}
