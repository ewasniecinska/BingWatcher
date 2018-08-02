package com.sniecinska.bingwatcher;

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
import com.sniecinska.bingwatcher.fragments.ExploreFragment;
import com.sniecinska.bingwatcher.fragments.SearchFragment;
import com.sniecinska.bingwatcher.fragments.TrackedShowsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    Fragment mFragment;
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

        checkUserId();

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
    }

    private void checkUserId(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("DB_TEST", "onAuthStateChanged:signed_in:" + user.getUid());
               } else {
                    Log.d("DB_TEST", "onAuthStateChanged:signed_out");
                }

            }
        };

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("DB_TEST", "OnComplete : " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("DB_TEST", "Failed : ", task.getException());
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
