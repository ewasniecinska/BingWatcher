package com.sniecinska.bingwatcher.api;

import com.sniecinska.bingwatcher.models.ExternalApiResult;
import com.sniecinska.bingwatcher.models.NextEpisode;
import com.sniecinska.bingwatcher.models.PopularResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ewasniecinska on 26.07.2018.
 */

public interface Service {

    @GET("tv/popular")
    Call<PopularResult> getPopularSeries(@Query("api_key") String user);

    @GET("tv/top_rated")
    Call<PopularResult> getTopRated(@Query("api_key") String user);

    @GET("search/tv")
    Call<PopularResult> getSearchResults(@Query("api_key") String user, @Query("query") String query);

    @GET("tv/{tv_id}/external_ids")
    Call<ExternalApiResult> getExternalID(@Path("tv_id") int tv_id, @Query("api_key") String user);

    @GET("shows/{id}/next_episode")
    Call<NextEpisode> getNextEpisode(@Path("id") String id);
}
