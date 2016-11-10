package com.willowtreeapps.skrej.login;

import android.content.Intent;

/**
 * Created by chrisestes on 11/9/16.
 */

public interface LoginPresenter {

    void bindView(LoginView view);
    void unbindView();
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
