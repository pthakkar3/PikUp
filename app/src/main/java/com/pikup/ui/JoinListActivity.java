package com.pikup.ui;



import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.util.Log;
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
import java.util.Map;


public class JoinListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference currentRef;
    private DatabaseReference mDatabase;


    private static final String TAG = "Join Activity";

    private final String sportsListURL = "sportsList/";
    private final String gamesListURL = "gamesList/";

    private boolean gamesExist;

    ExpandableListView filterBy;
    ListView listViewGame;
    List<Game> gameList;
    String userUID;
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
    List<SportsLocations> lSportsLocations;

    String spSelected;
    String loSelected;
    String plSelected;
    String inSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_list);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        currentRef = FirebaseDatabase.getInstance().getReference(gamesListURL);
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
        lSportsLocations = new ArrayList<SportsLocations>();

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

        // Add sports and locations to the spinners
        // Get Sports List and Locations List from the Database
        try {
            currentRef = mDatabase.child(sportsListURL);
            currentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshotChunk: dataSnapshot.getChildren()) {
                        JoinListActivity.this.lSportsLocations
                                .add(snapshotChunk.getValue(SportsLocations.class));
                    }

                    // This Adds all possible sports to the sport list.
                    for (SportsLocations s: lSportsLocations) {
                         sport.add(s.getGame());
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, databaseError.getMessage());
                }
            });

        } catch (NullPointerException ex) {
            Log.e(TAG, "database reference retrieved was null");
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }



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
        locationSpinner.setOnItemSelectedListener(this);
        playerSpinner.setOnItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentRef = mDatabase.child(gamesListURL);
        currentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                gameList.clear();
                // Need this to properly prompt the user when there are legit no games.
                gamesExist = dataSnapshot.exists();
                for(DataSnapshot gameSnapshot: dataSnapshot.getChildren()) {
                    Game game = gameSnapshot.getValue(Game.class);
                    if ((fitsFilter(game)) && (!userUID.equals(game.getHostUID()))
                            && (!(game.getPlayerUIDList().contains(userUID)))
                            && game.getCapacity() > game.getPlayerUIDList().size()) {
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





    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(JoinListActivity.this, HomeScreenActivity.class));
        finish();
    }

    private void isGameListEmpty() {
        if (!gamesExist) {
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
            // clear the list back to default, if a sport is selected then populate it
            location.clear();
            location.add("-Select Location-");
            if (parent.getId() == sportSpinner.getId()) {
                // If they selected a sport, then fill that spinner with a list of valid locations
                for (SportsLocations s: lSportsLocations) {
                    // Log.v(TAG, "TEMP: " + temp + " SportsLocations: " + s.toString() + " comparison: " + (s.equals(temp)));
                    if (s.equals(temp)) {
                        location.addAll(s.getLocations());
                    }
                }

                // TODO: Make this less garbage by using global variables to prevent a waste of resources
                ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, location);
                locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                locationSpinner.setAdapter(locationAdapter);

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

        // Refreshes the listView, without this call the filters don't change anything
        onStart();

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
            && (inSelected.equals(Integer.toString(g.getIntensity())) || inSelected.equals("-Select Intensity-"))) {
            return true;
            //for each player in game g
            //check if player is type selected
        }
        return false;

    }
}
