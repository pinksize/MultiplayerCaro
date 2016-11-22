package com.example.vedhn.multiplayercaro;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.vedhn.multiplayercaro.models.PlayerInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements GameScene.GameSceneListener {

    public static final String EXTRA_PLAYER = "player";
    private final String TAG = this.getClass().getSimpleName();
    private GameScene gameScene;
    private PlayerInfo user1;
    private PlayerInfo user2;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameScene = (GameScene) findViewById(R.id.gameScene);

        database = FirebaseDatabase.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        user1 = getIntent().getParcelableExtra(EXTRA_PLAYER);

        // waiting for another players
        DatabaseReference users = database.getReference("users");

    }

    public void startGame() {
        if (user1 != null && user2 != null) {
            gameScene.setPlayers(user1, user2, this);
            gameScene.startGame();
        }
    }

    @Override
    public void onStepFinished(PlayerInfo playerInfo) {
        Log.d(TAG, "onStepFinished() called with: playerInfo = [" + playerInfo + "]");
    }

    @Override
    public void onGameFinished(PlayerInfo winner) {
        Log.d(TAG, "onGameFinished() called with: winner = [" + winner + "]");
    }

    @Override
    public void onGameStarted(PlayerInfo player1, PlayerInfo player2) {
        Log.d(TAG, "onGameStarted() called with: player1 = [" + player1 + "], player2 = [" + player2 + "]");
    }
}
