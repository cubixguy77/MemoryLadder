package com.memoryladder.taketest.images.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mastersofmemory.memoryladder.R;
import com.mastersofmemory.memoryladder.databinding.ImagesFragmentBinding;
import com.memoryladder.taketest.GameManager;
import com.memoryladder.taketest.GamePhase;
import com.memoryladder.taketest.images.memorysheetproviders.MemorySheetProvider;
import com.memoryladder.taketest.images.settings.ImagesSettings;
import com.memoryladder.taketest.images.ui.adapters.ImagesAdapter;
import com.memoryladder.taketest.images.ui.adapters.OnStartDragListener;
import com.memoryladder.taketest.images.ui.adapters.SimpleItemTouchHelperCallback;
import com.memoryladder.taketest.images.models.TestSheet;
import com.memoryladder.taketest.images.ui.viewmodel.ImagesViewModel;
import com.memoryladder.taketest.images.ui.viewmodel.ImagesViewModelFactory;
import com.memoryladder.taketest.scorepanel.Score;
import com.memoryladder.taketest.timer.TimerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagesGameManager extends Fragment implements GameManager, OnStartDragListener {

    @BindView(R.id.grid_images) RecyclerView grid;
    @BindView(R.id.text_timer) TimerView timerView;

    private ImagesViewModel viewModel;
    private ImagesAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private GridLayoutManager layoutManager;

    ImagesSettings settings;

    public static ImagesGameManager newInstance(ImagesSettings settings) {
        ImagesGameManager imagesGameManager = new ImagesGameManager();

        Bundle args = new Bundle();
        args.putParcelable("settings", settings);
        imagesGameManager.setArguments(args);

        return imagesGameManager;
    }

    public ImagesGameManager() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ImagesFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.viewgroup_images_arena, container, false);
        View root = binding.getRoot();
        ButterKnife.bind(this, root);

        if (getArguments() != null) {
            // Data
            settings = getArguments().getParcelable("settings");

            // View Model
            ImagesViewModelFactory factory = new ImagesViewModelFactory(settings);
            viewModel = ViewModelProviders.of(this, factory).get(ImagesViewModel.class);
            binding.setViewModel(viewModel);
            binding.setLifecycleOwner(this);

            // Grid Layout Manager
            layoutManager = new GridLayoutManager(getContext(), 26);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position % 6 == 0)
                        return 1;
                    return 5;
                }
            });
            grid.setLayoutManager(layoutManager);

            // Observe
            viewModel.getTimerVisible().observe(this, visible -> timerView.setVisibility(visible != null && visible ? View.VISIBLE : View.INVISIBLE));
            viewModel.getTestSheet().observe(this, newTestSheet -> {
                grid.setAdapter(adapter = new ImagesAdapter(newTestSheet, this, layoutManager));
                ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
                mItemTouchHelper = new ItemTouchHelper(callback);
                mItemTouchHelper.attachToRecyclerView(grid);
            });
            viewModel.getGamePhase().observe(this, newGamePhase -> { if (adapter != null) adapter.setGamePhase(newGamePhase);});
        }

        return root;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    private TestSheet getTestSheet(ImagesSettings settings) {
        return MemorySheetProvider.getTestSheet(settings.getRowCount(), getImages());
    }

    private List<Integer> getImages() {
        Resources res = getResources();
        String packageName = Objects.requireNonNull(getActivity()).getPackageName();

        int start = settings.isFullImageDataSet() ? 200 : 0;
        int end = settings.isFullImageDataSet() ? 1087 : 199;

        List<Integer> images = new ArrayList<>(end + 1 - start);
        for (int i=start; i<end; i++)
            images.add(res.getIdentifier("image"+i, "drawable", packageName));

        return images;
    }

    @Override
    public void setGamePhase(GamePhase phase) {
        if (phase == GamePhase.PRE_MEMORIZATION) {
            viewModel.resetTestSheets(getTestSheet(settings));
        }
        else if (phase == GamePhase.RECALL) {
            viewModel.sortImagesForRecall();
        }
        else if (phase == GamePhase.REVIEW) {
            viewModel.sortUnattemptedRowsForReview();
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
