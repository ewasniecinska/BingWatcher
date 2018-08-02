package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.models.Episode;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class EpisodeFragment extends Fragment {
    Episode episode;
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
    TextView episodeNumber;
    @BindView(R.id.divider)
    ImageView divider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            episode = bundle.getParcelable(getString(R.string.EPISODE));
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_episode, container, false);
        ButterKnife.bind(this, view);

        initToolbar();

        Picasso.get()
                .load(getString(R.string.image_based_url) + episode.getStillPath())
                .into(episodeImage);

        overview.setText(episode.getOverview());
        title.setText(episode.getName());
        airDate.setText("Air date: " + episode.getAirDate());
        episodeNumber.setText("Session " + episode.getSeasonNumber() + ", Episode " + episode.getEpisodeNumber());

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
                    collapsingToolbarLayout.setTitle("Session " + episode.getSeasonNumber() + ", Episode " + episode.getEpisodeNumber());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
