package com.willowtreeapps.skrej.calendarapi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.willowtreeapps.skrej.ConferenceApplication;
import com.willowtreeapps.skrej.util.EricRichardsonLoader;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by barrybryant on 11/8/16.
 */

public class CalendarLoader extends EricRichardsonLoader<List<Event>> {

    public interface CalendarLoadedAuthRequestListener {
        void onRequestAuth(Intent intent, int requestId);
    }

    @Inject
    CredentialHelper credentialHelper;

    private static final String TAG = "CalendarLoader";
    private static final int AUTH_REQUEST_ID = 3;

    private com.google.api.services.calendar.Calendar service;
    private Exception lastError;
    private String roomId;
    CalendarLoadedAuthRequestListener listener;

    public CalendarLoader(Context context, String roomId, CalendarLoadedAuthRequestListener listener) {
        super(context);
        ConferenceApplication.get(context.getApplicationContext()).component().inject(this);
        this.roomId = roomId;
        this.listener = listener;
        service = credentialHelper.getCalendarService();
    }

    @Override
    public List<Event> loadInBackground() {
        try {
            return getDataFromApi();
        } catch (UserRecoverableAuthIOException e) {
            listener.onRequestAuth(e.getIntent(), AUTH_REQUEST_ID);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return null;
    }

    private List<Event> getDataFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list(roomId)
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        return events.getItems();
    }
}
