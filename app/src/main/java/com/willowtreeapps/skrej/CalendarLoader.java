package com.willowtreeapps.skrej;

import android.content.Context;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by barrybryant on 11/8/16.
 */

public class CalendarLoader extends EricRichardsonLoader<List<Event>> {

    @Inject
    CredentialHelper credentialHelper;

    private com.google.api.services.calendar.Calendar service;
    private Exception lastError;
    private String roomId;

    public CalendarLoader(Context context, String roomId) {
        super(context);
        ConferenceApplication.get(context.getApplicationContext()).component().inject(this);
        this.roomId = roomId;
        service = credentialHelper.getCalendarService();
    }

    @Override
    public List<Event> loadInBackground() {
        try {
            return getDataFromApi();
        } catch (Exception e) {
            lastError = e;
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
