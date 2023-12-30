package com.memoryladder.taketest.dates.ui;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
public class DatesGameManager extends Fragment implements GameManager {

    RecyclerView dateList;
    TimerView timerView;

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

        dateList = root.findViewById(R.id.date_list);
        timerView = root.findViewById(R.id.text_timer);

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
            viewModel.getTimerVisible().observe(getViewLifecycleOwner(), visible -> timerView.setVisibility(visible != null && visible ? View.VISIBLE : View.INVISIBLE));
            viewModel.getGamePhase().observe(getViewLifecycleOwner(), newGamePhase -> { if (adapter != null) adapter.setGamePhase(newGamePhase);});
            viewModel.getTestSheet().observe(getViewLifecycleOwner(), newTestSheet -> dateList.setAdapter(adapter = new DatesAdapter(newTestSheet)));
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