package com.willowtreeapps.skrej;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.model.Event;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.willowtreeapps.skrej.CredentialHelper.REQUEST_ACCOUNT_PICKER;
import static com.willowtreeapps.skrej.CredentialHelper.REQUEST_AUTHORIZATION;
import static com.willowtreeapps.skrej.CredentialHelper.REQUEST_GOOGLE_PLAY_SERVICES;

/**
 * Created by barrybryant on 11/7/16.
 */

public class ConferencePresenterImpl implements ConferencePresenter,

        LoaderManager.LoaderCallbacks<List<Event>>{

    private static final String TAG = "ConferencePresenterImpl";

    private GoogleAccountCredential credential;
    private Context context;
    private CredentialHelper credentialHelper;
    private ConferenceView view;

    String selectedRoomName;

    public ConferencePresenterImpl(Context context, CredentialHelper credentialHelper) {
        this.context = context;
        this.credentialHelper = credentialHelper;
        selectedRoomName = ((Activity)context).getIntent().getExtras().getString("room_name");

        Log.d(TAG, "Selected room: " + selectedRoomName);
    }

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
    public void initializeLoader(Activity activity) {
        Log.d(TAG, "Init loader");
        activity.getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void loadCalendar() {
        credentialHelper.getValidCredential();
        view.showSpinner();
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int i, Bundle bundle) {
        return new CalendarLoader(context, credential);
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        Log.d(TAG, "" + data.size());
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {

    }




//    @Override
//    public void onCalendarLoadFinished(List<Event> events) {
//        view.hideSpinner();
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
//        view.hideSpinner();
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
