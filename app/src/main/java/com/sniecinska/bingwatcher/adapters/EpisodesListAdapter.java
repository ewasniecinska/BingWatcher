package com.sniecinska.bingwatcher.adapters;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.fragments.EpisodeFragment;
import com.sniecinska.bingwatcher.models.EpisodeDetails;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class EpisodesListAdapter extends RecyclerView.Adapter<EpisodesListAdapter.RecipeHolder>{
    private List<EpisodeDetails> list;
    Fragment fragment;
    FragmentManager fragmentManager;
    Bundle bundle;
    Context context;
    int tvId;

    public EpisodesListAdapter(Context context, FragmentManager fragmentManager, List<EpisodeDetails> list, int tvId){
        this.context = context;
        this.list = list;
        this.tvId = tvId;
        this.fragmentManager = fragmentManager;
    }

    public static class RecipeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.episodes_number)
        TextView episodesNumber;

        public RecipeHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public EpisodesListAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_episodes_list, parent, false);
        return new RecipeHolder(view);

    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, final int position) {

        RecipeHolder recipeHolder = holder;

        recipeHolder.title.setText(list.get(position).getName());
        recipeHolder.episodesNumber.setText(list.get(position).getAirDate());

       // Picasso.get()
               // .load(context.getString(R.string.image_based_url) + list.get(position).getStillPath())
               // .into(recipeHolder.poster);

        recipeHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = null;
                fragment = new EpisodeFragment();
                bundle = new Bundle();
                bundle.putInt(context.getString(R.string.TV_ID), tvId);
                bundle.putInt(context.getString(R.string.SEASON_NUMBER), list.get(position).getSeasonNumber());
                bundle.putInt(context.getString(R.string.EPISODE_NUMBER), list.get(position).getEpisodeNumber());
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