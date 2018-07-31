package com.sniecinska.bingwatcher.models;

/**
 * Created by ewasniecinska on 31.07.2018.
 */

public class DatabaseModel {
    String user_id;
    TvSeries tvSeries;

    public DatabaseModel(String user_id, TvSeries tvSeries) {
        this.tvSeries = tvSeries;
        this.user_id = user_id;
    }


}
