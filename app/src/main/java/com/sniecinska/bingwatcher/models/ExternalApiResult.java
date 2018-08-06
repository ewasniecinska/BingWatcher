package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ewasniecinska on 06.08.2018.
 */

public class ExternalApiResult implements Parcelable{
    String imdb_id;
    String freebase_mid;
    String freebase_id;
    int tvdb_id;
    int tvrage_id;
    int id;

    protected ExternalApiResult(Parcel in) {
        imdb_id = in.readString();
        freebase_mid = in.readString();
        freebase_id = in.readString();
        tvdb_id = in.readInt();
        tvrage_id = in.readInt();
        id = in.readInt();
    }

    public static final Creator<ExternalApiResult> CREATOR = new Creator<ExternalApiResult>() {
        @Override
        public ExternalApiResult createFromParcel(Parcel in) {
            return new ExternalApiResult(in);
        }

        @Override
        public ExternalApiResult[] newArray(int size) {
            return new ExternalApiResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imdb_id);
        parcel.writeString(freebase_mid);
        parcel.writeString(freebase_id);
        parcel.writeInt(tvdb_id);
        parcel.writeInt(tvrage_id);
        parcel.writeInt(id);
    }

    public int getTvdbId() {
        return tvdb_id;
    }
}
