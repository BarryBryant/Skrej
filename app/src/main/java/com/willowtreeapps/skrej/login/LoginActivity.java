package com.willowtreeapps.skrej.login;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.android.gms.common.GoogleApiAvailability;
import com.willowtreeapps.skrej.CredentialHelper;
import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.conference.ConferenceRoomActivity;

import java.util.List;
import pub.devrel.easypermissions.EasyPermissions;
import static com.willowtreeapps.skrej.CredentialHelper.REQUEST_PERMISSION_GET_ACCOUNTS;



public class LoginActivity extends AppCompatActivity
        implements
        LoginView,
        EasyPermissions.PermissionCallbacks,
        View.OnClickListener {

    //Tag for logging.
    private static final String TAG = "Login activity";
    private static final int CACTUAR = 100;
    private static final int  DEKU = 101;
    //The presenter for this view.
    private LoginPresenterImpl presenter;

    //A dialog that tells us we're waiting on the API.
    private ProgressDialog waitingForAPILoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Drawable cactuarIcon = ResourcesCompat.getDrawable(getResources(), R.mipmap.cactuar_icon, null);
        addRoomToList("Cactuar", cactuarIcon, CACTUAR);

        Drawable dekuIcon = ResourcesCompat.getDrawable(getResources(), R.mipmap.deku_icon, null);
        addRoomToList("Deku", dekuIcon, DEKU);

        //Set up waiting dialog.
        waitingForAPILoader = new ProgressDialog(this);
        waitingForAPILoader.setMessage("Waiting on Google.");

        //Create our presenter.
        presenter = (LoginPresenterImpl) getLastCustomNonConfigurationInstance();
        if (presenter == null) {
            Log.d(TAG, "New Presenter");
            SharedPreferences preferences = getSharedPreferences(getString(
                    R.string.credentials_preference_key), MODE_PRIVATE);
            presenter = new LoginPresenterImpl(new CredentialHelper(this, preferences));
        }
    }

    /**
     *
     * Bind / unbind to presenter on Start, Resume / Stop, Pause.
     *
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
     *
     * Pass results of activities launched here back down to our presenter. (Error, permission,
     * account select dialogs, etc.)
     *
     * @param requestCode
     * @param resultCode
     * @param data
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     *
     * Show an error dialog that will direct the user to install or update google play services.
     *
     * @param statusCode why we got here
     * @param requestCode what we need to do
     *
     */
    @Override
    public void showPlayServicesErrorDialog(int statusCode, int requestCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(this, statusCode, requestCode);
        dialog.show();
    }

    /**
     *
     * Show a general error dialog.
     *
     * @param message error message to show.
     *
     */
    @Override
    public void showErrorDialog(String message) {

    }

    /**
     *
     * Show / hide a 'Waiting on google API' dialog.
     */
    @Override
    public void showLoading() {
        waitingForAPILoader.show();
    }

    @Override
    public void hideLoading() {
        waitingForAPILoader.hide();
    }

    /**
     *
     * Show dialog to request permission to access google accounts.
     *
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

    /**
     *
     * Add a selectable room to our list of rooms.
     *
     * @param roomName the name of the room.
     * @param roomIcon an icon to display for the room.
     *
     */
    private void addRoomToList(String roomName, Drawable roomIcon, int id) {

        //Get out list layout.
        LinearLayout roomList = (LinearLayout)findViewById(R.id.room_list_view);

        //Create a room selector layout.
        View newRoom = View.inflate(this, R.layout.room_selector, null);

        //Set room icon.
        ImageView roomIconView = (ImageView)newRoom.findViewById(R.id.room_selector_image);
        roomIconView.setImageDrawable(roomIcon);

        //Set room name.
        Button roomButton = ((Button)newRoom.findViewById(R.id.room_selector_button));
        roomButton.setText(roomName);
        roomButton.setId(id);

        //Set select callback.
        roomButton.setOnClickListener(this);

        //Add room to list.
        roomList.addView(newRoom);
    }

    /**
     *
     * Callbacks for easyPermissions.
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case CACTUAR:
                Intent cactuarIntent = new Intent(this, ConferenceRoomActivity.class);
                cactuarIntent.putExtra(getString(R.string.room_id_bundle_key), "Cactuar");
                startActivity(cactuarIntent);
                break;
            case DEKU:
                Intent dekuIntent = new Intent(this, ConferenceRoomActivity.class);
                dekuIntent.putExtra(getString(R.string.room_id_bundle_key), "Deku");
                startActivity(dekuIntent);
                break;
            default:
                break;
        }
    }
}