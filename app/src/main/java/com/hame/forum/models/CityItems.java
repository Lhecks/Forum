package com.hame.forum.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CityItems implements Parcelable {
    public static final Creator<CityItems> CREATOR = new Creator<CityItems>() {
        @Override
        public CityItems createFromParcel(Parcel in) {
            return new CityItems(in);
        }

        @Override
        public CityItems[] newArray(int size) {
            return new CityItems[size];
        }
    };

    public CityItems() {
    }

    private String idCity, cityName, country_name;

    public CityItems(String idCity, String cityName, String country_name) {
        this.idCity = idCity;
        this.cityName = cityName;
        this.country_name = country_name;
    }

    protected CityItems(Parcel in) {
        idCity = in.readString();
        cityName = in.readString();
        country_name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idCity);
        dest.writeString(cityName);
        dest.writeString(country_name);
    }

    public String getIdCity() {
        return idCity;
    }

    public void setIdCity(String idCity) {
        this.idCity = idCity;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }
}
