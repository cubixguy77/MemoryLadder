package com.memoryladder.taketest.dates.memorysheetproviders;

import com.memoryladder.Utils;
import com.memoryladder.taketest.dates.models.HistoricDate;
import com.memoryladder.taketest.dates.models.TestSheet;

import java.util.ArrayList;
import java.util.List;

public class TestSheetProvider {

    public static TestSheet getTestSheet(List<String> dateBank, int numDates) {
        List<HistoricDate> dates = new ArrayList<>();
        List<String> eventDetails = getMemorySheet(dateBank, numDates);
        List<Integer> years = new ArrayList<>(1100);

        /* Dates range from 1000 to 2099 and do not repeat */
        for (int year=1000; year<=2099; year++) {
            years.add(year);
        }

        years = Utils.pickNRandomElements(years, numDates);
        if (years == null) {
            years = new ArrayList<>();
        }

        int dateNum = 0;
        for (String event : eventDetails) {
            dates.add(new HistoricDate(years.get(dateNum++).toString(), event));
        }

        return new TestSheet(dates);
    }

    private static List<String> getMemorySheet(List<String> dateBank, int numDates) {
        return Utils.pickNRandomElements(dateBank, numDates);
    }
}
