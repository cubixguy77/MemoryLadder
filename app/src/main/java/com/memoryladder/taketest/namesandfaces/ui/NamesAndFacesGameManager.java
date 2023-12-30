package com.memoryladder.taketest.namesandfaces.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.NamesAndFacesFragmentBinding;
import com.memoryladder.taketest.GameManager;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.namesandfaces.memorysheetproviders.MemorySheetProvider;
import com.memoryladder.taketest.namesandfaces.settings.NamesAndFacesSettings;
import com.memoryladder.taketest.namesandfaces.ui.adapters.FaceAdapter;
import com.memoryladder.taketest.namesandfaces.ui.adapters.TestSheet;
import com.memoryladder.taketest.namesandfaces.ui.viewmodel.NamesAndFacesViewModel;
import com.memoryladder.taketest.namesandfaces.ui.viewmodel.NamesAndFacesViewModelFactory;
import com.memoryladder.taketest.scorepanel.Score;
import com.memoryladder.taketest.timer.TimerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NamesAndFacesGameManager extends Fragment implements GameManager {

    private RecyclerView grid;
    private TimerView timerView;

    private NamesAndFacesViewModel viewModel;
    private FaceAdapter adapter;

    NamesAndFacesSettings settings;

    public static NamesAndFacesGameManager newInstance(NamesAndFacesSettings settings) {
        NamesAndFacesGameManager cardGameManager = new NamesAndFacesGameManager();

        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        cardGameManager.setArguments(args);

        return cardGameManager;
    }

    public NamesAndFacesGameManager() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NamesAndFacesFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.viewgroup_names_and_faces_arena, container, false);
        View root = binding.getRoot();

        grid = root.findViewById(R.id.grid_faces);
        timerView = root.findViewById(R.id.text_timer);

        if (getArguments() != null) {
            // Data
            settings = getArguments().getParcelable("settings");

            // View Model
            NamesAndFacesViewModelFactory factory = new NamesAndFacesViewModelFactory(settings);
            viewModel = ViewModelProviders.of(this, factory).get(NamesAndFacesViewModel.class);
            binding.setViewModel(viewModel);
            binding.setLifecycleOwner(this);

            // Grid Adapter
            grid.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.faces_Columns)));

            // Observe
            viewModel.getTimerVisible().observe(getViewLifecycleOwner(), visible -> timerView.setVisibility(visible != null && visible ? View.VISIBLE : View.INVISIBLE));
            viewModel.getTestSheet().observe(getViewLifecycleOwner(), newTestSheet -> grid.setAdapter(adapter = new FaceAdapter(newTestSheet)));
            viewModel.getGamePhase().observe(getViewLifecycleOwner(), newGamePhase -> { if (adapter != null) adapter.setGamePhase(newGamePhase);});
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private TestSheet getTestSheet(NamesAndFacesSettings settings) {
        return new TestSheet(MemorySheetProvider.getTestSheet(getFirstNames(true), getFirstNames(false), getLastNames(), settings.getFaceCount(), getImages(true, settings.isFullFaceDataSet()), getImages(false, settings.isFullFaceDataSet())));
    }

    private List<String> getFirstNames (boolean isMale) {
        return isMale ?
                Arrays.asList(getResources().getStringArray(R.array.malenames)) :
                Arrays.asList(getResources().getStringArray(R.array.femalenames));
    }

    private List<String> getLastNames() {
        return Arrays.asList(getResources().getStringArray(R.array.lastnames));
    }

    private List<Integer> getImages(boolean isMale, boolean isHighQuality) {
        Resources res = getResources();
        String packageName = requireActivity().getPackageName();

        int numImages;
        String prefix;
        if (isMale) {
            numImages = isHighQuality ? 384 : 117;
            prefix = isHighQuality ? "male" : "lq_male";
        }
        else {
            numImages = isHighQuality ? 364 : 118;
            prefix = isHighQuality ? "female" : "lq_female";
        }

        List<Integer> images = new ArrayList<>(numImages);
        for (int i=0; i<numImages; i++)
            images.add(res.getIdentifier(prefix+i, "drawable", packageName));

        return images;
    }

    @Override
    public void setGamePhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            viewModel.resetTestSheets(getTestSheet(settings));
        }
        else if (phase == GamePhase.RECALL) {
            viewModel.shuffleTestSheet();
        }

        viewModel.setGamePhase(phase);
    }

    @Override
    public void displayTime(int secondsRemaining) {
        timerView.displayTime(secondsRemaining);
    }

    @Override
    public void render(GamePhase phase) {
        //System.out.println("ML NamesAndFacesGameManager: render: " + phase.toString());
        switch (phase) {
            case PRE_MEMORIZATION:
                break;
            case MEMORIZATION:
                break;
            case RECALL:
                break;
            case REVIEW:
                break;
            default:
                break;
        }
    }

    @Override
    public Score getScore() {
        return viewModel.getScore();
    }
}
