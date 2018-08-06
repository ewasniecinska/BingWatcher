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
import com.sniecinska.bingwatcher.fragments.SeasonsDetailFragment;
import com.sniecinska.bingwatcher.models.Season;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class SeasonsListAdapter extends RecyclerView.Adapter<SeasonsListAdapter.RecipeHolder>{
    private List<Season> list;
    Fragment fragment;
    FragmentManager fragmentManager;
    Bundle bundle;
    Context context;
    int tvId;

    public SeasonsListAdapter (Context context, FragmentManager fragmentManager, List<Season> list, int tvId){
        this.context = context;
        this.list = list;
        this.fragmentManager = fragmentManager;
        this.tvId = tvId;
    }

    public static class RecipeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.poster)
        ImageView poster;
        @BindView(R.id.episodes_number)
        TextView episodesNumber;

        public RecipeHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public SeasonsListAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_seasons_list, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, final int position) {
        String title = list.get(position).getName();
        RecipeHolder recipeHolder = holder;

        recipeHolder.title.setText(title);

        recipeHolder.episodesNumber.setText(Integer.toString(list.get(position).getEpisodeCount()));

        Picasso.get()
                .load(context.getString(R.string.poster_based_url) + list.get(position).getPosterPath())
                .resize(42, 56)
                .into(recipeHolder.poster);

        recipeHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = null;
                fragment = new SeasonsDetailFragment();
                bundle = new Bundle();
                bundle.putParcelable(context.getString(R.string.SEASON), list.get(position));
                bundle.putInt(context.getString(R.string.TV_ID), tvId);
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