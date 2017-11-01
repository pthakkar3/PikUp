package com.pikup.ui;



import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
<<<<<<< Updated upstream
import android.util.Log;
=======
<<<<<<< HEAD
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
=======
import android.util.Log;
>>>>>>> origin/master
>>>>>>> Stashed changes
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pikup.R;
import com.pikup.model.Game;
import com.pikup.model.Sports;
import com.pikup.model.SportsLocations;
import com.pikup.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JoinListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference currentRef;
    private DatabaseReference dbref;



    ExpandableListView filterBy;
    ListView listViewGame;
    List<Game> gameList;
    String userUID;
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    Map<Integer, String> viewTohostUID;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    Spinner sportSpinner;
    Spinner locationSpinner;
    Spinner playerSpinner;
    Spinner intensitySpinner;


    List<String> sport;
    List<String> location;
    List<String> player;
    List<String> intensity;

    String spSelected;
    String loSelected;
    String plSelected;
    String inSelected;
=======
>>>>>>> origin/master
>>>>>>> Stashed changes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_list);
        mAuth = FirebaseAuth.getInstance();
        currentRef = FirebaseDatabase.getInstance().getReference("gamesList");
        listViewGame = (ListView) findViewById(R.id.listViewGame);
        userUID = mAuth.getCurrentUser().getUid();
        gameList = new ArrayList<>();

        sportSpinner = (Spinner) findViewById(R.id.sport_spinner);
        locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        playerSpinner = (Spinner) findViewById(R.id.player_spinner);
        intensitySpinner = (Spinner) findViewById(R.id.intensity_spinner);

        sport = new ArrayList<String>();
        location = new ArrayList<String>();
        player = new ArrayList<String>();
        intensity = new ArrayList<String>();

        //populating the spinners

        spSelected = "-Select Sport-";
        loSelected = "-Select Location-";
        inSelected = "-Select Intensity-";
        plSelected = "-Select Player-";


        sport.add("-Select Sport-");
        location.add("-Select Location-");
        player.add("-Select Player-");
        intensity.add("-Select Intensity-");

        player.add("Student");
        player.add("Faculty");


        intensity.add("1");
        intensity.add("2");
        intensity.add("3");
        intensity.add("4");
        intensity.add("5");


        //TODO: populate the sports and locations lists with those from firebase

        ArrayAdapter<String> sportAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sport);
        sportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sportSpinner.setAdapter(sportAdapter);

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, location);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        ArrayAdapter<String> playerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, player);
        playerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerSpinner.setAdapter(playerAdapter);


        ArrayAdapter<String> intensityAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, intensity);
        intensityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intensitySpinner.setAdapter(intensityAdapter);


        sportSpinner.setOnItemSelectedListener(this);
        intensitySpinner.setOnItemSelectedListener(this);

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
                    if ((fitsFilter(game)) && (!userUID.equals(game.getHostUID())) && (!(game.getPlayerUIDList().contains(userUID))) && game.getCapacity() > game.getPlayerUIDList().size()) {
                        gameList.add(game);
                    }

                }

                listAdapter adapter = new listAdapter(JoinListActivity.this, gameList, dataSnapshot);
                listViewGame.setAdapter(adapter);

                isGameListEmpty();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


<<<<<<< Updated upstream
=======
<<<<<<< HEAD
        });



=======
>>>>>>> origin/master
>>>>>>> Stashed changes
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(JoinListActivity.this, HomeScreenActivity.class));
        finish();
    }

    private void isGameListEmpty() {

        if (gameList.isEmpty()) {
            AlertDialog.Builder builder;

            builder = new AlertDialog.Builder(this);
            // AlertDialog alert = builder.create();
            if (!this.isFinishing()) {
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getItemAtPosition(position) instanceof String){
            String temp = (String) parent.getItemAtPosition(position);
            if (parent.getId() == sportSpinner.getId()) {
                spSelected = temp;
            }
            if (parent.getId() == locationSpinner.getId()) {
                loSelected = temp;
            }
            if (parent.getId() == playerSpinner.getId()) {
                plSelected = temp;
            }
            if (parent.getId() == intensitySpinner.getId()) {
                inSelected = temp;
            }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spSelected = "-Select Sport-";
        loSelected = "-Select Location-";
        inSelected = "-Select Intensity-";
        plSelected = "-Select Player-";
    }


    //TODO: add the logic for player type
    private boolean fitsFilter(Game g) {
        if ((g.getSport().equals(spSelected) || spSelected.equals("-Select Sport-"))
            && (g.getLocationTitle().equals(loSelected) || loSelected.equals("-Select Location-"))
            && (inSelected.equals(g.getIntensity()) || inSelected.equals("-Select Intensity-"))) {
            return true;
            //for each player in game g
            //check if player is type selected
        }
        return false;

    }
}
