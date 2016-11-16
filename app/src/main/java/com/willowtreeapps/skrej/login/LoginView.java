package com.willowtreeapps.skrej.login;

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
}
