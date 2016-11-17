package com.willowtreeapps.skrej.util;

import com.google.api.services.calendar.model.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barrybryant on 11/9/16.
 */

public class EventTimeWizard {

    public static List<Event> filterEventsForToday(List<Event> events) {
        //TODO: Beware all day events may not have time, account for dat
        List<Event> filteredEvents = new ArrayList<Event>();
        long currentTime = System.currentTimeMillis();
        long currentTimePlusTwelveHours = currentTime + 43200000;
        for (Event event : events) {
            if (event.getStart().getDateTime().getValue() < currentTimePlusTwelveHours &&
                    event.getEnd().getDateTime().getValue() > currentTime) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }
}
