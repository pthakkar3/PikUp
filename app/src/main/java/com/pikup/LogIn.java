package com.pikup;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void logInAttempt(View view) {
        EditText emailEdit = (EditText) findViewById(R.id.enterEmailLogin);
        EditText passwordEdit = (EditText) findViewById(R.id.enterPasswordLogin);
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        if (email.matches("") || password.matches("")) {
            Toast.makeText(this, "There shouldn't be any empty fields!", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                Toast.makeText(LogIn.this, "Incorrect Email Address or Password entered!",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(LogIn.this, HomeScreen.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
        }
    }

    public void cancelLogIn(View view) {
        Intent intent = new Intent(this, WelcomeScreen.class);
        startActivity(intent);
        finish();
    }

    public void forgotPassword(View view) {
        // Goto different screen to send in the password to?
        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
        finish();
    }
}
