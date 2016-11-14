package com.willowtreeapps.skrej.login;

import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.willowtreeapps.skrej.calendarapi.CredentialHelper;

import static android.app.Activity.RESULT_OK;
import static com.willowtreeapps.skrej.calendarapi.CredentialHelper.REQUEST_ACCOUNT_PICKER;
import static com.willowtreeapps.skrej.calendarapi.CredentialHelper.REQUEST_AUTHORIZATION;
import static com.willowtreeapps.skrej.calendarapi.CredentialHelper.REQUEST_GOOGLE_PLAY_SERVICES;


/**
 * Created by chrisestes on 11/8/16.
 */


public class LoginPresenterImpl implements CredentialHelper.CredentialListener, LoginPresenter {

    //Log tag.
    private static final String TAG = "Login presenter";

    //Class to get credentials.
    private CredentialHelper credentialHelper;

    //View instance.
    private LoginView view;


    public LoginPresenterImpl(CredentialHelper credentialHelper) {
        this.credentialHelper = credentialHelper;
        //Register this presenter as a listener to the credential helper.
        this.credentialHelper.registerListener(this);
    }


    @Override
    public void bindView(LoginView view) {
        this.view = view;
        this.view.showLoading();
        //Start running credential helper on bind view.
        this.credentialHelper.getValidCredential();
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
     * We have valid credentials now and we can show the list of rooms.
     *
     * @param credential
     */
    @Override
    public void onReceiveValidCredentials(GoogleAccountCredential credential) {
        if (view != null) {
            //Hide our waiting dialog.
            view.hideLoading();
        }
    }

    @Override
    public void onUserResolvablePlayServicesError(int connectionStatusCode, int requestCode) {
        if (view != null) {
            view.hideLoading();
            view.showPlayServicesErrorDialog(connectionStatusCode, requestCode);
        }
    }

    @Override
    public void networkUnavailable() {
        if (view != null) {
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
