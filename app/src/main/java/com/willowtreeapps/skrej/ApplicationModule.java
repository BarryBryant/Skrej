package com.willowtreeapps.skrej;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.willowtreeapps.skrej.attendeeSelection.AttendeeDialogPresenter;
import com.willowtreeapps.skrej.attendeeSelection.AttendeeDialogPresenterImpl;
import com.willowtreeapps.skrej.calendarApi.CalendarEventService;
import com.willowtreeapps.skrej.calendarApi.CredentialWizard;
import com.willowtreeapps.skrej.calendarApi.RoomService;
import com.willowtreeapps.skrej.calendarApi.UserService;
import com.willowtreeapps.skrej.conference.ConferencePresenter;
import com.willowtreeapps.skrej.conference.ConferencePresenterImpl;
import com.willowtreeapps.skrej.login.LoginPresenter;
import com.willowtreeapps.skrej.login.LoginPresenterImpl;
import com.willowtreeapps.skrej.model.ConferenceRepository;
import com.willowtreeapps.skrej.model.ConferenceRepositoryImpl;
import com.willowtreeapps.skrej.model.LoginRepository;
import com.willowtreeapps.skrej.model.LoginRepositoryImpl;
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
    public UserService provideUserService(@NonNull CredentialWizard credentialWizard) {
        return new UserService(credentialWizard);
    }

    @Provides
    @NonNull
    public RoomService provideRoomService(@NonNull CredentialWizard credentialWizard) {
        return new RoomService(credentialWizard);
    }

    @Provides
    @NonNull
    public LoginRepository provideLoginRepository(@NonNull UserService userService, @NonNull RoomService roomService, @NonNull RealmWizard realmWizard) {
        return new LoginRepositoryImpl(userService, roomService, realmWizard);
    }

    @Provides
    @NonNull
    public LoginPresenter provideLoginPresenter(@NonNull CredentialWizard credentialWizard, @NonNull LoginRepository repository) {
        return new LoginPresenterImpl(credentialWizard, repository);
    }

    @Provides
    @NonNull
    public CalendarEventService provideCalendarEventService(@NonNull CredentialWizard credentialWizard) {
        return new CalendarEventService(credentialWizard);
    }

    @Provides
    @NonNull
    public ConferenceRepository provideConferenceRepository(@NonNull CalendarEventService calendarEventService) {
        return new ConferenceRepositoryImpl(calendarEventService);
    }

    @Provides
    @NonNull
    public ConferencePresenter provideConferencePresenter(@Nonnull ConferenceRepository repository) {
        return new ConferencePresenterImpl(repository);
    }

    @Provides
    @NonNull
    public AttendeeDialogPresenter providesAttendeeDialogPresenter(@NonNull RealmWizard realmWizard) {
        return new AttendeeDialogPresenterImpl(realmWizard);
    }

}
