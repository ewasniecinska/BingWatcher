package com.sniecinska.bingwatcher.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ewasniecinska on 02.08.2018.
 */

public class Crew implements Parcelable {
    public int id;
    public String credit_id;
    public String name;
    public String department;
    public String job;
    public String profile_path;

    public Crew() {

    }

    protected Crew(Parcel in) {
        id = in.readInt();
        credit_id = in.readString();
        name = in.readString();
        department = in.readString();
        job = in.readString();
        profile_path = in.readString();
    }

    public static final Creator<Crew> CREATOR = new Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel in) {
            return new Crew(in);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };

    public int getId(){
        return id;
    }

    public String getCreditId() {
        return credit_id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getJob() {
        return job;
    }

    public String getProfilePath() {
        return profile_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(credit_id);
        parcel.writeString(name);
        parcel.writeString(department);
        parcel.writeString(job);
        parcel.writeString(profile_path);
    }
}
