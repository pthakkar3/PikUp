package com.pikup.ui;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pikup.R;
import com.pikup.model.Game;
import com.pikup.model.User;

import java.util.ArrayList;
import java.util.List;



public class myGameListAdapter extends ArrayAdapter<Game> {
    static java.util.Calendar cal = java.util.Calendar.getInstance();
    private Activity context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference currentRef;
    private DataSnapshot gamesList;
    private List<Game> gameList;
    //private String gameKey;
    //private Game game;


    public myGameListAdapter(Activity context, List<Game> gameList, DataSnapshot gamesList) {
        super(context, R.layout.join_game_list_layout, gameList);
        this.context = context;
        this.gameList = gameList;
        this.gamesList = gamesList;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        mAuth = FirebaseAuth.getInstance();
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.my_games_list_layout, null, true);
        TextView listSport = (TextView) listViewItem.findViewById(R.id.listSport);
        TextView listLocation = (TextView) listViewItem.findViewById(R.id.listLocation);
        TextView listTime = (TextView) listViewItem.findViewById(R.id.listTime);
        TextView listDate = (TextView) listViewItem.findViewById(R.id.listDate);
        final TextView hostName = (TextView) listViewItem.findViewById(R.id.myGameHostName);
        final TextView hostNumber = (TextView) listViewItem.findViewById(R.id.myGameHostNumber);

        RatingBar listIntensityBar = (RatingBar) listViewItem.findViewById(R.id.listIntensityBar);
        Button quitGame = (Button) listViewItem.findViewById(R.id.quitGame);

        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        java.text.DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        final Game game = gameList.get(position);
        String gameKey = "";



        cal.setTime(game.getTimeOfGame());
        listSport.setText(game.getSport());
        listTime.setText(timeFormat.format(game.getTimeOfGame()));
        listDate.setText(dateFormat.format(game.getTimeOfGame()));
        listLocation.setText(game.getLocationTitle());
        listIntensityBar.setRating(game.getIntensity());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentRef = mDatabase.child("gamesList");

        DatabaseReference hostRef = mDatabase.child("userList").child(game.getHostUID());
        hostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User hostUser = dataSnapshot.getValue(User.class);
                hostName.setText("Host: " + hostUser.getDisplayName());
                hostNumber.setText("Contact Number: " + hostUser.getPhoneNumber().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //final String game_key = mDatabase.child("gamesList").push().getKey();
        //currentRef.child(gameKey).removeValue();
        //System.out.print("Game selected: " + gameKey);


        // TODO: Fix all the potato code (sorry)
        for (DataSnapshot gameSnapshot : gamesList.getChildren()) {
            Game g = gameSnapshot.getValue(Game.class);
            if (g.equals(game)) {
                gameKey = gameSnapshot.getKey();
                //Log.i("JOIN - JoinGameListAdapter", "the games were equal");

            }
        }

        final String game_key = gameKey;
        quitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> editedList = game.getPlayerUIDList();
                editedList.remove(mAuth.getCurrentUser().getUid());
                game.setPlayerUIDList((ArrayList<String>) editedList);
                mDatabase.child("gamesList").child(game_key).child("playerUIDList").setValue(editedList);
                String toastText = "You have successfully quited the " + game.getSport() + " game on " + game.getTimeOfGame().toString().substring(0, 10);
                Toast temp = Toast.makeText(context, toastText, Toast.LENGTH_LONG);
                temp.setGravity(Gravity.CENTER,0,0);
                temp.show();
            }
        });

        return listViewItem;
    }
}