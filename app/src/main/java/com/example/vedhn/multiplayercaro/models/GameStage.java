package com.example.vedhn.multiplayercaro.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VedHN on 11/21/2016.
 */

public class GameStage implements Parcelable {
    public String p1_id;
    public String p2_id;
    public String stageID;

    public GameStage() {
    }

    public GameStage(PlayerInfo player1, PlayerInfo player2, String stageID) {
        this.p1_id = player1.getuID();
        this.p2_id = player2.getuID();
        this.stageID = stageID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.p1_id);
        dest.writeString(this.p2_id);
        dest.writeString(this.stageID);
    }

    protected GameStage(Parcel in) {
        this.p1_id = in.readString();
        this.p2_id = in.readString();
        this.stageID = in.readString();
    }

    public static final Creator<GameStage> CREATOR = new Creator<GameStage>() {
        @Override
        public GameStage createFromParcel(Parcel source) {
            return new GameStage(source);
        }

        @Override
        public GameStage[] newArray(int size) {
            return new GameStage[size];
        }
    };
}
