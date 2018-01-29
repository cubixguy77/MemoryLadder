package com.MemoryLadder.TestDetailsScreen;

import android.content.Context;

import com.MemoryLadder.Constants;
import com.MemoryLadder.Settings.Setting;
import com.MemoryLadder.Settings.SettingLoader;

import java.util.ArrayList;

class TestDetailsFragmentLoader {

    static TestDetailsFragment getTestDetailsFragmentLevels(Context context, int game) {
        String title = "LEVEL " + SettingLoader.getCurrentLevel(context, game);
        ArrayList<Setting> settings = SettingLoader.getSettings(context, game, Constants.STEPS);

        return TestDetailsFragment.newInstance(title, settings, game, Constants.STEPS, false, false);
    }

    static TestDetailsFragment getTestDetailsFragmentCustom(Context context, int game) {
        String title = "CUSTOM";
        ArrayList<Setting> settings = SettingLoader.getSettings(context, game, Constants.CUSTOM);

        return TestDetailsFragment.newInstance(title, settings, game, Constants.CUSTOM, true, true);
    }
}
