package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.api.RetrofitConnector;
import com.sniecinska.bingwatcher.models.DatabaseModel;
import com.sniecinska.bingwatcher.models.Episode;
import com.sniecinska.bingwatcher.models.TvSeries;
import com.sniecinska.bingwatcher.models.TvSeriesDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailFragment extends Fragment {
    TvSeries tvSeries;
    TvSeriesDetails tvSeriesDetails;
    Episode episode;

    // FireBase
    private FirebaseAnalytics firebaseAnalytics;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    FragmentManager fragmentManager;
    Bundle bundle;
    DatabaseReference postRef;
    DatabaseModel series;
    boolean TRACED_MOVIE;

    Snackbar snackbar;

    @BindView(R.id.show_title)
    TextView title;
    @BindView(R.id.show_backpath)
    ImageView backpathPoster;
    @BindView(R.id.floating_action_button)
    FloatingActionButton addToTrackedButton;
    @BindView(R.id.label_episodes)
    TextView labelEpisodes;
    @BindView(R.id.show_overview)
    TextView overview;
    @BindView(R.id.next_episode_card_view)
    CardView nextEpisodeCard;
    @BindView(R.id.all_episodes)
    CardView allEpisodes;
    @BindView(R.id.next_episode_name)
    TextView nextEpisodeName;
    @BindView(R.id.next_episode_number)
    TextView nextEpisodeNumber;
    @BindView(R.id.next_episode_date)
    TextView nextEpisodeDate;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_detail, container, false);

        ButterKnife.bind(this, view);

        TRACED_MOVIE = false;

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tvSeries = bundle.getParcelable(getString(R.string.TV_SHOW));
            if (tvSeries == null) {
                tvSeriesDetails = bundle.getParcelable(getString(R.string.SERIES_DETAILS));
                updateUI();
                initFirebaseAnalytics();
                initFirebaseDatabase();
            } else {
                getSeriesDetailsThenUpdateUi();
            }
        }

        return view;
    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(tvSeriesDetails.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void initFirebaseAnalytics() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity().getApplicationContext());
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, tvSeriesDetails.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, tvSeriesDetails.getName());
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        firebaseAnalytics.setMinimumSessionDuration(20000);
        firebaseAnalytics.setSessionTimeoutDuration(500);
        firebaseAnalytics.setUserId(String.valueOf(tvSeriesDetails.getId()));
        firebaseAnalytics.setUserProperty("Tv Series", tvSeriesDetails.getName());
    }

    private void initFirebaseDatabase() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

        updateDataForTrackedShow();
    }

    public void getSeriesDetailsThenUpdateUi() {
        Call call = RetrofitConnector.getService().getSeriesDetails(tvSeries.getId(), getString(R.string.api_key));

        call.enqueue(new Callback<TvSeriesDetails>() {
            @Override
            public void onResponse(Call<TvSeriesDetails> call, Response<TvSeriesDetails> response) {
                tvSeriesDetails = response.body();
                updateUI();
                initFirebaseAnalytics();
                initFirebaseDatabase();
            }

            @Override
            public void onFailure(Call<TvSeriesDetails> call, Throwable throwable) {
                Log.e(getString(R.string.RETROFIT_ERROR), throwable.getMessage());
            }
        });
    }

    public void updateUI() {
        initToolbar();

        Picasso.get()
                .load(getString(R.string.image_based_url) + tvSeriesDetails.getBackdropPath())
                .into(backpathPoster);

        overview.setText(tvSeriesDetails.getOverview());
        title.setText(tvSeriesDetails.getName());

        updateEpisodeCard();
        updateAllSeasonsButton();
        updateFab();

    }

    public void updateEpisodeCard() {
        if (tvSeriesDetails.getNextEpisode() == null) {
            updateViewForLastEpisode();
        } else {
            updateViewForNextEpisode();
        }
    }

    public void updateViewForLastEpisode() {
        labelEpisodes.setText(R.string.last_episode);
        episode = tvSeriesDetails.getLastEpisode();

        nextEpisodeName.setText(episode.getName());
        nextEpisodeNumber.setText("Session " + episode.getSeasonNumber() + ", Episode " + episode.getEpisodeNumber());
        nextEpisodeDate.setVisibility(View.GONE);
        setEpisodeButton(episode);
    }

    public void updateViewForNextEpisode() {
        labelEpisodes.setText(R.string.next_episode);
        episode = tvSeriesDetails.getNextEpisode();

        nextEpisodeName.setText(episode.getName());
        nextEpisodeNumber.setText("Session " + episode.getSeasonNumber() + ", Episode " + episode.getEpisodeNumber());
        nextEpisodeDate.setText(episode.getAirDateUsFormat());
        setEpisodeButton(episode);
    }


    public void updateFab() {
        updateFabIcon();

        addToTrackedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TRACED_MOVIE){
                    showSnackBar(tvSeriesDetails.getName() + " has been removed from tracked series");
                    addToTrackedButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_tv));
                    deleteDataFromDatabase();
                    TRACED_MOVIE = false;
                } else {
                    showSnackBar(tvSeriesDetails.getName() + " has been added to tracked series");
                    addToTrackedButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_tv_off));
                    Episode nextEpisode = tvSeriesDetails.getNextEpisode();
                    if(nextEpisode == null){
                        series = new DatabaseModel(currentUser.getUid(), String.valueOf(tvSeriesDetails.getId()), Long.MAX_VALUE , tvSeriesDetails);
                    } else {
                        series = new DatabaseModel(currentUser.getUid(), String.valueOf(tvSeriesDetails.getId()), nextEpisode.getAirDate().getTime(), tvSeriesDetails);
                    }
                    databaseReference.push().setValue(series);
                    TRACED_MOVIE = true;
                }
            }
        });
    }

    public void updateFabIcon() {
        if(TRACED_MOVIE){
            addToTrackedButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_tv_off));
        } else {
            addToTrackedButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_tv));
        }
    }

    public void setEpisodeButton(final Episode episode) {
        fragmentManager = getActivity().getSupportFragmentManager();
        nextEpisodeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EpisodeFragment fragment = new EpisodeFragment();
                bundle = new Bundle();
                bundle.putInt(getString(R.string.TV_ID), tvSeriesDetails.getId());
                bundle.putInt(getString(R.string.SEASON_NUMBER), episode.getSeasonNumber());
                bundle.putInt(getString(R.string.EPISODE_NUMBER), episode.getEpisodeNumber());
                fragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_box, fragment).commit();
            }
        });
    }

    public void updateAllSeasonsButton() {
        allEpisodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeasonsListFragment fragment = new SeasonsListFragment();
                bundle = new Bundle();
                bundle.putParcelableArrayList(getString(R.string.SEASONS), (ArrayList<? extends Parcelable>) tvSeriesDetails.getSeasons());
                bundle.putInt(getString(R.string.TV_ID), tvSeriesDetails.getId());
                fragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_box, fragment).commit();
            }
        });
    }

    private void updateDataForTrackedShow() {
        postRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.DB_CHILD_USERS));
        postRef.orderByChild(getString(R.string.DB_CHILD_USER_ID)).equalTo(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            postRef.orderByChild(getString(R.string.DB_CHILD_SERIES_ID)).equalTo(String.valueOf(tvSeriesDetails.getId()))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                TRACED_MOVIE = true;
                                                updateFabIcon();
                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().child(getString(R.string.DB_CHILD_TVSERIES)).setValue(tvSeriesDetails);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                });
    }

    public void deleteDataFromDatabase() {
        postRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.DB_CHILD_USERS));
        postRef.orderByChild(getString(R.string.DB_CHILD_USER_ID)).equalTo(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            postRef.orderByChild(getString(R.string.DB_CHILD_SERIES_ID)).equalTo(String.valueOf(tvSeriesDetails.getId()))
                                    .addListenerForSingleValueEvent(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                TRACED_MOVIE = false;
                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().removeValue();
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                        }

                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }

                });
    }

    public void showSnackBar(String meassage) {
        snackbar = Snackbar.make(coordinatorLayout, meassage, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}




