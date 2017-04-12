package com.ipl.model;


import android.os.Parcel;
import android.os.Parcelable;

public class UserDetailsModel {
    private String userName;
    private String userEmail;
    private String userNickName;
    private String userProfileImgUrl;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserProfileImgUrl() {
        return userProfileImgUrl;
    }

    public void setUserProfileImgUrl(String userProfileImgUrl) {
        this.userProfileImgUrl = userProfileImgUrl;
    }
}
