package com.pikup.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
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
        TextView listCapacity = (TextView) listViewItem.findViewById(R.id.listCapacity);
        RatingBar listIntensityBar = (RatingBar) listViewItem.findViewById(R.id.listIntensityBar);
        Button quitGame = (Button) listViewItem.findViewById(R.id.quitGame);

        final java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
        final java.text.DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        final Game game = gameList.get(position);
        String gameKey = "";



        cal.setTime(game.getTimeOfGame());
        listSport.setText(game.getSport());
        listTime.setText(timeFormat.format(game.getTimeOfGame()));
        listDate.setText(dateFormat.format(game.getTimeOfGame()));
        listLocation.setText(game.getLocationTitle());
        listIntensityBar.setRating(game.getIntensity());
        listCapacity.setText("Capacity: " + game.getPlayerUIDList().size() + " / " + game.getCapacity());

        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentRef = mDatabase.child("gamesList");


        for (DataSnapshot gameSnapshot : gamesList.getChildren()) {
            Game g = gameSnapshot.getValue(Game.class);
            if (g.equals(game)) {
                gameKey = gameSnapshot.getKey();
            }
        }

        final String game_key = gameKey;
        quitGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> editedList = game.getPlayerUIDList();
                String user = mAuth.getCurrentUser().getUid();
                editedList.remove(user);
                game.setPlayerUIDList((ArrayList<String>) editedList);
                if (editedList.size() == 0 || game.getHostUID().equals(user)) {
                    mDatabase.child("gamesList").child(game_key).removeValue();
                } else {
                    mDatabase.child("gamesList").child(game_key).child("playerUIDList").setValue(editedList);
                }
                String toastText = "You have successfully quited the " + game.getSport() + " game on " + game.getTimeOfGame().toString().substring(0, 10);
                Toast temp = Toast.makeText(context, toastText, Toast.LENGTH_LONG);
                temp.setGravity(Gravity.CENTER,0,0);
                temp.show();
            }
        });

        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("sport", game.getSport());
                bundle.putString("location", game.getLocationTitle());
                bundle.putString("time", timeFormat.format(game.getTimeOfGame()));
                bundle.putString("date", dateFormat.format(game.getTimeOfGame()));
                bundle.putFloat("intensity", game.getIntensity());
                bundle.putString("hostID", game.getHostUID());
                bundle.putString("gameID", game_key);
                Fragment fragment = new GameDetailFragment();
                fragment.setArguments(bundle);
                FragmentManager fm = ((Activity)context).getFragmentManager();
                fm.beginTransaction().replace(R.id.home_frame, fragment).commit();
            }
        });

        return listViewItem;
    }
}