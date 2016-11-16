package com.willowtreeapps.skrej.calendarapi;

import android.text.format.DateUtils;

import com.google.api.services.calendar.model.Event;
import com.willowtreeapps.skrej.model.RoomAvailabilityStatus;

import java.util.List;


/**
 * Created by chrisestes on 11/14/16.
 */

public class CalendarWizard {

    private final String TAG = CalendarWizard.class.getSimpleName();

//    public RoomAvailabilityStatus parseEventData(List<Event> roomSchedule) {
//        //Number of 15 minute blocks the room is free for.
//        int roomBlocks = 0;
//
//        //Get current time.
//        long currentTime = System.currentTimeMillis();
//
//        //Init event times.
//        long eventEndTime = 0;
//        long eventStartTime = 0;
//
//        //Init free time length.
//        long freeTimeLength = 0;
//
//        long freeTimeStart = 0;
//
//        //Init flag for room occupied.
//        boolean roomCurrentlyOccupied = false;
//
//        //For all scheduled events...
//        for (Event event : roomSchedule) {
//            //Get event start time.
//            eventStartTime = event.getStart().getDateTime().getValue();
//
//            //If we have not seen that the room is occupied...
//            if (!roomCurrentlyOccupied) {
//
//                //If an event has not yet started...
//                if (eventStartTime > currentTime) {
//
//                    //Get length of time until next event.
//                    freeTimeLength = eventStartTime - currentTime;
//
//                    //The room is available right now.
//                    freeTimeStart = currentTime;
//
//                    //And we're done.
//                    break;
//
//                    //If an event is currently in progress...
//                } else {
//                    //Set occupied flag.
//                    roomCurrentlyOccupied = true;
//
//                    //Get end time for this event.
//                    eventEndTime = event.getEnd().getDateTime().getValue();
//
//                    //Go on to check next event.
//                }
//            } else {
//
//                //If there is any time between the start of this event and the end of the last
//                //event...
//                if (eventStartTime > eventEndTime) {
//
//                    //Set available time length.
//                    freeTimeLength = (eventStartTime - eventEndTime);
//
//                    //Room is available at the end of the last event.
//                    freeTimeStart = eventEndTime;
//
//                    //And we're done.
//                    break;
//                }
//
//                //If there is no time between this event and the last one...
//                else {
//
//                    //Update end time and check next event.
//                    eventEndTime = event.getEnd().getDateTime().getValue();
//                }
//            }
//        }
//
//        //Get available time in 15 minute blocks.
//        roomBlocks = (int) (freeTimeLength / (15 * DateUtils.MINUTE_IN_MILLIS));
//
//        //Linit to 1 hour.
//        if (roomBlocks > 4) {
//            roomBlocks = 4;
//        }
//
//        //Create room status object.
//
//        return (new RoomAvailabilityStatus(eventStartTime, eventEndTime,
//                roomBlocks, roomSchedule.get(0).getSummary()));
//    }

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

