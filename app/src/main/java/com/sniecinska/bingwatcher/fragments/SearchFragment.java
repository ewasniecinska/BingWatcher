package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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


public class SearchFragment extends Fragment {
    GridLayoutManager gridLayoutManager;
    FragmentManager fragmentManager;
    Call<PopularResult> call;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, view);

        initToolbar();

        fragmentManager = getFragmentManager();

        searchView.setQueryHint("Title of TV series");
        searchView.onActionViewExpanded();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getSearchResults(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null) {
                    getSearchResults(newText);
                }
                return false;
            }
        });


        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);



        return view;
    }

    private void initToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        getActivity().setTitle(getString(R.string.app_name));
    }


    public void callApi(Call call){
        call.enqueue(new Callback<PopularResult>() {
            @Override
            public void onResponse(Call<PopularResult> call, Response<PopularResult> response) {
                if(response.body() != null) {
                    recyclerView.setAdapter(new PopularSeriesAdapter(fragmentManager, response.body().getListOfSeries()));
                }
            }
            @Override
            public void onFailure(Call<PopularResult> call, Throwable throwable) {
            }
        });
    }

    public void getSearchResults(String query){
        call = RetrofitConnector.getService().getSearchResults(getString(R.string.api_key), query);
        callApi(call);
    }

}
