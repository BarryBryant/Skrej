package com.willowtreeapps.skrej;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.willowtreeapps.skrej.calendarapi.CredentialHelper;
import com.willowtreeapps.skrej.conference.ConferencePresenter;
import com.willowtreeapps.skrej.conference.ConferencePresenterImpl;
import com.willowtreeapps.skrej.login.LoginPresenter;
import com.willowtreeapps.skrej.login.LoginPresenterImpl;

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
    public CredentialHelper provideCredentialHelper(@NonNull Context context, @NonNull SharedPreferences preferences) {
        return new CredentialHelper(context, preferences);
    }

    @Provides
    @NonNull
    public LoginPresenter provideLoginPresenter(@NonNull CredentialHelper credentialHelper) {
        return new LoginPresenterImpl(credentialHelper);
    }



    @Provides
    @NonNull
    public ConferencePresenter provideConferencePresenter() {
        return new ConferencePresenterImpl();
    }

}
