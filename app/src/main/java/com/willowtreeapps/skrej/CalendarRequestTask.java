package com.willowtreeapps.skrej;

/**
 * Created by barrybryant on 11/7/16.
 */

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class CalendarRequestTask extends AsyncTask<Void, Void, List<String>> {
    private static final String TAG = "CalendarRequestTask";
    private com.google.api.services.calendar.Calendar mService = null;
    private Exception mLastError = null;

    public CalendarRequestTask(GoogleAccountCredential credential) {
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
    protected List<String> doInBackground(Void... params) {
        try {
            return getDataFromApi();
        } catch (Exception e) {
            mLastError = e;
            cancel(true);
            return null;
        }
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private List<String> getDataFromApi() throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<>();
        CalendarList calendarList = mService.calendarList().list().setMaxResults(10).execute();
        List<CalendarListEntry> calendarItems = calendarList.getItems();
        for (CalendarListEntry entry : calendarItems) {
            Log.d("LOL", entry.getId());
        }
        Events events = mService.events().list("willowtreeapps.com_3632363436343537393337@resource.calendar.google.com")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        Log.d("LOL", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                .format(new Date()));
        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            DateTime end = event.getEnd().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
            if (end == null) {
                // All-day events don't have start times, so just use
                // the start date.
                end = event.getEnd().getDate();
            }
            eventStrings.add(
                    String.format("%s (%s)(%s)", event.getSummary(), start, end));
        }
        return eventStrings;
    }


    @Override
    protected void onPreExecute() {
//        mProgress.show();
    }

    @Override
    protected void onPostExecute(List<String> output) {
//        mProgress.hide();
        if (output == null || output.size() == 0) {
            Log.d(TAG,"No results returned.");
        } else {
            output.add(0, "Data retrieved using the Google Calendar API:");
            Log.d(TAG, TextUtils.join("\n", output));
        }
    }

    @Override
    protected void onCancelled() {
//        mProgress.hide();
        if (mLastError != null) {
            if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
//                showGooglePlayServicesAvailabilityErrorDialog(
//                        ((GooglePlayServicesAvailabilityIOException) mLastError)
//                                .getConnectionStatusCode());
            } else if (mLastError instanceof UserRecoverableAuthIOException) {
//                startActivityForResult(
//                        ((UserRecoverableAuthIOException) mLastError).getIntent(),
//                        ConferenceRoomActivity.REQUEST_AUTHORIZATION);
            } else {
                Log.d(TAG, "The following error occurred:\n"
                        + mLastError.getMessage());
            }
        } else {
            Log.d(TAG,"Request cancelled.");
        }
    }
}
