package com.memoryladder.taketest.dates.models;

import com.memoryladder.taketest.dates.ui.adapters.ReviewCellOutcome;

public class HistoricDate {
    private String memoryDate;
    private String recallDate;
    private String eventDetails;

    private ReviewCellOutcome result;

    public HistoricDate(String memoryDate, String eventDetails) {
        this.memoryDate = memoryDate;
        this.recallDate = "";
        this.eventDetails = eventDetails;
    }

    public ReviewCellOutcome getResult() {
        if (result == null) {
            result = !isAttempted() ? ReviewCellOutcome.BLANK : memoryDate.equals(recallDate) ? ReviewCellOutcome.CORRECT : ReviewCellOutcome.WRONG;
        }

        return result;
    }

    private boolean isAttempted() {
        return recallDate != null && recallDate.length() > 0;
    }

    public String getMemoryDate() {
        return memoryDate;
    }

    public String getRecallDate() {
        return recallDate;
    }

    public void setRecallDate(String recallDate) {
        this.recallDate = recallDate;
    }

    public String getEventDetails() {
        return eventDetails;
    }
}
