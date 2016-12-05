package com.willowtreeapps.skrej.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.willowtreeapps.skrej.R;
import com.willowtreeapps.skrej.calendarApi.CredentialWizard;
import com.willowtreeapps.skrej.realm.RealmWizard;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chrisestes on 12/5/16.
 */

@Module
public class ServicesModule {

    @Provides
    @NonNull
    public SharedPreferences providePreferences(@NonNull Application context) {
        return context.getSharedPreferences(context.getString(R.string.credentials_preference_key), Context.MODE_PRIVATE);
    }

    @Provides
    @NonNull
    @Singleton
    public CredentialWizard provideCredentialHelper(@NonNull Application context, @NonNull SharedPreferences preferences) {
        return new CredentialWizard(context, preferences);
    }

    @Provides
    @NonNull
    @Singleton
    public RealmWizard provideRealmWizard() {
        return new RealmWizard();
    }

}
