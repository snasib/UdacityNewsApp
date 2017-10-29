package com.example.sadin.news.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sadin on 29-Oct-17.
 */

public class Fields implements Parcelable {
    private String thumbnail;

    protected Fields(Parcel in) {
        thumbnail = in.readString();
    }

    public static final Creator<Fields> CREATOR = new Creator<Fields>() {
        @Override
        public Fields createFromParcel(Parcel in) {
            return new Fields(in);
        }

        @Override
        public Fields[] newArray(int size) {
            return new Fields[size];
        }
    };

    public String getThumbnail() {
        return thumbnail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(thumbnail);
    }
}
