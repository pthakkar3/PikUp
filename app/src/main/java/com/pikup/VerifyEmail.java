package com.pikup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;

public class VerifyEmail extends AppCompatActivity {

    //FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: idk what im doing ... (insert dog on internet meme)
        //this.user = (FirebaseUser) getIntent().getExtras().get("user");
        setContentView(R.layout.activity_verify_email);
    }

    public void verifyEmail(View view) {

    }
}
