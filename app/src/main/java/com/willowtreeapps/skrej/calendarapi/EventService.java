package com.willowtreeapps.skrej.calendarapi;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.willowtreeapps.skrej.ConferenceApplication;
import com.willowtreeapps.skrej.R;

import java.io.IOException;
import java.util.Arrays;

import javax.inject.Inject;

/**
 * Created by barrybryant on 11/11/16.
 */

public class EventService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String CACTUAR_ID = "willowtreeapps.com_3632363436343537393337@resource.calendar.google.com";
    private static final String DEKU_ID = "willowtreeapps.com_2d3531383336393730383033@resource.calendar.google.com";
    private static final String ELDERBERRY_ID = "willowtreeapps.com_2d3839383537323139333730@resource.calendar.google.com";
    private static final String SUDOWOODO_ID = "willowtreeapps.com_2d3331363639303230383838@resource.calendar.google.com";
    public static final long FIFTEEN_MINUTES = 900000;
    public static final long THREE_HOURS = FIFTEEN_MINUTES * 12;

    private static final String TAG = "EventService";
    private com.google.api.services.calendar.Calendar service;

    @Inject
    CredentialHelper credentialHelper;

    public EventService() {
        super(EventService.class.getName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ConferenceApplication.get(getApplicationContext()).component().inject(this);
        service = credentialHelper.getCalendarService();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String roomId = intent.getStringExtra(getString(R.string.room_id_bundle_key));
        //create receiver notify of start download
        try {
            createEvent(roomId);
        } catch (Exception e) {
            //send error message
            Log.d(TAG, e.getMessage());
        }
    }

    private void createEvent(String roomId) throws IOException {
        Event event = new Event()
                .setSummary("Just a lil test")
                .setDescription("A calendar doodad");
        DateTime startDateTime = new DateTime(System.currentTimeMillis() + (THREE_HOURS * 2));
        DateTime endDateTime = new DateTime(System.currentTimeMillis() + (THREE_HOURS * 2) + FIFTEEN_MINUTES);
        EventDateTime start = new EventDateTime().setDateTime(startDateTime);
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        event.setStart(start);
        event.setEnd(end);
        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail(DEKU_ID)
        };
        event.setAttendees(Arrays.asList(attendees));
        service.events().insert(roomId, event).execute();
        Log.d(TAG, "GOT TO END OF THE CREATE VENT DAG");
    }
}
