package com.willowtreeapps.skrej.login;

import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.admin.directory.model.User;
import com.willowtreeapps.skrej.calendarapi.CredentialWizard;
import com.willowtreeapps.skrej.realm.RealmWizard;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.willowtreeapps.skrej.calendarapi.CredentialWizard.REQUEST_ACCOUNT_PICKER;
import static com.willowtreeapps.skrej.calendarapi.CredentialWizard.REQUEST_AUTHORIZATION;
import static com.willowtreeapps.skrej.calendarapi.CredentialWizard.REQUEST_GOOGLE_PLAY_SERVICES;


/**
 * Created by chrisestes on 11/8/16.
 */


public class LoginPresenterImpl implements CredentialWizard.CredentialListener, LoginPresenter {

    //Log tag.
    private static final String TAG = "Login presenter";

    //Class to get credentials.
    private CredentialWizard credentialWizard;
    private RealmWizard realmWizard;

    private boolean contactsLoaded = false;

    //View instance.
    private LoginView view;


    public LoginPresenterImpl(CredentialWizard credentialWizard, RealmWizard realmWizard) {
        this.credentialWizard = credentialWizard;
        this.realmWizard = realmWizard;
        //Register this presenter as a listener to the credential helper.
        this.credentialWizard.registerListener(this);
    }


    @Override
    public void bindView(LoginView view) {
        this.view = view;
        //Start running credential helper on bind view.
        if (!credentialWizard.hasValidCredential() || !contactsLoaded) {
            this.credentialWizard.getValidCredential();
            this.view.showLoading();
            this.view.disableRoomButtons();
        } else {
            this.view.hideLoading();
            this.view.enableRoomButtons();
        }
    }

    @Override
    public void unbindView() {
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

    @Override
    public void onContactsLoaded(List<User> contacts) {
        contactsLoaded = true;
        view.hideLoading();
        view.enableRoomButtons();
        realmWizard.storeContacts(contacts);
        Log.d(TAG, "******CONTACTSLOADED*********" + contacts.size());
    }

    /**
     * We have valid credentials now and we can show the list of rooms.
     *
     * @param credential
     */
    @Override
    public void onReceiveValidCredentials(GoogleAccountCredential credential) {
        if (view != null) {
            //Hide our waiting dialog.
            view.onReceiveValidCredentials();
        }
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
            view.showAccountPicker();
        }
    }

    @Override
    public void requestPermissions() {
        if (view != null) {
            view.hideLoading();
            view.showUserPermissionsDialog();
        }
    }


}
