package com.willowtreeapps.skrej.calendarapi;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by chrisestes on 11/14/16.
 */

public class CalendarWizard {

    private final String TAG = CalendarWizard.class.getSimpleName();

    public RoomAvailabilityStatus parseEventData(List<Event> roomSchedule) {

        //Number of 15 minute blocks the room is free for.
        long roomBlocks = 0;

        //Get current time.
        long currentTime = System.currentTimeMillis();

        //Init event times.
        long eventEndTime = 0;
        long eventStartTime = 0;

        //Init free time length.
        long freeTimeLength = 0;

        long freeTimeStart = 0;

        //Init flag for room occupied.
        boolean roomCurrentlyOccupied = false;

        //For all scheduled events...
        for (Event event : roomSchedule) {

            //Get event start time.
            eventStartTime = event.getStart().getDateTime().getValue();

            //If we have not seen that the room is occupied...
            if (!roomCurrentlyOccupied) {

                //If an event has not yet started...
                if (eventStartTime > currentTime) {

                    //Get length of time until next event.
                    freeTimeLength = eventStartTime - currentTime;

                    //The room is available right now.
                    freeTimeStart = currentTime;

                    //And we're done.
                    break;

                    //If an event is currently in progress...
                } else {

                    //Set occupied flag.
                    roomCurrentlyOccupied = true;

                    //Get end time for this event.
                    eventEndTime = event.getEnd().getDateTime().getValue();

                    //Go on to check next event.
                }
            }

            //If the room is currently occupied...
            else {

                //If there is any time between the start of this event and the end of the last
                //event...
                if (eventStartTime > eventEndTime) {

                    //Set available time length.
                    freeTimeLength = (eventStartTime - eventEndTime);

                    //Room is available at the end of the last event.
                    freeTimeStart = eventEndTime;

                    //And we're done.
                    break;
                }

                //If there is no time between this event and the last one...
                else {

                    //Update end time and check next event.
                    eventEndTime = event.getEnd().getDateTime().getValue();
                }
            }
        }

        //Get available time in 15 minute blocks.
        roomBlocks = freeTimeLength / (15 * DateUtils.MINUTE_IN_MILLIS);

        //Linit to 1 hour.
        if(roomBlocks > 4) { roomBlocks = 4; }

        //Create room status object.
        RoomAvailabilityStatus roomAvailabilityStatus = new RoomAvailabilityStatus(
                freeTimeStart,
                (int) roomBlocks
        );

        return (roomAvailabilityStatus);
    }
}

