package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by ewasniecinska on 26.07.2018.
 */

public class PopularResult implements Parcelable{
    public int page;
    public String total_results;
    public String total_pages;
    public List<TvSeries> results;

    protected PopularResult(Parcel in) {
        page = in.readInt();
        total_results = in.readString();
        total_pages = in.readString();
        results = in.createTypedArrayList(TvSeries.CREATOR);
    }

    public static final Creator<PopularResult> CREATOR = new Creator<PopularResult>() {
        @Override
        public PopularResult createFromParcel(Parcel in) {
            return new PopularResult(in);
        }

        @Override
        public PopularResult[] newArray(int size) {
            return new PopularResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(page);
        parcel.writeString(total_results);
        parcel.writeString(total_pages);
        parcel.writeTypedList(results);
    }

    public String getTotalResults(){
        return total_results;
    }

    public String getTotalPages(){
        return total_pages;
    }
    public int getPage() {
        return page;
    }

    public List<TvSeries> getListOfSeries(){
        return results;
    }
}
