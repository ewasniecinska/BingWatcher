package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ewasniecinska on 01.08.2018.
 */

public class TvSeriesDetails implements Parcelable{
    public String backdrop_path;
    public List<SeriesCreator> created_by;
    public List<String> episode_run_time;
    public String first_air_date;
    public List<Genre> genres;
    public String homepage;
    public int id;
    public boolean in_production;
    public List<String> languages;
    public String last_air_date;
    public Episode last_episode_to_air;
    public String name;
    public Episode next_episode_to_air;
    public List<TvNetwork> networks;
    public int number_of_episodes;
    public int number_of_seasons;
    public List<String> origin_country;
    public String original_language;
    public String original_name;
    public String overview;
    public String popularity;
    public String poster_path;
    public List<ProductionCompany> production_companies;
    public List<Season> seasons;
    public String status;
    public String type;
    public float vote_average;
    public int vote_count;

    public TvSeriesDetails(){
    }

    protected TvSeriesDetails(Parcel in) {
        backdrop_path = in.readString();
        episode_run_time = in.createStringArrayList();
        first_air_date = in.readString();
        homepage = in.readString();
        id = in.readInt();
        in_production = in.readByte() != 0;
        languages = in.createStringArrayList();
        last_air_date = in.readString();
        last_episode_to_air = in.readParcelable(Episode.class.getClassLoader());
        name = in.readString();
        next_episode_to_air = in.readParcelable(Episode.class.getClassLoader());
        number_of_episodes = in.readInt();
        number_of_seasons = in.readInt();
        origin_country = in.createStringArrayList();
        original_language = in.readString();
        original_name = in.readString();
        overview = in.readString();
        popularity = in.readString();
        poster_path = in.readString();
        seasons = in.createTypedArrayList(Season.CREATOR);
        status = in.readString();
        type = in.readString();
        vote_average = in.readFloat();
        vote_count = in.readInt();
    }

    public static final Creator<TvSeriesDetails> CREATOR = new Creator<TvSeriesDetails>() {
        @Override
        public TvSeriesDetails createFromParcel(Parcel in) {
            return new TvSeriesDetails(in);
        }

        @Override
        public TvSeriesDetails[] newArray(int size) {
            return new TvSeriesDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(backdrop_path);
        parcel.writeStringList(episode_run_time);
        parcel.writeString(first_air_date);
        parcel.writeString(homepage);
        parcel.writeInt(id);
        parcel.writeByte((byte) (in_production ? 1 : 0));
        parcel.writeStringList(languages);
        parcel.writeString(last_air_date);
        parcel.writeParcelable(last_episode_to_air, i);
        parcel.writeString(name);
        parcel.writeParcelable(next_episode_to_air, i);
        parcel.writeInt(number_of_episodes);
        parcel.writeInt(number_of_seasons);
        parcel.writeStringList(origin_country);
        parcel.writeString(original_language);
        parcel.writeString(original_name);
        parcel.writeString(overview);
        parcel.writeString(popularity);
        parcel.writeString(poster_path);
        parcel.writeTypedList(seasons);
        parcel.writeString(status);
        parcel.writeString(type);
        parcel.writeFloat(vote_average);
        parcel.writeInt(vote_count);
    }

    public List<TvNetwork> getListOfNetworks() {
        return networks;
    }

    public Episode getLastEpisode(){
        return last_episode_to_air;
    }

    public Episode getNextEpisode(){
        return next_episode_to_air;
    }

    public boolean getProductionStatus() {
        return in_production;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public String getWebsite() {
        return homepage;
    }

    public String getBackdropPath() {
        return backdrop_path;
    }

    public String getFirstAirDate() {
        return first_air_date;
    }

    public int getId() {
        return id;
    }

    public float getVoteAverage() {
        return vote_average;
    }

    public int getVoteCount() {
        return vote_count;
    }

    public String getStatus(){
        return status;
    }

    public String getType(){
        return type;
    }

    public String getName(){
        return name;
    }

    public int getNumberOfEpisodes(){
        return number_of_episodes;
    }

    public int getNumberOfSeasons(){
        return  number_of_seasons;
    }

    public String getPosterPath(){
        return poster_path;
    }

    public List<Season> getSeasons(){
        return seasons;
    }

    public String getOverview(){
        return overview;
    }

    public List<SeriesCreator> getCreators() {
        return created_by;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return production_companies;
    }

}
