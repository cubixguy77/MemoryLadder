package com.memoryladder.taketest.scorepanel;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.BottomSheetBehavior;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.memoryladder.taketest.timer.TimeFormat;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.mastersofmemory.memoryladder.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScorePanel extends LinearLayout {

    @BindView(R.id.accuracyText) TextView accuracyText;
    @BindView(R.id.scoreText) TextView scoreText;
    @BindView(R.id.memTimeText) TextView memTimeText;
    @BindView(R.id.viewgroup_score_panel_graph) GraphView graph;

    BottomSheetBehavior panel;

    public ScorePanel(Context context) { super(context); }
    public ScorePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        panel = BottomSheetBehavior.from(this);
    }

    @OnClick(R.id.viewgroup_score_panel_summary_section) void onSummaryPanelClick() {
        if (panel.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            panel.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else {
            panel.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void show(Score score, float secondsElapsed, DataPoint[] pastScores) {
        accuracyText.setText(getAccuracyText(score.accuracy));
        scoreText.setText(getScoreText(score.score));
        memTimeText.setText(getMemTimeText(secondsElapsed));

        LineGraphSeries series = new LineGraphSeries<>(pastScores);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setColor(Color.GREEN);
        series.setThickness(6);
        series.setAnimated(true);

        graph.addSeries(series);
        graph.setTitle("Past Scores");
        graph.setTitleColor(Color.WHITE);
        graph.setBackgroundColor(Color.GRAY);

        // styling grid/labels
        GridLabelRenderer renderer = graph.getGridLabelRenderer();
        renderer.setGridColor(Color.YELLOW);
        renderer.setHorizontalAxisTitleColor(Color.WHITE);
        renderer.setVerticalLabelsColor(Color.WHITE);
        renderer.setHighlightZeroLines(false);
        renderer.setHorizontalLabelsVisible(false);
        renderer.setNumHorizontalLabels(pastScores.length);
        renderer.setVerticalLabelsVAlign(GridLabelRenderer.VerticalLabelsVAlign.MID);
        renderer.setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        renderer.setHorizontalAxisTitle("Time");
        renderer.reloadStyles();

        //graph.getLegendRenderer().setMargin(64); // Doesn't seem to do anything

        panel.setState(BottomSheetBehavior.STATE_COLLAPSED);
        this.setVisibility(View.VISIBLE);
    }

    private SpannableString getAccuracyText(int percentCorrect) {
        String s = percentCorrect + "%";
        SpannableString span =  new SpannableString(s);
        float shrinkSize = .5f;
        span.setSpan(new RelativeSizeSpan(shrinkSize), s.length()-1, s.length(), 0); // shrink the percentage symbol
        return span;
    }

    private String getScoreText(int score) {
        return Integer.toString(score);
    }

    private SpannableString getMemTimeText(float seconds) {
        float shrinkFactor = 0.7f;

        if (seconds < 60) {
            String s = String.format(java.util.Locale.US, "%.1f", seconds) + "s";
            SpannableString span =  new SpannableString(s);
            span.setSpan(new RelativeSizeSpan(shrinkFactor), s.length()-1, s.length(), 0); // shrink the "s"
            return span;
        }

        String s = TimeFormat.formatIntoHHMMSStruncated((int) seconds);
        SpannableString span =  new SpannableString(s);

        if (seconds >= 3600)
            span.setSpan(new RelativeSizeSpan(.6f), 0, s.length(), 0); // shrink all text to fit better
        else if (seconds >= 600)
            span.setSpan(new RelativeSizeSpan(.9f), 0, s.length(), 0); // shrink all text to fit better

        return span;
    }
}
