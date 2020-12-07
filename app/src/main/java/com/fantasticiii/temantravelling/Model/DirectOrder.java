package com.fantasticiii.temantravelling.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class DirectOrder implements Parcelable{
    private String language;
    private String dateAndTime;
    private int duration;
    private String timeType;
    private boolean needVehicle;
    private String paymentMethod;
    private int price;

    public DirectOrder(String language, String dateAndTime, int duration, String timeType, boolean needVehicle, String paymentMethod, int price) {
        this.language = language;
        this.dateAndTime = dateAndTime;
        this.duration = duration;
        this.timeType = timeType;
        this.needVehicle = needVehicle;
        this.paymentMethod = paymentMethod;
        this.price = price;
    }

    protected DirectOrder(Parcel in) {
        language = in.readString();
        dateAndTime = in.readString();
        duration = in.readInt();
        timeType = in.readString();
        needVehicle = in.readByte() != 0;
        paymentMethod = in.readString();
        price = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(language);
        dest.writeString(dateAndTime);
        dest.writeInt(duration);
        dest.writeString(timeType);
        dest.writeByte((byte) (needVehicle ? 1 : 0));
        dest.writeString(paymentMethod);
        dest.writeInt(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DirectOrder> CREATOR = new Creator<DirectOrder>() {
        @Override
        public DirectOrder createFromParcel(Parcel in) {
            return new DirectOrder(in);
        }

        @Override
        public DirectOrder[] newArray(int size) {
            return new DirectOrder[size];
        }
    };

    public String getPaymentMethod() {
        return paymentMethod;
    }


    public int getPrice() {
        return price;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isNeedVehicle() {
        return needVehicle;
    }

    public void setNeedVehicle(boolean needVehicle) {
        this.needVehicle = needVehicle;
    }
}
