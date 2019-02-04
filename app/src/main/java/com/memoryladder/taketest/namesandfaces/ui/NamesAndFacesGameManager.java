package com.memoryladder.taketest.namesandfaces.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

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
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NamesAndFacesGameManager extends Fragment implements GameManager, ViewTreeObserver.OnGlobalLayoutListener {

    @BindView(R.id.grid_faces) RecyclerView grid;
    @BindView(R.id.text_timer) TimerView timerView;

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
        ButterKnife.bind(this, root);

        root.getViewTreeObserver().addOnGlobalLayoutListener(this);

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
            viewModel.getTimerVisible().observe(this, visible -> timerView.setVisibility(visible != null && visible ? View.VISIBLE : View.INVISIBLE));
            viewModel.getTestSheet().observe(this, newTestSheet -> grid.setAdapter(adapter = new FaceAdapter(newTestSheet)));
            viewModel.getViewPortHeight().observe(this, newViewPortHeight -> {
                if (adapter != null) {
                    adapter.adjustToViewPortHeight(newViewPortHeight);
                }
            });
            viewModel.getGamePhase().observe(this, newGamePhase -> { if (adapter != null) adapter.setGamePhase(newGamePhase);});
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Objects.requireNonNull(getView()).getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        else {
            Objects.requireNonNull(getView()).getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
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
        String packageName = Objects.requireNonNull(getActivity()).getPackageName();

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

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        View view = Objects.requireNonNull(getActivity()).getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(r);
        viewModel.setViewPortHeight(r.bottom);
    }
}