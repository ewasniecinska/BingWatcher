package com.sniecinska.bingwatcher.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.adapters.PopularSeriesAdapter;
import com.sniecinska.bingwatcher.api.RetrofitConnector;
import com.sniecinska.bingwatcher.models.PopularResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExploreFragment extends Fragment {
    GridLayoutManager gridLayoutManager;
    Call<PopularResult> call;
    FragmentManager fragmentManager;
    SharedPreferences sharedpreferences;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        ButterKnife.bind(this, view);


        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        fragmentManager = getFragmentManager();

        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        initToolbar();

        getSavedSortByCategory();

        return view;
    }

    private void initToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.options_explore_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sort_by_popular:
                updateSharedPreference(getString(R.string.app_title_popular));
                call = RetrofitConnector.getService().getPopularSeries(getString(R.string.api_key));
                callApi(call);
                getActivity().setTitle(getString(R.string.app_title_popular));
                return true;
            case R.id.sort_by_top_rated:
                updateSharedPreference(getString(R.string.app_title_top_rated));
                call = RetrofitConnector.getService().getTopRated(getString(R.string.api_key));
                callApi(call);
                getActivity().setTitle(getString(R.string.app_title_top_rated));
                return true;
        }
        return true;
    }

    public void getSavedSortByCategory(){
        String sortMode = sharedpreferences.getString(getString(R.string.sort), "");
        switch (sortMode) {
            case "Top rated TV shows":
                updateSharedPreference(getString(R.string.app_title_popular));
                call = RetrofitConnector.getService().getPopularSeries(getString(R.string.api_key));
                callApi(call);
                getActivity().setTitle(getString(R.string.app_title_popular));
                return;
            case "Popular TV shows":
                updateSharedPreference(getString(R.string.app_title_top_rated));
                call = RetrofitConnector.getService().getTopRated(getString(R.string.api_key));
                callApi(call);
                getActivity().setTitle(getString(R.string.app_title_top_rated));
                return;
        }
    }

    public void callApi(Call call){
        call.enqueue(new Callback<PopularResult>() {
            @Override
            public void onResponse(Call<PopularResult> call, Response<PopularResult> response) {
                recyclerView.setAdapter(new PopularSeriesAdapter(fragmentManager, response.body().getListOfSeries()));
            }
            @Override
            public void onFailure(Call<PopularResult> call, Throwable throwable) {
            }
        });
    }

    public void updateSharedPreference(String sortMode){
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString(getString(R.string.sort), sortMode);
        edit.commit();
    }

}
