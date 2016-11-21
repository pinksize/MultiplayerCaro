package com.example.vedhn.multiplayercaro.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by VedHN on 11/21/2016.
 */

public class GameStage {
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
    
}
