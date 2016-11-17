package com.willowtreeapps.skrej.login;

import com.google.api.services.admin.directory.model.User;

import java.util.List;

/**
 * Created by chrisestes on 11/9/16.
 */

public interface LoginPresenter {

    void bindView(LoginView view);

    void unbindView();

    void onActivityResult(int requestCode, int resultCode, String name);

    void onContactsLoaded(List<User> contacts);
}
