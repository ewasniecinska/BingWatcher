package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
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
import com.google.firebase.database.ValueEventListener;
import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.adapters.PopularSeriesAdapter;
import com.sniecinska.bingwatcher.models.TvSeries;

import java.util.ArrayList;
import java.util.List;

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

    private void getDataFromDb(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<TvSeries> seriesList = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uid = ds.child("user_id").getValue(String.class);
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(uid.equals(currentUser.getUid())){
                        TvSeries tvSeries = ds.child("tvSeries").getValue(TvSeries.class);
                        seriesList.add(tvSeries);
                    }
                }
                recyclerView.setAdapter(new PopularSeriesAdapter(fragmentManager, seriesList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        databaseReference.addListenerForSingleValueEvent(valueEventListener);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post = String.valueOf(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
