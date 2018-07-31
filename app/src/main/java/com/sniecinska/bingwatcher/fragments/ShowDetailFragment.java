package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
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

import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.api.RetrofitConnector;
import com.sniecinska.bingwatcher.models.ExternalApiResult;
import com.sniecinska.bingwatcher.models.TvSeries;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailFragment extends Fragment {
    TvSeries tvSeries;

    @BindView(R.id.show_title) TextView title;
    @BindView(R.id.show_backpath) ImageView backpathPoster;
    @BindView(R.id.floating_action_button) FloatingActionButton addToTrackedButton;
    @BindView(R.id.show_overview) TextView overview;
    @BindView(R.id.next_episode_card_view) CardView nextEpisode;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout) CollapsingToolbarLayout collapsingToolbarLayout ;
    @BindView(R.id.app_bar_layout) AppBarLayout appBarLayout ;
    @BindView(R.id.next_episode_date) TextView nextEpisodeDate ;
    @BindView(R.id.nested_scroll_view) NestedScrollView nestedScrollView ;

    @Override
    public void onCreate (Bundle savedInstanceState) {
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

        addToTrackedButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_tv));

        Picasso.get()
                .load("http://image.tmdb.org/t/p/original/" + tvSeries.getBackdropPath())
                .into(backpathPoster);

        overview.setText(tvSeries.getOverview());

        Log.d("TEST", String.valueOf(tvSeries.getId()));

        callApi();

        return view;

    }

    private void initToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

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
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }




    public void callApi(){
        Call call = RetrofitConnector.getService().getExternalID(tvSeries.getId(), getString(R.string.api_key));


        call.enqueue(new Callback<ExternalApiResult>() {
            @Override
            public void onResponse(Call<ExternalApiResult> call, Response<ExternalApiResult> response) {
                if(response.body() != null) {
                    Log.d("TEST2", response.body().getImdbId());
                }
            }
            @Override
            public void onFailure(Call<ExternalApiResult> call, Throwable throwable) {
            }
        });
    }
}
