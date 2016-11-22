package com.willowtreeapps.skrej.attendeeSelection;


import android.os.Bundle;

import com.willowtreeapps.skrej.adapter.AttendeeAdapter;
import com.willowtreeapps.skrej.model.Attendee;
import com.willowtreeapps.skrej.realm.RealmWizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by barrybryant on 11/18/16.
 */

public class AttendeeDialogPresenterImpl implements AttendeeDialogPresenter, AttendeeAdapter.AttendeeCheckedListener {

    private static final String ATTENDEES_KEY = "ATTENDEES_BUNDLE_KEY";
    private static final String SELECTED_ATTENDEES_KEY = "SELECTED_ATTENDEES_BUNDLE_KEY";
    private AttendeeDialogView view;
    private List<Attendee> attendees;
    private ArrayList<String> selectedAttendees = new ArrayList<>();
    private RealmWizard realmWizard;

    public AttendeeDialogPresenterImpl(RealmWizard realmWizard) {
        this.realmWizard = realmWizard;
    }

    @Override
    public void bindView(AttendeeDialogView view) {
        this.view = view;
        if (attendees == null) {
            realmWizard = new RealmWizard();
            attendees = realmWizard.getStoredContacts();
        }
        view.initializeAttendeeList(attendees);
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    @Override
    public void onSearchTextChanged(String searchText) {
        if (attendees != null && view != null) {
            view.updateAttendees(searchText);
        }
    }

    @Override
    public void onClickDone() {
        if (view != null) {
            view.dismissAndReturnSelectedAttendees(selectedAttendees);
        }
    }

    @Override
    public void restoreInstanceState(Bundle bundle) {
        if (bundle.containsKey(ATTENDEES_KEY) && bundle.getParcelableArray(ATTENDEES_KEY) != null) {
            attendees = Arrays.asList((Attendee[]) bundle.getParcelableArray(ATTENDEES_KEY));
        }

        if (bundle.containsKey(SELECTED_ATTENDEES_KEY) && bundle.getStringArrayList(SELECTED_ATTENDEES_KEY) != null) {
            selectedAttendees = bundle.getStringArrayList(SELECTED_ATTENDEES_KEY);
        }
    }

    @Override
    public void saveInstanceState(Bundle bundle) {
        Attendee[] parcelableAttendees = new Attendee[this.attendees.size()];
        parcelableAttendees = this.attendees.toArray(parcelableAttendees);
        bundle.putParcelableArray(ATTENDEES_KEY, parcelableAttendees);

        bundle.putStringArrayList(SELECTED_ATTENDEES_KEY, selectedAttendees);
    }

    @Override
    public void onAttendeeChecked(Attendee attendee) {
        if (attendee.isChecked()) {
            selectedAttendees.add(attendee.getEmail());
        } else selectedAttendees.remove(selectedAttendees.indexOf(attendee.getEmail()));
    }
}
