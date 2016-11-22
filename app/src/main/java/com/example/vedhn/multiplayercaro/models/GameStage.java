package com.example.vedhn.multiplayercaro.models;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by VedHN on 11/21/2016.
 */

public class GameStage implements Parcelable {
    private PlayerInfo player1;
    private PlayerInfo player2;
    private String stageID;
    private List<Move> moves;

    public GameStage(PlayerInfo player1, PlayerInfo player2, String stageID) {
        this.player1 = player1;
        this.player2 = player2;
        this.stageID = stageID;

        moves = new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.player1, flags);
        dest.writeParcelable(this.player2, flags);
        dest.writeString(this.stageID);
        dest.writeTypedList(this.moves);
    }

    protected GameStage(Parcel in) {
        this.player1 = in.readParcelable(PlayerInfo.class.getClassLoader());
        this.player2 = in.readParcelable(PlayerInfo.class.getClassLoader());
        this.stageID = in.readString();
        this.moves = in.createTypedArrayList(Move.CREATOR);
    }

    public static final Parcelable.Creator<GameStage> CREATOR = new Parcelable.Creator<GameStage>() {
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
