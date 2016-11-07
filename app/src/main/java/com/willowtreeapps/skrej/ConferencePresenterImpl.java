package com.willowtreeapps.skrej;

import android.content.Intent;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.calendar.model.Event;

import java.util.List;

import static com.willowtreeapps.skrej.CalendarApi.REQUEST_AUTHORIZATION;

/**
 * Created by barrybryant on 11/7/16.
 */

public class ConferencePresenterImpl implements ConferencePresenter, CalendarListener {

    private static final String TAG = "ConferencePresenterImpl";
    private ConferenceView view;
    private CalendarApi api;

    public ConferencePresenterImpl(CalendarApi api) {
        this.api = api;
        api.register(this);
    }

    @Override
    public void bindView(ConferenceView view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        this.view = null;
        api.unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, String name) {
        api.onActivityResult(requestCode, resultCode, name);
    }

    @Override
    public void onClickSchedule() {
        view.showSpinner();
    }

    @Override
    public void onCalendarLoadFinished(List<Event> events) {

    }

    @Override
    public void onError(Throwable error) {
            if (error instanceof GooglePlayServicesAvailabilityIOException) {
                view.showPlayServicesErrorDialog(
                        ((GooglePlayServicesAvailabilityIOException) error)
                                .getConnectionStatusCode());
            } else if (error instanceof UserRecoverableAuthIOException) {
                view.startActivityForResult(
                        ((UserRecoverableAuthIOException) error).getIntent(),
                        REQUEST_AUTHORIZATION);
            } else {
                view.showErrorDialog("The following error occurred:\n"
                        + error.getMessage());
            }
    }

    @Override
    public void onGooglePlayServicesError(int statusCode) {
        view.showPlayServicesErrorDialog(statusCode);
    }

    @Override
    public void showChooseAccountActivity(Intent intent, int requestCode) {
        view.startActivityForResult(intent, requestCode);
    }
}
