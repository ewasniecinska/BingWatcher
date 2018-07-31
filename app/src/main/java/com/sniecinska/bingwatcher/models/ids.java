package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ids implements Parcelable {
    String trakt;
    String tvdb;
    String imdb;
    String tmdb;

    protected ids(Parcel in) {
        trakt = in.readString();
        tvdb = in.readString();
        imdb = in.readString();
        tmdb = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trakt);
        dest.writeString(tvdb);
        dest.writeString(imdb);
        dest.writeString(tmdb);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ids> CREATOR = new Creator<ids>() {
        @Override
        public ids createFromParcel(Parcel in) {
            return new ids(in);
        }

        @Override
        public ids[] newArray(int size) {
            return new ids[size];
        }
    };
}
