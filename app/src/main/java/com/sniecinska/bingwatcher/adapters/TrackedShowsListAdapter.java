package com.sniecinska.bingwatcher.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.fragments.ShowDetailFragment;
import com.sniecinska.bingwatcher.models.Episode;
import com.sniecinska.bingwatcher.models.TvSeriesDetails;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class TrackedShowsListAdapter extends RecyclerView.Adapter<TrackedShowsListAdapter.RecipeHolder>{
    private List<TvSeriesDetails> list;
    Fragment fragment;
    FragmentManager fragmentManager;
    Bundle bundle;
    Context context;

    public TrackedShowsListAdapter (Context context,FragmentManager fragmentManager, List<TvSeriesDetails> list) {
        this.context = context;
        this.list = list;
        this.fragmentManager = fragmentManager;
    }

    public static class RecipeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.poster)
        ImageView poster;
        @BindView(R.id.episode_number)
        TextView episodeNumber;
        @BindView(R.id.season_number)
        TextView seasonNumber;
        @BindView(R.id.next_episode_day)
        TextView nextEpisodeDay;
        @BindView(R.id.next_episode_date)
        TextView nextEpisodeDate;
        @BindView(R.id.next_episode_time)
        TextView nextEpisodeTime;
        @BindView(R.id.season_number_label)
        TextView seasonNumberLabel;
        @BindView(R.id.episode_number_label)
        TextView episodeNumberLabel;


        public RecipeHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public TrackedShowsListAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_tracked_shows, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, final int position) {
        String title = list.get(position).getName();
        RecipeHolder recipeHolder = holder;
        recipeHolder.title.setText(title);


        Picasso.get()
                .load(context.getString(R.string.poster_based_url) + list.get(position).getPosterPath())
                .resize(42, 56)
                .into(recipeHolder.poster);

        if(list.get(position).getProductionStatus()){
            Episode nextEpisode = list.get(position).getNextEpisode();
            if(nextEpisode != null) {
                recipeHolder.seasonNumber.setText(Integer.toString(nextEpisode.getSeasonNumber()));
                recipeHolder.episodeNumber.setText(Integer.toString(nextEpisode.getEpisodeNumber()));
                recipeHolder.nextEpisodeDay.setText(list.get(position).getNextEpisode().getAirDay());
                recipeHolder.nextEpisodeDate.setText(nextEpisode.getAirDateUsFormat());
                recipeHolder.nextEpisodeTime.setText(list.get(position).getListOfNetworks().get(0).getName());
            } else {
                recipeHolder.seasonNumberLabel.setText(R.string.NO_DATA_YET);
                recipeHolder.episodeNumber.setVisibility(View.GONE);
                recipeHolder.episodeNumberLabel.setVisibility(View.GONE);
                recipeHolder.seasonNumber.setVisibility(View.GONE);
                recipeHolder.nextEpisodeDay.setText("");
                recipeHolder.nextEpisodeDate.setText(R.string.NO_DATE_YET);
                recipeHolder.nextEpisodeTime.setText("");
            }
        } else {
            recipeHolder.seasonNumber.setVisibility(View.GONE);
            recipeHolder.episodeNumber.setVisibility(View.GONE);
            recipeHolder.episodeNumberLabel.setVisibility(View.GONE);
            recipeHolder.seasonNumberLabel.setText(R.string.NO_DATA);
            recipeHolder.nextEpisodeDay.setText("");
            recipeHolder.nextEpisodeDate.setText("");
            recipeHolder.nextEpisodeTime.setText("");
        }

        recipeHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = null;
                fragment = new ShowDetailFragment();
                bundle = new Bundle();
                bundle.putParcelable(context.getString(R.string.SERIES_DETAILS), list.get(position));
                fragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragment_box, fragment).commit();

            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}