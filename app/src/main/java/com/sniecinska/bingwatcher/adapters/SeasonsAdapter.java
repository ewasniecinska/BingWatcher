package com.sniecinska.bingwatcher.adapters;

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
import com.sniecinska.bingwatcher.models.Season;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class SeasonsAdapter extends RecyclerView.Adapter<SeasonsAdapter.RecipeHolder>{
    private List<Season> list;
    Fragment fragment;
    FragmentManager fragmentManager;
    Bundle bundle;

    public SeasonsAdapter (FragmentManager fragmentManager, List<Season> list){
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
        @BindView(R.id.episodes_number)
        TextView episodesNumber;

        public RecipeHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public SeasonsAdapter.RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_seasons_list, parent, false);
        return new RecipeHolder(view);

    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, final int position) {
        String title = list.get(position).getName();
        RecipeHolder recipeHolder = holder;

        String airYear = list.get(position).getAirYear();

        if(airYear == null) {
            recipeHolder.title.setText(title);
        } else {
            recipeHolder.title.setText(title + " (" + airYear +")");
        }

        recipeHolder.episodesNumber.setText("Episodes: " + list.get(position).getEpisodeCount());

        Picasso.get()
                .load("http://image.tmdb.org/t/p/w185/" + list.get(position).getPosterPath())
                .resize(42, 56)
                .into(recipeHolder.poster);



        recipeHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = null;
                fragment = new ShowDetailFragment();
                bundle = new Bundle();
                bundle.putParcelable("TV_SHOW", list.get(position));
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