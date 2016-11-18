package com.willowtreeapps.skrej.attendeeSelection;

import com.willowtreeapps.skrej.model.Attendee;

import java.util.List;

/**
 * Created by barrybryant on 11/18/16.
 */

public interface AttendeeDialogView {

    void initializeAttendeeList(List<Attendee> attendees);

    void updateAttendees(String filter);

    void dismissAndReturnSelectedAttendees(List<String> attendees);
}
