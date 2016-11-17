package com.willowtreeapps.skrej.model;

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
}
