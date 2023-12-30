package com.memoryladder.testdetailsscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.memoryladder.billing.BillingUpdatesListener;
import com.memoryladder.billing.google.BillingManager;
import com.memoryladder.Constants;
import com.memoryladder.settings.LanguageSetting;
import com.memoryladder.settings.Setting;
import com.memoryladder.settings.SettingLoader;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mastersofmemory.memoryladder.BuildConfig;
import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.TestDetailsCardBinding;
import com.memoryladder.taketest.GameActivity;

import java.util.ArrayList;

public class TestDetailsFragment extends Fragment {

    private BillingManager billingManager;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TestDetailsCardBinding binding = TestDetailsCardBinding.inflate(inflater, container, false);

        card = binding.getRoot();

        // View view = inflater.inflate(R.layout.test_details_card, container, false);
        card.findViewById(R.id.testDetailsPlayButton).setOnClickListener(v -> onPlayClick());
        card.findViewById(R.id.testDetailsUnlockButton).setOnClickListener(v -> onUnlockClick());

        analytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle args = getArguments();
        String title = args.getString("title");
        settings = args.getParcelableArrayList("settings");
        gameType = getArguments().getInt("gameType");
        mode = getArguments().getInt("mode");
        boolean lockable = getArguments().getBoolean("lockable") && !BuildConfig.DEBUG;
        boolean editable = getArguments().getBoolean("editable");

        card.setTitle(title);
        card.setSettings(settings, editable, Constants.getPrefsName(gameType));

        if (lockable) {
            billingManager = new BillingManager(getActivity(), Constants.getGameSku(gameType), new BillingUpdatesListener() {
                @Override
                public void onBillingSetupSuccess() {
                    billingManager.isPurchased(isPurchased -> card.setLocked(!isPurchased));
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

        return card;
    }

    private void savePurchase() {
        if (getContext() == null)
            return;

        SharedPreferences purchases = getContext().getSharedPreferences("Purchases", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = purchases.edit();
        editor.putBoolean(Constants.getGameSku(gameType), true);
        editor.apply();
    }

    private boolean isPurchased() {
        if (getContext() == null)
            return false;

        SharedPreferences purchases = getContext().getSharedPreferences("Purchases", Context.MODE_PRIVATE);
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

    private void onPlayClick() {
        logChooseTestEvent();

        Intent i = new Intent();
        i.setClass(getActivity(), GameActivity.class);

        for (Setting setting : settings) {
            i.putExtra(setting.key, setting.value);

            if (setting instanceof LanguageSetting) {
                i.putExtra(setting.key, ((LanguageSetting) setting).getLocale());
            }
        }

        i.putExtra("gameType", gameType);
        i.putExtra("mode", mode);
        if (mode == Constants.STEPS) {
            i.putExtra("step", SettingLoader.getCurrentLevel(getActivity(), gameType));
        }

        startActivity(i);
    }

    private void onUnlockClick() {
        logUnlockClickEvent();

        if (billingManager != null) {
            billingManager.launchPurchaseDialog();
        }
    }
}