package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class EpisodeDetails implements Parcelable {
    public Date air_date;
    public List<Crew> crew;
    public int episode_number;
    public List<Actor> quest_stars;
    public String name;
    public String overview;
    public int id;
    public String production_code;
    public int season_number;
    public String still_path;
    public float vote_average;
    public int vote_count;

    protected EpisodeDetails(Parcel in) {
        episode_number = in.readInt();
        quest_stars = in.createTypedArrayList(Actor.CREATOR);
        name = in.readString();
        overview = in.readString();
        id = in.readInt();
        production_code = in.readString();
        season_number = in.readInt();
        still_path = in.readString();
        vote_average = in.readFloat();
        vote_count = in.readInt();
    }

    public static final Creator<EpisodeDetails> CREATOR = new Creator<EpisodeDetails>() {
        @Override
        public EpisodeDetails createFromParcel(Parcel in) {
            return new EpisodeDetails(in);
        }

        @Override
        public EpisodeDetails[] newArray(int size) {
            return new EpisodeDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(episode_number);
        parcel.writeTypedList(quest_stars);
        parcel.writeString(name);
        parcel.writeString(overview);
        parcel.writeInt(id);
        parcel.writeString(production_code);
        parcel.writeInt(season_number);
        parcel.writeString(still_path);
        parcel.writeFloat(vote_average);
        parcel.writeInt(vote_count);
    }

    public List<Actor> getQuestActors(){
        return quest_stars;
    }

    public int getId() {
        return id;
    }

    public String getProductionCode() {
        return production_code;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public int getVoteCount() {
        return vote_count;
    }

    public int getEpisodeNumber() {
        return episode_number;
    }

    public List<Crew> getCrew() {
        return crew;
    }

    public String getAirDate() {
        try {
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
            String airDate = format.format(air_date);
            return airDate;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getStillPath() {
        return still_path;
    }

    public int getSeasonNumber() {
        return season_number;
    }
}
