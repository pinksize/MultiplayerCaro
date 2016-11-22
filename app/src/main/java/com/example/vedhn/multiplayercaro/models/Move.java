package com.example.vedhn.multiplayercaro.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VedHN on 11/21/2016.
 */

public class Move implements Parcelable {
    public PlayerInfo player;
    public String moveKey;
    public int row;
    public int col;

    public Move(PlayerInfo player, String moveKey, int col, int row) {
        this.player = player;
        this.moveKey = moveKey;
        this.col = col;
        this.row = row;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.player, flags);
        dest.writeString(this.moveKey);
        dest.writeInt(this.row);
        dest.writeInt(this.col);
    }

    protected Move(Parcel in) {
        this.player = in.readParcelable(PlayerInfo.class.getClassLoader());
        this.moveKey = in.readString();
        this.row = in.readInt();
        this.col = in.readInt();
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
