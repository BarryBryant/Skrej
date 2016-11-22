package com.willowtreeapps.skrej.login;

import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.willowtreeapps.skrej.calendarApi.CredentialWizard;
import com.willowtreeapps.skrej.model.Attendee;
import com.willowtreeapps.skrej.model.Room;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.willowtreeapps.skrej.calendarApi.CredentialWizard.REQUEST_ACCOUNT_PICKER;
import static com.willowtreeapps.skrej.calendarApi.CredentialWizard.REQUEST_AUTHORIZATION;
import static com.willowtreeapps.skrej.calendarApi.CredentialWizard.REQUEST_GOOGLE_PLAY_SERVICES;


/**
 * Created by chrisestes on 11/8/16.
 */


public class LoginPresenterImpl implements CredentialWizard.CredentialListener,
        LoginPresenter, LoginRepositoryImpl.LoginRepositoryListener {

    //Log tag.
    private static final String TAG = "LoginPresenterImpl";

    //Class to get credentials.
    private CredentialWizard credentialWizard;
    private LoginRepository repository;
    private List<Room> rooms;

    private boolean usersLoaded = false;
    private boolean roomsLoaded = false;

    //View instance.
    private LoginView view;

    public LoginPresenterImpl(CredentialWizard credentialWizard, LoginRepository repository) {
        this.credentialWizard = credentialWizard;
        this.repository = repository;
        //Register this presenter as a listener to the credential helper.
        this.credentialWizard.registerListener(this);
    }

    @Override
    public void bindView(LoginView view) {
        this.view = view;
        this.repository.registerListener(this);
        //Start running credential helper on bind view.
        if (!credentialWizard.hasValidCredential() || !usersLoaded || !roomsLoaded) {
            this.credentialWizard.getValidCredential();
            this.view.showLoading();
        } else {
            this.view.addRoomButtons(rooms);
            this.view.hideLoading();
        }

    }

    @Override
    public void unbindView() {
        this.repository.unbindListener();
        this.view = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, String name) {
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    //TODO: Show that user what the real deal is about G00glePlayServices
                    Log.d(TAG,
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    view.showLoading();
                    //retry with verified google play services
                    credentialWizard.getValidCredential();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && name != null) {
                    //retry after selecting account
                    credentialWizard.onAccountPicked(name);
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    credentialWizard.getValidCredential();
                }
                break;
        }
    }

    /**
     * We have valid credentials now and we can show the list of rooms.
     *
     * @param credential
     */
    @Override
    public void onReceiveValidCredentials(GoogleAccountCredential credential) {
        repository.getUsers();
        repository.getConferenceRooms();
    }

    @Override
    public void onUserResolvablePlayServicesError(int connectionStatusCode, int requestCode) {
        if (view != null) {
            view.showPlayServicesErrorDialog(connectionStatusCode, requestCode);
            view.hideLoading();
        }
    }

    @Override
    public void networkUnavailable() {
        if (view != null) {
            view.hideLoading();
            view.showErrorDialog("Network is unavailable");
        }
    }

    @Override
    public void requestAccountPicker() {
        if (view != null) {
            view.hideLoading();
            view.showAccountPicker(credentialWizard.getCredential().newChooseAccountIntent());
        }
    }

    @Override
    public void requestPermissions() {
        if (view != null) {
            view.hideLoading();
            view.showUserPermissionsDialog();
        }
    }


    @Override
    public void onUsersLoaded(List<Attendee> users) {
        usersLoaded = true;
        if (view != null) {
            view.hideLoading();
        }
    }

    @Override
    public void onConferenceRoomsLoaded(List<Room> rooms) {
        roomsLoaded = true;
        this.rooms = rooms;
        if (view != null) {
            view.hideLoading();
            view.addRoomButtons(rooms);
        }
    }

    @Override
    public void onError(Throwable error) {
        if (error instanceof UserRecoverableAuthIOException) {
            view.onAuthIOException((UserRecoverableAuthIOException) error);
        }
    }
}
