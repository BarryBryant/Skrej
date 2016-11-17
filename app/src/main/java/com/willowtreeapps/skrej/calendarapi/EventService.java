package com.willowtreeapps.skrej.calendarapi;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.willowtreeapps.skrej.ConferenceApplication;
import com.willowtreeapps.skrej.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by barrybryant on 11/11/16.
 */

public class EventService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    public static final long FIFTEEN_MINUTES = 900000;
    private static final String TAG = "EventService";

    @Inject
    CredentialWizard credentialWizard;

    private com.google.api.services.calendar.Calendar service;

    public EventService() {
        super(EventService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ConferenceApplication.get(getApplicationContext()).component().inject(this);
        service = credentialWizard.getCalendarService();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String roomId = intent.getStringExtra(getString(R.string.room_id_bundle_key));
        int numOfBlocks = intent.getIntExtra(getString(R.string.num_of_blocks_intent_key), 1);
        ArrayList<String> attendees = intent.getStringArrayListExtra("attendeesKey");
        //create receiver notify of start download
        try {
            createEvent(roomId, numOfBlocks, attendees);
        } catch (Exception e) {
            //send error message
            Log.d(TAG, e.getMessage());
        }
    }

    private void createEvent(String roomId, int numOfBlocks, List<String> attendees) throws IOException {
        Event event = new Event()
                .setSummary("Ad Hoc Meeting")
                .setDescription("Ad Hoc Meeting");
        DateTime startDateTime = new DateTime(System.currentTimeMillis());
        DateTime endDateTime = new DateTime(System.currentTimeMillis() + (numOfBlocks * FIFTEEN_MINUTES));
        EventDateTime start = new EventDateTime().setDateTime(startDateTime);
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        event.setStart(start);
        event.setEnd(end);
        List<EventAttendee> eventAttendees = new ArrayList<>();
        eventAttendees.add(new EventAttendee().setEmail(roomId));
        for (String attendee : attendees) {
            eventAttendees.add(new EventAttendee().setEmail(attendee));
        }
        event.setAttendees(eventAttendees);
        SharedPreferences preferences = getSharedPreferences(getString(R.string.credentials_preference_key), Context.MODE_PRIVATE);
        String accountId = preferences.getString(PREF_ACCOUNT_NAME, null);
        if (accountId == null) {
            throw new Error("Account is fucked");
        }
        service.events().insert(accountId, event).execute();
        Log.d(TAG, "GOT TO END OF THE CREATE VENT DAG");
    }
}
