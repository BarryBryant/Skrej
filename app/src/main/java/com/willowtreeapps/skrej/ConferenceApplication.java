package com.willowtreeapps.skrej;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by barrybryant on 11/10/16.
 */

public class ConferenceApplication extends Application {

    private ApplicationComponent component;

    public static ConferenceApplication get(@NonNull Context context) {
        return (ConferenceApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
    }


    public ApplicationComponent component() {
        return component;
    }

    private ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

}
