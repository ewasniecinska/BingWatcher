package com.sniecinska.bingwatcher.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sniecinska.bingwatcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackedShowsFragment extends Fragment {
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_tracked_shows, container, false);
       ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

       return view;
    }


}
