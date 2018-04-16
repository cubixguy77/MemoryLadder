package com.MemoryLadder.TestDetailsScreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.MemoryLadder.Billing.BillingManager;
import com.MemoryLadder.Constants;
import com.MemoryLadder.Settings.Setting;
import com.MemoryLadder.Settings.SettingLoader;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mastersofmemory.memoryladder.BuildConfig;
import com.mastersofmemory.memoryladder.R;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestDetailsFragment extends Fragment {

    BillingManager billingManager;
    private FirebaseAnalytics analytics;

    private ArrayList<Setting> settings;
    private TestDetailsCard card;
    private int gameType;
    private int mode;

    public static TestDetailsFragment newInstance(String title, ArrayList<Setting> settings, int gameType, int mode, boolean lockable, boolean editable) {
        TestDetailsFragment frag = new TestDetailsFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putParcelableArrayList("settings", settings);
        args.putInt("gameType", gameType);
        args.putInt("mode", mode);
        args.putBoolean("lockable", lockable);
        args.putBoolean("editable", editable);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_details_card, container, false);
        ButterKnife.bind(this, view);
        analytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle args = getArguments();
        String title = args.getString("title");
        settings = args.getParcelableArrayList("settings");
        gameType = getArguments().getInt("gameType");
        mode = getArguments().getInt("mode");
        boolean lockable = getArguments().getBoolean("lockable") && !BuildConfig.DEBUG;
        boolean editable = getArguments().getBoolean("editable");

        card = view.findViewById(R.id.testDetailsCard);
        card.setTitle(title);
        card.setSettings(settings, editable, Constants.getPrefsName(gameType));

        if (lockable) {
            billingManager = new BillingManager(getActivity(), Constants.getGameSku(gameType), new BillingManager.BillingUpdatesListener() {
                @Override
                public void onBillingSetupSuccess() {
                    card.setLocked(!billingManager.isPurchased());
                }

                @Override
                public void onBillingSetupFailed() {
                    card.setLocked(!isPurchased());
                }

                @Override
                public void onUnlockChallenge() {
                    card.setLocked(false);
                    savePurchase();

                }
            });
        }
        else {
            card.setLocked(false);
        }

        return view;
    }

    private void savePurchase() {
        SharedPreferences purchases = getActivity().getSharedPreferences("Purchases", 0);
        SharedPreferences.Editor editor = purchases.edit();
        editor.putBoolean(Constants.getGameSku(gameType), true);
        editor.apply();
    }

    private boolean isPurchased() {
        SharedPreferences purchases = getActivity().getSharedPreferences("Purchases", 0);
        return purchases.getBoolean(Constants.getGameSku(gameType), false);
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        if (billingManager != null) {
            billingManager.destroy();
        }
    }

    private void logChooseTestEvent() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, Constants.getGameName(gameType));
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, mode == Constants.STEPS ? "Steps" : "Custom");
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void logUnlockClickEvent() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Unlock_Click");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Constants.getGameName(gameType));
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @OnClick(R.id.testDetailsPlayButton) public void onPlayClick() {
        logChooseTestEvent();

        Intent i = new Intent();
        i.setClass(getActivity(), Constants.getClass(gameType));

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
        logUnlockClickEvent();

        if (billingManager != null) {
            billingManager.launchPurchaseDialog();
        }
    }
}