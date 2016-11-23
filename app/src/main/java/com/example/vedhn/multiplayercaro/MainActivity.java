package com.example.vedhn.multiplayercaro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.vedhn.multiplayercaro.models.GameStage;
import com.example.vedhn.multiplayercaro.models.Move;
import com.example.vedhn.multiplayercaro.models.PlayerInfo;
import com.example.vedhn.multiplayercaro.views.GameScene;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainActivity extends AppCompatActivity implements GameScene.GameSceneListener {
    public static final String EXTRA_PLAYER = "player";
    private final String TAG = this.getClass().getSimpleName();
    private GameScene gameScene;
    private PlayerInfo user1;
    private PlayerInfo user2;
    private FirebaseDatabase database;
    private static List<PlayerInfo> listWaitingUsers = new ArrayList<>();
    private AlertDialog waitingDialog;
    private GameStage gameStage;

    public interface AvailableUserListener {
        void onChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameScene = (GameScene) findViewById(R.id.gameScene);

        database = FirebaseDatabase.getInstance();
        listWaitingUsers = new ArrayList<>();

        user1 = getIntent().getParcelableExtra(EXTRA_PLAYER);

        // waiting for another players
        showWaitingDialog(true);
        getListUserAvailable(new AvailableUserListener() {
            @Override
            public void onChanged() {
                // can get a player here
                if (user2 == null && listWaitingUsers.get(0) != null) {
                    user2 = listWaitingUsers.get(0);
                    // change status of user1,user2 to playing
                    HashMap<String, Object> playingMap = new HashMap<>();
                    playingMap.put("status", PlayerInfo.PLAYER_STATE_PLAYING);
                    DatabaseReference users = database.getReference("users");
                    users.child(user1.getuID()).updateChildren(playingMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Log.d(TAG, "onComplete: changed status of user1 to PLAYING");
                        }
                    });
                    users.child(user2.getuID()).updateChildren(playingMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Log.d(TAG, "onComplete: changed status of user2 to PLAYING");
                        }
                    });

                    showWaitingDialog(false);

                    startGame();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listWaitingUsers = null;

        // change state of user1 to offline
        DatabaseReference users = database.getReference("users");
        HashMap<String, Object> offlineMap = new HashMap<>();
        offlineMap.put("status", PlayerInfo.PLAYER_STATE_OFF);
        users.child(user1.getuID()).updateChildren(offlineMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Log.d(TAG, "onComplete: changed status of user1 to OFF");
            }
        });
    }

    private void getListUserAvailable(final AvailableUserListener listener) {
        DatabaseReference users = database.getReference("users");
        final Query status = users.orderByChild("status").equalTo(PlayerInfo.PLAYER_STATE_WAITING);
        status.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    PlayerInfo playerInfo = dataSnapshot.getValue(PlayerInfo.class);
                    if (!playerInfo.getuID().equals(user1.getuID())) {
                        Log.d(TAG, "onChildAdded: found a waiting user");
                        listWaitingUsers.add(playerInfo);
                        if (listener != null)
                            listener.onChanged();
                        //                        status.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    PlayerInfo playerInfo = dataSnapshot.getValue(PlayerInfo.class);
                    if (!playerInfo.getuID().equals(user1.getuID())) {
                        Log.d(TAG, "onChildAdded: found a waiting user");
                        listWaitingUsers.add(playerInfo);
                        if (listener != null)
                            listener.onChanged();
                        //                        status.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PlayerInfo playerInfo = dataSnapshot.getValue(PlayerInfo.class);
                    if (!playerInfo.getuID().equals(user1.getuID())) {
                        listWaitingUsers.remove(playerInfo);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void showWaitingDialog(boolean isShow) {
        if (waitingDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Waiting for player ...");
            builder.setCancelable(true);
            waitingDialog = builder.create();
        }

        if (!isShow && waitingDialog != null && waitingDialog.isShowing()) {
            waitingDialog.dismiss();
        } else
            waitingDialog.show();
    }

    public void startGame() {
        if (user1 != null && user2 != null) {
            // create an entry for each game
            DatabaseReference stage = database.getReference("stage");
            String key = stage.push().getKey();
            gameStage = new GameStage(user1, user2, key);
            stage.child(key).setValue(gameStage);

            gameScene.setPlayers(user1, user2, this);
            gameScene.startGame();
        }
    }

    @Override
    public void onStepFinished(PlayerInfo playerInfo, int row, int col) {
        Log.d(TAG, "onStepFinished() called with: playerInfo = [" + playerInfo + "]");
        // update moves
        DatabaseReference moves = database.getReference("stage").child(gameStage.stageID).child("moves");
        String key = moves.push().getKey();
        Move move = new Move(playerInfo, key, col, row);
        moves.child(key).setValue(move);
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
