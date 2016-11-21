package com.willowtreeapps.skrej.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by barrybryant on 11/21/16.
 */

public class UserModel implements Parcelable {

    private String userName;
    private String userId;

    public UserModel(String userName, String userId) {
        this.userName = userName;
        this.userId = userId;
    }

    protected UserModel(Parcel in) {
        userName = in.readString();
        userId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }
}
