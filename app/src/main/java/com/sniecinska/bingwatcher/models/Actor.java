package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class Actor implements Parcelable {
    int id;
    String name;
    String credit_id;
    String character;
    int order;
    String profile_path;

    protected Actor(Parcel in) {
        id = in.readInt();
        name = in.readString();
        credit_id = in.readString();
        character = in.readString();
        order = in.readInt();
        profile_path = in.readString();
    }

    public static final Creator<Actor> CREATOR = new Creator<Actor>() {
        @Override
        public Actor createFromParcel(Parcel in) {
            return new Actor(in);
        }

        @Override
        public Actor[] newArray(int size) {
            return new Actor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(credit_id);
        parcel.writeString(character);
        parcel.writeInt(order);
        parcel.writeString(profile_path);
    }
}
