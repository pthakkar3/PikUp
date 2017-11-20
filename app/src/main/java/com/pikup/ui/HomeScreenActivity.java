package com.pikup.ui;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;

import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ListView;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.pikup.R;



public class HomeScreenActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        String[] drawerItems = {"Home", "Profile","My Games", "Host Game", "Join Game", "Contact Us", "Logout"};

        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, R.id.text1 ,drawerItems ));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.open_drawer,  /* "open drawer" description for accessibility */
                R.string.close_drawer  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        Fragment fragment = new HomeScreenFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.home_frame, fragment).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView textView = (TextView) view.findViewById(R.id.text1);
            String text = textView.getText().toString();
            selectDrawerItem(text);
        }
    }

    private void selectDrawerItem(String item) {
        if (item.equals("Home")) {
            Fragment fragment = new HomeScreenFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.home_frame, fragment).commit();
            mDrawerLayout.closeDrawer(mDrawerList);
        } else if (item.equals("Profile")) {
            Fragment fragment = new UserProfileFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.home_frame, fragment).commit();
            mDrawerLayout.closeDrawer(mDrawerList);
        } else if(item.equals("My Games")) {
            Fragment fragment = new MyGamesFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.home_frame, fragment).commit();
            mDrawerLayout.closeDrawer(mDrawerList);
        } else if (item.equals("Join Game")){
            Fragment fragment = new JoinGameFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.home_frame, fragment).commit();
            mDrawerLayout.closeDrawer(mDrawerList);
        } else if (item.equals("Host Game")) {
            Fragment fragment = new HostGameFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.home_frame, fragment).commit();
            mDrawerLayout.closeDrawer(mDrawerList);
        } else if (item.equals("Contact Us")) {
            Fragment fragment = new ContactUsFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.home_frame, fragment).commit();
            mDrawerLayout.closeDrawer(mDrawerList);
        } else if (item.equals("Logout")) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, WelcomeScreenActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
