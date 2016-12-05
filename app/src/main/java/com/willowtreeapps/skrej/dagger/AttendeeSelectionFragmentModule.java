package com.willowtreeapps.skrej.dagger;

import android.support.annotation.NonNull;

import com.willowtreeapps.skrej.attendeeSelection.AttendeeDialogPresenter;
import com.willowtreeapps.skrej.attendeeSelection.AttendeeDialogPresenterImpl;
import com.willowtreeapps.skrej.realm.RealmWizard;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chrisestes on 12/5/16.
 */

@Module
public class AttendeeSelectionFragmentModule {

    @Provides
    @NonNull
    public AttendeeDialogPresenter providesAttendeeDialogPresenter(@NonNull RealmWizard realmWizard) {
        return new AttendeeDialogPresenterImpl(realmWizard);
    }

}
