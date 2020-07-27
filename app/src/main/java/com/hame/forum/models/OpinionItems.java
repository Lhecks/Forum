package com.hame.forum.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OpinionItems implements Parcelable {
    public static final Creator<OpinionItems> CREATOR = new Creator<OpinionItems>() {
        @Override
        public OpinionItems createFromParcel(Parcel in) {
            return new OpinionItems(in);
        }

        @Override
        public OpinionItems[] newArray(int size) {
            return new OpinionItems[size];
        }
    };
    private int idOpinion;
    private String opinion, hospitalName, serviceName, dateOpinion, timeOpinion, userName;
    private String rating_bar;

    public OpinionItems() {
    }

    public OpinionItems(int idOpinion, String opinion, String hospitalName, String serviceName, String dateOpinion, String timeOpinion, String userName, String rating_bar) {
        this.idOpinion = idOpinion;
        this.opinion = opinion;
        this.hospitalName = hospitalName;
        this.serviceName = serviceName;
        this.dateOpinion = dateOpinion;
        this.timeOpinion = timeOpinion;
        this.userName = userName;
        this.rating_bar = rating_bar;
    }

    protected OpinionItems(Parcel in) {
        idOpinion = in.readInt();
        opinion = in.readString();
        hospitalName = in.readString();
        serviceName = in.readString();
        dateOpinion = in.readString();
        timeOpinion = in.readString();
        userName = in.readString();
        rating_bar = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idOpinion);
        dest.writeString(opinion);
        dest.writeString(hospitalName);
        dest.writeString(serviceName);
        dest.writeString(dateOpinion);
        dest.writeString(timeOpinion);
        dest.writeString(userName);
        dest.writeString(rating_bar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIdOpinion() {
        return idOpinion;
    }

    public void setIdOpinion(int idOpinion) {
        this.idOpinion = idOpinion;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDateOpinion() {
        return dateOpinion;
    }

    public void setDateOpinion(String dateOpinion) {
        this.dateOpinion = dateOpinion;
    }

    public String getTimeOpinion() {
        return timeOpinion;
    }

    public void setTimeOpinion(String timeOpinion) {
        this.timeOpinion = timeOpinion;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRating_bar() {
        return rating_bar;
    }

    public void setRating_bar(String rating_bar) {
        this.rating_bar = rating_bar;
    }
}
