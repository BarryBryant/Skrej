package com.willowtreeapps.skrej.attendeeSelection;


import com.willowtreeapps.skrej.adapter.AttendeeCheckedListener;
import com.willowtreeapps.skrej.model.Attendee;
import com.willowtreeapps.skrej.realm.RealmWizard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barrybryant on 11/18/16.
 */

public class AttendeeDialogPresenterImpl implements AttendeeDialogPresenter, AttendeeCheckedListener {

    private AttendeeDialogView view;
    private List<Attendee> attendees;
    private List<String> selectedAttendees = new ArrayList<>();
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
    public void onAttendeeChecked(Attendee attendee) {
        if (attendee.isChecked()) {
            selectedAttendees.add(attendee.getEmail());
        } else selectedAttendees.remove(selectedAttendees.indexOf(attendee.getEmail()));
    }
}
