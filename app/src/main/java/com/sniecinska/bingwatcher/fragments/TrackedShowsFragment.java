package com.sniecinska.bingwatcher.fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;

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
import com.sniecinska.bingwatcher.widget.AppWidget;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackedShowsFragment extends Fragment {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.empty_view)
    ConstraintLayout emptyView;
    GridLayoutManager gridLayoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FragmentManager fragmentManager;
    FirebaseAuth mAuth;
    ArrayList<TvSeriesDetails> seriesList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_tracked_shows, container, false);
       ButterKnife.bind(this, view);

       initToolbar();

       fragmentManager = getFragmentManager();
       gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
       recyclerView.setLayoutManager(gridLayoutManager);

       getDataFromDb();

        return view;
    }

    private void initToolbar() {
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle(getString(R.string.app_name));
    }

    private void getDataFromDb() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        Query tvSeriesQuery = databaseReference.child(getString(R.string.DB_CHILD_USERS)).orderByChild(getString(R.string.DB_CHILD_AIR_DATE));
        tvSeriesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                seriesList = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uid = ds.child(getString(R.string.DB_CHILD_USER_ID)).getValue(String.class);
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(currentUser != null) {
                        if (uid.equals(currentUser.getUid())) {
                            TvSeriesDetails tvSeries = ds.child(getString(R.string.DB_CHILD_TVSERIES_DETAILS)).getValue(TvSeriesDetails.class);
                            seriesList.add(tvSeries);
                        }
                    }
                }
                updateWidget();
                if (seriesList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
                recyclerView.setAdapter(new TrackedShowsListAdapter(getActivity().getApplicationContext(), fragmentManager, seriesList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateWidget() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity().getApplicationContext());
        RemoteViews remoteViews = new RemoteViews(getActivity().getApplicationContext().getPackageName(), R.layout.app_widget);
        ComponentName thisWidget = new ComponentName(getActivity().getApplicationContext(), AppWidget.class);
        remoteViews.setTextViewText(R.id.widget_list, getThreeNextShows());
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    public String getThreeNextShows(){
        String nextTvShows = "";
        StringBuilder sB = new StringBuilder(nextTvShows);


        for(int i = 0; i < seriesList.size(); i++){
            if(seriesList.get(i).getNextEpisode() != null) {
                sB.append("- " + seriesList.get(i).getName() + " " + getString(R.string.print_labes) + " " + seriesList.get(i).getNextEpisode().getAirDay() + System.lineSeparator());
            }
        }

        return sB.toString();
    }
}
