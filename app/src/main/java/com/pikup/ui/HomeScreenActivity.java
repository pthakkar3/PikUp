package com.pikup.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.pikup.R;

public class HomeScreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        TextView t = (TextView) findViewById(R.id.toBe);
        t.setText("Welcome, " + mAuth.getCurrentUser().getEmail());
    }

    public void viewProfile(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
        finish();
    }
    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, WelcomeScreenActivity.class);
        startActivity(intent);
        finish();
    }

    public void hostGame(View view) {
        Intent intent = new Intent(this, HostActivity.class);
        startActivity(intent);
        finish();
    }
}
