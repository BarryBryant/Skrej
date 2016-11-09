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

/**
 * Created by barrybryant on 11/8/16.
 */

public class CalendarLoader extends EricRichardsonLoader<List<Event>> {

    private static final String CACTUAR_ID = "willowtreeapps.com_3632363436343537393337@resource.calendar.google.com";

    private GoogleAccountCredential credential;
    private com.google.api.services.calendar.Calendar service;
    private Exception lastError;
    private String roomId;

    public CalendarLoader(Context context, GoogleAccountCredential credential, String roomId) {
        super(context);
        this.credential = credential;
//        this.roomId = roomId;
        this.roomId = CACTUAR_ID; // HARD CODED FOR TESTING LYF
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        service = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
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
