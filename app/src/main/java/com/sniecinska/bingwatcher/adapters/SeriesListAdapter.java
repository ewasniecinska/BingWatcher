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
import android.widget.RatingBar;
import android.widget.TextView;

import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.fragments.ShowDetailFragment;
import com.sniecinska.bingwatcher.models.TvSeries;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ewasniecinska on 26.07.2018.
 */

public class SeriesListAdapter extends RecyclerView.Adapter<SeriesListAdapter.RecipeHolder>{
    private List<TvSeries> list;
    Fragment fragment;
    FragmentManager fragmentManager;
    Bundle bundle;
    Context context;

    public SeriesListAdapter (Context context, FragmentManager fragmentManager, List<TvSeries> list){
        this.context = context;
        this.list = list;
        this.fragmentManager = fragmentManager;
    }

    public static class RecipeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.card_view) CardView cardView;
        @BindView(R.id.poster) ImageView poster;
        @BindView(R.id.vote_averge) RatingBar ratingBar;

        public RecipeHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public SeriesListAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_popular_series, parent, false);
        return new RecipeHolder(view);

    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, final int position) {
        String title = list.get(position).getTitle();
        RecipeHolder recipeHolder = holder;
        recipeHolder.title.setText(title);

        recipeHolder.ratingBar.setRating(list.get(position).getVoteAverage()/2);

        Picasso.get()
                .load(context.getString(R.string.poster_based_url) + list.get(position).getPosterPath())
                .resize(42, 56)
                .into(recipeHolder.poster);



       recipeHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = null;
                fragment = new ShowDetailFragment();
                bundle = new Bundle();
                bundle.putParcelable(context.getString(R.string.TV_SHOW), list.get(position));
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