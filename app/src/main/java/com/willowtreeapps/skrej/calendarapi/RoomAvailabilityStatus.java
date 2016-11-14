package com.willowtreeapps.skrej.calendarapi;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.google.api.services.calendar.model.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

/**
 * Created by chrisestes on 11/14/16.
 */

public class RoomAvailabilityStatus

    implements
    Parcelable
{

    //Room availability start and end time.
    private long roomAvailableStartTime;


    //Number of 15 minute blocks the room is available for.
    private int availableBlocks;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<RoomAvailabilityStatus> CREATOR
        = new Parcelable.Creator<RoomAvailabilityStatus>() {

        public RoomAvailabilityStatus createFromParcel(Parcel in) {
            return new RoomAvailabilityStatus(in);
        }

        public RoomAvailabilityStatus[] newArray(int size) {
            return new RoomAvailabilityStatus[size];
        }
    };

    public RoomAvailabilityStatus(
        long roomAvailableStartTime,
        int availableBlocks
    ){
        this.roomAvailableStartTime = roomAvailableStartTime;
        this.availableBlocks = availableBlocks;
    }

    public String getRoomAvailability() {

        //Init return.
        String availability = "";

        //Return "Available" or "Unavailable".
        if(availableBlocks > 0) {
            availability = "Available";
        }

        else {
            availability = "Occupied";
        }

        return(availability);
    }

    public String getRoomAvailabilityTimeInfo() {

        //Set time format.
        SimpleDateFormat mySDF = new SimpleDateFormat("h:mm a");

        //Init return.
        String availability = "";

        //If room is available, return for how long.
        if(availableBlocks > 0) {

            availability = "Until: " + mySDF.format(roomAvailableStartTime + (availableBlocks * 15 * DateUtils.MINUTE_IN_MILLIS));
        }

        //Otherwise, return until when.
        else {
            availability = "Until: " + mySDF.format(roomAvailableStartTime);
        }

        return(availability);
    }

    private RoomAvailabilityStatus(Parcel in) {
        roomAvailableStartTime = in.readLong();
        availableBlocks = in.readInt();
    }
}
