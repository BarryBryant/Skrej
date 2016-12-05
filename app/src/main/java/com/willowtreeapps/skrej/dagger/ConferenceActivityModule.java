package com.willowtreeapps.skrej.dagger;

import android.support.annotation.NonNull;

import com.willowtreeapps.skrej.calendarApi.CalendarService;
import com.willowtreeapps.skrej.calendarApi.CredentialWizard;
import com.willowtreeapps.skrej.conference.ConferencePresenter;
import com.willowtreeapps.skrej.conference.ConferencePresenterImpl;
import com.willowtreeapps.skrej.conference.ConferenceRepository;
import com.willowtreeapps.skrej.conference.ConferenceRepositoryImpl;

import javax.annotation.Nonnull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chrisestes on 12/5/16.
 */

@Module
public class ConferenceActivityModule {

    @Provides
    @NonNull
    public CalendarService provideCalendarEventService(@NonNull CredentialWizard credentialWizard) {
        return new CalendarService(credentialWizard);
    }

    @Provides
    @NonNull
    public ConferenceRepository provideConferenceRepository(@NonNull CalendarService calendarService) {
        return new ConferenceRepositoryImpl(calendarService);
    }

    @Provides
    @NonNull
    public ConferencePresenter provideConferencePresenter(@Nonnull ConferenceRepository repository) {
        return new ConferencePresenterImpl(repository);
    }
}
