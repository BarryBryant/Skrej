package com.willowtreeapps.skrej.conference;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.willowtreeapps.skrej.CredentialHelper;

/**
 * Created by barrybryant on 11/7/16.
 */

public class ConferencePresenterImpl implements ConferencePresenter {

    private static final String TAG = "ConferencePresenterImpl";

    private ConferenceView view;

    @Override
    public void bindView(ConferenceView view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        this.view = null;
    }


    @Override
    public void onClickSchedule() {
        view.showSpinner();
    }

    @Override
    public void loadCalendar() {

    }

//    @Override
//    public void onCalendarLoadFinished(List<Event> events) {
//        view.hideLoading();
//        for (Event event : events) {
//            DateTime start = event.getStart().getDateTime();
//            DateTime end = event.getEnd().getDateTime();
//            if (start == null) {
//                // All-day events don't have start times, so just use
//                // the start date.
//                start = event.getStart().getDate();
//            }
//            if (end == null) {
//                // All-day events don't have start times, so just use
//                // the start date.
//                end = event.getEnd().getDate();
//            }
//            Log.d(TAG,
//                    String.format("%s (%s)(%s)", event.getSummary(), start, end));
//        }
//    }

//    @Override
//    public void onError(Throwable error) {
//        view.hideLoading();
//        Log.d(TAG, error.getMessage());
//            if (error instanceof GooglePlayServicesAvailabilityIOException) {
//                view.showPlayServicesErrorDialog(
//                        ((GooglePlayServicesAvailabilityIOException) error)
//                                .getConnectionStatusCode());
//            } else if (error instanceof UserRecoverableAuthIOException) {
//                view.startActivityForResult(
//                        ((UserRecoverableAuthIOException) error).getIntent(),
//                        REQUEST_AUTHORIZATION);
//            } else {
//                view.showErrorDialog("The following error occurred:\n"
//                        + error.getMessage());
//            }
//    }



}
