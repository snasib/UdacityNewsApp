package com.example.sadin.news.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sadin on 26-Oct-17.
 */

public class Result implements Parcelable {
    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
    public Fields fields;
    private String type;
    private String sectionName;
    private String webPublicationDate;
    private String webTitle;
    private String webUrl;

    protected Result(Parcel in) {
        type = in.readString();
        sectionName = in.readString();
        webPublicationDate = in.readString();
        webTitle = in.readString();
        webUrl = in.readString();
        fields = in.readParcelable(Fields.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(sectionName);
        dest.writeString(webPublicationDate);
        dest.writeString(webTitle);
        dest.writeString(webUrl);
        dest.writeParcelable(fields, flags);
    }

    public String getType() {
        return type;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPublicationDate() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                webPublicationDate.substring(
                        webPublicationDate.indexOf("T") + 1,
                        webPublicationDate.lastIndexOf(":")));
        stringBuilder.append(" ");
        stringBuilder.append(webPublicationDate.substring(0, webPublicationDate.indexOf("T")));
        return stringBuilder.toString();
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getAuthor() {
        if (webTitle.contains("|")) {
            return webTitle.substring(webTitle.indexOf("|") + 2);
        } else {
            return null;
        }
    }
}
