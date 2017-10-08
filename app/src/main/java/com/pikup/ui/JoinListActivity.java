package com.pikup.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pikup.R;
import com.pikup.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class JoinListActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference currentRef;

    ListView listViewGame;
    List<Game> gameList;
    String userUID;
    Map<Integer, String> viewTohostUID;

    // TODO: desribe this variable
    private int gameListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_list);
        mAuth = FirebaseAuth.getInstance();
        currentRef = FirebaseDatabase.getInstance().getReference("gamesList");
        listViewGame = (ListView) findViewById(R.id.listViewGame);
        userUID = mAuth.getCurrentUser().getUid();
        gameList = new ArrayList<>();
        gameListState = -1;
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameList.clear();
                for(DataSnapshot gameSnapshot: dataSnapshot.getChildren()) {
                    Game game = gameSnapshot.getValue(Game.class);
                    if ((!userUID.equals(game.getHostUID())) && (!(game.getPlayerUIDList().contains(userUID))) && game.getCapacity() > game.getPlayerUIDList().size()) {
                        gameList.add(game);
                    }
                }
                listAdapter adapter = new listAdapter(JoinListActivity.this, gameList, dataSnapshot);
                listViewGame.setAdapter(adapter);

                if (gameList.isEmpty()) {
                    gameListState = 0;
                } else {
                    gameListState = 1;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        GameThread p = new GameThread();
        p.start();


    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(JoinListActivity.this, HomeScreenActivity.class));
        finish();
    }

    private void isGameListEmpty() {

        if (gameListState == 0) {
            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder(this);
            builder.setTitle("There aren't any games available right now")
                    .setMessage("Would you like to host your own?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // go host a game
                            hostGame();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // go back to home screen
                            homeScreen();
                        }
                    })
                    .setIconAttribute(android.R.attr.alertDialogIcon)
                    .show();



        }
    }

    private void hostGame() {
        startActivity(new Intent(getApplicationContext(), HostActivity.class));
        finish();
    }

    private void homeScreen() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }



    class GameThread extends Thread {

        GameThread() {
        }

        public void run() {
            isGameListEmpty();
//            try {
//            sleep(250);
//            }
            if (gameListState == 1) {
                return;
            }
        }
    }

}
