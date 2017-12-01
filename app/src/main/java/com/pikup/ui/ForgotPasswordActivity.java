package com.pikup.ui;

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
import com.google.firebase.auth.FirebaseAuth;
import com.pikup.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        this.mAuth = FirebaseAuth.getInstance();
    }

    public void resetEmail(View view) {
        EditText emailEdit = (EditText) findViewById(R.id.enterEmailAddress);
        String email = emailEdit.getText().toString();

        if (email.matches("") || !(email.substring(email.lastIndexOf('@') + 1).equals("gatech.edu"))) {
            Toast.makeText(this, "Please enter your gatech email.", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("LOG:", "Email sent.");
                                Toast.makeText(ForgotPasswordActivity.this, "Reset Email Sent! Check your Inbox!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void goBack(View view) {
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
        finish();

    }
}
