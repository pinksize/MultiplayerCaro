package com.example.vedhn.multiplayercaro.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VedHN on 11/21/2016.
 */

public class PlayerInfo implements Parcelable {
    private String email;
    private String pwd;
    private int status; // on/off
    private String trophy;
    private String uID;
    private int winNo;
    private int loseNo;

    public PlayerInfo() {
    }

    public PlayerInfo(String email, String pwd, String uID) {
        this.email = email;
        this.pwd = pwd;
        this.uID = uID;
    }

    public String getPwd() {
        return pwd;
    }

    public String getEmail() {
        return email;
    }

    public int getStatus() {
        return status;
    }

    public String getTrophy() {
        return trophy;
    }

    public String getuID() {
        return uID;
    }

    public int getWinNo() {
        return winNo;
    }

    public int getLoseNo() {
        return loseNo;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTrophy(String trophy) {
        this.trophy = trophy;
    }

    public void setWinNo(int winNo) {
        this.winNo = winNo;
    }

    public void setLoseNo(int loseNo) {
        this.loseNo = loseNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.pwd);
        dest.writeInt(this.status);
        dest.writeString(this.trophy);
        dest.writeString(this.uID);
        dest.writeInt(this.winNo);
        dest.writeInt(this.loseNo);
    }

    protected PlayerInfo(Parcel in) {
        this.email = in.readString();
        this.pwd = in.readString();
        this.status = in.readInt();
        this.trophy = in.readString();
        this.uID = in.readString();
        this.winNo = in.readInt();
        this.loseNo = in.readInt();
    }

    public static final Parcelable.Creator<PlayerInfo> CREATOR = new Parcelable.Creator<PlayerInfo>() {
        @Override
        public PlayerInfo createFromParcel(Parcel source) {
            return new PlayerInfo(source);
        }

        @Override
        public PlayerInfo[] newArray(int size) {
            return new PlayerInfo[size];
        }
    };

    @Override
    public String toString() {
        return email;
    }
}
