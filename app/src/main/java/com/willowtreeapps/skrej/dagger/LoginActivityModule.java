package com.willowtreeapps.skrej.dagger;

import android.support.annotation.NonNull;

import com.willowtreeapps.skrej.calendarApi.CredentialWizard;
import com.willowtreeapps.skrej.calendarApi.RoomService;
import com.willowtreeapps.skrej.calendarApi.UserService;
import com.willowtreeapps.skrej.login.LoginPresenter;
import com.willowtreeapps.skrej.login.LoginPresenterImpl;
import com.willowtreeapps.skrej.login.LoginRepository;
import com.willowtreeapps.skrej.login.LoginRepositoryImpl;
import com.willowtreeapps.skrej.realm.RealmWizard;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chrisestes on 12/5/16.
 */

@Module
public class LoginActivityModule {

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
}
