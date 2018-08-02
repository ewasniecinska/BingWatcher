package com.sniecinska.bingwatcher.models;

import java.util.List;

/**
 * Created by ewasniecinska on 01.08.2018.
 */

public class TvSeriesDetails {
    String backdrop_path;
    List<SeriesCreator> created_by;
    List<String> episode_run_time;
    String first_air_date;
    List<Genre> genres;
    String homepage;
    int id;
    boolean in_production;
    List<String> languages;
    String last_air_date;
    Episode last_episode_to_air;
    String name;
    Episode next_episode_to_air;
    List<TvNetwork> networks;
    int number_of_episodes;
    int number_of_seasons;
    List<String> origin_country;
    String original_language;
    String original_name;
    String overview;
    String popularity;
    String poster_path;
    List<ProductionCompany> production_companies;
    List<Season> seasons;
    String status;
    String type;
    float vote_average;
    int vote_count;

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


}
