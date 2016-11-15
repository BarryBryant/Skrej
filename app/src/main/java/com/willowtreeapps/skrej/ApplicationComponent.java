package com.willowtreeapps.skrej;

import com.willowtreeapps.skrej.calendarapi.CalendarLoader;
import com.willowtreeapps.skrej.calendarapi.CalendarWizard;
import com.willowtreeapps.skrej.calendarapi.EventService;
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

    void inject(CalendarWizard wizard);

    void inject(CalendarLoader loader);

    void inject(EventService service);
}
