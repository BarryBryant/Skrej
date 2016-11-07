package com.willowtreeapps.skrej;

import android.content.Intent;

import com.google.api.services.calendar.model.Event;

import java.util.List;

/**
 * Created by barrybryant on 11/7/16.
 */

public interface CalendarListener {
    void onCalendarLoadFinished(List<Event> events);
    void onError(Throwable error);
    void onGooglePlayServicesError(int statusCode);
    void showChooseAccountActivity(Intent intent, int requestCode);
}
