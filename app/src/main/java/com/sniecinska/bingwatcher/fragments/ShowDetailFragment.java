package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.api.RetrofitConnector;
import com.sniecinska.bingwatcher.models.DatabaseModel;
import com.sniecinska.bingwatcher.models.ExternalApiResult;
import com.sniecinska.bingwatcher.models.NextEpisode;
import com.sniecinska.bingwatcher.models.TvSeries;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailFragment extends Fragment {
    TvSeries tvSeries;
    String imbd;
    private FirebaseAnalytics firebaseAnalytics;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String user_id;

    @BindView(R.id.show_title)
    TextView title;
    @BindView(R.id.show_backpath)
    ImageView backpathPoster;
    @BindView(R.id.floating_action_button)
    FloatingActionButton addToTrackedButton;
    @BindView(R.id.show_overview)
    TextView overview;
    @BindView(R.id.next_episode_card_view)
    CardView nextEpisode;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.next_episode_date)
    TextView nextEpisodeDate;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tvSeries = bundle.getParcelable("TV_SHOW");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_detail, container, false);

        ButterKnife.bind(this, view);

        initToolbar();

        initFirebaseAnalytics();

        initFirebaseDatabase();

        addToTrackedButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_tv));

        Picasso.get()
                .load("http://image.tmdb.org/t/p/original/" + tvSeries.getBackdropPath())
                .into(backpathPoster);

        overview.setText(tvSeries.getOverview());

        addToTrackedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseModel series = new DatabaseModel(user_id, tvSeries);

                databaseReference.push().setValue(series);

            }
        });

        callApi();

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
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("DB_TEST", "onAuthStateChanged:signed_in:" + user.getUid());
                    user_id = user.getUid();
                } else {
                    // User is signed out
                    Log.d("DB_TEST", "onAuthStateChanged:signed_out");
                }

            }
        };

        mAuth.signInAnonymously()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("DB_TEST", "OnComplete : " + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("DB_TEST", "Failed : ", task.getException());
                            Toast.makeText(getActivity().getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");
    }

    public void callApi() {
        Call call = RetrofitConnector.getService().getExternalID(tvSeries.getId(), getString(R.string.api_key));


        call.enqueue(new Callback<ExternalApiResult>() {
            @Override
            public void onResponse(Call<ExternalApiResult> call, Response<ExternalApiResult> response) {
                if (response.body() != null) {
                    imbd = response.body().getTvdbId();
                    Log.d("TBVD", imbd);
                    getLastEpisode(imbd);
                }
            }

            @Override
            public void onFailure(Call<ExternalApiResult> call, Throwable throwable) {
            }
        });
    }

    public void getLastEpisode(String movie_id) {
        Call call = RetrofitConnector.getService2().getNextEpisode("game-of-thrones");


        call.enqueue(new Callback<NextEpisode>() {
            @Override
            public void onResponse(Call<NextEpisode> call, Response<NextEpisode> response) {
                if (response.body() == null) {
                    Log.d("TITLE", "null");
                } else {
                    Log.d("TITLE", response.body().getTitle());
                }

            }

            @Override
            public void onFailure(Call<NextEpisode> call, Throwable throwable) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // release listener in onStop
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
