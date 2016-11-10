package com.willowtreeapps.skrej.conference;

import android.content.Intent;

import com.google.api.services.calendar.model.Event;

/**
 * Created by barrybryant on 11/7/16.
 */

public interface ConferenceView {
    void showSpinner();

    void hideSpinner();

    void updateAvailability(String availability);

    void updateAvailabilityTimeInfo(String availabilityTimeInfo);

    void updateDate(String date);

    void enableScheduleButton();

    void disableScheduleButton();

}
