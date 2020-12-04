package com.fantasticiii.temantravelling.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class History implements Parcelable{
    private String title1;
    private String title2;
    private String location;
    private int numDay;
    private String date;
    private String detail;
    private int image;

    public History(){

    }


    protected History(Parcel in) {
        title1 = in.readString();
        title2 = in.readString();
        location = in.readString();
        numDay = in.readInt();
        date = in.readString();
        detail = in.readString();
        image = in.readInt();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumDay() {
        return numDay;
    }

    public void setNumDay(int numDay) {
        this.numDay = numDay;
    }

    public int getImage() {
        return image;
    }

    public void setImage (int image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title1);
        parcel.writeString(title2);
        parcel.writeString(location);
        parcel.writeInt(numDay);
        parcel.writeString(date);
        parcel.writeString(detail);
        parcel.writeInt(image);
    }
}
