package com.willowtreeapps.skrej.login;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.willowtreeapps.skrej.ConferenceApplication;
import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.adapter.RoomAdapter;
import com.willowtreeapps.skrej.conference.ConferenceRoomActivity;
import com.willowtreeapps.skrej.model.RoomModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pub.devrel.easypermissions.EasyPermissions;

import static com.willowtreeapps.skrej.calendarApi.CredentialWizard.REQUEST_ACCOUNT_PICKER;
import static com.willowtreeapps.skrej.calendarApi.CredentialWizard.REQUEST_AUTHORIZATION;
import static com.willowtreeapps.skrej.calendarApi.CredentialWizard.REQUEST_PERMISSION_GET_ACCOUNTS;

public class LoginActivity extends AppCompatActivity implements LoginView,
        EasyPermissions.PermissionCallbacks {

    //Tag for logging.
    private static final String TAG = LoginActivity.class.getSimpleName();

    //The presenter for this view.
    @Inject
    LoginPresenter presenter;

    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConferenceApplication.get(this).component().inject(this);
        setContentView(R.layout.activity_login);

        recyclerView = (RecyclerView) findViewById(R.id.room_list_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (getLastCustomNonConfigurationInstance() != null) {
            presenter = (LoginPresenter) getLastCustomNonConfigurationInstance();
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    /**
     * Bind / unbind to presenter on Start, Resume / Stop, Pause.
     */
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
    protected void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    /**
     * Pass results of activities launched here back down to our presenter. (Error, permission,
     * account select dialogs, etc.)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case REQUEST_ACCOUNT_PICKER:
                String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                presenter.onActivityResult(requestCode, resultCode, name);
                break;
            default:
                break;
        }

    }

    /**
     * Show an error dialog that will direct the user to install or update google play services.
     *
     * @param statusCode  why we got here
     * @param requestCode what we need to do
     */
    @Override
    public void showPlayServicesErrorDialog(int statusCode, int requestCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(this, statusCode, requestCode);
        dialog.show();
    }

    /**
     * Show a general error dialog.
     *
     * @param message error message to show.
     */
    @Override
    public void showErrorDialog(String message) {

    }

    /**
     * Show / hide a 'Waiting on google API' dialog.
     */
    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Show dialog to request permission to access google accounts.
     */
    @Override
    public void showUserPermissionsDialog() {
        EasyPermissions.requestPermissions(
                this,
                "This app needs to access your Google account (via Contacts).",
                REQUEST_PERMISSION_GET_ACCOUNTS,
                Manifest.permission.GET_ACCOUNTS
        );
    }

    @Override
    public void showAccountPicker(Intent intent) {
        startActivityForResult(intent, REQUEST_ACCOUNT_PICKER);
    }

    @Override
    public void addRoomButtons(List<RoomModel> rooms) {
        RoomAdapter roomAdapter = new RoomAdapter(rooms);
        recyclerView.setAdapter(roomAdapter);
    }


    /**
     * Callbacks for easyPermissions.
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onAuthIOException(UserRecoverableAuthIOException exception) {
        Intent intent = exception.getIntent();
        startActivityForResult(intent, REQUEST_AUTHORIZATION);
    }

}
