package com.willowtreeapps.skrej.dagger;

import android.app.Application;

import javax.annotation.Nonnull;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chrisestes on 12/5/16.
 */

@Module
public class AppModule {

    @Nonnull
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return(application);
    }
}
