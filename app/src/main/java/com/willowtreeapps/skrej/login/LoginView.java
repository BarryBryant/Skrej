package com.willowtreeapps.skrej.login;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.willowtreeapps.skrej.calendarapi.CredentialHelper;

/**
 * Created by chrisestes on 11/9/16.
 */

public interface LoginView {

    void showPlayServicesErrorDialog(int statusCode, int requestCode);

    void showErrorDialog(String message);

    void showLoading();

    void hideLoading();

    void showUserPermissionsDialog();

    void showAccountPicker();

    void onReceiveValidCredentials();

    void disableRoomButtons();

    void enableRoomButtons();
}
