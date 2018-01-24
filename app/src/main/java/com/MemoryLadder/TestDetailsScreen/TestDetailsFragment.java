package com.MemoryLadder.TestDetailsScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MemoryLadder.Cards.CardPrototype;
import com.MemoryLadder.Cards_Settings;
import com.MemoryLadder.Constants;
import com.mastersofmemory.memoryladder.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestDetailsFragment extends Fragment {

    private ArrayList<Setting> settings;
    private TestDetailsCard card;
    private int gameType;
    private int mode;
    private boolean locked;

    public static TestDetailsFragment newInstance(String title, ArrayList<Setting> settings, int gameType, int mode, boolean locked) {
        TestDetailsFragment frag = new TestDetailsFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelableArrayList("settings", settings);
        args.putInt("gameType", gameType);
        args.putInt("mode", mode);
        args.putBoolean("locked", locked);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_details_card, container, false);
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        String title = args.getString("title");
        settings = args.getParcelableArrayList("settings");
        gameType = getArguments().getInt("gameType");
        mode = getArguments().getInt("mode");
        locked = getArguments().getBoolean("locked");

        this.card = view.findViewById(R.id.testDetailsCard);
        card.setTitle(title);
        card.setSettings(settings);
        card.setEditableSettings(mode == Constants.CUSTOM);
        card.setLocked(locked);

        return view;
    }

    @OnClick(R.id.testDetailsPlayButton) public void onPlayClick() {
        Intent i = new Intent();
        i.setClass(getActivity(), CardPrototype.class);

        for (Setting setting : settings) {
            i.putExtra(setting.key, setting.value);
        }

        i.putExtra("gameType", gameType);
        i.putExtra("mode", mode);
        if (mode == Constants.STEPS) {
            i.putExtra("step", SettingLoader.getCurrentLevel(getActivity(), gameType));
        }

        startActivity(i);
    }

    @OnClick(R.id.testDetailsUnlockButton) public void onUnlockClick() {

    }

    @OnClick(R.id.testDetailsEditSettings) public void onEditSettings() {
        Intent i = new Intent();
        i.setClass(getActivity(), Cards_Settings.class);
        startActivity(i);
        getActivity().finish();
    }
}