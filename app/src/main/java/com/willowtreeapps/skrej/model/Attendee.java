package com.willowtreeapps.skrej.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barrybryant on 11/17/16.
 */

public class Attendee {


    private String name;
    private String email;
    private boolean isChecked;

    public Attendee(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static List<Attendee> filter(List<Attendee> attendees, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Attendee> filteredAttendees = new ArrayList<>();
        for (Attendee attendee : attendees) {
            final String name = attendee.getName().toLowerCase();
            if(name.contains(lowerCaseQuery)) {
                filteredAttendees.add(attendee);
            }
        }
        return filteredAttendees;
    }
}
