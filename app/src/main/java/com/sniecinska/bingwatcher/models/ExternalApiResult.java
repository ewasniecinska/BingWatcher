package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ewasniecinska on 31.07.2018.
 */

public class ExternalApiResult implements Parcelable {
    String id;
    String imdb_id;
    String freebase_mid;
    String freebase_id;
    String tvdb_id;
    String tvrage_id;
    String facebook_id;
    String instagram_id;
    String twitter_id;

    protected ExternalApiResult(Parcel in) {
        id = in.readString();
        imdb_id = in.readString();
        freebase_mid = in.readString();
        freebase_id = in.readString();
        tvdb_id = in.readString();
        tvrage_id = in.readString();
        facebook_id = in.readString();
        instagram_id = in.readString();
        twitter_id = in.readString();
    }

    public static final Creator<ExternalApiResult> CREATOR = new Creator<ExternalApiResult>() {
        @Override
        public ExternalApiResult createFromParcel(Parcel in) {
            return new ExternalApiResult(in);
        }

        @Override
        public ExternalApiResult[] newArray(int size) {
            return new ExternalApiResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(imdb_id);
        parcel.writeString(freebase_mid);
        parcel.writeString(freebase_id);
        parcel.writeString(tvdb_id);
        parcel.writeString(tvrage_id);
        parcel.writeString(facebook_id);
        parcel.writeString(instagram_id);
        parcel.writeString(twitter_id);
    }

    public String getImdbId(){
        return imdb_id;
    }

    public String getTvdbId(){
        return tvdb_id;
    }

    public String getFacebookId(){
        return facebook_id;
    }

    public String getInstagram_id(){
        return instagram_id;
    }

    public String getTwitter_id(){
        return twitter_id;
    }
}
