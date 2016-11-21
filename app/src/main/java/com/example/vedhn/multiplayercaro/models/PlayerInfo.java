package com.example.vedhn.multiplayercaro.models;

/**
 * Created by VedHN on 11/21/2016.
 */

public class PlayerInfo {
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
}
