package com.willowtreeapps.skrej;

import com.willowtreeapps.skrej.conference.ConferenceRoomActivity;
import com.willowtreeapps.skrej.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by barrybryant on 11/10/16.
 */

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(LoginActivity activity);

    void inject(ConferenceRoomActivity activity);
}
