package com.sniecinska.bingwatcher.models;

/**
 * Created by ewasniecinska on 31.07.2018.
 */

public class DatabaseModel {
    String user_id;
    String series_id;
    TvSeriesDetails tvSeries;

    public DatabaseModel(String user_id, String series_id, TvSeriesDetails tvSeries) {
        this.tvSeries = tvSeries;
        this.series_id = series_id;
        this.user_id = user_id;
    }

}
