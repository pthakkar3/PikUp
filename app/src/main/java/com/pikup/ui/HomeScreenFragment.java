package com.pikup.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pikup.R;
import com.pikup.model.User;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by pranshav on 11/3/2017.
 */

public class HomeScreenFragment extends Fragment implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private MapView mapView;
    private GoogleMap map;
    private LatLng userLatLng;

    private GoogleApiClient mGoogleApiClient;

    private View root;

    public HomeScreenFragment() {
        //required empty constructor for fragment subclasses
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        Button hostGame = (Button) root.findViewById(R.id.hostGame);
        Button joinGame = (Button) root.findViewById(R.id.joinGame);
        Button myGames = (Button) root.findViewById(R.id.myGames);
        hostGame.setOnClickListener(this);
        joinGame.setOnClickListener(this);
        myGames.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        MapsInitializer.initialize(this.getActivity());

        mapView = (MapView) root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                try {
                    googleMap.setMyLocationEnabled(true);
                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    String provider = locationManager.getBestProvider(criteria, true);
                    Location location = locationManager.getLastKnownLocation(provider);
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();

                        LatLng latLng = new LatLng(lat, lng);
                        userLatLng = latLng;

                        googleMap.addMarker(new MarkerOptions().position(userLatLng));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));
                    }
                } catch (SecurityException e) {
                    Log.e("ERROR:", e.toString());
                }

                mapView.onResume();
            }});

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        DatabaseReference userRef = mDatabase.child("userList").child(mAuth.getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                TextView t = (TextView) getView().findViewById(R.id.toBe);

                if (currentUser != null) {
                    String tempText = currentUser.getDisplayName();
                    t.setText("Welcome, " + tempText);
                } else {
                    t.setText("Welcome");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.hostGame){
            Fragment fragment = new HostGameFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.home_frame, fragment).commit();

        } else if (v.getId() == R.id.joinGame) {
            Fragment fragment = new JoinGameFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.home_frame, fragment).commit();

        } else if (v.getId() == R.id.myGames) {
            Fragment fragment = new MyGamesFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.home_frame, fragment).commit();
        }
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    //required methods for interfaces

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

}
