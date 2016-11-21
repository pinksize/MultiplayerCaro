package com.example.vedhn.multiplayercaro.models;

/**
 * Created by VedHN on 11/21/2016.
 */

public class Move {
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

}
