package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.adapters.EpisodesListAdapter;
import com.sniecinska.bingwatcher.api.RetrofitConnector;
import com.sniecinska.bingwatcher.models.EpisodeDetails;
import com.sniecinska.bingwatcher.models.Season;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class SeasonsDetailFragment extends Fragment {
    Season season;
    int tvId;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.episode_overview)
    TextView overview;
    @BindView(R.id.show_backpath)
    ImageView episodeImage;
    @BindView(R.id.episode_title)
    TextView title;
    @BindView(R.id.episode_air_date)
    TextView airDate;
    @BindView(R.id.episode_number)
    TextView episodeNumber;

    FragmentManager fragmentManager;
    GridLayoutManager gridLayoutManager;
    EpisodesListAdapter adapter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    List<EpisodeDetails> episodeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            season = bundle.getParcelable(getString(R.string.SEASON));
            tvId = bundle.getInt(getString(R.string.TV_ID));
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_season, container, false);
        ButterKnife.bind(this, view);

        initToolbar();

        Picasso.get()
                .load(getString(R.string.image_based_url) + season.getPosterPath())
                .into(episodeImage);

        overview.setText(season.getOverview());
        title.setText(season.getName());
        airDate.setText(season.getAirDateUsFormat());
        episodeNumber.setText(Integer.toString(season.getEpisodeCount()));

        fragmentManager = getFragmentManager();
        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        episodeList = new ArrayList<>();
        adapter = new EpisodesListAdapter(getActivity().getApplicationContext(), fragmentManager, episodeList, tvId);
        recyclerView.setAdapter(adapter);

        getEpisodeDetailsList();

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
                    collapsingToolbarLayout.setTitle(season.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    public void getEpisodeDetailsList() {
        int seasonNumber = season.getSeasonNumber();
        int episodeCount = season.getEpisodeCount();

        for(int i = 0; i<= episodeCount; i++) {
            Call call = RetrofitConnector.getService().getEpisodeDetails(tvId, seasonNumber, i, getString(R.string.api_key));
            call.enqueue(new Callback<EpisodeDetails>() {
                @Override
                public void onResponse(Call<EpisodeDetails> call, Response<EpisodeDetails> response) {
                       if(response.body() != null) {
                           episodeList.add(response.body());
                           adapter.notifyDataSetChanged();
                       }
                }

                @Override
                public void onFailure(Call<EpisodeDetails> call, Throwable throwable) {
                    Log.e(getString(R.string.RETROFIT_ERROR), throwable.getMessage());
                }
            });
        }
    }
}
