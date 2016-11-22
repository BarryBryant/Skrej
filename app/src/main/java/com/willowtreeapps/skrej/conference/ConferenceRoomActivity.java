package com.willowtreeapps.skrej.conference;

import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.willowtreeapps.skrej.ConferenceApplication;
import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.attendeeSelection.AttendeeDialogFragment;
import com.willowtreeapps.skrej.calendarApi.EventService;
import com.willowtreeapps.skrej.model.RoomAvailabilityStatus;
import com.willowtreeapps.skrej.model.RoomModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ConferenceRoomActivity extends AppCompatActivity implements ConferenceView,
        View.OnClickListener,
        AttendeeDialogFragment.AttendeeSelectedListener {

    //Log tag.
    private static final String TAG = ConferenceRoomActivity.class.getSimpleName();

    //Request ID for authorization activity.
    private static final int AUTH_REQUEST_ID = 3;

    //Our presenter.
    @Inject
    ConferencePresenter presenter;

    //View widgets.
    private Button useButton;
    private TextView availabilityTextView;
    private TextView availabilityTimeInfoTextView;
    private TextView dateTextView;

    private String roomId;

    //region AppCompatActivity lifecycle methods:

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConferenceApplication.get(this).component().inject(this);

        //Init room name and ID.
        String roomName = "";
        roomId = "";

        //Get intent extras.
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            RoomModel room = extras.getParcelable(getString(R.string.room_id_bundle_key));
            //Set room ID and room name.
            roomName = room.getRoomName();
            roomId = room.getRoomResourceEmail();
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

        presenter.setRoomId(roomId);
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
        presenter.bindView(this);
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
                presenter.bindView(this);
                break;
            default:
                break;
        }
    }

    //endregion

    //region ConferenceView interface:

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
        useButton.setEnabled(true);
    }

    @Override
    public void disableScheduleButton() {
        useButton.setEnabled(false);
    }

    @Override
    public void showEventDurationPrompt(final RoomAvailabilityStatus roomStatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int numOfBlocks = roomStatus.getAvailableBlocks();
        builder.setTitle("Set meeting duration");
        String[] types = new String[numOfBlocks];
        for (int i = 0; i < numOfBlocks; i++) {
            types[i] = ((i + 1) * 15) + " Minutes";
            Log.d(TAG, types[i]);
        }
        builder.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                int chosenNumOfBlocks = which + 1;
                presenter.onNumOfBlocksChosen(chosenNumOfBlocks);
            }

        });

        builder.show();
    }

    @Override
    public void showEventAttendeesPrompt() {
        AttendeeDialogFragment dialog = AttendeeDialogFragment.getInstance();
        FragmentManager fragmentManager = getFragmentManager();
        dialog.show(fragmentManager, "attendee_fragment");

    }

    @Override
    public void createEvent(int chosenNumOfBlocks, List<String> attendees) {
        //Create an intent for the event service.
        Intent intent = new Intent(this, EventService.class);
        //Add our room ID.
        intent.putExtra(getString(R.string.room_id_bundle_key), roomId);
        intent.putExtra(getString(R.string.num_of_blocks_intent_key), chosenNumOfBlocks);
        ArrayList<String> attendeesList = new ArrayList<>(attendees);
        intent.putStringArrayListExtra("attendeesKey", attendeesList);
        //Launch activity.
        startService(intent);
        Log.d(TAG, "Num of blocks: " + chosenNumOfBlocks + "num of attendees: " + attendees.size());
    }

    //endregion

    //region View.onClickListener interface:

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //'Use Now' button:
            case R.id.useRoomButton:
                presenter.onClickSchedule();
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttendeesSelected(List<String> attendees) {
        presenter.onAttendeesSelected(attendees);
    }

    //endregion

}
