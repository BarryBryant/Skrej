package com.willowtreeapps.skrej;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static com.willowtreeapps.skrej.CalendarApi.REQUEST_GOOGLE_PLAY_SERVICES;

public class ConferenceRoomActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks, ConferenceView {

    ProgressDialog mProgress;

    private static final String TAG = "ConferenceRoomActivity";

    private ConferencePresenter presenter;

    /**
     * Create the main activity.
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ConferencePresenterImpl(new CalendarApi(this, getPreferences(MODE_PRIVATE)));
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling Google Calendar API ...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.bindView(this);
    }

    @Override
    protected void onPause() {
        presenter.unbindView();
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        presenter.onActivityResult(requestCode, resultCode, name);
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param statusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    @Override
    public void showPlayServicesErrorDialog(int statusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                ConferenceRoomActivity.this,
                statusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    @Override
    public void showErrorDialog(String message) {

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
}
