package com.willowtreeapps.skrej.conference;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.api.services.calendar.model.Event;
import com.willowtreeapps.skrej.calendarapi.CalendarLoader;
import com.willowtreeapps.skrej.ConferenceApplication;
import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.calendarapi.CredentialHelper;
import com.willowtreeapps.skrej.calendarapi.EventService;
import java.util.List;
import javax.inject.Inject;

public class ConferenceRoomActivity

        extends
        AppCompatActivity

        implements
        ConferenceView,
        View.OnClickListener,
        LoaderManager.LoaderCallbacks<List<Event>>,
        CalendarLoader.CalendarLoadedAuthRequestListener
{

    //Log tag.
    private static final String TAG = ConferenceRoomActivity.class.getSimpleName();

    //Request ID for authorization activity.
    private static final int AUTH_REQUEST_ID = 3;

    //Our presenter.
    @Inject
    ConferencePresenter presenter;

    @Inject
    CredentialHelper creds;

    //View widgets.
    private Button useButton;
    private TextView availabilityTextView;
    private TextView availabilityTimeInfoTextView;
    private TextView dateTextView;

    //Name and ID of the room we're looking at handed in from the login activity.
    private String roomName;
    private String roomID;

    //region AppCompatActivity lifecycle methods:

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConferenceApplication.get(this).component().inject(this);

        //Init room name and ID.
        roomName = "";
        roomID = "";

        //Get intent extras.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            //Set room ID and room name.
            roomName = extras.getString(getString(R.string.room_name_bundle_key));
            roomID = extras.getString(getString(R.string.room_id_bundle_key));
        }

        //Set main view.
        setContentView(R.layout.activity_conference_room);

        //Set click listener for 'Use' button.
        useButton = (Button) findViewById(R.id.useRoomButton);
        useButton.setOnClickListener(this);

        //Set room name text.
        TextView roomNameTextView = (TextView) findViewById(R.id.roomNameText);
        roomNameTextView.setText(roomName);

        //Get other text views.
        availabilityTextView = (TextView) findViewById(R.id.statusText);
        availabilityTimeInfoTextView = (TextView) findViewById(R.id.timeInfoText);
        dateTextView = (TextView) findViewById(R.id.dateText);

    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        presenter.bindView(this);
        getLoaderManager().restartLoader(0, null, this);
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
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
        //presenter.unbindView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case AUTH_REQUEST_ID:
                loadCalendar();
                break;
            default:
                break;
        }
    }

    //endregion

    //region ConferenceView interface:

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
    public void loadCalendar() {
        getLoaderManager().restartLoader(0, null, this);
    }

    //endregion

    //region LoaderManager.LoaderCallbacks interface:

    @Override
    public Loader<List<Event>> onCreateLoader(int i, Bundle bundle) {

        //Return a new loader with this context, the API service from our presenter, the
        //selected roomID, and set this as a listener for the loader.
        return new CalendarLoader(this, creds.getCalendarService(), roomID, this);
    }

    @Override
    public void onLoadFinished(Loader<List<Event>> loader, List<Event> events) {

        //If we get valid ersults back...
        if (events != null) {

            //log number of envents and hand data down to the presenter.
            Log.d(TAG, "number of events:" + events.size());
            presenter.onEventsLoaded(events);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Event>> loader) {

    }

    //endregion

    //region CalendarLoader.CalendarLoadedAuthRequestListener interface:

    @Override
    public void onRequestAuth(Intent intent) {

        //Start authorization request activity with the intent.
        startActivityForResult(intent, AUTH_REQUEST_ID);
    }

    //endregion

    //region View.onClickListener interface:

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            //'Use Now' button:
            case R.id.useRoomButton:

                //Create an intent for the event service.
                Intent intent = new Intent(this, EventService.class);

                //Add our room ID.
                intent.putExtra(getString(R.string.room_id_bundle_key), roomID);

                //Launch activity.
                startService(intent);
                break;

            default:
                break;
        }
    }

    //endregion

}
