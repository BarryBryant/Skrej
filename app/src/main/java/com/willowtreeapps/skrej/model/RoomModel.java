package com.willowtreeapps.skrej.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by barrybryant on 11/21/16.
 */

public class RoomModel implements Parcelable, Comparable<RoomModel> {

    private String roomName;
    private String roomResourceEmail;


    public RoomModel(String roomName, String roomResourceEmail) {
        this.roomName = roomName;
        this.roomResourceEmail = roomResourceEmail;
    }

    protected RoomModel(Parcel in) {
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

    public static final Creator<RoomModel> CREATOR = new Creator<RoomModel>() {
        @Override
        public RoomModel createFromParcel(Parcel in) {
            return new RoomModel(in);
        }

        @Override
        public RoomModel[] newArray(int size) {
            return new RoomModel[size];
        }
    };

    public String getRoomResourceEmail() {
        return roomResourceEmail;
    }

    public String getRoomName() {
        return roomName;
    }

    @Override
    public int compareTo(@NonNull RoomModel roomModel) {
        return this.roomName.compareTo(roomModel.getRoomName());
    }
}
