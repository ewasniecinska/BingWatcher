package com.sniecinska.bingwatcher.models;

/**
 * Created by ewasniecinska on 01.08.2018.
 */

public class ProductionCompany {
    public String id;
    public String logo_path;
    public String name;
    public String origin_country;

    public ProductionCompany(){

    }

    public String getId() {
        return id;
    }

    public String getLogoPath(){
        return logo_path;
    }

    public String getName() {
        return name;
    }

    public String getOrginCountry() {
        return origin_country;
    }
}
