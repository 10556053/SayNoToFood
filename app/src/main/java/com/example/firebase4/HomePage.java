package com.example.firebase4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.firebase4.Fragment.dashboard_fragment;
import com.example.firebase4.Fragment.home_Fragment;
import com.example.firebase4.Fragment.notification_Fragment;
import com.example.firebase4.Fragment.userProfile_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePage extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);

        loadFragment(new home_Fragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragment = new home_Fragment();
                break;

            case R.id.navigation_dashboard:
                fragment = new dashboard_fragment();
                break;

            case R.id.navigation_notifications:
                fragment = new notification_Fragment();
                break;

            case R.id.navigation_profile:
                fragment = new userProfile_Fragment();
                break;
        }

        return loadFragment(fragment);
    }
}
