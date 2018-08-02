package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.adapters.SeasonsAdapter;
import com.sniecinska.bingwatcher.models.Season;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class SeasonsListFragment extends Fragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    FragmentManager fragmentManager;
    List<Season> seasonList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            seasonList = bundle.getParcelableArrayList(getString(R.string.SEASONS));
        }
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_season_list, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        fragmentManager = getFragmentManager();

        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(new SeasonsAdapter(fragmentManager, seasonList));

        return view;
    }
}
