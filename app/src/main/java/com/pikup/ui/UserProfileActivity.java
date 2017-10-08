package com.pikup.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

/**
 * Created by Altan on 9/7/2017.
 */

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Populate fields with data from the logged in user
        DatabaseReference userRef = mDatabase.child("userList").child(mAuth.getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);

                EditText displayNameTextBox = (EditText) findViewById(R.id.userProfileDisplayName);
                EditText phoneNumberTextBox = (EditText) findViewById(R.id.userProfilePhoneNumber);
                EditText ageTextBox = (EditText) findViewById(R.id.userProfileAge);
                RadioGroup selectGenderGroup = (RadioGroup) findViewById(R.id.userProfileGender);
                RadioGroup selectAffiliationGroup = (RadioGroup) findViewById(R.id.userProfileAffiliation);

                if (currentUser != null) {
                    displayNameTextBox.setText(currentUser.getDisplayName());
                    phoneNumberTextBox.setText(Long.toString(currentUser.getPhoneNumber()));
                    ageTextBox.setText(Integer.toString(currentUser.getAge()));
                    if (currentUser.getGender().equals("Male")) {
                        selectGenderGroup.check(R.id.userProfileMaleSelect);
                    } else {
                        selectGenderGroup.check(R.id.userProfileFemaleSelect);
                    }
                    if (currentUser.getIsStudent()) {
                        selectAffiliationGroup.check(R.id.userProfileStudentSelect);
                    } else {
                        selectAffiliationGroup.check(R.id.userProfileFacultySelect);
                    }
                } else {
                    displayNameTextBox.setText("Change Me!");
                    phoneNumberTextBox.setText("1234567890");
                    ageTextBox.setText("0");
                    selectGenderGroup.check(R.id.userProfileMaleSelect);
                    selectAffiliationGroup.check(R.id.userProfileStudentSelect);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void saveUserProfile(View view) {

        EditText displayNameTextBox = (EditText) findViewById(R.id.userProfileDisplayName);
        EditText phoneNumberTextBox = (EditText) findViewById(R.id.userProfilePhoneNumber);
        EditText ageTextBox = (EditText) findViewById(R.id.userProfileAge);
        RadioGroup selectGenderGroup = (RadioGroup) findViewById(R.id.userProfileGender);
        RadioGroup selectAffiliationGroup = (RadioGroup) findViewById(R.id.userProfileAffiliation);

        if(displayNameTextBox.getText().toString().length() > 15) {
            Toast.makeText(UserProfileActivity.this, "You can only have upto 15 characters in your name :(",
                    Toast.LENGTH_LONG).show();
        } else {
            String displayName = displayNameTextBox.getText().toString();
            String phoneNumber = phoneNumberTextBox.getText().toString();
            int age = Integer.parseInt(ageTextBox.getText().toString());
            String gender;
            if (selectGenderGroup.getCheckedRadioButtonId() == R.id.userProfileMaleSelect) {
                gender = "Male";
            } else {
                gender = "Female";
            }
            boolean isStudent;
            if (selectAffiliationGroup.getCheckedRadioButtonId() == R.id.userProfileStudentSelect) {
                isStudent = true;
            } else {
                isStudent = false;
            }

            User currentUser = new User(displayName, age, gender, Long.parseLong(phoneNumber), isStudent);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = mDatabase.child("userList").child(mAuth.getCurrentUser().getUid());

            userRef.setValue(currentUser);

            Toast.makeText(UserProfileActivity.this, "Your changes have been saved!",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void profileOnBack(View view) {
        Toast.makeText(UserProfileActivity.this, "You exited without saving your changes.",
                Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(UserProfileActivity.this, HomeScreenActivity.class));
        finish();

    }


    public void deleteAccount(View view) {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Account")
                .setMessage("Are you sure you wanna leave us? :(")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleting();
                        Intent intent = new Intent(UserProfileActivity.this, WelcomeScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(UserProfileActivity.this, "We're glad you're staying :)",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .show();

    }

    public void deleting() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mDatabase.child("userList").child(mAuth.getCurrentUser().getUid());
        userRef.removeValue();
        //TODO: remove this user from every game they are in

        FirebaseUser user = mAuth.getCurrentUser();
        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "User account deleted.");
                        }
                    }
                });

    }
}
