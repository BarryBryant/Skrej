package com.willowtreeapps.skrej;

/**
 * Created by barrybryant on 11/7/16.
 */

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class CalendarRequestTask extends AsyncTask<Void, Void, List<Event>> {
    private static final String TAG = "CalendarRequestTask";
    private static final String CACTUAR_ID = "willowtreeapps.com_3632363436343537393337@resource.calendar.google.com";
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;
    private ArrayList<CalendarListener> listeners;

    public CalendarRequestTask(GoogleAccountCredential credential, ArrayList<CalendarListener> listeners) {
        this.listeners = listeners;
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();
    }

    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected List<Event> doInBackground(Void... params) {
        try {
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
        }
        return null;
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private List<Event> getDataFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        //hardcoded to cactuar currently
        Events events = mService.events().list(CACTUAR_ID)
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        return events.getItems();
    }

    @Override
    protected void onPostExecute(List<Event> events) {
        super.onPostExecute(events);
        for (CalendarListener listener: listeners) {
            listener.onCalendarLoadFinished(events);
        }
    }

    @Override
    protected void onCancelled() {
        if (mLastError != null) {
            for (CalendarListener listener: listeners) {
                listener.onError(mLastError);
            }
        } else {
            for (CalendarListener listener: listeners) {
                listener.onError(new Error("Request Cancelled"));
            }
        }
    }
}
