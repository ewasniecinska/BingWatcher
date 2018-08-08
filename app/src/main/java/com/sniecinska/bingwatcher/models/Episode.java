package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ewasniecinska on 01.08.2018.
 */

public class Episode implements Parcelable {
    public Date air_date;
    public int episode_number;
    public String id;
    public String name;
    public String overview;
    public String production_code;
    public int season_number;
    public String show_id;
    public String still_path;
    public float vote_average;
    public int vote_count;

    public Episode() {
    }

    protected Episode(Parcel in) {
        episode_number = in.readInt();
        id = in.readString();
        name = in.readString();
        overview = in.readString();
        production_code = in.readString();
        season_number = in.readInt();
        show_id = in.readString();
        still_path = in.readString();
        vote_average = in.readFloat();
        vote_count = in.readInt();
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(episode_number);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(overview);
        parcel.writeString(production_code);
        parcel.writeInt(season_number);
        parcel.writeString(show_id);
        parcel.writeString(still_path);
        parcel.writeFloat(vote_average);
        parcel.writeInt(vote_count);
    }

    public String getId() {
        return id;
    }

    public String getShowId() {
        return show_id;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public int getVoteCount(){
        return vote_count;
    }

    public String getProductionCode() {
        return production_code;
    }

    public String getName() {
        return name;
    }

    public String getAirDateUsFormat() {
        try {
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            String airDate = format.format(air_date);
            return airDate;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getAirDay(){
        if(air_date != null) {
            DateTime airDate = new DateTime(air_date);
            DateTime now = DateTime.now();

            int airDayOfYear = airDate.getDayOfYear();
            int nowDayOfYear = now.getDayOfYear();

            if (airDayOfYear == nowDayOfYear - 1) {
                return "Yesterday";
            } else if (airDayOfYear == nowDayOfYear) {
                return "Today";
            } else if (airDayOfYear == nowDayOfYear + 1) {
                return "Tomorrow";
            } else if (airDayOfYear == nowDayOfYear + 2) {
                return "After tomorrow";
            } else if (airDayOfYear >= nowDayOfYear + 3) {
                int difference = airDayOfYear - nowDayOfYear;
                return "In " + difference + " days";
            }

            return " ";
        } else {
            return " ";
        }

    }

    public Date getAirDate() {
        return air_date;
    }

    public String getOverview() {
        return overview;
    }

    public int getEpisodeNumber() {
        return episode_number;
    }

    public int getSeasonNumber() {
        return season_number;
    }

    public String getStillPath() {
        return still_path;
    }
}
