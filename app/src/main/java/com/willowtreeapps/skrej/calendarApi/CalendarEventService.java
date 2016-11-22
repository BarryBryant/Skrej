package com.willowtreeapps.skrej.calendarApi;

import android.text.format.DateUtils;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.willowtreeapps.skrej.model.RoomAvailabilityStatus;

import java.io.IOException;
import java.util.List;

import rx.Observable;

/**
 * Created by barrybryant on 11/21/16.
 */

public class CalendarEventService {

    private static final String TAG = "CalendarEventService";
    private static final int MAX_EVENTS_TO_RETURN = 50;

    private final CredentialWizard credentialWizard;

    public CalendarEventService(CredentialWizard credentialWizard) {
        this.credentialWizard = credentialWizard;
    }

    /** ============================================================================================
     *
     * Get an Observable that will emit a RoomAvailabilityStatus for the given room. The status
     * will be valid from the current time until midnight.
     *
     * @param calendarResourceEmail The room to get events for.
     * @return An observable that will emit the room availability status upon subscription.
     *
     * ------------------------------------------------------------------------------------------ */
    public Observable<RoomAvailabilityStatus> getRoomAvailability(String calendarResourceEmail) {

        //Current time in ms.
        long nowTimeMillis = System.currentTimeMillis();

        //ms since midnight.
        long calc = (nowTimeMillis % DateUtils.DAY_IN_MILLIS);

        //ms until midnight.
        calc = (DateUtils.DAY_IN_MILLIS - calc);

        //Midnight in ms.
        long midnightTimeMillis = nowTimeMillis + calc;

        //Now and midnight as DateTime.
        DateTime nowTime = new DateTime(nowTimeMillis);
        DateTime midnightTime = new DateTime(midnightTimeMillis);

        return(
                //Get raw events list Observable.
                getEventsObservable(calendarResourceEmail, nowTime, midnightTime, MAX_EVENTS_TO_RETURN)

                        //Map events object to list Observable.from() events.
                        .map(events -> events.getItems())

                        //Parse events list into room availability status.
                        .map(events -> parseFirstEvent(events))
        );
    }

    //region Private methods:

    /** ============================================================================================
     *
     * Create an Observable to get a list of events for a room.
     *
     * @param calendarResourceEmail The room to get events for.
     * @param startTime Start time for events.
     * @param endTime End time for events.
     * @param maxResults Max number of events to get.
     * @return An Observable that will emit a list of events for the given room.
     *
     * ------------------------------------------------------------------------------------------ */
    private Observable<Events> getEventsObservable(
            String calendarResourceEmail,
            DateTime startTime,
            DateTime endTime,
            int maxResults
    ) {
        return(Observable.fromCallable(() ->
                getRoomEvents(calendarResourceEmail, startTime, endTime,  maxResults)
        ));
    }

    /** ============================================================================================
     *
     * Get an Events list object from the API.
     *
     * @param calendarResourceEmail The room to get events for.
     * @param startTime Start time for events.
     * @param endTime End time for events.
     * @param maxResults Max number of events to get.
     * @return Events object containing all events between the start and end times.
     * @throws IOException
     *
     * ------------------------------------------------------------------------------------------ */
    private Events getRoomEvents(
            String calendarResourceEmail,
            DateTime startTime,
            DateTime endTime,
            int maxResults
    ) throws IOException {

        return(
                credentialWizard
                        .getCalendarService()
                        .events()
                        .list(calendarResourceEmail)
                        .setMaxResults(maxResults)
                        .setTimeMin(startTime)
                        .setTimeMax(endTime)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute()
        );
    }


    private static RoomAvailabilityStatus parseFirstEvent(List<Event> events) {
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
