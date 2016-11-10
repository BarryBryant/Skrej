package com.willowtreeapps.skrej;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.willowtreeapps.skrej.CredentialHelper.REQUEST_PERMISSION_GET_ACCOUNTS;

public class ConferenceRoomActivity extends AppCompatActivity implements ConferenceView, View.OnClickListener {

    private static final String TAG = "ConferenceRoomActivity";
    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    private ConferencePresenterImpl presenter;
    private Button useButton;

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_room);
        presenter = (ConferencePresenterImpl) getLastCustomNonConfigurationInstance();

        if (presenter == null) {
            Log.d(TAG, "New Presenter");
            presenter = new ConferencePresenterImpl(this);
        }
        useButton = (Button) findViewById(R.id.useRoomButton);
        useButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
        presenter.bindView(this);
        presenter.loadCalendar();
        presenter.initializeLoader(this);
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
    public void updateTime(String newRoomTime) {

        TextView timeView = (TextView)findViewById(R.id.timeInfoText);
        timeView.setText(newRoomTime);
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

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    public void setRoomName(String roomName) {
        ((TextView)findViewById(R.id.roomNameText)).setText(roomName);
    }
}
