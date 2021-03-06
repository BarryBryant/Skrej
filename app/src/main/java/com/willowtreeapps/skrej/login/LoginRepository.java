package com.willowtreeapps.skrej.login;

/**
 * Created by barrybryant on 11/21/16.
 */

public interface LoginRepository {

    void getUsers();

    void getConferenceRooms();

    void registerListener(LoginRepositoryImpl.LoginRepositoryListener listener);

    void unbindListener();

}
