package com.example.vedhn.multiplayercaro.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

/**
 * Created by VedHN on 11/21/2016.
 */

public class Move implements Parcelable {
    public String playerId;
    public String moveKey;
    public int row;
    public int col;
    public long timeStamp;

    public Move(PlayerInfo player, String moveKey, int col, int row) {
        this.playerId = player.getuID();
        this.moveKey = moveKey;
        this.col = col;
        this.row = row;
        this.timeStamp = System.currentTimeMillis();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.playerId);
        dest.writeString(this.moveKey);
        dest.writeInt(this.row);
        dest.writeInt(this.col);
        dest.writeLong(this.timeStamp);
    }

    protected Move(Parcel in) {
        this.playerId = in.readString();
        this.moveKey = in.readString();
        this.row = in.readInt();
        this.col = in.readInt();
        this.timeStamp = in.readLong();
    }

    public static final Parcelable.Creator<Move> CREATOR = new Parcelable.Creator<Move>() {
        @Override
        public Move createFromParcel(Parcel source) {
            return new Move(source);
        }

        @Override
        public Move[] newArray(int size) {
            return new Move[size];
        }
    };
}
