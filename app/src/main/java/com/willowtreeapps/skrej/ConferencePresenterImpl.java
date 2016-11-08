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
        LoaderManager.LoaderCallbacks<List<Event>>,
        CredentialHelper.CredentialListener {

    private static final String TAG = "ConferencePresenterImpl";

    private GoogleAccountCredential credential;
    private Context context;
    private CredentialHelper credentialHelper;
    private ConferenceView view;


    public ConferencePresenterImpl(Context context, CredentialHelper credentialHelper) {
        this.context = context;
        this.credentialHelper = credentialHelper;
        this.credentialHelper.registerListener(this);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    //TODO: Show that user what the real deal is about G00glePlayServices
                    Log.d(TAG,
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    //retry with verified google play services
                    credentialHelper.getValidCredential();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && name != null) {
                    //retry after selecting account
                    credentialHelper.onAccountPicked(name);
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    credentialHelper.getValidCredential();
                }
                break;
        }
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

    @Override
    public void onReceiveValidCredentials(GoogleAccountCredential credential) {
        this.credential = credential;
        view.onVerifiedValidCredentials();
    }

    @Override
    public void onUserResolvablePlayServicesError(int connectionStatusCode) {
        view.showPlayServicesErrorDialog(connectionStatusCode);
    }

    @Override
    public void networkUnavailable() {
        view.showErrorDialog("Network unavailable");
    }

    @Override
    public void requestAccountPicker() {
        view.startActivityForResult(
                credential.newChooseAccountIntent(),
                REQUEST_ACCOUNT_PICKER);
    }

    @Override
    public void requestPermissions() {
        view.showUserPermissionsDialog();
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
