package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by ewasniecinska on 01.08.2018.
 */

public class Season implements Parcelable {
    Date air_date;
    int episode_count;
    String id;
    String name;
    String overview;
    String poster_path;
    int season_number;

    public Season() {

    }

    protected Season(Parcel in) {
        episode_count = in.readInt();
        id = in.readString();
        name = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        season_number = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(episode_count);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeInt(season_number);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Season> CREATOR = new Creator<Season>() {
        @Override
        public Season createFromParcel(Parcel in) {
            return new Season(in);
        }

        @Override
        public Season[] newArray(int size) {
            return new Season[size];
        }
    };

    public Date getAirDate() {
        return air_date;
    }


    public int getEpisodeCount() {
        return episode_count;
    }

    public String getSeasonId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return poster_path;
    }

    public int getSeasonNumber() {
        return season_number;
    }
}
