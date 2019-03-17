package com.memoryladder.taketest.dates.models;

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

    public void shuffleDates() {
        Collections.shuffle(dates);
    }

    public List<HistoricDate> getDates() {
        return this.dates;
    }

    public int getDateCount() {
        return dates.size();
    }
}
