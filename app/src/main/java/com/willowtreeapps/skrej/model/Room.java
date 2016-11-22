package com.willowtreeapps.skrej.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by barrybryant on 11/21/16.
 */

public class Room implements Parcelable, Comparable<Room> {

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };
    private String roomName;
    private String roomResourceEmail;

    public Room(String roomName, String roomResourceEmail) {
        this.roomName = roomName;
        this.roomResourceEmail = roomResourceEmail;
    }

    protected Room(Parcel in) {
        roomName = in.readString();
        roomResourceEmail = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(roomName);
        dest.writeString(roomResourceEmail);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getRoomResourceEmail() {
        return roomResourceEmail;
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public int compareTo(@NonNull Room room) {
        return this.roomName.compareTo(room.getRoomName());
    }
}
