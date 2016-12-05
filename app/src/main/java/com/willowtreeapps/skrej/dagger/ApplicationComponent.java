package com.willowtreeapps.skrej.dagger;

import com.willowtreeapps.skrej.attendeeSelection.AttendeeDialogFragment;
import com.willowtreeapps.skrej.calendarApi.EventService;
import com.willowtreeapps.skrej.conference.ConferenceRoomActivity;
import com.willowtreeapps.skrej.dagger.AppModule;
import com.willowtreeapps.skrej.dagger.AttendeeSelectionFragmentModule;
import com.willowtreeapps.skrej.dagger.ConferenceActivityModule;
import com.willowtreeapps.skrej.dagger.LoginActivityModule;
import com.willowtreeapps.skrej.dagger.ServicesModule;
import com.willowtreeapps.skrej.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by barrybryant on 11/10/16.
 */

@Singleton
@Component(
    modules = {
        AppModule.class,
        AttendeeSelectionFragmentModule.class,
        ConferenceActivityModule.class,
        LoginActivityModule.class,
        ServicesModule.class
    }
)
public interface ApplicationComponent {
    void inject(LoginActivity activity);

    void inject(ConferenceRoomActivity activity);

    void inject(EventService service);

    void inject(AttendeeDialogFragment fragment);
}
