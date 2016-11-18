package com.willowtreeapps.skrej.calendarApi;

import android.text.format.DateUtils;

import com.google.api.services.calendar.model.Event;
import com.willowtreeapps.skrej.model.RoomAvailabilityStatus;

import java.util.List;


/**
 * Created by chrisestes on 11/14/16.
 */

public class CalendarWizard {

    private final String TAG = CalendarWizard.class.getSimpleName();

    public RoomAvailabilityStatus parseFirstEvent(List<Event> events) {
        long currentTime = System.currentTimeMillis();
        if (events.size() == 0) {
            long oneHour = currentTime + DateUtils.HOUR_IN_MILLIS * 12;
            long twoHours = oneHour + DateUtils.HOUR_IN_MILLIS;
            return new RoomAvailabilityStatus(oneHour, twoHours, 49, "");
        }
        Event currentEvent = events.get(0);
        long eventStart = currentEvent.getStart().getDateTime().getValue();
        long eventEnd = currentEvent.getEnd().getDateTime().getValue();
        String eventTitle = currentEvent.getSummary();
        int freeBlocks = (int) ((eventStart - currentTime) / (15 * DateUtils.MINUTE_IN_MILLIS));
        return new RoomAvailabilityStatus(eventStart, eventEnd, freeBlocks, eventTitle);
    }
}

