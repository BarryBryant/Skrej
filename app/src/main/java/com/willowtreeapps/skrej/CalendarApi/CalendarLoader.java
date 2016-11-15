package com.willowtreeapps.skrej.calendarapi;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.willowtreeapps.skrej.ConferenceApplication;
import com.willowtreeapps.skrej.util.EricRichardsonLoader;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.Component;

/**
 * Created by barrybryant on 11/8/16.
 */

public class CalendarLoader extends EricRichardsonLoader<List<Event>> {

    //Interface defines a callback to request user authorization if our API query fails.
    public interface CalendarLoadedAuthRequestListener {
        void onRequestAuth(Intent intent);
    }

    //Log tag.
    private static final String TAG = CalendarLoader.class.getSimpleName();

    //The ID of the room we want to query.
    private String roomId;

    //Authorization request listener instance.
    private CalendarLoadedAuthRequestListener listener;

    //Calendar service object.
    private Calendar service;

    public CalendarLoader(
        Context context,
        Calendar service,
        String roomID,
        CalendarLoadedAuthRequestListener authRequestListener
    ) {
        super(context);
        this.service = service;
        this.roomId = roomID;
        this.listener = authRequestListener;
    }

    @Override
    public List<Event> loadInBackground() {
        try {
            return getDataFromApi();
        } catch (UserRecoverableAuthIOException e) {
            listener.onRequestAuth(e.getIntent());
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return null;
    }

    private List<Event> getDataFromApi() throws IOException {

        //Current time in ms.
        long nowTimeMillis = System.currentTimeMillis();

        //Midnight in ms.
        long midnightTimeMillis = nowTimeMillis + (nowTimeMillis % DateUtils.DAY_IN_MILLIS);

        //Now and midnight as DateTime.
        DateTime nowTime = new DateTime(nowTimeMillis);
        DateTime midnightTime = new DateTime(midnightTimeMillis);

        //Get no more than ten events between now and midnight tonight, ordered by start time.
        Events events = service.events()
                .list(roomId)
                .setMaxResults(10)
                .setTimeMin(nowTime)
                .setTimeMax(midnightTime)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        //Return event list.
        return events.getItems();
    }
}
