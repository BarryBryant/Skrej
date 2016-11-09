package com.willowtreeapps.skrej;

import android.content.Intent;

/**
 * Created by chrisestes on 11/9/16.
 */

public interface LoginPresenterInterface {

    void bindView(LoginViewInterface view);
    void unbindView();
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
