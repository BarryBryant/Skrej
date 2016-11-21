package com.willowtreeapps.skrej.calendarApi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;
import com.willowtreeapps.skrej.util.EricRichardsonLoader;

import java.io.IOException;
import java.util.List;

/**
 * Created by barrybryant on 11/8/16.
 */

public class ContactsLoader extends EricRichardsonLoader<List<User>> {

    //Log tag.
    private static final String TAG = CalendarLoader.class.getSimpleName();
    //Authorization request listener instance.
    private CredentialAuthRequestListener listener;
    //Calendar service object.z
    private Directory service;

    public ContactsLoader(Context context,
                          Directory service,
                          CredentialAuthRequestListener authRequestListener) {
        super(context);
        this.service = service;
        this.listener = authRequestListener;
    }

    @Override
    public List<User> loadInBackground() {
        try {
            return getDataFromApi();
        } catch (UserRecoverableAuthIOException e) {
            listener.onRequestAuth(e.getIntent());
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        return null;
    }

    private List<User> getDataFromApi() throws IOException {
        //Get no more than ten events between now and midnight tonight, ordered by start time.
        Users result = service.users().list()
                .setMaxResults(500)
                .setCustomer("my_customer")
                .setOrderBy("email")
                .setViewType("domain_public")
                .execute();
        List<User> users = result.getUsers();
        Log.d(TAG, users.size() + "");
        if (users != null) {
            for (User user : users) {
            }
        }

        //Return event list.
        return users;
    }

    //Interface defines a callback to request user authorization if our API query fails.
    public interface CredentialAuthRequestListener {
        void onRequestAuth(Intent intent);
    }
}
