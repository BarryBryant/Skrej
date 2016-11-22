package com.willowtreeapps.skrej.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;

/**
 * Created by chrisestes on 11/14/16.
 */

public class RoomAvailabilityStatus implements Parcelable {

    public static final Parcelable.Creator<RoomAvailabilityStatus> CREATOR =
            new Parcelable.Creator<RoomAvailabilityStatus>() {

                public RoomAvailabilityStatus createFromParcel(Parcel in) {
                    return new RoomAvailabilityStatus(in);
                }

                public RoomAvailabilityStatus[] newArray(int size) {
                    return new RoomAvailabilityStatus[size];
                }
            };
    private long eventStart;
    private long eventEnd;
    private int availableBlocks; //Number of 15 minute blocks the room is available for.
    private String eventTitle;

    public RoomAvailabilityStatus(long eventStart, long eventEnd, int availableBlocks, String eventTitle) {
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventTitle = eventTitle;
        this.availableBlocks = availableBlocks;
    }

    private RoomAvailabilityStatus(Parcel in) {
        eventStart = in.readLong();
        eventEnd = in.readLong();
        availableBlocks = in.readInt();
        eventTitle = in.readString();
    }

    public String getRoomAvailability() {
        //Return "Available" or "Event title".
        if (availableBlocks > 0) {
            return "Available";
        } else {
            return eventTitle;
        }
    }

    public String getRoomAvailabilityTimeInfo() {
        //Set time format.
        SimpleDateFormat mySDF = new SimpleDateFormat("h:mm a");

        //Init return.
        String availability;

        //If room is available, return for how long.
        if (availableBlocks > 48) {
            availability = "All Day";
        } else if (availableBlocks > 0) {
            availability = "Until: " + mySDF.format(eventStart);
        } else { //Otherwise, return until when.
            availability = mySDF.format(eventStart) + " - " + mySDF.format(eventEnd);
        }
        return availability;
    }

    public int getAvailableBlocks() {
        return (availableBlocks > 4) ? 4 : availableBlocks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
