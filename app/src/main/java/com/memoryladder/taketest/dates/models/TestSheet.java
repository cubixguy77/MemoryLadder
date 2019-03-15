package com.memoryladder.taketest.dates.models;

import com.memoryladder.taketest.dates.ui.adapters.ReviewCellOutcome;

import java.util.Collections;
import java.util.List;

public class TestSheet {

    private List<HistoricDate> dates;

    public TestSheet(List<HistoricDate> dates) {
        this.dates = dates;
    }

    public HistoricDate getDate(int position) {
        return dates.get(position);
    }

    public ReviewCellOutcome getOutcome(int position) {
        return getDate(position).getResult();
    }

    public void shuffleDates() {
        Collections.shuffle(dates);
    }

    public int getDateCount() {
        return dates.size();
    }
}
