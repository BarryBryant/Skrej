package com.willowtreeapps.skrej.conference;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.calendar.model.Event;
import com.willowtreeapps.skrej.CalendarLoader;
import com.willowtreeapps.skrej.ConferenceApplication;
import com.willowtreeapps.skrej.CredentialHelper;
import com.willowtreeapps.skrej.R;

import java.util.List;

import javax.inject.Inject;

public class ConferenceRoomActivity extends AppCompatActivity implements ConferenceView,
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<List<Event>> {

    private static final String TAG = "ConferenceRoomActivity";

    @Inject
    ConferencePresenter presenter;
    @Inject
    CredentialHelper credentialHelper;

    private Button useButton;
    private TextView availabilityTextView;
    private TextView availabilityTimeInfoTextView;
    private TextView dateTextView;
    private String roomId;

    /**
     * Create the main activity.
     *
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConferenceApplication.get(this).component().inject(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomId = extras.getString(getString(R.string.room_id_bundle_key));
            Log.d(TAG, roomId);
        }
        setContentView(R.layout.activity_conference_room);
        useButton = (Button) findViewById(R.id.useRoomButton);
        useButton.setOnClickListener(this);
        availabilityTextView = (TextView) findViewById(R.id.statusText);
        availabilityTimeInfoTextView = (TextView) findViewById(R.id.timeInfoText);
        dateTextView = (TextView) findViewById(R.id.dateText);
        TextView nameText = (TextView)findViewById(R.id.roomNameText);
        nameText.setText(roomId);
        setupLoader();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
        presenter.unbindView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.bindView(this);
        Log.d(TAG, "onStart");
    }

    @Override
    public void showSpinner() {

    }

    @Override
    public void hideSpinner() {

    }

    @Override
    public void updateAvailability(String availability) {
        availabilityTextView.setText(availability);
    }

    @Override
    public void updateAvailabilityTimeInfo(String availabilityTimeInfo) {
        availabilityTimeInfoTextView.setText(availabilityTimeInfo);
    }

    @Override
    public void updateDate(String date) {
        dateTextView.setText(date);
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
                break;
            default:
                break;
        }
    }



    private void setupLoader() {
        if (credentialHelper.hasValidCredential()) {
            getLoaderManager().initLoader(0, null, this);
        } else {
            //TODO: Tell user their creds are no bueno and return to login
            Log.d(TAG, "BAD CREDS BRUH");
        }
    }

    @Override
    public Loader<List<Event>> onCreateLoader(int i, Bundle bundle) {
        GoogleAccountCredential credential = credentialHelper.getCredential();
        return new CalendarLoader(this, credential, roomId);
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> events) {
        Log.d(TAG, "number of events:" + events.size());
        presenter.onEventsLoaded(events);

    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {

    }
}
