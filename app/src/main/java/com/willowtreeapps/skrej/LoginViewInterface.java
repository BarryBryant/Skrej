package com.willowtreeapps.skrej;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by chrisestes on 11/9/16.
 */

public interface LoginViewInterface {

    void startActivityForResult(Intent intent, int requestCode);
    void showPlayServicesErrorDialog(int statusCode, int requestCode);
    void showErrorDialog(String message);
    void showSpinner();
    void hideSpinner();
    void showUserPermissionsDialog();
    void addRoomToList(String roomName, Drawable roomIcon, View.OnClickListener onClick);
}
