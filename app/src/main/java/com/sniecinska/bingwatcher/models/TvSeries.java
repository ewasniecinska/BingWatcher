package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ewasniecinska on 26.07.2018.
 */

public class TvSeries implements Parcelable {
    String original_name;
    List<Integer> genre_ids;
    String name;
    String popularity;
    List<String> origin_country;
    Integer vote_count;
    String first_air_date;
    String backdrop_path;
    String original_language;
    int id;
    float vote_average;
    String overview;
    String poster_path;

    public TvSeries(){
    }

    private TvSeries(Parcel in) {
        original_name = in.readString();
        name = in.readString();
        popularity = in.readString();
        origin_country = in.createStringArrayList();
        if (in.readByte() == 0) {
            vote_count = null;
        } else {
            vote_count = in.readInt();
        }
        first_air_date = in.readString();
        backdrop_path = in.readString();
        original_language = in.readString();
        id = in.readInt();
        vote_average = in.readFloat();
        overview = in.readString();
        poster_path = in.readString();
    }

    public static final Creator<TvSeries> CREATOR = new Creator<TvSeries>() {
        @Override
        public TvSeries createFromParcel(Parcel in) {
            return new TvSeries(in);
        }

        @Override
        public TvSeries[] newArray(int size) {
            return new TvSeries[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(original_name);
        parcel.writeString(name);
        parcel.writeString(popularity);
        parcel.writeStringList(origin_country);
        if (vote_count == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(vote_count);
        }
        parcel.writeString(first_air_date);
        parcel.writeString(backdrop_path);
        parcel.writeString(original_language);
        parcel.writeInt(id);
        parcel.writeFloat(vote_average);
        parcel.writeString(overview);
        parcel.writeString(poster_path);
    }

    public String getTitle(){
        return name;
    }

    public String getOrginalTitle(){
        return original_name;
    }

    public String getPosterPath(){
        return poster_path;
    }

    public String getBackdropPath(){
        return backdrop_path;
    }

    public List<Integer> getGenres(){
        return genre_ids;
    }

    public String getPopularity(){
        return popularity;
    }

    public List<String> getOriginCountries(){
        return origin_country;
    }

    public int getVoteCount(){
        return vote_count;
    }

    public int getId() {
        return id;
    }

    public String getFirstAirDate(){
        return first_air_date;
    }

    public String getOrginalLanguage(){
        return original_language;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public String getOverview(){
        return overview;
    }

}
