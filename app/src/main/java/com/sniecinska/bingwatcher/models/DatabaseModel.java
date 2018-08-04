package com.sniecinska.bingwatcher.models;

/**
 * Created by ewasniecinska on 31.07.2018.
 */

public class DatabaseModel {
    String user_id;
    String series_id;
    long air_date;
    TvSeriesDetails tv_series_details;

    public DatabaseModel(String user_id, String series_id, long air_date, TvSeriesDetails tv_series_details) {
        this.tv_series_details = tv_series_details;
        this.air_date = air_date;
        this.series_id = series_id;
        this.user_id = user_id;
    }

}
