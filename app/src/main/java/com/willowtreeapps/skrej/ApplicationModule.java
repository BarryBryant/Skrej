package com.willowtreeapps.skrej;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.willowtreeapps.skrej.attendeeSelection.AttendeeDialogPresenter;
import com.willowtreeapps.skrej.attendeeSelection.AttendeeDialogPresenterImpl;
import com.willowtreeapps.skrej.calendarApi.CalendarWizard;
import com.willowtreeapps.skrej.calendarApi.CredentialWizard;
import com.willowtreeapps.skrej.conference.ConferencePresenter;
import com.willowtreeapps.skrej.conference.ConferencePresenterImpl;
import com.willowtreeapps.skrej.login.LoginPresenter;
import com.willowtreeapps.skrej.login.LoginPresenterImpl;
import com.willowtreeapps.skrej.realm.RealmWizard;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by barrybryant on 11/10/16.
 */

@Module
class ApplicationModule {

    @NonNull
    private final Application application;

    public ApplicationModule(@NonNull Application application) {
        this.application = application;
    }

    @Provides
    @NonNull
    @Singleton
    public Application provideApplication() {
        return this.application;
    }

    @Provides
    @NonNull
    @Singleton
    public Context provideContext(@NonNull Application application) {
        return application;
    }

    @Provides
    @NonNull
    public SharedPreferences providePreferences(@NonNull Context context) {
        return context.getSharedPreferences(context.getString(R.string.credentials_preference_key), Context.MODE_PRIVATE);
    }

    @Provides
    @NonNull
    @Singleton
    public CredentialWizard provideCredentialHelper(@NonNull Context context, @NonNull SharedPreferences preferences) {
        return new CredentialWizard(context, preferences);
    }

    @Provides
    @NonNull
    @Singleton
    public RealmWizard provideRealmWizard() {
        return new RealmWizard();
    }

    @Provides
    @NonNull
    public LoginPresenter provideLoginPresenter(@NonNull CredentialWizard credentialWizard, @NonNull RealmWizard realmWizard) {
        return new LoginPresenterImpl(credentialWizard, realmWizard);
    }


    @Provides
    @Nonnull
    public CalendarWizard provideCalendarServiceWizard() {
        return new CalendarWizard();
    }


    @Provides
    @NonNull
    public ConferencePresenter provideConferencePresenter(@Nonnull CalendarWizard wizard) {
        return new ConferencePresenterImpl(wizard);
    }

    @Provides
    @NonNull
    public AttendeeDialogPresenter providesAttendeeDialogPresenter(@NonNull RealmWizard realmWizard) {
        return new AttendeeDialogPresenterImpl(realmWizard);
    }

}
