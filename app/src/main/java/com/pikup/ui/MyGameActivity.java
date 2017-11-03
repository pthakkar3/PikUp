package com.pikup.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyGameActivity extends AppCompatActivity { // AppCompatActivity
    private FirebaseAuth mAuth;
    private DatabaseReference currentRef;

    ListView listViewGame;
    List<Game> gameList;
    Game selectedGame;
    String userUID;
    Map<Integer, String> viewTohostUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_game);
        mAuth = FirebaseAuth.getInstance();
        currentRef = FirebaseDatabase.getInstance().getReference("gamesList");
        listViewGame = (ListView) findViewById(R.id.listViewGame);
        userUID = mAuth.getCurrentUser().getUid();
        gameList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //String gameKey = dataSnapshot.get
                gameList.clear();
                for (DataSnapshot gameSnapshot : dataSnapshot.getChildren()) {
                    Game game = gameSnapshot.getValue(Game.class);
                    //Log.i(TAG, gam)
                    if (game.getPlayerUIDList().contains(userUID)) {
                        gameList.add(game);
                    }
                }
                myGameListAdapter adapter = new myGameListAdapter(MyGameActivity.this, gameList, dataSnapshot);
                listViewGame.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyGameActivity.this, HomeScreenActivity.class));
        finish();

    }
}