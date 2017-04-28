package com.wang.imagepicker.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

/**
 * Created by wang
 * on 2017/4/27
 */

public class Video extends Photo{

    private static final long MB = 1024 * 1024;

    public long size;
    public String title;
    public long duration;
    public String dateTaken;
    public String mimeType;


    public Video(int id, String path) {
        super(id, path);
    }


    public Video() {
        super();
    }

    public String getDuration() {
        if (duration <= 0) {
            return String.format(Locale.US, "%02d:%02d", 0, 0);
        }
        long totalSeconds = duration / 1000;

        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d", hours * 60 + minutes,
                    seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

    public String getSize() {
        double size = this.size > 0 ? this.size : 0;
        if (size == 0) {
            return "0K";
        }
        if (size >= MB) {
            double sizeInM = size / MB;
            return String.format(Locale.getDefault(), "%.1f", sizeInM) + "M";
        }
        double sizeInK = size / 1024;
        return String.format(Locale.getDefault(), "%.1f", sizeInK) + "K";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this.size);
        dest.writeString(this.title);
        dest.writeLong(this.duration);
        dest.writeString(this.dateTaken);
        dest.writeString(this.mimeType);
    }

    protected Video(Parcel in) {
        super(in);
        this.size = in.readLong();
        this.title = in.readString();
        this.duration = in.readLong();
        this.dateTaken = in.readString();
        this.mimeType = in.readString();
    }

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
