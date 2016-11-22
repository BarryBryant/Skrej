package com.willowtreeapps.skrej.login;

import android.content.Intent;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.willowtreeapps.skrej.model.RoomModel;

import java.util.List;

/**
 * Created by chrisestes on 11/9/16.
 */

public interface LoginView {

    void showPlayServicesErrorDialog(int statusCode, int requestCode);

    void showErrorDialog(String message);

    void showLoading();

    void hideLoading();

    void showUserPermissionsDialog();

    void showAccountPicker(Intent intent);

    void addRoomButtons(List<RoomModel> rooms);

    void onAuthIOException(UserRecoverableAuthIOException exception);

}
