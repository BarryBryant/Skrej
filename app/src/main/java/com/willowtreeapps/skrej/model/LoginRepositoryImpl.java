package com.willowtreeapps.skrej.model;

import android.util.Log;

import com.willowtreeapps.skrej.calendarApi.RoomService;
import com.willowtreeapps.skrej.calendarApi.UserService;
import com.willowtreeapps.skrej.realm.RealmWizard;

import java.util.Collections;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by barrybryant on 11/21/16.
 */

public class LoginRepositoryImpl implements LoginRepository {

    private final RoomService roomService;
    private final RealmWizard realmWizard;
    private LoginRepositoryListener listener;
    private UserService userService;

    private static final String TAG = "LoginRepositoryImpl";

    public LoginRepositoryImpl(UserService userService, RoomService roomService, RealmWizard realmWizard) {
        this.userService = userService;
        this.roomService = roomService;
        this.realmWizard = realmWizard;
    }

    @Override
    public void getUsers() {
        userService.getListObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        users -> onUsersLoaded(users),
                        error -> onLoadError(error)
                );
    }

    @Override
    public void getConferenceRooms() {
        roomService.getConferenceRoomsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        rooms -> onConferenceRoomsLoaded(rooms),
                        error -> onLoadError(error)
                );
    }

    @Override
    public void registerListener(LoginRepositoryListener listener) {
        this.listener = listener;
    }

    @Override
    public void unbindListener() {
        this.listener = null;
    }

    private void onUsersLoaded(List<Attendee> users) {
        if (listener != null) {
            listener.onUsersLoaded(users);
        }
        saveUsersToRealm(users);
    }

    private void onConferenceRoomsLoaded(List<RoomModel> rooms) {
        Log.d(TAG, "ROOM LOADED " + rooms.size());
        if (listener != null) {
            Collections.sort(rooms);
            listener.onConferenceRoomsLoaded(rooms);
        }
    }

    private void onLoadError(Throwable error) {
        Log.e(TAG, "ERROR", error);
        if (listener != null) {
            listener.onError(error);
        }
    }

    private void saveUsersToRealm(List<Attendee> users) {
        realmWizard.storeContacts(users);
        Log.d(TAG, "SAVIN THEM USERS" + users.size());
    }

    public interface LoginRepositoryListener {

        void onUsersLoaded(List<Attendee> users);

        void onConferenceRoomsLoaded(List<RoomModel> rooms);

        void onError(Throwable error);

    }
}
