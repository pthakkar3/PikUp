package com.pikup.ui;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;
import com.pikup.R;
import com.pikup.model.Game;

import java.util.ArrayList;
import java.util.List;



public class listAdapter extends ArrayAdapter<Game> {
    static java.util.Calendar cal = java.util.Calendar.getInstance();
    private Activity context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference currentRef;
    private DataSnapshot gamesList;
    private List<Game> gameList;
    private String gameKey;





    public listAdapter(Activity context, List<Game> gameList, DataSnapshot gamesList) {
        super(context, R.layout.activity_list_layout, gameList);
        this.context = context;
        this.gameList = gameList;
        this.gamesList = gamesList;
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        mAuth = FirebaseAuth.getInstance();
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.activity_list_layout, null, true);
        TextView listSport = (TextView) listViewItem.findViewById(R.id.listSport);
        TextView listLocation = (TextView) listViewItem.findViewById(R.id.listLocation);
        TextView listTime = (TextView) listViewItem.findViewById(R.id.listTime);
        TextView listDate = (TextView) listViewItem.findViewById(R.id.listDate);

        RatingBar listIntensityBar = (RatingBar) listViewItem.findViewById(R.id.listIntensityBar);
        Button joinGame = (Button) listViewItem.findViewById(R.id.joinGame);

        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        java.text.DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        final Game game = gameList.get(position);

        cal.setTime(game.getTimeOfGame());
        listSport.setText(game.getSport());
        listTime.setText(timeFormat.format(game.getTimeOfGame()));
        listDate.setText(dateFormat.format(game.getTimeOfGame()));
        listLocation.setText(game.getLocationTitle());
        listIntensityBar.setRating(game.getIntensity());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentRef = mDatabase.child("gamesList");

        final String game_key = mDatabase.child("gamesList").push().getKey();
        //currentRef.child(gameKey).removeValue();
        //System.out.print("Game selected: " + gameKey);


        // TODO: Fix all the potato code (sorry)
        for(DataSnapshot gameSnapshot: gamesList.getChildren()) {
            Game g = gameSnapshot.getValue(Game.class);
            if (g.equals(game)) {
                gameKey = gameSnapshot.getKey();
                //Log.i("JOIN - listAdapter", "the games were equal");

            }
        }

        //Log.i("JOIN - listAdapter", "game Key: " + gameKey);
        //currentRef.child(gameKey).removeValue();
        //System.out.print(game_key);
        joinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> editedList = game.getPlayerUIDList();
                editedList.add(mAuth.getCurrentUser().getUid());
                game.setPlayerUIDList((ArrayList<String>) editedList);
                mDatabase.child("gamesList").child(gameKey).child("playerUIDList").setValue(editedList);
                //currentRef.push().setValue(game);
            }
        });

        return listViewItem;
    }

//    public void joinExistedGame(View view) {
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        currentRef = mDatabase.child(gamesListURL);
//        mDatabase.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Game game = dataSnapshot.getValue(Game.class);
//                List<String> editedList = game.getPlayerUIDList();
//                editedList.add(mAuth.getCurrentUser().getUid());
//                currentRef.setValue(editedList);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
}