package com.willowtreeapps.skrej.login;

/**
 * Created by chrisestes on 11/9/16.
 */

public interface LoginPresenter {

    void bindView(LoginView view);

    void unbindView();

    void onActivityResult(int requestCode, int resultCode, String name);

}
