package com.sniecinska.bingwatcher.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sniecinska.bingwatcher.R;
import com.sniecinska.bingwatcher.api.RetrofitConnector;
import com.sniecinska.bingwatcher.models.TvSeriesDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ewasniecinska on 04.08.2018.
 */

public class MyService extends Service {
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<TvSeriesDetails> seriesList;
    DatabaseReference postRef;
    FirebaseUser currentUser;
    TvSeriesDetails tvSeriesDetails;
    TvSeriesDetails responseBody;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        getDataFromDb();
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void getDataFromDb() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        Query peopleBloodTypeQuery = databaseReference.child("users").orderByChild("air_date");
        peopleBloodTypeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                seriesList = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uid = ds.child(getString(R.string.DB_CHILD_USER_ID)).getValue(String.class);
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(uid.equals(currentUser.getUid())){
                        TvSeriesDetails tvSeries = ds.child(getString(R.string.DB_CHILD_TVSERIES_DETAILS)).getValue(TvSeriesDetails.class);
                        seriesList.add(tvSeries);
                    }
                }
                updateTvSeriesDataById();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateTvSeriesDataById() {

        for(int i = 0; i < seriesList.size(); i++) {

            Call call = RetrofitConnector.getService().getSeriesDetails(seriesList.get(i).getId(), getString(R.string.api_key));


            final int finalI = i;
            call.enqueue(new Callback<TvSeriesDetails>() {
                @Override
                public void onResponse(Call<TvSeriesDetails> call, final Response<TvSeriesDetails> response) {
                    tvSeriesDetails = seriesList.get(finalI);
                    responseBody = response.body();

                    if(tvSeriesDetails.getNextEpisode() == response.body().getNextEpisode()){
                        postRef = FirebaseDatabase.getInstance().getReference().child(getString(R.string.DB_CHILD_USERS));
                        postRef.orderByChild(getString(R.string.DB_CHILD_USER_ID)).equalTo(currentUser.getUid())
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (dataSnapshot.exists()) {
                                                    String tvId = ds.child("series_id").getValue(String.class);
                                                Log.d("TEST", "testowo");
                                                if (tvId.equals(Integer.toString(tvSeriesDetails.getId()))){
                                                    ds.getRef().child("tv_series_details").setValue(tvSeriesDetails);
                                                }
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }

                                });
                    }
                }

                @Override
                public void onFailure(Call<TvSeriesDetails> call, Throwable throwable) {
                    Log.e(getString(R.string.RETROFIT_ERROR), throwable.getMessage());
                }
            });
        }
    }



}
