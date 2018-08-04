package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ewasniecinska on 01.08.2018.
 */

public class TvNetwork implements Parcelable {
    String name;
    int id;
    String logo_path;
    String origin_country;

    public TvNetwork() {
    }


    protected TvNetwork(Parcel in) {
        name = in.readString();
        id = in.readInt();
        logo_path = in.readString();
        origin_country = in.readString();
    }

    public static final Creator<TvNetwork> CREATOR = new Creator<TvNetwork>() {
        @Override
        public TvNetwork createFromParcel(Parcel in) {
            return new TvNetwork(in);
        }

        @Override
        public TvNetwork[] newArray(int size) {
            return new TvNetwork[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(id);
        parcel.writeString(logo_path);
        parcel.writeString(origin_country);
    }

    public String getName() {
        return name;
    }
}
