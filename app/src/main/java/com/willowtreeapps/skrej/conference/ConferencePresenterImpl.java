package com.willowtreeapps.skrej.conference;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.model.Event;
import com.willowtreeapps.skrej.CredentialHelper;
import com.willowtreeapps.skrej.EventTimeUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by barrybryant on 11/7/16.
 */

public class ConferencePresenterImpl implements ConferencePresenter {

    private static final String TAG = "ConferencePresenterImpl";

    private ConferenceView view;
    private List<Event> todaysEvents;

    @Override
    public void bindView(ConferenceView view) {
        this.view = view;
        String date = getFormattedDate();
        view.updateDate(date);
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    @Override
    public void onEventsLoaded(List<Event> events) {
        Log.d(TAG, "Events loaded to presenter");
        String availability = "";
        String availabilityTimeInfo = "";

        if(events.size() <= 0) {

            availability = "Available";
            availabilityTimeInfo = "All day";
        }

        else {
            this.todaysEvents = EventTimeUtility.filterEventsForToday(events);

            Event event = events.get(0);
            availability = getRoomAvailability(event);
            availabilityTimeInfo = getRoomAvailabilityTimeInfo(event);
        }

        if (view != null) {
            view.updateAvailability(availability);
            view.updateAvailabilityTimeInfo(availabilityTimeInfo);
        }
    }

    @Override
    public void onClickSchedule() {
        view.showSpinner();
    }

    private String getFormattedDate() {
        long currentTime = System.currentTimeMillis();
        DateFormat formatter = DateFormat.getDateInstance(DateFormat.LONG);
        return formatter.format(currentTime);

    }

    private String getRoomAvailability(Event event) {
        if (event.getStart().getDateTime().getValue() > System.currentTimeMillis()) {
            return "Available";
        } else {
            return event.getSummary();
        }
    }

    private String getRoomAvailabilityTimeInfo(Event event) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        if (event.getStart().getDateTime().getValue() > System.currentTimeMillis()) {
            return "Until " + simpleDateFormat.format(event.getStart().getDateTime().getValue());
        } else {
            return simpleDateFormat.format(event.getStart().getDateTime().getValue()) + " - " +
                    simpleDateFormat.format(event.getEnd().getDateTime().getValue());
        }
    }

}
