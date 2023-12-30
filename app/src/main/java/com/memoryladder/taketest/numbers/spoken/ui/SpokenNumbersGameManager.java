package com.memoryladder.taketest.numbers.spoken.ui;

import androidx.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.SpokenNumbersFragmentBinding;
import com.memoryladder.taketest.numbers.spoken.settings.DigitSpeed;
import com.memoryladder.taketest.GameActivity;
import com.memoryladder.taketest.GameManager;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.numbers.CellSelectListener;
import com.memoryladder.taketest.numbers.spoken.models.TestSheet;
import com.memoryladder.taketest.numbers.spoken.models.TestSheetProvider;
import com.memoryladder.taketest.numbers.spoken.settings.SpokenNumbersSettings;
import com.memoryladder.taketest.numbers.spoken.ui.adapters.SpokenNumberGridAdapter;
import com.memoryladder.taketest.numbers.spoken.ui.sound.SoundManager;
import com.memoryladder.taketest.numbers.spoken.ui.viewmodel.SpokenNumbersViewModel;
import com.memoryladder.taketest.numbers.spoken.ui.viewmodel.SpokenNumbersViewModelFactory;
import com.memoryladder.taketest.numbers.written.keyboard.KeyListener;
import com.memoryladder.taketest.numbers.written.keyboard.NumericKeyboardView;
import com.memoryladder.taketest.scorepanel.Score;
import com.memoryladder.taketest.timer.TimerView;

public class SpokenNumbersGameManager extends Fragment implements GameManager, CellSelectListener, KeyListener {

    private RecyclerView grid;
    private TimerView timerView;
    private NumericKeyboardView keyboardView;
    private ImageView nightModeIcon;
    private ImageView gridLinesIcon;

    private SpokenNumbersViewModel viewModel;
    private SpokenNumberGridAdapter adapter;
    private SoundManager soundManager;

    private SpokenNumbersSettings settings;
    private TestSheet testSheet;

    public static SpokenNumbersGameManager newInstance(SpokenNumbersSettings settings) {
        SpokenNumbersGameManager spokenNumbersGameManager = new SpokenNumbersGameManager();

        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        spokenNumbersGameManager.setArguments(args);

        return spokenNumbersGameManager;
    }

    public SpokenNumbersGameManager() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SpokenNumbersFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.viewgroup_spoken_numbers_arena, container, false);
        View root = binding.getRoot();

        grid = root.findViewById(R.id.grid_spoken_numbers);
        timerView = root.findViewById(R.id.text_timer);
        keyboardView = root.findViewById(R.id.keyboard);
        nightModeIcon = root.findViewById(R.id.buttonToggleNightMode);
        nightModeIcon.setOnClickListener(v -> onToggleNightMode());
        gridLinesIcon = root.findViewById(R.id.buttonToggleGridLines);
        gridLinesIcon.setOnClickListener(v -> onToggleDrawGridLines());

        if (getArguments() != null) {
            // Data
            settings = getArguments().getParcelable("settings");

            // View Model
            SpokenNumbersViewModelFactory factory = new SpokenNumbersViewModelFactory(settings);
            viewModel = ViewModelProviders.of(this, factory).get(SpokenNumbersViewModel.class);
            binding.setViewModel(viewModel);
            binding.setLifecycleOwner(this);

            // Sound
            initSpeaker();

            // Grid
            grid.setLayoutManager(new GridLayoutManager(getContext(), settings.getNumCols()));

            // Observe
            viewModel.getTimerVisible().observe(getViewLifecycleOwner(), visible -> timerView.setVisibility(visible != null && visible ? View.VISIBLE : View.INVISIBLE));
            viewModel.getTestSheet().observe(getViewLifecycleOwner(), newTestSheet -> {
                testSheet = newTestSheet;
                grid.setAdapter(adapter = new SpokenNumberGridAdapter(this, newTestSheet, false, true));
            });

            viewModel.getHighlight().observe(getViewLifecycleOwner(), highlight -> {
            	if (adapter != null && highlight != null)
	                adapter.setHighlight(highlight);
            });

            viewModel.getGamePhase().observe(getViewLifecycleOwner(), newGamePhase -> {
                if (adapter != null)
                    adapter.setGamePhase(newGamePhase);

                if (newGamePhase == GamePhase.PRE_MEMORIZATION) {
                    if (!isSpeakerActive()) {
                        initSpeaker();
                    }
                }
                else if (newGamePhase == GamePhase.MEMORIZATION) {
                    if (!isSpeakerActive()) {
                        initSpeaker();
                    }
                    viewModel.setHighlight(0);
                    nextSpokenDigit(0);
                }
                else if (newGamePhase == GamePhase.RECALL) {
                    shutDownSound();
                    keyboardView.show(10);
                    keyboardView.setKeyListener(this);
                }
                else if (newGamePhase == GamePhase.REVIEW) {
	                keyboardView.setVisibility(View.GONE);
	                keyboardView.setKeyListener(null);
                }
            });
        }

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        shutDownSound();
    }

    private void shutDownSound() {
        if (soundManager != null && soundManager.isSpeakerActive()) {
            soundManager.stop();
            soundManager = null;
        }
    }

    private void playSound(int digit) {
        soundManager.playSound(digit);
    }

    private void proceedToRecall() {
        if (getActivity() != null){
            ((GameActivity) getActivity()).setGamePhase(GamePhase.RECALL);
            shutDownSound();
            viewModel.setHighlight(0);
        }
    }

    private void initSpeaker() {
        soundManager = new SoundManager(requireContext(), DigitSpeed.getSpeechRate(settings.getDigitSpeed()), settings.getLocale());
    }

    private boolean isSpeakerActive() {
        return soundManager != null && soundManager.isSpeakerActive();
    }

    private void nextSpokenDigit(final int index) {
        if (!isSpeakerActive() || viewModel.getGamePhase().getValue() != GamePhase.MEMORIZATION) {
            shutDownSound();
            return;
        }

        if (index >= settings.getDigitCount())
            proceedToRecall();
        else {
            viewModel.setHighlight(index);
            playSound(Integer.parseInt(testSheet.getMemoryText(index)));
            new Handler().postDelayed(() -> nextSpokenDigit(index + 1), DigitSpeed.getMillisPerSecond(settings.getDigitSpeed()));
        }
    }

    private TestSheet getTestSheet(SpokenNumbersSettings settings) {
        return TestSheetProvider.getTestSheet(settings.getDigitCount(), false);
    }

    void onToggleNightMode() {
        settings.setNightMode(!settings.isNightMode());
        adapter.setNightMode(settings.isNightMode());

        SharedPreferences.Editor editor = requireContext().getSharedPreferences("Number_Preferences", 0).edit();
        editor.putBoolean("SPOKEN_nightMode", settings.isNightMode());
        editor.apply();

        refreshNightModeIcon();
    }

    void onToggleDrawGridLines() {
        settings.setDrawGridLines(!settings.isDrawGridLines());
        adapter.setDrawGridLines(settings.isDrawGridLines());

        SharedPreferences.Editor editor = requireContext().getSharedPreferences("Number_Preferences", 0).edit();
        editor.putBoolean("SPOKEN_drawGridLines", settings.isDrawGridLines());
        editor.apply();

        refreshGridLinesIcon();
    }

    @Override
    public void onDigit(char key) {
        if (viewModel.getHighlight().getValue() != null) {
            testSheet.registerRecall(key, viewModel.getHighlight().getValue());
            //adapter.notifyItemChanged(viewModel.getHighlight().getValue());

            if (key == TestSheet.EMPTY_CHAR)
                onBack();
            else
                onForward();
        }
    }

    @Override
    public void onBack() {
        Integer curHighlight = viewModel.getHighlight().getValue();
        if (curHighlight != null && curHighlight > 0) {
            viewModel.setHighlight(curHighlight - 1);
            adapter.notifyItemRangeChanged(curHighlight, 2);
        }
        else if (curHighlight != null && curHighlight == 0) {
            adapter.notifyItemChanged(0);
        }
    }

    @Override
    public void onForward() {
        Integer curHighlight = viewModel.getHighlight().getValue();
        int maxIndexAllowed = settings.getDigitCount() - 1;
        if (curHighlight != null && curHighlight < maxIndexAllowed) {
            viewModel.setHighlight(curHighlight + 1);
            adapter.notifyItemRangeChanged(curHighlight - 1, 2);
        }
        else if (curHighlight != null && curHighlight == maxIndexAllowed) {
            adapter.notifyItemChanged(maxIndexAllowed);
        }
    }

    @Override
    public void onBackspace() {
        onDigit(TestSheet.EMPTY_CHAR);
        //onBack();
    }

    @Override
    public void setGamePhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            viewModel.resetTestSheets(getTestSheet(settings));
        }

        viewModel.setGamePhase(phase);
    }

    @Override
    public void displayTime(int secondsRemaining) {
        timerView.displayTime(secondsRemaining);
    }

    @Override
    public void render(GamePhase phase) {}

    @Override
    public Score getScore() {
        return viewModel.getScore();
    }

    @Override
    public void onCellHighlighted(int pos) {
		viewModel.setHighlight(pos);
    }

    private void refreshNightModeIcon() {
        nightModeIcon.setImageResource(settings.isNightMode() ? R.drawable.icon_night_mode_on : R.drawable.icon_night_mode_off);
    }

    private void refreshGridLinesIcon() {
        gridLinesIcon.setImageResource(settings.isDrawGridLines() ? R.drawable.icon_gridlines_on : R.drawable.icon_gridlines_off);
    }
}
