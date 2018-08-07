package com.sniecinska.bingwatcher.models;

/**
 * Created by ewasniecinska on 01.08.2018.
 */

public class SeriesCreator {
    public int id;
    public String credit_id;
    public String name;
    public int gender;
    public String profile_path;

    public SeriesCreator(){

    }

    public int getId(){
        return id;
    }

    public String getCreditId() {
        return credit_id;
    }

    public String getName() {
        return name;
    }

    public int getGender(){
        return gender;
    }

    public String getProfilePath(){
        return profile_path;
    }

}
