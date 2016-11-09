package com.willowtreeapps.skrej;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.willowtreeapps.skrej.CredentialHelper.REQUEST_ACCOUNT_PICKER;
import static com.willowtreeapps.skrej.CredentialHelper.REQUEST_AUTHORIZATION;
import static com.willowtreeapps.skrej.CredentialHelper.REQUEST_GOOGLE_PLAY_SERVICES;


/**
 * Created by chrisestes on 11/8/16.
 */


public class LoginPresenter implements CredentialHelper.CredentialListener, LoginPresenterInterface{

    //Log tag.
    private static final String TAG = "Login presenter";

    //Context.
    private Context context;

    //Class to get credentials.
    private CredentialHelper credentialHelper;

    //View instance.
    private LoginViewInterface view;

    //A click listener to detect when we select a room from the list.
    private View.OnClickListener roomClickListener;

    public LoginPresenter(Context context) {

        //Set context and credential helper
        this.context = context;

        //Create a credential helper to get permissions and google account and stuff.
        this.credentialHelper = new CredentialHelper(
            context,
            ((Activity)context).getPreferences(Context.MODE_PRIVATE)
        );

        //Register this presenter as a listener to the credential helper.
        this.credentialHelper.registerListener(this);

        //Create click listener for room list.
        createRoomClickListener();
    }


    @Override
    public void bindView(LoginViewInterface view) {
        this.view = view;
        this.view.showSpinner();

        //Start running credential helper on bind view.
        this.credentialHelper.getValidCredential();
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
                    view.showSpinner();
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

    /**
     *
     * We have valid credentials now and we can show the list of rooms.
     *
     * @param credential
     */
    @Override
    public void onReceiveValidCredentials(GoogleAccountCredential credential) {

        if(view != null) {

            //Hide our waiting dialog.
            view.hideSpinner();
            Drawable myRoomIcon;

            myRoomIcon = ResourcesCompat.getDrawable(context.getResources(), R.mipmap.cactuar_icon, null);
            view.addRoomToList("Cactuar", myRoomIcon, roomClickListener);

            myRoomIcon = ResourcesCompat.getDrawable(context.getResources(), R.mipmap.deku_icon, null);
            view.addRoomToList("Deku", myRoomIcon, roomClickListener);
        }
    }

    @Override
    public void onUserResolvablePlayServicesError(int connectionStatusCode, int requestCode) {
        if(view != null) {
            view.hideSpinner();
            view.showPlayServicesErrorDialog(connectionStatusCode, requestCode);
        }
    }

    @Override
    public void networkUnavailable() {
        if(view != null) {
            view.showErrorDialog("Network is unavailable");
        }
    }

    @Override
    public void requestAccountPicker(Intent acctPickerIntent) {
        if(view != null) {
            view.hideSpinner();
            view.startActivityForResult(
                    acctPickerIntent,
                    REQUEST_ACCOUNT_PICKER);
        }
    }

    @Override
    public void requestPermissions() {
        if(view != null) {
            view.hideSpinner();
            view.showUserPermissionsDialog();
        }
    }

    /**
     *
     * Create a listener to handle clicking on a room in the list.
     *
     */
    private void createRoomClickListener() {

        roomClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get name of selected room.
                String roomName = ((Button)v).getText().toString();
                Log.d(TAG, "Room selected: " + roomName);

                //Create activity launch intent with room name.
                Intent startConfRoomActivityIntent = new Intent(context, ConferenceRoomActivity.class);
                startConfRoomActivityIntent.putExtra("room_name", roomName);

                //Start conferenceroom activity.
                view.startActivityForResult(startConfRoomActivityIntent, 0);
            }
        };
    }

}
