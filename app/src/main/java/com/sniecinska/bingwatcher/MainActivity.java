package com.sniecinska.bingwatcher;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Fragment mFragment = null;
                        FragmentManager fragmentManager = getSupportFragmentManager();


                        switch (item.getItemId()) {
                            case R.id.action_tracked:
                                mFragment = new TrackedShowsFragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_box, mFragment).commit();
                                return true;
                            case R.id.action_search:
                                mFragment = new SearchFragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_box, mFragment).commit();
                                return true;
                            case R.id.action_popular:
                                mFragment = new ExploreFragment();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_box, mFragment).commit();
                                return true;
                        }
                        return true;
                    }
                });
    }
}
