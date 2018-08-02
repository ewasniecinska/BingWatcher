package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    private FirebaseAnalytics firebaseAnalytics;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    FirebaseUser currentUser;

    FragmentManager fragmentManager;
    Bundle bundle;
    Episode ewa;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tvSeries = bundle.getParcelable(getString(R.string.TV_SHOW));
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_detail, container, false);

        ButterKnife.bind(this, view);

        getSeriesDetails();

        initFirebaseAnalytics();
        initFirebaseDatabase();

        initToolbar();

        Picasso.get()
                .load(getString(R.string.image_based_url) + tvSeries.getBackdropPath())
                .into(backpathPoster);

        overview.setText(tvSeries.getOverview());
        title.setText(tvSeries.getTitle());

        addToTrackedButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_tv));

        //Todo: change FAB when user has already add movie to tracked
        addToTrackedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseModel series = new DatabaseModel(currentUser.getUid(), String.valueOf(tvSeries.getId()), tvSeriesDetails);
                databaseReference.push().setValue(series);

            }
        });


        return view;

    }

    private void initToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(tvSeries.getTitle());
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
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, tvSeries.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, tvSeries.getTitle());

        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        //Sets whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        firebaseAnalytics.setMinimumSessionDuration(20000);

        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        firebaseAnalytics.setSessionTimeoutDuration(500);

        //Sets the user ID property.
        firebaseAnalytics.setUserId(String.valueOf(tvSeries.getId()));

        //Sets a user property to a given value.
        firebaseAnalytics.setUserProperty("Food", tvSeries.getTitle());


    }

    private void initFirebaseDatabase() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");
    }

    public void getSeriesDetails() {
        Call call = RetrofitConnector.getService().getSeriesDetails(tvSeries.getId(), getString(R.string.api_key));

        call.enqueue(new Callback<TvSeriesDetails>() {
            @Override
            public void onResponse(Call<TvSeriesDetails> call, Response<TvSeriesDetails> response) {
                tvSeriesDetails = response.body();

                if(tvSeriesDetails.getNextEpisode() == null){
                    updateViewForLastEpisode();
                } else {
                    updateViewForNextEpisode();
                }

                updateViewForAllSeasons();
            }

            @Override
            public void onFailure(Call<TvSeriesDetails> call, Throwable throwable) {
                Log.e(getString(R.string.RETROFIT_ERROR), throwable.getMessage());
            }
        });
    }

    public void updateViewForLastEpisode() {
        labelEpisodes.setText("Last Episode");
        episode = tvSeriesDetails.getLastEpisode();

        nextEpisodeName.setText(episode.getName());
        nextEpisodeNumber.setText("Session " + episode.getSeasonNumber() + ", Episode " + episode.getEpisodeNumber());
        nextEpisodeDate.setVisibility(View.GONE);
        setEpisodeButton(episode);
    }

    public void updateViewForNextEpisode() {
        labelEpisodes.setText("Next Episode");
        episode = tvSeriesDetails.getNextEpisode();

        nextEpisodeName.setText(episode.getName());
        nextEpisodeNumber.setText("Session " + episode.getSeasonNumber() + ", Episode " + episode.getEpisodeNumber());
        nextEpisodeDate.setText(episode.getAirDate());
        setEpisodeButton(episode);
    }

    public void setEpisodeButton(final Episode episode) {
        fragmentManager = getActivity().getSupportFragmentManager();
        nextEpisodeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EpisodeFragment fragment = new EpisodeFragment();
                bundle = new Bundle();
                bundle.putParcelable(getString(R.string.EPISODE), episode);
                fragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_box, fragment).commit();
            }
        });
    }

    public void updateViewForAllSeasons() {
        allEpisodes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeasonsListFragment fragment = new SeasonsListFragment();
                bundle = new Bundle();
                bundle.putParcelableArrayList(getString(R.string.SEASONS), (ArrayList<? extends Parcelable>) tvSeriesDetails.getSeasons());
                fragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_box, fragment).commit();
            }
        });
    }
}
