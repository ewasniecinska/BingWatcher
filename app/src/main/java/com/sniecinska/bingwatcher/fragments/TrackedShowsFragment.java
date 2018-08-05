package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.adapters.TrackedShowsListAdapter;
import com.sniecinska.bingwatcher.models.TvSeriesDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackedShowsFragment extends Fragment {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    ArrayList<TvSeriesDetails> seriesList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_tracked_shows, container, false);
       ButterKnife.bind(this, view);

       ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

       fragmentManager = getFragmentManager();
       gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
       recyclerView.setLayoutManager(gridLayoutManager);

       getDataFromDb();

        return view;
    }

    private void getDataFromDb() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        final TreeMap treeMap = new TreeMap<Date, TvSeriesDetails>();
        Query peopleBloodTypeQuery = databaseReference.child("users").orderByChild("air_date");
        peopleBloodTypeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                seriesList = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uid = ds.child(getString(R.string.DB_CHILD_USER_ID)).getValue(String.class);
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(uid.equals(currentUser.getUid())){
                        TvSeriesDetails tvSeries = ds.child(getString(R.string.DB_CHILD_TVSERIES_DETAILS)).getValue(TvSeriesDetails.class);
                        seriesList.add(tvSeries);
                    }
                }
                recyclerView.setAdapter(new TrackedShowsListAdapter(getActivity().getApplicationContext(), fragmentManager, seriesList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
