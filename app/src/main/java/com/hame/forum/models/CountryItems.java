package com.hame.forum.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CountryItems implements Parcelable {
    public static final Creator<CountryItems> CREATOR = new Creator<CountryItems>() {
        @Override
        public CountryItems createFromParcel(Parcel in) {
            return new CountryItems(in);
        }

        @Override
        public CountryItems[] newArray(int size) {
            return new CountryItems[size];
        }
    };
    private int idCountry;
    private String countryName, countryCode, countryInd;

    public CountryItems() {
    }

    public CountryItems(int idCountry, String countryName, String countryInd) {
        this.idCountry = idCountry;
        this.countryName = countryName;
        this.countryInd = countryInd;
    }

    public CountryItems(int idCountry, String countryName, String countryCode, String countryInd) {
        this.idCountry = idCountry;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.countryInd = countryInd;
    }

    protected CountryItems(Parcel in) {
        idCountry = in.readInt();
        countryName = in.readString();
        countryCode = in.readString();
        countryInd = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCountry);
        dest.writeString(countryName);
        dest.writeString(countryCode);
        dest.writeString(countryInd);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(int idCountry) {
        this.idCountry = idCountry;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryInd() {
        return countryInd;
    }

    public void setCountryInd(String countryInd) {
        this.countryInd = countryInd;
    }
}
