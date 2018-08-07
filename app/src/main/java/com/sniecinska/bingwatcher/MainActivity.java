package com.sniecinska.bingwatcher;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.sniecinska.bingwatcher.fragments.ExploreFragment;
import com.sniecinska.bingwatcher.fragments.SearchFragment;
import com.sniecinska.bingwatcher.fragments.TrackedShowsFragment;
import com.sniecinska.bingwatcher.utils.InternetConnection;
import com.sniecinska.bingwatcher.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    Fragment mFragment;
    FirebaseDatabase firebaseDatabase;
    FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @BindView(R.id.bottom_navigation) BottomNavigationView bottomNavigationView;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // check internet connection
        new InternetConnection(getApplicationContext()).execute();

        firebaseDatabase = FirebaseDatabase.getInstance();

        checkUserId();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Util.scheduleJob(getApplicationContext());
        }


        mFragment = null;
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        mFragment = null;
                        fragmentManager = getSupportFragmentManager();

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

        mFragment = new TrackedShowsFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_box, mFragment).commit();
    }

    private void checkUserId(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(getString(R.string.AUTH), "onAuthStateChanged:signed_in:" + user.getUid());
               } else {
                    Log.d(getString(R.string.AUTH), "onAuthStateChanged:signed_out");
                }

            }
        };

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(getString(R.string.AUTH), "OnComplete : " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(getString(R.string.AUTH), "Failed : ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}
