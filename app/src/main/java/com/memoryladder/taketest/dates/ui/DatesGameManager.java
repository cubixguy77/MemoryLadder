package com.memoryladder.taketest.dates.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.DatesFragmentBinding;
import com.memoryladder.taketest.GameManager;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.dates.memorysheetproviders.TestSheetProvider;
import com.memoryladder.taketest.dates.score.DatesScoreProvider;
import com.memoryladder.taketest.dates.settings.DatesSettings;
import com.memoryladder.taketest.dates.ui.adapters.DatesAdapter;
import com.memoryladder.taketest.dates.ui.viewmodel.DatesViewModel;
import com.memoryladder.taketest.dates.ui.viewmodel.DatesViewModelFactory;
import com.memoryladder.taketest.scorepanel.Score;
import com.memoryladder.taketest.timer.TimerView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DatesGameManager extends Fragment implements GameManager {

    @BindView(R.id.date_list) RecyclerView dateList;
    @BindView(R.id.text_timer) TimerView timerView;

    private DatesViewModel viewModel;
    private DatesAdapter adapter;
    private DatesSettings settings;

    public static DatesGameManager newInstance(DatesSettings settings) {
        DatesGameManager cardGameManager = new DatesGameManager();

        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        cardGameManager.setArguments(args);

        return cardGameManager;
    }

    public DatesGameManager() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DatesFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.viewgroup_dates_arena, container, false);
        View root = binding.getRoot();
        ButterKnife.bind(this, root);

        if (getArguments() != null) {
            // Data
            settings = getArguments().getParcelable("settings");
            if (settings == null) {
                settings = new DatesSettings(10, false);
            }

            // View Model
            DatesViewModelFactory factory = new DatesViewModelFactory(TestSheetProvider.getTestSheet(getDateList(settings.isFullDateList()), settings.getDateCount()), settings, new DatesScoreProvider());
            viewModel = ViewModelProviders.of(this, factory).get(DatesViewModel.class);
            binding.setViewModel(viewModel);
            binding.setLifecycleOwner(this);

            // Grid Layout Manager
            dateList.setLayoutManager(new LinearLayoutManager(getContext()));
            dateList.addItemDecoration(new DividerItemDecoration(dateList.getContext(), DividerItemDecoration.VERTICAL));

            // Observe
            viewModel.getTimerVisible().observe(this, visible -> timerView.setVisibility(visible != null && visible ? View.VISIBLE : View.INVISIBLE));
            viewModel.getGamePhase().observe(this, newGamePhase -> { if (adapter != null) adapter.setGamePhase(newGamePhase);});
            viewModel.getTestSheet().observe(this, newTestSheet -> dateList.setAdapter(adapter = new DatesAdapter(newTestSheet)));
        }

        return root;
    }

    private List<String> getDateList(boolean useFullDateList) {
        return Arrays.asList(useFullDateList ? getResources().getStringArray(R.array.historical_dates_custom) : getResources().getStringArray(R.array.historical_dates_steps));
    }

    @Override
    public void setGamePhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            viewModel.resetTestSheets(TestSheetProvider.getTestSheet(getDateList(settings.isFullDateList()), settings.getDateCount()));
        }
        else if (phase == GamePhase.RECALL) {
            viewModel.shuffleDates();
        }

        viewModel.setGamePhase(phase);
    }

    @Override
    public void displayTime(int secondsRemaining) {
        timerView.displayTime(secondsRemaining);
    }

    @Override
    public void render(GamePhase phase) {
        switch (phase) {
            case PRE_MEMORIZATION:
                break;
            case MEMORIZATION:
                break;
            case RECALL:
                break;
            case REVIEW:
                break;
        }
    }

    @Override
    public Score getScore() {
        return viewModel.getScore();
    }
}
