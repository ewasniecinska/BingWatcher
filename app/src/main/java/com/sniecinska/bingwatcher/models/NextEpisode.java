package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ewasniecinska on 31.07.2018.
 */

public class NextEpisode implements Parcelable {


    int season;
    int number;
    String title;
    List<ids> ids;


    protected NextEpisode(Parcel in) {
        season = in.readInt();
        number = in.readInt();
        title = in.readString();
        ids = in.createTypedArrayList(com.sniecinska.bingwatcher.models.ids.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(season);
        dest.writeInt(number);
        dest.writeString(title);
        dest.writeTypedList(ids);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NextEpisode> CREATOR = new Creator<NextEpisode>() {
        @Override
        public NextEpisode createFromParcel(Parcel in) {
            return new NextEpisode(in);
        }

        @Override
        public NextEpisode[] newArray(int size) {
            return new NextEpisode[size];
        }
    };

    public int getSeason(){
        return season;
    }

    public int getEpisode(){
        return number;
    }

    public String getTitle(){
        return title;
    }
}

