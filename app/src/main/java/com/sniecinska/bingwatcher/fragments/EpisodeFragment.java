package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.api.RetrofitConnector;
import com.sniecinska.bingwatcher.models.EpisodeDetails;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class EpisodeFragment extends Fragment {
    EpisodeDetails episodeDetails;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.episode_overview)
    TextView overview;
    @BindView(R.id.episode_image)
    ImageView episodeImage;
    @BindView(R.id.episode_title)
    TextView title;
    @BindView(R.id.episode_air_date)
    TextView airDate;
    @BindView(R.id.episode_number)
    TextView episodeNumberView;
    @BindView(R.id.season_number)
    TextView seasonNumberView;
    @BindView(R.id.divider)
    ImageView divider;

    int tvId;
    int seasonNumber;
    int episodeNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tvId = bundle.getInt(getString(R.string.TV_ID));
            seasonNumber = bundle.getInt(getString(R.string.SEASON_NUMBER));
            episodeNumber = bundle.getInt(getString(R.string.SEASON_NUMBER));
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode, container, false);
        ButterKnife.bind(this, view);

        getEpisodeAndUpdateUI();

        return view;
    }

    public void getEpisodeAndUpdateUI() {
        Call call = RetrofitConnector.getService().getEpisodeDetails(tvId, seasonNumber, episodeNumber, getString(R.string.api_key));
        call.enqueue(new Callback<EpisodeDetails>() {
            @Override
            public void onResponse(Call<EpisodeDetails> call, Response<EpisodeDetails> response) {
                if(response.body() != null) {
                    episodeDetails = response.body();
                    updateUI();
                }
            }

            @Override
            public void onFailure(Call<EpisodeDetails> call, Throwable throwable) {
                Log.e(getString(R.string.RETROFIT_ERROR), throwable.getMessage());
            }
        });
    }

    public void updateUI() {
        initToolbar();

        Picasso.get()
                .load(getString(R.string.image_based_url) + episodeDetails.getStillPath())
                .into(episodeImage);

        overview.setText(episodeDetails.getOverview());
        title.setText(episodeDetails.getName());
        airDate.setText(episodeDetails.getAirDate());
        seasonNumberView.setText(Integer.toString(episodeDetails.getSeasonNumber()));
        episodeNumberView.setText(Integer.toString(episodeDetails.getEpisodeNumber()));
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


        if(episodeDetails.getStillPath() == null || episodeDetails.getStillPath() == ""){
            appBarLayout.setExpanded(false);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Session " + episodeDetails.getSeasonNumber() + ", Episode " + episodeDetails.getEpisodeNumber());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
