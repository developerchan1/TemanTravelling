package com.fantasticiii.temantravelling.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class PlanOrder implements Parcelable {
    private String city, language, startDateAndTime, timeType;
    private int duration;
    private boolean needVehicle;
    private String paymentMethod;
    private int price;

    public PlanOrder(String city, String language, String startDateAndTime, int duration, String timeType, boolean needVehicle, String paymentMethod, int price) {
        this.city = city;
        this.language = language;
        this.startDateAndTime = startDateAndTime;
        this.timeType = timeType;
        this.duration = duration;
        this.needVehicle = needVehicle;
        this.paymentMethod = paymentMethod;
        this.price = price;
    }

    protected PlanOrder(Parcel in) {
        city = in.readString();
        language = in.readString();
        startDateAndTime = in.readString();
        timeType = in.readString();
        duration = in.readInt();
        needVehicle = in.readByte() != 0;
        paymentMethod = in.readString();
        price = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(language);
        dest.writeString(startDateAndTime);
        dest.writeString(timeType);
        dest.writeInt(duration);
        dest.writeByte((byte) (needVehicle ? 1 : 0));
        dest.writeString(paymentMethod);
        dest.writeInt(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlanOrder> CREATOR = new Creator<PlanOrder>() {
        @Override
        public PlanOrder createFromParcel(Parcel in) {
            return new PlanOrder(in);
        }

        @Override
        public PlanOrder[] newArray(int size) {
            return new PlanOrder[size];
        }
    };

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStartDateAndTime() {
        return startDateAndTime;
    }

    public void setStartDateAndTime(String startDateAndTime) {
        this.startDateAndTime = startDateAndTime;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
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
