package com.willowtreeapps.skrej.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by barrybryant on 11/17/16.
 */

public class Attendee implements Parcelable {


    public static final Creator<Attendee> CREATOR = new Creator<Attendee>() {
        @Override
        public Attendee createFromParcel(Parcel in) {
            return new Attendee(in);
        }

        @Override
        public Attendee[] newArray(int size) {
            return new Attendee[size];
        }
    };

    private String name;
    private String email;
    private boolean isChecked;

    public Attendee(String name, String email) {
        this.name = name;
        this.email = email;
    }

    protected Attendee(Parcel in) {
        name = in.readString();
        email = in.readString();
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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
