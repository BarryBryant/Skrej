package com.willowtreeapps.skrej;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.accounts.GoogleAccountManager;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static android.support.v7.appcompat.R.id.time;
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

    private ConferenceView view;
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };
    String selectedRoomName;

    public ConferencePresenterImpl(Context context) {
        this.context = context;
        this.credential =  GoogleAccountCredential.usingOAuth2(
                context, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        selectedRoomName = ((Activity)context).getIntent().getExtras().getString(context.getString(R.string.room_name_intent_key));

        Log.d(TAG, "Selected room: " + selectedRoomName);
    }

    @Override
    public void bindView(ConferenceView view) {
        this.view = view;
        this.view.setRoomName(selectedRoomName);
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
        SharedPreferences myPrefs = context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String acctName = myPrefs.getString(context.getString(R.string.pref_account_name), "");
        credential.setSelectedAccountName(acctName);
        activity.getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void loadCalendar() {
        //credentialHelper.getValidCredential();

        view.showSpinner();
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int i, Bundle bundle) {
        CalendarLoader retVal;
        retVal = new CalendarLoader(context, credential, selectedRoomName);
        return (retVal);
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> data) {
        Log.d(TAG, "" + data.size());

        Event myEvent;
        myEvent = data.get(0);

        EventDateTime myTime = myEvent.getStart();



        Long myMillis = myTime.getDateTime().getValue();

        Date date = new Date(myMillis); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a \n EEE, d MMM yyyy"); // the format of your date
       // sdf.setTimeZone(TimeZone.getTimeZone("GMT-4")); // give a timezone reference for formating (see comment at the bottom
        String formattedTime = sdf.format(date);






        view.updateTime(formattedTime);
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
