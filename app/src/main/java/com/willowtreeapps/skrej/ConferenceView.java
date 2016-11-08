package com.willowtreeapps.skrej;

import android.content.Intent;

/**
 * Created by barrybryant on 11/7/16.
 */

public interface ConferenceView {
    void startActivityForResult(Intent intent, int requestCode);
    void showPlayServicesErrorDialog(int statusCode);
    void showErrorDialog(String message);
    void showSpinner();
    void hideSpinner();
    void updateAvailability();
    void updateDate();
    void enableScheduleButton();
    void disableScheduleButton();
    void onVerifiedValidCredentials();
    void showUserPermissionsDialog();
}
