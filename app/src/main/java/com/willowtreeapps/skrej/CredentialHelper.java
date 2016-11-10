package com.willowtreeapps.skrej;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.util.Arrays;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by barrybryant on 11/8/16.
 */

public class CredentialHelper {


    public interface CredentialListener {
        void onReceiveValidCredentials(GoogleAccountCredential credential);

        void onUserResolvablePlayServicesError(int connectionStatusCode, int requestCode);

        void networkUnavailable();

        void requestAccountPicker();

        void requestPermissions();
    }

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String TAG = "ConferencePresenterImpl";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY};

    private GoogleAccountCredential credential;
    private Context context;
    private SharedPreferences preferences;
    private CredentialListener listener;

    public CredentialHelper(Context context, SharedPreferences preferences) {
        this.context = context;
        this.preferences = preferences;
        credential = GoogleAccountCredential.usingOAuth2(
                context.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    public GoogleAccountCredential getCredential() {
        return credential;
    }

    public void registerListener(CredentialListener listener) {
        this.listener = listener;
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    public void getValidCredential() {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (!isDeviceOnline()) {
            listener.networkUnavailable();
        } else {
            listener.onReceiveValidCredentials(credential);
        }
    }

    public boolean hasValidCredential() {
        if (!isGooglePlayServicesAvailable()) {
            Log.d(TAG, "no play services");
            return false;
        } else if (credential.getSelectedAccountName() == null) {
            Log.d(TAG, "no account name");
            return false;
        } else if (!isDeviceOnline()) {
            Log.d(TAG, "device offline");
            return false;
        } else {
            Log.d(TAG, "good credentials");
            return true;
        }
    }

    public void onAccountPicked(String name) {
        if (name != null) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(PREF_ACCOUNT_NAME, name);
            editor.apply();
            credential.setSelectedAccountName(name);
            getValidCredential();
        }
    }


    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                context, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = preferences.getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                credential.setSelectedAccountName(accountName);
                getValidCredential();
            } else {
                listener.requestAccountPicker();
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            listener.requestPermissions();
        }
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            listener.onUserResolvablePlayServicesError(connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        }
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
