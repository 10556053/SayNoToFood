package com.example.firebase4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(HomePage.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent i = new Intent(Intent.ACTION_MAIN);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    i.addCategory(Intent.CATEGORY_HOME);
                                    startActivity(i);
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
        }
        return true;
    }
}
