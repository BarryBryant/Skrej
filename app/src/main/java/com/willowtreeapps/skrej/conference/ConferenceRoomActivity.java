package com.willowtreeapps.skrej.conference;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.willowtreeapps.skrej.CalendarLoader;
import com.willowtreeapps.skrej.CredentialHelper;
import com.willowtreeapps.skrej.R;

import java.util.Arrays;
import java.util.List;

public class ConferenceRoomActivity extends AppCompatActivity implements ConferenceView,
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<List<Event>> {

    private static final String TAG = "ConferenceRoomActivity";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };
    private static final String PREF_ACCOUNT_NAME = "accountName";

    private ConferencePresenterImpl presenter;
    private Button useButton;
    private GoogleAccountCredential credential;
    private String roomId;

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomId = extras.getString(getString(R.string.room_id_bundle_key));
            Log.d(TAG, roomId);
        }
        setContentView(R.layout.activity_conference_room);
        presenter = new ConferencePresenterImpl();
        useButton = (Button) findViewById(R.id.useRoomButton);
        useButton.setOnClickListener(this);
        setupLoader();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        presenter.unbindView();
    }

    @Override
    public void showSpinner() {

    }

    @Override
    public void hideSpinner() {

    }

    @Override
    public void updateAvailability() {

    }

    @Override
    public void updateDate() {

    }

    @Override
    public void enableScheduleButton() {

    }

    @Override
    public void disableScheduleButton() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.useRoomButton:
                presenter.loadCalendar();
            default:
                break;
        }
    }

    private void setupLoader() {
        //Generate a GoogleAccountCredential and verify that it is valid before attemping
        //to use it for the Calendar API call
        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        SharedPreferences preferences = getSharedPreferences(getString(R.string.credentials_preference_key), MODE_PRIVATE);
        String accountName = preferences.getString(PREF_ACCOUNT_NAME, null);
        credential.setSelectedAccountName(accountName);
        CredentialHelper credentialHelper = new CredentialHelper(this, preferences);
        if (credentialHelper.isValidCredential(credential)) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            //TODO: Tell user their creds are no bueno and return to login
            Log.d(TAG, "BAD CREDS BRUH");
        }
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int i, Bundle bundle) {
        return new CalendarLoader(this, credential, "");
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> events) {
        Log.d(TAG, "number of events:" + events.size());
    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {

    }
}
