package com.pikup.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference currentRef;
    private static final String TAG = "Game";

    private static List<String> sportsList;
    private static List<SportsLocations> sportsLocationsList;
    private List<Game> gamesList;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private Spinner locationSpinner;
    private Spinner sportSpinner;
    private String sportSelected;
    private String locationSelected;
    static java.util.Calendar cal = java.util.Calendar.getInstance();

    private final String sportsListURL = "sportsList/";
    private final String locationListURL = "sportsList/locations/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

        gamesList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //timePicker = (TimePicker) findViewById(R.id.timePicker);
        //datePicker = (DatePicker) findViewById(R.id.datePicker);


        sportsLocationsList = new ArrayList<>();
        sportsList = new ArrayList<>();

        locationSpinner = (Spinner) findViewById(R.id.locationSpinner);

        // TODO: Remove temp code
        sportsList.add("Basketball");
        sportsList.add("Football");
        sportsList.add("Soccer");
        sportsList.add("Volleyball");
        sportsList.add("Ultimate Frisbee");
        sportsList.add("Tennis");
        // =========================== Pranshav Help
        List<String> lBasketball = new ArrayList<>();
        List<String> lFootball = new ArrayList<>();
        List<String> lSoccer = new ArrayList<>();
        List<String> lVolleyball = new ArrayList<>();
        List<String> lFrisbee = new ArrayList<>();
        List<String> lTennis = new ArrayList<>();

        lBasketball.add("CRC 4th floor Courts");
        lBasketball.add("North Avenue Gym");
        lBasketball.add("Peters Parking Deck");

        lFootball.add("CRC Fields");
        lFootball.add("Burger Bowl");
        lFootball.add("Tech Green");

        lSoccer.add("CRC Fields");
        lSoccer.add("Burger Bowl");

        lVolleyball.add("North Ave Courtyard");
        lVolleyball.add("CRC Fields");

        lFrisbee.add("CRC Fields");
        lFrisbee.add("Tech Green");

        lTennis.add("Peters Parking Deck");

        sportsLocationsList.add(new SportsLocations("Basketball", lBasketball));
        sportsLocationsList.add(new SportsLocations("Football", lFootball));
        sportsLocationsList.add(new SportsLocations("Soccer", lSoccer));
        sportsLocationsList.add(new SportsLocations("Volleyball", lVolleyball));
        sportsLocationsList.add(new SportsLocations("Ultimate Frisbee", lFrisbee));
        sportsLocationsList.add(new SportsLocations("Tennis", lTennis));
        // =========================== Please contact me at 867-5309


        // Populate the sports and location spinners from the database
        // Create a database reference to the gameList folder

        // TODO: Get Sports List and Locations List from the Database
        try {
            /*
            currentRef = mDatabase.child(sportsListURL);

            currentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Sports listOfSports = dataSnapshot.getValue(Sports.class);
                    try { // why is my IDE always yelling at me????
                        sportsList = listOfSports.getSports();
                        //for (String s: sportsList) {
                        //    Log.i(TAG, s);
                        //}
                    } catch (NullPointerException ex) {
                        Log.e(TAG, "List of sports retrieved from the database was null "
                                + ex.toString());
                    }
                    for (String sport: sportsList) {
                        Log.i(TAG, locationListURL + sport);
                        DatabaseReference sportRef = mDatabase.child(locationListURL + sport);

                        sportRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                SportsLocations a = dataSnapshot.getValue(SportsLocations.class);
                                Log.i(TAG, a.toString());
                                for (String s: a.getLocations()) {
                                    Log.i(TAG, s);
                                }
                                sportsLocationsList.add(a);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            }); */

            // Populate the Sports dropdown with the Sports pulled from the database
            sportSpinner = (Spinner) findViewById(R.id.sportSpinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, sportsList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sportSpinner.setAdapter(adapter);

            // adds listener so that on ItemSelected is called
            sportSpinner.setOnItemSelectedListener(this);
            locationSpinner.setOnItemSelectedListener(this);

            // Populate the Locations on condition of what sport is selected.

        } catch (NullPointerException ex) {
            Log.e(TAG, "user retrieved form database was null");
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }

    }



    public void hostNewGame(View view) {
        // gameRef.setValue()
        Log.i(TAG, sportSelected);
        Log.i(TAG, locationSelected);

        // Game newGame = new Game("sport", Date, "locationTitle", lat, longi, capacity, intentisty, status, hostUID, playerList)
        Game newGame = new Game();
        newGame.setHostUID(mAuth.getCurrentUser().getUid());
        newGame.setSport(sportSelected);
        newGame.setLocationTitle(locationSelected);
        // storing the date from Calendar object
        // MONTH IS STORED FROM 0-11, ADD 1 WHEN CALLED FROM DATABASE
        newGame.setTimeOfGame(cal.getTime());

        currentRef = mDatabase.child("gamesList");
        currentRef.push().setValue(newGame);

        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();

    }

    public void cancelGame(View view) {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }


    // These two methods are required for OnItemSelectedListener
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        Log.i(TAG, "OnSelectedCalled");
        if (parent.getItemAtPosition(pos) instanceof String
                && parent.getId() == sportSpinner.getId()) {
            String currentItem = (String) parent.getItemAtPosition(pos);
            Log.i(TAG, (String) parent.getItemAtPosition(pos));
            sportSelected = currentItem;
            for (SportsLocations s: sportsLocationsList) {
                // SportsLocation equals has been made to accept Strings and SportsLocations
                if (s.equals(currentItem)) {
                    // Populate spinner
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            this, android.R.layout.simple_spinner_item, s.getLocations());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    locationSpinner.setAdapter(adapter);
                    locationSelected = (String) locationSpinner.getSelectedItem();
                }
            }
        }

        if (parent.getItemAtPosition(pos) instanceof String
                && parent.getId() == locationSpinner.getId()) {
            locationSelected = (String) parent.getItemAtPosition(pos);
        }


    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            final java.util.Calendar c = java.util.Calendar.getInstance();
//            int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
//            int minute = c.get(java.util.Calendar.MINUTE);
            int hour = HostActivity.cal.get(java.util.Calendar.HOUR_OF_DAY);
            int minute = HostActivity.cal.get(java.util.Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            HostActivity.cal.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
            HostActivity.cal.set(java.util.Calendar.MINUTE, minute);
            HostActivity.cal.set(java.util.Calendar.SECOND, 0);
            HostActivity.cal.set(java.util.Calendar.MILLISECOND, 0);
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            final java.util.Calendar c = java.util.Calendar.getInstance();
//            int hour = c.get(java.util.Calendar.HOUR_OF_DAY);
//            int minute = c.get(java.util.Calendar.MINUTE);
            int year = HostActivity.cal.get(java.util.Calendar.YEAR);
            int month = HostActivity.cal.get(java.util.Calendar.MONTH);
            int day = HostActivity.cal.get(java.util.Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            HostActivity.cal.set(java.util.Calendar.YEAR, year);
            HostActivity.cal.set(java.util.Calendar.MONTH, month);
            HostActivity.cal.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    }

    public void showTimePicker(View v) {
        DialogFragment fragment = new TimePickerFragment();
        fragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePicker(View v) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "datePicker");
    }

}


/* This code was used to populate the database for the first time

List<String> listOfLocations = new ArrayList<>();
             SportsLocations test;

            //currentRef = mDatabase.child("sportsList");
            currentRef = mDatabase.child("sportsList/locations/Basketball");


            listOfLocations.add("CRC 4th floor Courts");
            listOfLocations.add("North Avenue Gym");
            listOfLocations.add("Peters Parking Deck");
            test = new SportsLocations("Basketball", listOfLocations);
            currentRef.setValue(test);

            // ===========

            currentRef = mDatabase.child("sportsList/locations/Football");

            listOfLocations = new ArrayList<>();
            listOfLocations.add("CRC Fields");
            listOfLocations.add("Burger Bowl");
            listOfLocations.add("Tech Green");
            test = new SportsLocations("Football", listOfLocations);
            currentRef.setValue(test);

            // ===========

            currentRef = mDatabase.child("sportsList/locations/Soccer");

            listOfLocations = new ArrayList<>();
            listOfLocations.add("CRC Fields");
            listOfLocations.add("Burger Bowl");
            test = new SportsLocations("Soccer", listOfLocations);
            currentRef.setValue(test);

            // ===========

            currentRef = mDatabase.child("sportsList/locations/Volleyball");

            listOfLocations = new ArrayList<>();
            listOfLocations.add("North Ave Courtyard");
            listOfLocations.add("CRC Fields");
            test = new SportsLocations("Volleyball", listOfLocations);
            currentRef.setValue(test);

            // ===========

            currentRef = mDatabase.child("sportsList/locations/UltimateFrisbee");

            listOfLocations = new ArrayList<>();
            listOfLocations.add("CRC Fields");
            listOfLocations.add("Tech Green");
            test = new SportsLocations("UltimateFrisbee", listOfLocations);
            currentRef.setValue(test);

            // ===========

            currentRef = mDatabase.child("sportsList/locations/Tennis");

            listOfLocations = new ArrayList<>();
            listOfLocations.add("Peters Parking Deck");
            test = new SportsLocations("Tennis", listOfLocations);
            currentRef.setValue(test);


 */